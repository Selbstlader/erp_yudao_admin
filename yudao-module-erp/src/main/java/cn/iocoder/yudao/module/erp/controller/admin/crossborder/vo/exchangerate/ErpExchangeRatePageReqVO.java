package cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.exchangerate;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY;

@Schema(description = "管理后台 - ERP 汇率分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ErpExchangeRatePageReqVO extends PageParam {

    @Schema(description = "源币种编号", example = "1")
    private Long fromCurrencyId;

    @Schema(description = "目标币种编号", example = "2")
    private Long toCurrencyId;

    @Schema(description = "汇率状态", example = "1")
    private Integer status;

    @Schema(description = "生效日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    private LocalDate[] effectiveDate;

}
