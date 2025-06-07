package cn.iocoder.yudao.module.erp.dal.dataobject.logistics;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * ERP 物流服务商 DO
 *
 * @author 芋道源码
 */
@TableName("erp_logistics_provider")
@KeySequence("erp_logistics_provider_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TenantIgnore
public class ErpLogisticsProviderDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    
    /**
     * 服务商代码
     * 例如：DHL, UPS, FEDEX
     */
    private String code;
    
    /**
     * 服务商名称
     */
    private String name;
    
    /**
     * 官网地址
     */
    private String website;
    
    /**
     * API配置(JSON格式)
     */
    private String apiConfig;
    
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