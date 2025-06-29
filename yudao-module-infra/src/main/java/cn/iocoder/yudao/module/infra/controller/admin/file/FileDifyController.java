package cn.iocoder.yudao.module.infra.controller.admin.file;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.dify.DifyConfigRespVO;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.dify.DifyConfigUpdateReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.dify.FileDifySyncPageReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.dify.FileDifySyncRespVO;
import cn.iocoder.yudao.module.infra.convert.file.FileDifyConvert;
import cn.iocoder.yudao.module.infra.dal.dataobject.file.FileDifyDO;
import cn.iocoder.yudao.module.infra.dal.mysql.file.FileMapper;
import cn.iocoder.yudao.module.infra.framework.dify.config.DifyProperties;
import cn.iocoder.yudao.module.infra.service.file.FileDifyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * 管理后台 - 文件Dify同步
 */
@Tag(name = "管理后台 - 文件Dify同步")
@RestController
@RequestMapping("/infra/file-dify")
@Validated
public class FileDifyController {

    @Resource
    private FileDifyService fileDifyService;
    
    @Resource
    private DifyProperties difyProperties;
    
    @Resource
    private FileMapper fileMapper;

    @PostMapping("/sync")
    @Operation(summary = "同步文件到Dify知识库")
    @PreAuthorize("@ss.hasPermission('infra:file:sync')")
    public CommonResult<FileDifySyncRespVO> syncFile(@RequestParam("fileId") Long fileId,
                                                    @RequestParam(value = "datasetId", required = false) String datasetId) {
        FileDifyDO fileDifyDO = fileDifyService.syncFileToDataset(fileId, datasetId);
        FileDifySyncRespVO respVO = FileDifyConvert.INSTANCE.convert(fileDifyDO);
        FileDifyConvert.INSTANCE.fillFileName(fileMapper, fileDifyDO, respVO);
        return success(respVO);
    }
    
    @PostMapping("/batch-sync")
    @Operation(summary = "批量同步文件到Dify知识库")
    @PreAuthorize("@ss.hasPermission('infra:file:sync')")
    public CommonResult<List<FileDifySyncRespVO>> batchSyncFiles(@RequestParam("fileIds") List<Long> fileIds,
                                                               @RequestParam(value = "datasetId", required = false) String datasetId) {
        List<FileDifyDO> fileDifyDOList = fileDifyService.batchSyncFiles(fileIds, datasetId);
        List<FileDifySyncRespVO> respVOList = FileDifyConvert.INSTANCE.convertList(fileDifyDOList);
        FileDifyConvert.INSTANCE.fillFileNames(fileMapper, fileDifyDOList, respVOList);
        return success(respVOList);
    }
    
    @PutMapping("/update-sync")
    @Operation(summary = "更新文件同步")
    @PreAuthorize("@ss.hasPermission('infra:file:sync')")
    public CommonResult<FileDifySyncRespVO> updateFileSync(@RequestParam("fileId") Long fileId,
                                                         @RequestParam(value = "datasetId", required = false) String datasetId) {
        FileDifyDO fileDifyDO = fileDifyService.updateFileSync(fileId, datasetId);
        FileDifySyncRespVO respVO = FileDifyConvert.INSTANCE.convert(fileDifyDO);
        FileDifyConvert.INSTANCE.fillFileName(fileMapper, fileDifyDO, respVO);
        return success(respVO);
    }
    
    @DeleteMapping("/delete-sync")
    @Operation(summary = "删除文件同步")
    @PreAuthorize("@ss.hasPermission('infra:file:sync')")
    public CommonResult<Boolean> deleteFileSync(@RequestParam("fileId") Long fileId) {
        fileDifyService.deleteFileSync(fileId);
        return success(true);
    }
    
