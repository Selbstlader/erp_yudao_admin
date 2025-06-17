package cn.iocoder.yudao.module.infra.service.file;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.io.FileUtils;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.dify.FileDifySyncPageReqVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.file.FileDifyDO;
import cn.iocoder.yudao.module.infra.dal.dataobject.file.FileDO;
import cn.iocoder.yudao.module.infra.dal.mysql.file.FileDifyMapper;
import cn.iocoder.yudao.module.infra.dal.mysql.file.FileMapper;
import cn.iocoder.yudao.module.infra.framework.dify.config.DifyProperties;
import cn.iocoder.yudao.module.infra.framework.dify.core.DifyClient;
import cn.iocoder.yudao.module.infra.framework.dify.dto.request.DifyCreateDatasetRequest;
import cn.iocoder.yudao.module.infra.framework.dify.dto.request.DocumentFileReqDTO;
import cn.iocoder.yudao.module.infra.framework.dify.dto.response.DocumentRespDTO;
import cn.iocoder.yudao.module.infra.framework.dify.dto.response.IndexingStatusRespDTO;
import cn.iocoder.yudao.module.infra.framework.dify.dto.response.DatasetListRespDTO;
import cn.iocoder.yudao.module.infra.framework.dify.core.enums.DifySyncStatusEnum;
import cn.iocoder.yudao.module.infra.framework.dify.exception.DifyApiException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static cn.iocoder.yudao.module.infra.enums.ErrorCodeConstants.*;

/**
 * 文件Dify服务实现类
 *
 * @author 芋道源码
 */
@Service
@Slf4j
public class FileDifyServiceImpl implements FileDifyService {

    @Resource
    private FileDifyMapper fileDifyMapper;

    @Resource
    private FileMapper fileMapper;

    @Resource
    private FileService fileService;

    @Resource
    private DifyClient difyClient;

