package cn.iocoder.yudao.module.erp.dal.dataobject.crossborder;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

/**
 * ERP 多币种 DO
 *
 * @author 芋道源码
 */
@TableName("erp_currency")
@KeySequence("erp_currency_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErpCurrencyDO extends BaseDO {

    /**
     * 币种编号
     */
    @TableId
    private Long id;
    
    /**
     * 币种代码
     * 例如：CNY, USD, EUR, JPY
     */
    private String code;
    
    /**
     * 币种名称
     * 例如：人民币, 美元, 欧元, 日元
     */
    private String name;
    
    /**
     * 币种英文名称
     * 例如：Chinese Yuan, US Dollar, Euro, Japanese Yen
     */
    private String englishName;
    
    /**
     * 币种符号
     * 例如：¥, $, €, ¥
     */
    private String symbol;
    
    /**
     * 币种状态
     * 
     * 枚举 {@link cn.iocoder.yudao.framework.common.enums.CommonStatusEnum}
     */
    private Integer status;
    
    /**
     * 是否基础币种
     * true-是，false-否
     */
    private Boolean isBase;
    
    /**
     * 汇率（相对于基础币种）
     * 基础币种的汇率为1.0
     */
    private BigDecimal exchangeRate;
    
    /**
     * 小数位数
     * 例如：2表示保留2位小数
     */
    private Integer decimalPlaces;
    
    /**
     * 排序
     */
    private Integer sort;
    
    /**
     * 备注
     */
    private String remark;

}
