package cn.iocoder.yudao.module.infra.dal.mysql.file;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.dify.FileDifySyncPageReqVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.file.FileDifyDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文件与Dify知识库关联 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface FileDifyMapper extends BaseMapperX<FileDifyDO> {

    default PageResult<FileDifyDO> selectPage(FileDifySyncPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<FileDifyDO>()
                .eqIfPresent(FileDifyDO::getFileId, reqVO.getFileId())
                .eqIfPresent(FileDifyDO::getDifyDocumentId, reqVO.getDifyDocumentId())
                .eqIfPresent(FileDifyDO::getSyncStatus, reqVO.getSyncStatus())
                .eqIfPresent(FileDifyDO::getDatasetId, reqVO.getDatasetId())
                .likeIfPresent(FileDifyDO::getErrorMessage, reqVO.getErrorMessage())
                .orderByDesc(FileDifyDO::getId));
    }

    default FileDifyDO selectByFileId(Long fileId) {
        return selectOne(FileDifyDO::getFileId, fileId);
    }

    default FileDifyDO selectByDifyDocumentId(String difyDocumentId) {
        return selectOne(FileDifyDO::getDifyDocumentId, difyDocumentId);
    }

    default List<FileDifyDO> selectListByStatus(Integer status) {
        return selectList(FileDifyDO::getSyncStatus, status);
    }

    /**
     * 根据知识库ID查询记录列表
     *
     * @param datasetId 知识库ID
     * @return 记录列表
     */
    List<FileDifyDO> selectListByDatasetId(@Param("datasetId") String datasetId);

    default List<FileDifyDO> selectListByFileId(Long fileId) {
        return selectList(FileDifyDO::getFileId, fileId);
    }
} 