    @Resource
    private DifyProperties difyProperties;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileDifyDO syncFileToDataset(Long fileId, String datasetId) {
        // 校验文件是否存在
        FileDO file = validateAndGetFile(fileId);
        
        // 检查datasetId, 如果为空，使用默认知识库
        if (StrUtil.isBlank(datasetId)) {
            datasetId = difyProperties.getDefaultDatasetId();
            
            // 如果配置中没有默认知识库ID，才尝试创建默认知识库
            if (StrUtil.isBlank(datasetId)) {
                try {
                    // 使用随机后缀创建默认知识库名称，避免名称冲突
                    String defaultDatasetName = "Default_" + RandomUtil.randomString(8);
                    log.info("[syncFileToDataset][配置中无默认知识库ID，开始创建默认知识库({})...]", defaultDatasetName);
                    datasetId = createDefaultDataset(defaultDatasetName);
                    log.info("[syncFileToDataset][创建默认知识库成功][datasetId: {}]", datasetId);
                } catch (Exception e) {
                    log.error("[syncFileToDataset][自动创建默认知识库失败]", e);
                    // 创建知识库失败，创建"待创建"状态的同步记录
                    return createPendingDatasetRecord(fileId);
                }
            } else {
                log.info("[syncFileToDataset][使用配置的默认知识库ID: {}]", datasetId);
            }
        }
        
        // 判断是否有已有的同步记录
        FileDifyDO fileDify = fileDifyMapper.selectListByFileId(fileId).stream()
                .findFirst().orElse(null);
        
        // 如果不存在同步记录，则创建新的
        if (fileDify == null) {
            fileDify = new FileDifyDO();
            fileDify.setFileId(fileId);
            fileDify.setDatasetId(datasetId);
            fileDify.setSyncStatus(DifySyncStatusEnum.TO_SYNC.getStatus());
            fileDify.setRetryCount(0);
            fileDify.setSyncTime(LocalDateTime.now());
            fileDifyMapper.insert(fileDify);
        } else {
            // 如果存在同步记录，更新状态为待同步
            fileDify.setDatasetId(datasetId);
            fileDify.setSyncStatus(DifySyncStatusEnum.TO_SYNC.getStatus());
            fileDify.setRetryCount(0);
            fileDify.setSyncTime(LocalDateTime.now());
            fileDify.setErrorMessage(null);
            fileDify.setErrorCode(null);
            fileDifyMapper.updateById(fileDify);
        }
        
        return fileDify;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileDifyDO updateFileSync(Long fileId, String datasetId) {
        // 参数校验
        FileDO file = validateAndGetFile(fileId);
        
        // 检查是否存在同步记录
        FileDifyDO existSync = fileDifyMapper.selectByFileId(fileId);
        if (existSync == null) {
            // 不存在同步记录，直接创建新记录
            return syncFileToDataset(fileId, datasetId);
        }
        
        // 如果知识库ID不同，则删除旧记录并重新同步
        if (datasetId != null && !datasetId.equals(existSync.getDatasetId())) {
            deleteFileSync(fileId);
            return syncFileToDataset(fileId, datasetId);
        }
        
        // 如果正在同步中，不进行操作
        if (DifySyncStatusEnum.SYNCING.getStatus().equals(existSync.getSyncStatus())) {
            return existSync;
        }
        
        // 更新同步记录状态
        existSync.setSyncStatus(DifySyncStatusEnum.SYNCING.getStatus());
        existSync.setRetryCount(0);
        existSync.setSyncTime(LocalDateTime.now());
        existSync.setErrorMessage(null);
        existSync.setErrorCode(null);
        fileDifyMapper.updateById(existSync);
        
        try {
            // 如果已有文档ID，则更新文档
            if (StrUtil.isNotBlank(existSync.getDifyDocumentId())) {
                DocumentRespDTO documentRespDTO = doUpdateFileToDify(file, existSync.getDatasetId(), existSync.getDifyDocumentId());
                
                // 更新同步记录 - 同步中
                existSync.setBatchId(documentRespDTO.getBatch());
                existSync.setSyncStatus(DifySyncStatusEnum.SYNCING.getStatus());
                fileDifyMapper.updateById(existSync);
                
                // 检查索引状态 - 如果索引完成，则更新状态为已同步
                checkAndUpdateIndexingStatus(existSync);
            } else {
                // 没有文档ID，则创建新文档
                DocumentRespDTO documentRespDTO = doSyncFileToDify(file, existSync.getDatasetId());
                
                // 更新同步记录 - 同步中
                existSync.setDifyDocumentId(documentRespDTO.getId());
                existSync.setBatchId(documentRespDTO.getBatch());
                existSync.setSyncStatus(DifySyncStatusEnum.SYNCING.getStatus());
                fileDifyMapper.updateById(existSync);
                
                // 检查索引状态 - 如果索引完成，则更新状态为已同步
                checkAndUpdateIndexingStatus(existSync);
            }
        } catch (Exception e) {
            log.error("[updateFileSync][文件({})更新同步到Dify失败]", file.getId(), e);
            // 更新同步记录 - 同步失败
            existSync.setSyncStatus(DifySyncStatusEnum.SYNC_FAILED.getStatus());
            existSync.setErrorMessage(e.getMessage());
            if (e instanceof DifyApiException) {
                existSync.setErrorCode(((DifyApiException) e).getErrorCode());
            }
            fileDifyMapper.updateById(existSync);
        }
        
        return existSync;
    }

    @Override
    public void deleteFileSync(Long fileId) {
        // 获取同步记录
        FileDifyDO fileDify = fileDifyMapper.selectByFileId(fileId);
        if (fileDify == null) {
            return;
        }
        
        // 删除Dify文档
        try {
            if (StrUtil.isNotBlank(fileDify.getDifyDocumentId())) {
                difyClient.deleteDocument(fileDify.getDatasetId(), fileDify.getDifyDocumentId());
            }
        } catch (Exception e) {
            // 删除失败，记录日志，但不影响后续流程
            log.error("[deleteFileSync][删除Dify文档({})失败]", fileDify.getDifyDocumentId(), e);
        }
        
        // 删除同步记录
        fileDifyMapper.deleteById(fileDify.getId());
    }

    @Override
    public FileDifyDO getFileSyncStatus(Long fileId) {
        return fileDifyMapper.selectByFileId(fileId);
    }

    @Override
    public PageResult<FileDifyDO> getFileSyncPage(FileDifySyncPageReqVO pageReqVO) {
        return fileDifyMapper.selectPage(pageReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<FileDifyDO> batchSyncFiles(List<Long> fileIds, String datasetId) {
        if (CollUtil.isEmpty(fileIds)) {
            return new ArrayList<>();
        }
        
        List<FileDifyDO> result = new ArrayList<>();
        for (Long fileId : fileIds) {
            try {
                FileDifyDO fileDifyDO = syncFileToDataset(fileId, datasetId);
                result.add(fileDifyDO);
            } catch (Exception e) {
                log.error("[batchSyncFiles][文件({})同步失败]", fileId, e);
                // 继续处理下一个文件
            }
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileDifyDO triggerSync(Long fileId) {
        // 参数校验
        FileDO file = validateAndGetFile(fileId);
        
        // 获取同步记录
        FileDifyDO fileDify = fileDifyMapper.selectByFileId(fileId);
        if (fileDify == null) {
            // 不存在同步记录，创建新记录
            return syncFileToDataset(fileId, null);
        }
        
        // 如果正在同步中，不进行操作
        if (DifySyncStatusEnum.SYNCING.getStatus().equals(fileDify.getSyncStatus())) {
            return fileDify;
        }
        
        // 更新同步记录状态
        fileDify.setSyncStatus(DifySyncStatusEnum.SYNCING.getStatus());
        fileDify.setSyncTime(LocalDateTime.now());
        fileDify.setErrorMessage(null);
        fileDify.setErrorCode(null);
        fileDifyMapper.updateById(fileDify);
        
        try {
            // 如果已有文档ID，则更新文档
            if (StrUtil.isNotBlank(fileDify.getDifyDocumentId())) {
                DocumentRespDTO documentRespDTO = doUpdateFileToDify(file, fileDify.getDatasetId(), fileDify.getDifyDocumentId());
                
                // 更新同步记录 - 同步中
                fileDify.setBatchId(documentRespDTO.getBatch());
                fileDify.setSyncStatus(DifySyncStatusEnum.SYNCING.getStatus());
                fileDifyMapper.updateById(fileDify);
                
                // 检查索引状态 - 如果索引完成，则更新状态为已同步
                checkAndUpdateIndexingStatus(fileDify);
            } else {
                // 没有文档ID，则创建新文档
                DocumentRespDTO documentRespDTO = doSyncFileToDify(file, fileDify.getDatasetId());
                
                // 更新同步记录 - 同步中
                fileDify.setDifyDocumentId(documentRespDTO.getId());
                fileDify.setBatchId(documentRespDTO.getBatch());
                fileDify.setSyncStatus(DifySyncStatusEnum.SYNCING.getStatus());
                fileDifyMapper.updateById(fileDify);
                
                // 检查索引状态 - 如果索引完成，则更新状态为已同步
                checkAndUpdateIndexingStatus(fileDify);
            }
        } catch (Exception e) {
            log.error("[triggerSync][文件({})触发同步到Dify失败]", file.getId(), e);
            // 更新同步记录 - 同步失败
            fileDify.setSyncStatus(DifySyncStatusEnum.SYNC_FAILED.getStatus());
            fileDify.setErrorMessage(e.getMessage());
            if (e instanceof DifyApiException) {
                fileDify.setErrorCode(((DifyApiException) e).getErrorCode());
            }
            fileDifyMapper.updateById(fileDify);
        }
        
        return fileDify;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileDifyDO retrySync(Long fileId) {
        // 参数校验
        FileDO file = validateAndGetFile(fileId);
        
        // 获取同步记录
        FileDifyDO fileDify = fileDifyMapper.selectByFileId(fileId);
        if (fileDify == null) {
            // 不存在同步记录，创建新记录
            return syncFileToDataset(fileId, null);
        }
        
        // 如果正在同步中，不进行操作
        if (DifySyncStatusEnum.SYNCING.getStatus().equals(fileDify.getSyncStatus())) {
            return fileDify;
        }
        
        // 增加重试次数
        fileDify.setRetryCount(fileDify.getRetryCount() + 1);
        fileDify.setSyncStatus(DifySyncStatusEnum.SYNCING.getStatus());
        fileDify.setSyncTime(LocalDateTime.now());
        fileDify.setErrorMessage(null);
        fileDify.setErrorCode(null);
        fileDify.setNextRetryTime(null); // 清空重试时间
        fileDifyMapper.updateById(fileDify);
        
        try {
            // 如果已有文档ID，则更新文档
            if (StrUtil.isNotBlank(fileDify.getDifyDocumentId())) {
                DocumentRespDTO documentRespDTO = doUpdateFileToDify(file, fileDify.getDatasetId(), fileDify.getDifyDocumentId());
                
                // 更新同步记录 - 同步中
                fileDify.setBatchId(documentRespDTO.getBatch());
                fileDify.setSyncStatus(DifySyncStatusEnum.SYNCING.getStatus());
                fileDifyMapper.updateById(fileDify);
                
                // 检查索引状态 - 如果索引完成，则更新状态为已同步
                checkAndUpdateIndexingStatus(fileDify);
            } else {
                // 没有文档ID，则创建新文档
                DocumentRespDTO documentRespDTO = doSyncFileToDify(file, fileDify.getDatasetId());
                
                // 更新同步记录 - 同步中
                fileDify.setDifyDocumentId(documentRespDTO.getId());
                fileDify.setBatchId(documentRespDTO.getBatch());
                fileDify.setSyncStatus(DifySyncStatusEnum.SYNCING.getStatus());
                fileDifyMapper.updateById(fileDify);
                
                // 检查索引状态 - 如果索引完成，则更新状态为已同步
                checkAndUpdateIndexingStatus(fileDify);
            }
        } catch (Exception e) {
            log.error("[retrySync][文件({})重试同步到Dify失败]", file.getId(), e);
            // 更新同步记录 - 同步失败
            fileDify.setSyncStatus(DifySyncStatusEnum.SYNC_FAILED.getStatus());
            fileDify.setErrorMessage(e.getMessage());
            if (e instanceof DifyApiException) {
                fileDify.setErrorCode(((DifyApiException) e).getErrorCode());
            }
            
            // 设置下次重试时间
            calculateNextRetryTime(fileDify);
            
            fileDifyMapper.updateById(fileDify);
        }
        
        return fileDify;
    }

    @Override
    @Async
    public void asyncSyncFile(Long fileId, String datasetId) {
        try {
            log.info("[asyncSyncFile][开始异步同步文件({}) 到知识库({})]", fileId, datasetId);
            
            // 校验文件是否存在
            FileDO file = validateAndGetFile(fileId);
            
            // 查询或创建同步记录
            FileDifyDO fileDifyDO = syncFileToDataset(fileId, datasetId);
            
            // 如果同步记录表示等待创建知识库，则暂时跳过，等待下一次定时任务处理
            if ("pending_creation".equals(fileDifyDO.getDatasetId())) {
                log.info("[asyncSyncFile][文件({})同步任务等待知识库创建，将在定时任务中重试]", fileId);
                return;
            }
            
            // 如果同步状态不是待同步，则不处理
            if (!DifySyncStatusEnum.TO_SYNC.getStatus().equals(fileDifyDO.getSyncStatus())) {
                log.info("[asyncSyncFile][文件({})同步任务状态为{}，不需要处理]", fileId, fileDifyDO.getSyncStatus());
                return;
            }
            
            // 更新为同步中状态
            fileDifyDO.setSyncStatus(DifySyncStatusEnum.SYNCING.getStatus());
            fileDifyDO.setSyncTime(LocalDateTime.now());
            fileDifyMapper.updateById(fileDifyDO);
            
            try {
                // 执行同步
                DocumentRespDTO documentRespDTO = doSyncFileToDify(file, fileDifyDO.getDatasetId());
                
                // 更新同步记录 - 维持同步中状态，同时更新文档ID和批处理ID
                fileDifyDO.setDifyDocumentId(documentRespDTO.getId());
                fileDifyDO.setBatchId(documentRespDTO.getBatch());
                fileDifyMapper.updateById(fileDifyDO);
                
                // 检查索引状态
                checkAndUpdateIndexingStatus(fileDifyDO);
                
                log.info("[asyncSyncFile][文件({})同步到知识库({})成功，文档ID={}，批处理ID={}]", 
                        fileId, fileDifyDO.getDatasetId(), documentRespDTO.getId(), documentRespDTO.getBatch());
            } catch (Exception e) {
                // 更新同步记录 - 同步失败
                fileDifyDO.setSyncStatus(DifySyncStatusEnum.SYNC_FAILED.getStatus());
                
                // 根据不同类型的异常设置不同的错误信息和代码
                if (e instanceof DifyApiException) {
                    DifyApiException difyException = (DifyApiException) e;
                    fileDifyDO.setErrorCode(difyException.getErrorCode());
                    String errorMessage = difyException.getMessage();
                    
                    // 特殊处理非JSON响应错误
                    if ("non_json_response".equals(difyException.getErrorCode())) {
                        errorMessage = "Dify API返回非JSON格式响应，可能服务不可用或URL配置错误。请检查配置: " + difyProperties.getBaseUrl();
                        log.error("[asyncSyncFile][文件({})同步失败，收到非JSON响应: {}]", 
                                fileId, difyException.getMessage());
                    }
                    
                    fileDifyDO.setErrorMessage(errorMessage);
                } else {
                    fileDifyDO.setErrorCode("sync_error");
                    fileDifyDO.setErrorMessage(e.getMessage());
                }
                
                // 设置重试策略
                calculateNextRetryTime(fileDifyDO);
                
                fileDifyMapper.updateById(fileDifyDO);
                log.error("[asyncSyncFile][文件({})同步到知识库({})失败: {}]", 
                        fileId, fileDifyDO.getDatasetId(), fileDifyDO.getErrorMessage(), e);
            }
        } catch (Exception e) {
            log.error("[asyncSyncFile][文件({})同步过程中发生未捕获异常]", fileId, e);
        }
    }

    @Override
    @Scheduled(fixedDelay = 60000) // 60秒执行一次
    public void syncCheckTask() {
        if (!difyProperties.getEnabled() || !difyProperties.getAutoSync()) {
            // 如果未启用或禁用自动同步，则不执行任务
            return;
        }

        log.info("[syncCheckTask][开始执行Dify知识库同步检查任务]");
        
        // 检查Dify配置
        String apiKey = difyProperties.getApiKey();
        String baseUrl = difyProperties.getBaseUrl();
        if (StrUtil.isBlank(apiKey)) {
            log.error("[syncCheckTask][Dify API密钥未配置，无法执行同步任务]");
            return;
        }
        
        log.info("[syncCheckTask][尝试连接Dify API，baseUrl={}]", baseUrl);
        
        try {
            // 测试API连接
            try {
                DatasetListRespDTO datasets = difyClient.getDatasets(1, 1);
                log.info("[syncCheckTask][成功连接Dify API，获取知识库列表]");
            } catch (Exception e) {
                log.error("[syncCheckTask][连接Dify API失败: {}]", e.getMessage(), e);
                log.error("[syncCheckTask][请检查baseUrl({})和apiKey({})配置是否正确，以及网络连接是否正常]", 
                         baseUrl, StrUtil.hide(apiKey, 3, apiKey.length() - 3));
                return;
            }
            
            // API密钥前缀检查
            if (StrUtil.startWithIgnoreCase(apiKey, "dataset-")) {
                log.warn("[syncCheckTask][当前使用数据集级别API密钥(dataset-*)，无法创建知识库，只能使用已存在的知识库]");
                // 检查是否配置了默认知识库ID
                if (StrUtil.isBlank(difyProperties.getDefaultDatasetId())) {
                    log.error("[syncCheckTask][使用数据集级别API密钥时必须配置default-dataset-id，否则无法同步文件]");
                    return;
                }
                // 验证默认知识库ID与API密钥是否匹配
                if (!difyProperties.getDefaultDatasetId().equals(apiKey)) {
                    log.warn("[syncCheckTask][默认知识库ID与API密钥不匹配，建议将default-dataset-id设置为与api-key相同的值]");
                }
            }
            
            // 记录当前配置信息，便于排查问题
            log.info("[syncCheckTask][Dify配置：baseUrl={}, apiKey类型={}, defaultDatasetId={}]", 
                    baseUrl,
                    apiKey.startsWith("app-") ? "应用程序级别(app-)" : 
                    apiKey.startsWith("dataset-") ? "数据集级别(dataset-)" : "未知类型",
                    StrUtil.isNotBlank(difyProperties.getDefaultDatasetId()) ? difyProperties.getDefaultDatasetId() : "未配置");
            
            // 记录当前任务开始时间
            LocalDateTime startTime = LocalDateTime.now();
            
            try {
                // 获取所有租户
                List<Long> tenantIds = new ArrayList<>();
                try {
                    tenantIds = TenantUtils.getTenantIds(); 
                } catch (NullPointerException e) {
                    // 处理TenantUtils.tenantProperties为null的情况
                    log.warn("[syncCheckTask][获取租户ID时发生NullPointerException，将使用默认租户ID 1]");
                    tenantIds.add(1L); // 添加默认租户ID
                }
                
                log.info("[syncCheckTask][共有{}个租户需要处理]", tenantIds.size());
                
                // 遍历每个租户，处理其待同步的记录
                for (Long tenantId : tenantIds) {
                    try {
                        // 尝试使用TenantUtils.execute
                        TenantUtils.execute(tenantId, () -> {
                            // 执行同步任务
                            try {
                                processSyncRecords(tenantId);
                            } catch (Exception e) {
                                log.error("[syncCheckTask][租户({})同步任务异常]", tenantId, e);
                            }
                        });
                    } catch (Exception e) {
                        log.error("[syncCheckTask][租户({})TenantUtils.execute执行异常，直接处理同步记录]", tenantId, e);
                        // 直接处理，不使用TenantUtils.execute
                        try {
                            processSyncRecords(tenantId);
                        } catch (Exception ex) {
                            log.error("[syncCheckTask][租户({})直接处理同步记录异常]", tenantId, ex);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("[syncCheckTask][Dify知识库同步检查任务异常]", e);
            } finally {
                // 计算任务耗时
                long costMillis = Duration.between(startTime, LocalDateTime.now()).toMillis();
                log.info("[syncCheckTask][Dify知识库同步检查任务完成，耗时: {}ms]", costMillis);
            }
        } catch (Exception e) {
            log.error("[syncCheckTask][Dify知识库同步检查任务异常]", e);
        }
    }

    /**
     * 创建默认知识库
     * 
     * @param name 知识库名称
     * @return 创建的知识库ID
     * @throws Exception 创建异常
     */
    private String createDefaultDataset(String name) throws Exception {
        log.info("[createDefaultDataset][开始创建知识库: {}]", name);
        try {
            if (StrUtil.isBlank(name)) {
                name = "Default_" + RandomUtil.randomString(8);
                log.info("[createDefaultDataset][知识库名称为空，使用默认名称: {}]", name);
            }
            
            // 记录请求详情便于排查问题
            String apiKey = difyProperties.getApiKey();
            log.info("[createDefaultDataset][API请求: baseUrl={}, apiKey={}, apiKeyType={}]", 
                     difyProperties.getBaseUrl(), 
                     StrUtil.hide(apiKey, 3, apiKey.length() - 3),
                     apiKey.startsWith("app-") ? "应用程序级别(app-)" : apiKey.startsWith("dataset-") ? "数据集级别(dataset-)" : "未知类型");
            
            // 使用索引技术参数
            DifyCreateDatasetRequest request = new DifyCreateDatasetRequest();
            request.setName(name);
            request.setPermission("only_me");
            request.setIndexingTechnique(difyProperties.getIndexingTechnique());
            
            // 调用API创建知识库
            String datasetId = difyClient.createDataset(request);
            
            if (StrUtil.isEmpty(datasetId)) {
                throw new Exception("API返回的知识库ID为空");
            }
            
            log.info("[createDefaultDataset][知识库({})创建成功，ID: {}]", name, datasetId);
            return datasetId;
        } catch (Exception e) {
            log.error("[createDefaultDataset][创建默认知识库({})失败: {}]", name, e.getMessage());
            throw new Exception(String.format("创建默认知识库(%s)失败: %s", name, e.getMessage()), e);
        }
    }

    /**
     * 执行文件同步到Dify
     *
     * @param file 文件对象
     * @param datasetId 知识库ID
     * @return 文档响应DTO
     * @throws Exception 同步异常
     */
    private DocumentRespDTO doSyncFileToDify(FileDO file, String datasetId) throws Exception {
        // 获取文件
        File localFile = null;
        try {
            byte[] fileContent = fileService.getFileContent(file.getConfigId(), file.getPath());
            if (fileContent == null) {
                throw ServiceExceptionUtil.exception(FILE_NOT_EXISTS);
            }
            
            // 创建临时文件
            String tempFileName = file.getName();
            if (StrUtil.isEmpty(tempFileName)) {
                tempFileName = "temp_" + IdUtil.fastSimpleUUID() + "." + FileUtil.extName(file.getPath());
            }
            // 使用FileUtils创建临时文件，然后重命名
            localFile = FileUtils.createTempFile(fileContent);
            // 重命名临时文件以设置正确的文件名
            File renamedFile = new File(localFile.getParent(), tempFileName);
            FileUtil.move(localFile, renamedFile, true);
            localFile = renamedFile;
            
            // 检查文件类型
            String fileType = FileUtil.extName(file.getPath());
            
            // 创建元数据
            Map<String, Object> metadata = createFileMetadata(file);
            
            // 创建文档请求对象，使用createDefault方法
            DocumentFileReqDTO fileReqDTO = DocumentFileReqDTO.createDefault(difyProperties.getIndexingTechnique(), metadata);
            
            // 调用Dify API创建文档
            return difyClient.createDocumentByFile(datasetId, localFile, fileReqDTO);
        } finally {
            // 清理临时文件
            if (localFile != null && localFile.exists()) {
                FileUtil.del(localFile);
            }
        }
    }

    /**
     * 更新文件到Dify
     *
     * @param file 文件对象
     * @param datasetId 知识库ID
     * @param documentId 文档ID
     * @return 文档响应DTO
     * @throws Exception 同步异常
     */
    private DocumentRespDTO doUpdateFileToDify(FileDO file, String datasetId, String documentId) throws Exception {
        // 获取文件
        File localFile = null;
        try {
            byte[] fileContent = fileService.getFileContent(file.getConfigId(), file.getPath());
            if (fileContent == null) {
                throw ServiceExceptionUtil.exception(FILE_NOT_EXISTS);
            }
            
            // 创建临时文件
            String tempFileName = file.getName();
            if (StrUtil.isEmpty(tempFileName)) {
                tempFileName = "temp_" + IdUtil.fastSimpleUUID() + "." + FileUtil.extName(file.getPath());
            }
            // 使用FileUtils创建临时文件，然后重命名
            localFile = FileUtils.createTempFile(fileContent);
            // 重命名临时文件以设置正确的文件名
            File renamedFile = new File(localFile.getParent(), tempFileName);
            FileUtil.move(localFile, renamedFile, true);
            localFile = renamedFile;
            
            // 创建元数据
            Map<String, Object> metadata = createFileMetadata(file);
            
            // 创建文档请求对象，使用createDefault方法
            DocumentFileReqDTO fileReqDTO = DocumentFileReqDTO.createDefault(difyProperties.getIndexingTechnique(), metadata);
            
            // 调用Dify API更新文档
            return difyClient.updateDocumentByFile(datasetId, documentId, localFile, fileReqDTO);
        } finally {
            // 清理临时文件
            if (localFile != null && localFile.exists()) {
                FileUtil.del(localFile);
            }
        }
    }

    /**
     * 创建文件元数据
     *
     * @param file 文件对象
     * @return 元数据Map
     */
    private Map<String, Object> createFileMetadata(FileDO file) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("file_id", file.getId().toString());
        metadata.put("file_name", file.getName());
        metadata.put("file_type", file.getType());
        metadata.put("file_size", file.getSize());
        metadata.put("create_time", file.getCreateTime().toString());
        
        // 可以添加更多元数据
        
        return metadata;
    }

    /**
     * 检查和更新索引状态
     *
     * @param fileDifyDO 文件Dify关联对象
     */
    private void checkAndUpdateIndexingStatus(FileDifyDO fileDifyDO) {
        // 检查批处理ID是否存在
        if (StrUtil.isBlank(fileDifyDO.getBatchId()) || StrUtil.isBlank(fileDifyDO.getDatasetId())) {
            return;
        }
        
        try {
            // 调用API获取索引状态
            IndexingStatusRespDTO statusRespDTO = difyClient.getDocumentIndexingStatus(
                    fileDifyDO.getDatasetId(), fileDifyDO.getBatchId());
            
            // 根据状态更新记录
            switch (statusRespDTO.getStatus()) {
                case "completed":
                    // 索引完成，更新状态为已同步
                    fileDifyDO.setSyncStatus(DifySyncStatusEnum.SYNCED.getStatus());
                    fileDifyDO.setSyncTime(LocalDateTime.now());
                    fileDifyDO.setErrorMessage(null);
                    fileDifyDO.setErrorCode(null);
                    fileDifyMapper.updateById(fileDifyDO);
                    break;
                case "error":
                    // 索引错误，更新状态为同步失败
                    fileDifyDO.setSyncStatus(DifySyncStatusEnum.SYNC_FAILED.getStatus());
                    fileDifyDO.setErrorMessage("索引失败: " + statusRespDTO.getError());
                    fileDifyDO.setErrorCode("indexing_error");
                    
                    // 设置下次重试时间
                    calculateNextRetryTime(fileDifyDO);
                    
                    fileDifyMapper.updateById(fileDifyDO);
                    break;
                default:
                    // 其他状态（等待中、解析中、索引中等），保持同步中状态
                    break;
            }
        } catch (Exception e) {
            log.error("[checkAndUpdateIndexingStatus][检查文件({})索引状态异常]", fileDifyDO.getFileId(), e);
            // 出现异常不更改状态
        }
    }

    /**
     * 计算下次重试时间
     *
     * @param fileDifyDO 文件Dify关联对象
     */
    private void calculateNextRetryTime(FileDifyDO fileDifyDO) {
        if (fileDifyDO.getRetryCount() >= difyProperties.getRetry().getMaxRetries()) {
            // 超过最大重试次数，不再设置重试时间
            fileDifyDO.setNextRetryTime(null);
            return;
        }
        
        // 计算重试时间间隔：initialRetryInterval * (retryMultiplier ^ retryCount)
        double interval = difyProperties.getRetry().getInitialRetryInterval() * 
                Math.pow(difyProperties.getRetry().getRetryMultiplier(), fileDifyDO.getRetryCount());
        
        // 设置下次重试时间
        fileDifyDO.setNextRetryTime(LocalDateTime.now().plusSeconds((long) interval / 1000));
    }

    /**
     * 校验并获取文件
     *
     * @param fileId 文件ID
     * @return 文件对象
     */
    private FileDO validateAndGetFile(Long fileId) {
        // 使用fileMapper查询文件
        FileDO file = fileMapper.selectById(fileId);
        if (file == null) {
            throw ServiceExceptionUtil.exception(FILE_NOT_EXISTS);
        }
        return file;
    }

    /**
     * 创建"待创建知识库"状态的同步记录
     *
     * @param fileId 文件ID
     * @return 同步记录
     */
    private FileDifyDO createPendingDatasetRecord(Long fileId) {
        FileDifyDO fileDifyDO = new FileDifyDO();
        fileDifyDO.setFileId(fileId);
        fileDifyDO.setSyncStatus(DifySyncStatusEnum.SYNC_FAILED.getStatus());
        fileDifyDO.setErrorMessage("自动创建默认知识库失败: 请配置有效的API密钥和数据集ID");
        fileDifyDO.setErrorCode("default_dataset_creation_failed");
        fileDifyDO.setDatasetId("pending_creation");  // 标记为等待创建
        fileDifyDO.setRetryCount(0);
        fileDifyDO.setSyncTime(LocalDateTime.now());
        
        // 计算下次重试时间
        LocalDateTime nextRetryTime = LocalDateTime.now().plusMinutes(5); // 5分钟后重试
        fileDifyDO.setNextRetryTime(nextRetryTime);
        
        // 保存记录
        fileDifyMapper.insert(fileDifyDO);
        log.warn("[createPendingDatasetRecord][文件({})同步任务已创建，但状态为失败：{}]", fileId, fileDifyDO.getErrorMessage());
        return fileDifyDO;
    }

    /**
     * 处理租户内的同步记录
     *
     * @param tenantId 租户ID
     */
    private void processSyncRecords(Long tenantId) {
        log.info("[processSyncRecords][开始处理租户({})同步记录]", tenantId);
        
        try {
            // 检查是否存在pending_creation的记录，且默认知识库已配置，则更新这些记录
            String defaultDatasetId = difyProperties.getDefaultDatasetId();
            if (StrUtil.isNotBlank(defaultDatasetId)) {
                List<FileDifyDO> pendingRecords = fileDifyMapper.selectListByDatasetId("pending_creation");
                log.info("[processSyncRecords][租户({})发现{}条等待知识库创建的记录]", tenantId, pendingRecords.size());
                
                for (FileDifyDO record : pendingRecords) {
                    try {
                        log.info("[processSyncRecords][租户({})更新文件({})的知识库ID为默认知识库ID: {}]", 
                                tenantId, record.getFileId(), defaultDatasetId);
                        record.setDatasetId(defaultDatasetId);
                        record.setSyncStatus(DifySyncStatusEnum.TO_SYNC.getStatus());
                        record.setSyncTime(LocalDateTime.now());
                        record.setErrorMessage(null);
                        record.setErrorCode(null);
                        fileDifyMapper.updateById(record);
                        
                        // 触发同步
                        asyncSyncFile(record.getFileId(), defaultDatasetId);
                    } catch (Exception e) {
                        log.error("[processSyncRecords][租户({})更新知识库ID并同步文件({})失败]", tenantId, record.getFileId(), e);
                    }
                }
            } else {
                // 如果默认知识库未配置，但存在pending_creation的记录，尝试创建一个默认知识库
                List<FileDifyDO> pendingRecords = fileDifyMapper.selectListByDatasetId("pending_creation");
                if (!pendingRecords.isEmpty()) {
                    log.info("[processSyncRecords][租户({})发现{}条等待知识库创建的记录，但未配置默认知识库]", 
                            tenantId, pendingRecords.size());
                    
                    try {
                        String defaultDatasetName = "Default_" + RandomUtil.randomString(8);
                        log.info("[processSyncRecords][租户({})尝试创建默认知识库: {}]", tenantId, defaultDatasetName);
                        String newDatasetId = createDefaultDataset(defaultDatasetName);
                        if (StrUtil.isNotBlank(newDatasetId)) {
                            // 保存到配置中
                            difyProperties.setDefaultDatasetId(newDatasetId);
                            log.info("[processSyncRecords][租户({})成功创建默认知识库：{}，ID：{}]", 
                                    tenantId, defaultDatasetName, newDatasetId);
                            
                            // 递归调用自己，让下一个循环处理pending_creation记录
                            processSyncRecords(tenantId);
                        }
                    } catch (Exception e) {
                        log.error("[processSyncRecords][租户({})自动创建默认知识库失败，将在下次定时任务中重试]", tenantId, e);
                    }
                }
            }
            
            // 处理待同步的记录
            List<FileDifyDO> toSyncRecords = fileDifyMapper.selectListByStatus(DifySyncStatusEnum.TO_SYNC.getStatus());
            if (!toSyncRecords.isEmpty()) {
                log.info("[processSyncRecords][租户({})发现{}条待同步记录]", tenantId, toSyncRecords.size());
                
                for (FileDifyDO record : toSyncRecords) {
                    try {
                        log.info("[processSyncRecords][租户({})开始同步文件({})]", tenantId, record.getFileId());
                        asyncSyncFile(record.getFileId(), record.getDatasetId());
                    } catch (Exception e) {
                        log.error("[processSyncRecords][租户({})同步文件({})失败]", tenantId, record.getFileId(), e);
                    }
                }
            }
            
            // 处理同步中的记录
            List<FileDifyDO> syncingRecords = fileDifyMapper.selectListByStatus(DifySyncStatusEnum.SYNCING.getStatus());
            if (!syncingRecords.isEmpty()) {
                log.info("[processSyncRecords][租户({})发现{}条同步中记录]", tenantId, syncingRecords.size());
                
                for (FileDifyDO record : syncingRecords) {
                    try {
                        checkAndUpdateIndexingStatus(record);
                    } catch (Exception e) {
                        log.error("[processSyncRecords][租户({})检查文件({})索引状态失败]", tenantId, record.getFileId(), e);
                    }
                }
            }
            
            // 处理同步失败但需要重试的记录
            List<FileDifyDO> failedRecords = fileDifyMapper.selectListByStatus(DifySyncStatusEnum.SYNC_FAILED.getStatus());
            if (!failedRecords.isEmpty()) {
                log.info("[processSyncRecords][租户({})发现{}条失败记录]", tenantId, failedRecords.size());
                
                for (FileDifyDO record : failedRecords) {
                    // 检查是否有设置下次重试时间，且已到达重试时间
                    if (record.getNextRetryTime() != null && record.getNextRetryTime().isBefore(LocalDateTime.now())) {
                        try {
                            log.info("[processSyncRecords][租户({})开始重试同步文件({})]", tenantId, record.getFileId());
                            retrySync(record.getFileId());
                        } catch (Exception e) {
                            log.error("[processSyncRecords][租户({})重试同步文件({})失败]", tenantId, record.getFileId(), e);
                        }
                    }
                }
            }
            
            log.info("[processSyncRecords][租户({})同步记录处理完成]", tenantId);
        } catch (Exception e) {
            log.error("[processSyncRecords][租户({})处理同步记录异常]", tenantId, e);
        }
    }

    /**
     * 创建文件同步记录
     *
     * @param fileId 文件ID
     * @return 同步记录ID
     */
    @Override
    public Long createSyncRecord(Long fileId) {
        return createSyncRecord(fileId, difyProperties.getDefaultDatasetId());
    }

    /**
     * 创建文件同步记录，使用指定的知识库ID
     *
     * @param fileId 文件ID
     * @param datasetId 知识库ID
     * @return 同步记录ID
     */
    @Override
    public Long createSyncRecord(Long fileId, String datasetId) {
        FileDO file = fileService.getFile(fileId);
        if (file == null) {
            throw ServiceExceptionUtil.exception(FILE_NOT_EXISTS);
        }

        // 创建同步记录
        FileDifyDO fileDifyDO = new FileDifyDO();
        fileDifyDO.setFileId(fileId);
        fileDifyDO.setDatasetId(datasetId);
        fileDifyDO.setSyncStatus(DifySyncStatusEnum.TO_SYNC.getStatus());
        fileDifyDO.setRetryCount(0);
        fileDifyDO.setSyncTime(LocalDateTime.now());
        fileDifyMapper.insert(fileDifyDO);
        return fileDifyDO.getId();
    }
} 