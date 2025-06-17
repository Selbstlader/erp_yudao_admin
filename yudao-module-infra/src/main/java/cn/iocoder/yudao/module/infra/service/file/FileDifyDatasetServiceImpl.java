package cn.iocoder.yudao.module.infra.service.file;

import cn.iocoder.yudao.module.infra.controller.admin.file.vo.dify.DifyDatasetCreateReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.dify.DifyDatasetPageReqVO;
import cn.iocoder.yudao.module.infra.framework.dify.config.DifyProperties;
import cn.iocoder.yudao.module.infra.framework.dify.core.DifyClient;
import cn.iocoder.yudao.module.infra.framework.dify.dto.request.DifyCreateDatasetRequest;
import cn.iocoder.yudao.module.infra.framework.dify.dto.response.DatasetListRespDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * Dify知识库 Service 实现类
 */
@Service
@Validated
@Slf4j
public class FileDifyDatasetServiceImpl implements FileDifyDatasetService {

    @Resource
    private DifyClient difyClient;

    @Resource
    private DifyProperties difyProperties;

    @Override
    public DatasetListRespDTO getDatasetPage(DifyDatasetPageReqVO pageReqVO) {
        try {
            return difyClient.getDatasets(pageReqVO.getPageNo(), pageReqVO.getPageSize());
        } catch (Exception e) {
            log.error("[getDatasetPage][获取Dify知识库分页异常]", e);
            throw e;
        }
    }

    @Override
    public String createDataset(DifyDatasetCreateReqVO createReqVO) {
        try {
            // 构建请求
            DifyCreateDatasetRequest request = new DifyCreateDatasetRequest();
            request.setName(createReqVO.getName());
            request.setPermission(createReqVO.getPermission());
            request.setIndexingTechnique(createReqVO.getIndexingTechnique());
            
            // 调用API创建
            return difyClient.createDataset(request);
        } catch (Exception e) {
            log.error("[createDataset][创建Dify知识库({})异常]", createReqVO.getName(), e);
            throw e;
        }
    }

    @Override
    public void deleteDataset(String id) {
        try {
            difyClient.deleteDataset(id);
        } catch (Exception e) {
            log.error("[deleteDataset][删除Dify知识库({})异常]", id, e);
            throw e;
        }
    }
} 