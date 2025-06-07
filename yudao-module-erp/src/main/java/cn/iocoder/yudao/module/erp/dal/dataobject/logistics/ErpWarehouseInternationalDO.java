package cn.iocoder.yudao.module.erp.dal.dataobject.logistics;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * ERP 国际仓库信息 DO
 *
 * @author 芋道源码
 */
@TableName("erp_warehouse_international")
@KeySequence("erp_warehouse_international_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TenantIgnore
public class ErpWarehouseInternationalDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    
    /**
     * 关联基础仓库ID
     */
    private Long warehouseId;
    
    /**
     * 国家代码
     * 例如：US, CN, UK, DE
     */
    private String countryCode;
    
    /**
     * 地区/州/省
     */
    private String region;
    
    /**
     * 城市
     */
    private String city;
    
    /**
     * 邮政编码
     */
    private String postalCode;
    
    /**
     * 详细地址
     */
    private String address;
    
    /**
     * 联系人
     */
    private String contactName;
    
    /**
     * 联系电话
     */
    private String contactPhone;
    
    /**
     * 联系邮箱
     */
    private String contactEmail;
    
    /**
     * 海关编码
     */
    private String customsCode;
    
    /**
     * 是否保税仓
     * true-是，false-否
     */
    private Boolean isBonded;
    
    /**
     * 增值税号
     */
    private String vatNumber;
    
    /**
     * 状态
     * 
     * 枚举 {@link cn.iocoder.yudao.framework.common.enums.CommonStatusEnum}
     */
    private Integer status;
    
    /**
     * 备注
     */
    private String remark;

} 