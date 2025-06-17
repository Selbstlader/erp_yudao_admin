package cn.iocoder.yudao.module.infra.dal.dataobject.file;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 文件与Dify知识库关联 DO
 *
 * @author 芋道源码
 */
@TableName("infra_file_dify")
@KeySequence("infra_file_dify_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileDifyDO extends TenantBaseDO {

    /**
     * 编号
     */
    private Long id;
    
    /**
     * 文件ID
     * 
     * 关联 {@link FileDO#getId()}
     */
    private Long fileId;
    
    /**
     * Dify文档ID
     */
    private String difyDocumentId;
    
    /**
     * 同步状态（0-未同步，1-同步中，2-已同步，3-同步失败）
     * 
     * 枚举 {@link cn.iocoder.yudao.module.infra.framework.dify.core.enums.DifySyncStatusEnum}
     */
    private Integer syncStatus;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    /**
     * 错误码
     */
    private String errorCode;
    
    /**
     * 知识库ID (由用户指定的目标知识库ID)
     */
    private String datasetId;
    
    /**
     * 批处理ID
     */
    private String batchId;
    
    /**
     * 重试次数
     */
    private Integer retryCount;
    
    /**
     * 下次重试时间
     */
    private LocalDateTime nextRetryTime;
    
    /**
     * 上次同步时间
     */
    private LocalDateTime syncTime;
} 