package cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.method;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - ERP 物流方式分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ErpLogisticsMethodPageReqVO extends PageParam {

    @Schema(description = "物流服务商编号", example = "1001")
    private Long providerId;

    @Schema(description = "方式名称", example = "快递")
    private String name;

    @Schema(description = "方式代码", example = "EXPRESS")
    private String code;

    @Schema(description = "状态", example = "1")
    private Integer status;

} 