    @GetMapping("/get-status")
    @Operation(summary = "获取文件同步状态")
    @Parameter(name = "fileId", description = "文件编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('infra:file:query')")
    public CommonResult<FileDifySyncRespVO> getFileSyncStatus(@RequestParam("fileId") Long fileId) {
        FileDifyDO fileDifyDO = fileDifyService.getFileSyncStatus(fileId);
        FileDifySyncRespVO respVO = FileDifyConvert.INSTANCE.convert(fileDifyDO);
        if (respVO != null) {
            FileDifyConvert.INSTANCE.fillFileName(fileMapper, fileDifyDO, respVO);
        }
        return success(respVO);
    }
    
    @GetMapping("/page")
    @Operation(summary = "获取文件同步分页")
    @PreAuthorize("@ss.hasPermission('infra:file:query')")
    public CommonResult<PageResult<FileDifySyncRespVO>> getFileSyncPage(@Valid FileDifySyncPageReqVO pageVO) {
        PageResult<FileDifyDO> pageResult = fileDifyService.getFileSyncPage(pageVO);
        PageResult<FileDifySyncRespVO> respPageResult = FileDifyConvert.INSTANCE.convertPage(pageResult);
        if (!pageResult.getList().isEmpty()) {
            FileDifyConvert.INSTANCE.fillFileNames(fileMapper, pageResult.getList(), respPageResult.getList());
        }
        return success(respPageResult);
    }
    
    @PostMapping("/trigger-sync")
    @Operation(summary = "触发文件同步")
    @PreAuthorize("@ss.hasPermission('infra:file:sync')")
    public CommonResult<FileDifySyncRespVO> triggerSync(@RequestParam("fileId") Long fileId) {
        FileDifyDO fileDifyDO = fileDifyService.triggerSync(fileId);
        FileDifySyncRespVO respVO = FileDifyConvert.INSTANCE.convert(fileDifyDO);
        FileDifyConvert.INSTANCE.fillFileName(fileMapper, fileDifyDO, respVO);
        return success(respVO);
    }
    
    @PostMapping("/retry-sync")
    @Operation(summary = "重试文件同步")
    @PreAuthorize("@ss.hasPermission('infra:file:sync')")
    public CommonResult<FileDifySyncRespVO> retrySync(@RequestParam("fileId") Long fileId) {
        FileDifyDO fileDifyDO = fileDifyService.retrySync(fileId);
        FileDifySyncRespVO respVO = FileDifyConvert.INSTANCE.convert(fileDifyDO);
        FileDifyConvert.INSTANCE.fillFileName(fileMapper, fileDifyDO, respVO);
        return success(respVO);
    }
    
    @GetMapping("/config")
    @Operation(summary = "获取Dify配置")
    @PreAuthorize("@ss.hasPermission('infra:file:query')")
    public CommonResult<DifyConfigRespVO> getConfig() {
        DifyConfigRespVO configVO = new DifyConfigRespVO();
        configVO.setEnabled(difyProperties.getEnabled());
        configVO.setAutoSync(difyProperties.getAutoSync());
        configVO.setDefaultDatasetId(difyProperties.getDefaultDatasetId());
        configVO.setBaseUrl(difyProperties.getBaseUrl());
        configVO.setChatApiKey(difyProperties.getChatApiKey());
        return success(configVO);
    }
    
    @PutMapping("/config")
    @Operation(summary = "更新Dify配置")
    @PreAuthorize("@ss.hasPermission('infra:file:sync')")
    public CommonResult<Boolean> updateConfig(@Valid @RequestBody DifyConfigUpdateReqVO updateReqVO) {
        // 由于配置是运行时的属性，这里直接更新内存中的值，不需要持久化
        difyProperties.setEnabled(updateReqVO.getEnabled());
        difyProperties.setAutoSync(updateReqVO.getAutoSync());
        if (updateReqVO.getDefaultDatasetId() != null) {
            difyProperties.setDefaultDatasetId(updateReqVO.getDefaultDatasetId());
        }
        if (updateReqVO.getChatApiKey() != null) {
            difyProperties.setChatApiKey(updateReqVO.getChatApiKey());
        }
        return success(true);
    }
} 