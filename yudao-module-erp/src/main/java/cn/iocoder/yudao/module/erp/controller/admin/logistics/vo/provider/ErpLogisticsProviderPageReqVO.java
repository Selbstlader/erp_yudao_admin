package cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.provider;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - ERP 物流服务商分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ErpLogisticsProviderPageReqVO extends PageParam {

    @Schema(description = "服务商名称", example = "DHL Express")
    private String name;

    @Schema(description = "服务商代码", example = "DHL")
    private String code;

    @Schema(description = "状态", example = "1")
    private Integer status;

} 