package cn.iocoder.yudao.module.infra.controller.admin.file.vo.dify;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import cn.iocoder.yudao.module.infra.framework.dify.dto.response.DatasetRespDTO;

import java.time.LocalDateTime;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "管理后台 - Dify知识库 Response VO")
@Data
public class DifyDatasetRespVO {

    @Schema(description = "知识库ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "dataset-123456")
    private String id;

    @Schema(description = "知识库名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "示例知识库")
    private String name;

    @Schema(description = "知识库描述")
    private String description;

    @Schema(description = "权限，可选值：only_me(仅自己), team(团队)", example = "only_me")
    private String permission;
    
    @Schema(description = "数据源类型", example = "upload_file")
    private String dataSourceType;
    
    @Schema(description = "索引技术", example = "high_quality")
    private String indexingTechnique;
    
    @Schema(description = "应用数量", example = "0")
    private Integer appCount;
    
    @Schema(description = "文档数量", example = "5")
    private Integer documentCount;
    
    @Schema(description = "词数", example = "1000")
    private Integer wordCount;
    
    @Schema(description = "创建者", example = "admin")
    private String createdBy;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新者", example = "admin")
    private String updatedBy;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
    
    /**
     * 将Dify API返回的DatasetRespDTO列表转换为VO列表
     */
    public static List<DifyDatasetRespVO> convertList(List<DatasetRespDTO> datasetList) {
        if (datasetList == null) {
            return new ArrayList<>();
        }
        List<DifyDatasetRespVO> result = new ArrayList<>(datasetList.size());
        for (DatasetRespDTO dataset : datasetList) {
            result.add(convert(dataset));
        }
        return result;
    }
    
    /**
     * 将单个DatasetRespDTO转换为VO
     */
    public static DifyDatasetRespVO convert(DatasetRespDTO dataset) {
        if (dataset == null) {
            return null;
        }
        DifyDatasetRespVO vo = new DifyDatasetRespVO();
        vo.setId(dataset.getId());
        vo.setName(dataset.getName());
        vo.setDescription(dataset.getDescription());
        vo.setPermission(dataset.getPermission());
        vo.setDataSourceType(dataset.getData_source_type());
        vo.setIndexingTechnique(dataset.getIndexing_technique());
        vo.setAppCount(dataset.getApp_count());
        vo.setDocumentCount(dataset.getDocument_count());
        vo.setWordCount(dataset.getWord_count());
        vo.setCreatedBy(dataset.getCreated_by());
        vo.setUpdatedBy(dataset.getUpdated_by());
        
        // 转换时间戳为LocalDateTime
        try {
            if (dataset.getCreated_at() != null) {
                long timestamp = Long.parseLong(dataset.getCreated_at());
                vo.setCreateTime(LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault()));
            }
            if (dataset.getUpdated_at() != null) {
                long timestamp = Long.parseLong(dataset.getUpdated_at());
                vo.setUpdateTime(LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault()));
            }
        } catch (NumberFormatException e) {
            // 忽略解析错误
        }
        
        return vo;
    }
} 