package cn.iocoder.yudao.module.infra.service.file;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.dify.FileDifySyncPageReqVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.file.FileDifyDO;
import cn.iocoder.yudao.module.infra.dal.dataobject.file.FileDO;
import cn.iocoder.yudao.module.infra.framework.dify.core.dto.DocumentRespDTO;

import java.io.File;
import java.util.List;

/**
 * 文件Dify服务接口
 *
 * @author 芋道源码
 */
public interface FileDifyService {

    /**
     * 同步文件到Dify知识库
     *
     * @param fileId 文件ID
     * @param datasetId 知识库ID
     * @return 文件Dify关联信息
     */
    FileDifyDO syncFileToDataset(Long fileId, String datasetId);

    /**
     * 更新文件同步到Dify知识库
     *
     * @param fileId 文件ID
     * @param datasetId 知识库ID
     * @return 文件Dify关联信息
     */
    FileDifyDO updateFileSync(Long fileId, String datasetId);

    /**
     * 删除文件同步
     *
     * @param fileId 文件ID
     */
    void deleteFileSync(Long fileId);

    /**
     * 获取文件同步状态
     *
     * @param fileId 文件ID
     * @return 文件Dify关联信息
     */
    FileDifyDO getFileSyncStatus(Long fileId);

    /**
     * 获取文件同步列表
     *
     * @param pageReqVO 分页参数
     * @return 文件Dify关联分页列表
     */
    PageResult<FileDifyDO> getFileSyncPage(FileDifySyncPageReqVO pageReqVO);

    /**
     * 批量同步文件到Dify知识库
     *
     * @param fileIds 文件ID列表
     * @param datasetId 知识库ID
     * @return 文件Dify关联信息列表
     */
    List<FileDifyDO> batchSyncFiles(List<Long> fileIds, String datasetId);

    /**
     * 触发文件同步
     *
     * @param fileId 文件ID
     * @return 文件Dify关联信息
     */
    FileDifyDO triggerSync(Long fileId);

    /**
     * 重试文件同步
     *
     * @param fileId 文件ID
     * @return 文件Dify关联信息
     */
    FileDifyDO retrySync(Long fileId);

    /**
     * 异步同步文件
     * 
     * @param fileId 文件ID
     * @param datasetId 知识库ID
     */
    void asyncSyncFile(Long fileId, String datasetId);

    /**
     * 同步检查任务
     */
    void syncCheckTask();
} 