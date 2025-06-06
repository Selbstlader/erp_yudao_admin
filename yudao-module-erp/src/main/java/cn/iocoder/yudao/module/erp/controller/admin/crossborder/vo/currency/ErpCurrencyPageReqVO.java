package cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.currency;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - ERP 多币种分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ErpCurrencyPageReqVO extends PageParam {

    @Schema(description = "币种名称", example = "人民币")
    private String name;

    @Schema(description = "币种代码", example = "CNY")
    private String code;

    @Schema(description = "币种状态", example = "1")
    private Integer status;

}
