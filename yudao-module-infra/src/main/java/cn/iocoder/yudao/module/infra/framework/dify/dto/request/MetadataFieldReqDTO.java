package cn.iocoder.yudao.module.infra.framework.dify.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dify 元数据字段请求DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetadataFieldReqDTO {

    /**
     * 字段名称
     */
    private String name;
    
    /**
     * 字段类型: text, number, boolean
     */
    private String type;
    
    /**
     * 字段描述
     */
    private String description;
} 