package cn.iocoder.yudao.module.infra.convert.file;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.dify.ChatMessageReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.dify.ChatMessageRespVO;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.dify.FileDifySyncPageReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.dify.FileDifySyncRespVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.file.FileDO;
import cn.iocoder.yudao.module.infra.dal.dataobject.file.FileDifyDO;
import cn.iocoder.yudao.module.infra.dal.mysql.file.FileMapper;
import cn.iocoder.yudao.module.infra.framework.dify.core.enums.DifySyncStatusEnum;
import cn.iocoder.yudao.module.infra.framework.dify.dto.request.ChatMessageReqDTO;
import cn.iocoder.yudao.module.infra.framework.dify.dto.response.ChatMessageRespDTO;
import jakarta.annotation.Resource;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 文件Dify转换器
 */
@Mapper
public interface FileDifyConvert {

    FileDifyConvert INSTANCE = Mappers.getMapper(FileDifyConvert.class);

    FileDifySyncRespVO convert(FileDifyDO bean);

    List<FileDifySyncRespVO> convertList(List<FileDifyDO> list);

    PageResult<FileDifySyncRespVO> convertPage(PageResult<FileDifyDO> page);

    /**
     * 将聊天消息请求VO转换为DTO
     */
    ChatMessageReqDTO convert(ChatMessageReqVO vo);

    /**
     * 将聊天消息响应DTO转换为VO
     */
    ChatMessageRespVO convert(ChatMessageRespDTO dto);

    /**
     * 将引用信息DTO转换为VO
     */
    List<ChatMessageRespVO.Reference> convertReferenceList(List<ChatMessageRespDTO.Reference> list);

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
    
    /**
     * 确保DTO和VO之间的字段正确映射
     */
    @AfterMapping
    default void ensureDtoToVoMapping(ChatMessageRespDTO dto, @MappingTarget ChatMessageRespVO vo) {
        // 确保字段正确映射
        if (dto.getCreatedAt() != null) {
            vo.setCreatedAt(dto.getCreatedAt());
        }
        if (dto.getConversationId() != null) {
            vo.setConversationId(dto.getConversationId());
        }
    }
    
    /**
     * 填充文件名称
     * 
     * @param fileMapper 文件Mapper
     * @param list 文件同步DO列表
     * @param respList 文件同步VO列表
     */
    default void fillFileNames(FileMapper fileMapper, List<FileDifyDO> list, List<FileDifySyncRespVO> respList) {
        // 1. 获取所有文件ID
        Set<Long> fileIds = list.stream()
                .map(FileDifyDO::getFileId)
                .collect(Collectors.toSet());
        if (fileIds.isEmpty()) {
            return;
        }
        
        // 2. 批量查询文件信息
        List<FileDO> files = fileMapper.selectBatchIds(fileIds);
        Map<Long, String> fileNameMap = files.stream()
                .collect(Collectors.toMap(FileDO::getId, FileDO::getName));
        
        // 3. 填充文件名称
        for (int i = 0; i < list.size(); i++) {
            FileDifyDO fileDifyDO = list.get(i);
            FileDifySyncRespVO respVO = respList.get(i);
            respVO.setFileName(fileNameMap.getOrDefault(fileDifyDO.getFileId(), "未知文件"));
        }
    }
    
    /**
     * 填充文件名称（单个对象）
     * 
     * @param fileMapper 文件Mapper
     * @param fileDifyDO 文件同步DO
     * @param respVO 文件同步VO
     */
    default void fillFileName(FileMapper fileMapper, FileDifyDO fileDifyDO, FileDifySyncRespVO respVO) {
        if (fileDifyDO == null || fileDifyDO.getFileId() == null) {
            return;
        }
        
        // 查询文件信息
        FileDO fileDO = fileMapper.selectById(fileDifyDO.getFileId());
        if (fileDO != null) {
            respVO.setFileName(fileDO.getName());
        } else {
            respVO.setFileName("未知文件");
        }
    }
} 