package cn.iocoder.yudao.module.erp.dal.dataobject.logistics;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * ERP 物流方式 DO
 *
 * @author 芋道源码
 */
@TableName("erp_logistics_method")
@KeySequence("erp_logistics_method_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TenantIgnore
public class ErpLogisticsMethodDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    
    /**
     * 物流服务商编号
     */
    private Long providerId;
    
    /**
     * 方式代码
     * 例如：EXPRESS, STANDARD, ECONOMY
     */
    private String code;
    
    /**
     * 方式名称
     */
    private String name;
    
    /**
     * 预计送达天数
     */
    private Integer estimatedDays;
    
    /**
     * 支持的国家代码，逗号分隔
     * 例如：US,UK,DE,FR
     */
    private String supportedCountries;
    
    /**
     * 状态
     * 
     * 枚举 {@link cn.iocoder.yudao.framework.common.enums.CommonStatusEnum}
     */
    private Integer status;
    
    /**
     * 排序
     */
    private Integer sort;
    
    /**
     * 备注
     */
    private String remark;

} 