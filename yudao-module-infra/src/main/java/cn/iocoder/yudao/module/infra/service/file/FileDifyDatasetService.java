package cn.iocoder.yudao.module.infra.service.file;

import cn.iocoder.yudao.module.infra.controller.admin.file.vo.dify.DifyDatasetCreateReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.dify.DifyDatasetPageReqVO;
import cn.iocoder.yudao.module.infra.framework.dify.dto.response.DatasetListRespDTO;

/**
 * Dify知识库 Service 接口
 */
public interface FileDifyDatasetService {

    /**
     * 获得Dify知识库分页
     *
     * @param pageReqVO 分页查询
     * @return Dify知识库分页
     */
    DatasetListRespDTO getDatasetPage(DifyDatasetPageReqVO pageReqVO);

    /**
     * 创建Dify知识库
     *
     * @param createReqVO 创建信息
     * @return 知识库ID
     */
    String createDataset(DifyDatasetCreateReqVO createReqVO);

    /**
     * 删除Dify知识库
     *
     * @param id 知识库ID
     */
    void deleteDataset(String id);
} 