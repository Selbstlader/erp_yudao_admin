package cn.iocoder.yudao.module.erp.controller.admin.crossborder;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.language.ErpLanguagePageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.language.ErpLanguageRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.language.ErpLanguageSaveReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.crossborder.ErpLanguageDO;
import cn.iocoder.yudao.module.erp.service.crossborder.ErpLanguageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - ERP 多语言")
@RestController
@RequestMapping("/erp/language")
@Validated
public class ErpLanguageController {

    @Resource
    private ErpLanguageService languageService;

    @PostMapping("/create")
    @Operation(summary = "创建多语言")
    @PreAuthorize("@ss.hasPermission('erp:language:create')")
    public CommonResult<Long> createLanguage(@Valid @RequestBody ErpLanguageSaveReqVO createReqVO) {
        return success(languageService.createLanguage(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新多语言")
    @PreAuthorize("@ss.hasPermission('erp:language:update')")
    public CommonResult<Boolean> updateLanguage(@Valid @RequestBody ErpLanguageSaveReqVO updateReqVO) {
        languageService.updateLanguage(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除多语言")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:language:delete')")
    public CommonResult<Boolean> deleteLanguage(@RequestParam("id") Long id) {
        languageService.deleteLanguage(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得多语言")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:language:query')")
    public CommonResult<ErpLanguageRespVO> getLanguage(@RequestParam("id") Long id) {
        ErpLanguageDO language = languageService.getLanguage(id);
        return success(BeanUtils.toBean(language, ErpLanguageRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得多语言分页")
    @PreAuthorize("@ss.hasPermission('erp:language:query')")
    public CommonResult<PageResult<ErpLanguageRespVO>> getLanguagePage(@Valid ErpLanguagePageReqVO pageReqVO) {
        PageResult<ErpLanguageRespVO> pageResult = languageService.getLanguagePage(pageReqVO);
        return success(pageResult);
    }

    @GetMapping("/simple-list")
    @Operation(summary = "获得多语言精简列表")
    @PreAuthorize("@ss.hasPermission('erp:language:query')")
    public CommonResult<List<ErpLanguageRespVO>> getLanguageSimpleList() {
        List<ErpLanguageRespVO> list = languageService.getLanguageListByStatus(1); // 1 表示启用状态
        return success(list);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出多语言 Excel")
    @PreAuthorize("@ss.hasPermission('erp:language:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportLanguageExcel(@Valid ErpLanguagePageReqVO pageReqVO,
                                   HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ErpLanguageRespVO> list = languageService.getLanguagePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "多语言.xls", "数据", ErpLanguageRespVO.class, list);
    }

}
