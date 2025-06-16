package cn.iocoder.yudao.module.infra.framework.dify.dto.response;

import lombok.Data;

import java.util.List;

/**
 * Dify 知识库列表响应DTO
 */
@Data
public class DatasetListRespDTO {

    /**
     * 分页数据
     */
    private PageData pageData;
    
    /**
     * 知识库列表
     */
    private List<DatasetRespDTO> data;
    
    /**
     * 分页数据
     */
    @Data
    public static class PageData {
        /**
         * 页码
         */
        private Integer page;
        
        /**
         * 每页条数
         */
        private Integer limit;
        
        /**
         * 总条数
         */
        private Integer total;
        
        /**
         * 总页数
         */
        private Integer totalPages;
    }
} 