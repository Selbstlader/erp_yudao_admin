package cn.iocoder.yudao.module.erp.dal.dataobject.crossborder;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * ERP 汇率 DO
 *
 * @author 芋道源码
 */
@TableName("erp_exchange_rate")
@KeySequence("erp_exchange_rate_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErpExchangeRateDO extends BaseDO {

    /**
     * 汇率编号
     */
    @TableId
    private Long id;
    
    /**
     * 源币种编号
     * 
     * 关联 {@link ErpCurrencyDO#getId()}
     */
    private Long fromCurrencyId;
    
    /**
     * 目标币种编号
     * 
     * 关联 {@link ErpCurrencyDO#getId()}
     */
    private Long toCurrencyId;
    
    /**
     * 汇率
     * 1单位源币种 = rate单位目标币种
     */
    private BigDecimal rate;
    
    /**
     * 生效日期
     */
    private LocalDate effectiveDate;
    
    /**
     * 失效日期
     */
    private LocalDate expiryDate;
    
    /**
     * 汇率状态
     * 
     * 枚举 {@link cn.iocoder.yudao.framework.common.enums.CommonStatusEnum}
     */
    private Integer status;
    
    /**
     * 汇率来源
     * 1-手动录入，2-自动获取
     */
    private Integer source;
    
    /**
     * 备注
     */
    private String remark;

}
