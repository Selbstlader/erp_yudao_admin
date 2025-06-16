package cn.iocoder.yudao.module.infra.convert.file;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.dify.FileDifySyncRespVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.file.FileDifyDO;
import cn.iocoder.yudao.module.infra.framework.dify.core.enums.DifySyncStatusEnum;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 文件Dify转换器
 */
@Mapper
public interface FileDifyConvert {

    FileDifyConvert INSTANCE = Mappers.getMapper(FileDifyConvert.class);

    FileDifySyncRespVO convert(FileDifyDO bean);

    List<FileDifySyncRespVO> convertList(List<FileDifyDO> list);

    PageResult<FileDifySyncRespVO> convertPage(PageResult<FileDifyDO> page);

    @AfterMapping
    default void fillSyncStatusName(FileDifyDO from, @MappingTarget FileDifySyncRespVO to) {
        if (from.getSyncStatus() != null) {
            for (DifySyncStatusEnum value : DifySyncStatusEnum.values()) {
                if (value.getStatus().equals(from.getSyncStatus())) {
                    to.setSyncStatusName(value.getName());
                    break;
                }
            }
        }
    }
} 