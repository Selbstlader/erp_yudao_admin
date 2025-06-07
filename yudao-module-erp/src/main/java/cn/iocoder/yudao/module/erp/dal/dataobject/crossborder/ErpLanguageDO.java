package cn.iocoder.yudao.module.erp.dal.dataobject.crossborder;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * ERP 多语言 DO
 *
 * @author 芋道源码
 */
@TableName("erp_language")
@KeySequence("erp_language_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TenantIgnore
public class ErpLanguageDO extends BaseDO {

    /**
     * 语言编号
     */
    @TableId
    private Long id;
    
    /**
     * 语言代码
     * 例如：zh-CN, en-US, ja-JP
     */
    private String code;
    
    /**
     * 语言名称
     * 例如：简体中文, English, 日本語
     */
    private String name;
    
    /**
     * 语言英文名称
     * 例如：Chinese Simplified, English, Japanese
     */
    private String englishName;
    
    /**
     * 语言状态
     * 
     * 枚举 {@link cn.iocoder.yudao.framework.common.enums.CommonStatusEnum}
     */
    private Integer status;
    
    /**
     * 是否默认语言
     * true-是，false-否
     */
    private Boolean isDefault;
    
    /**
     * 排序
     */
    private Integer sort;
    
    /**
     * 备注
     */
    private String remark;

}
