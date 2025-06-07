package cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.warehouse;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - ERP 国际仓库信息分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ErpWarehouseInternationalPageReqVO extends PageParam {

    @Schema(description = "国家代码", example = "US")
    private String countryCode;

    @Schema(description = "关联基础仓库ID", example = "1001")
    private Long warehouseId;

    @Schema(description = "是否保税仓", example = "true")
    private Boolean isBonded;

    @Schema(description = "状态", example = "1")
    private Integer status;

} 