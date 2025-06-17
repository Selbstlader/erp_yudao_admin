package cn.iocoder.yudao.module.infra.controller.admin.file;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.dify.DifyDatasetCreateReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.dify.DifyDatasetPageReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.dify.DifyDatasetRespVO;
import cn.iocoder.yudao.module.infra.framework.dify.dto.response.DatasetListRespDTO;
import cn.iocoder.yudao.module.infra.framework.dify.dto.response.DatasetRespDTO;
import cn.iocoder.yudao.module.infra.service.file.FileDifyDatasetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * 管理后台 - Dify知识库
 */
@Tag(name = "管理后台 - Dify知识库")
@RestController
@RequestMapping("/infra/dify-dataset")
@Validated
public class FileDifyDatasetController {

    @Resource
    private FileDifyDatasetService fileDifyDatasetService;

    @GetMapping("/page")
    @Operation(summary = "获得Dify知识库分页")
    @PreAuthorize("@ss.hasPermission('infra:file:list')")
    public CommonResult<PageResult<DifyDatasetRespVO>> getDifyDatasetPage(@Valid DifyDatasetPageReqVO pageVO) {
        DatasetListRespDTO datasetList = fileDifyDatasetService.getDatasetPage(pageVO);
        // 转换为VO列表
        PageResult<DifyDatasetRespVO> pageResult = new PageResult<>();
        pageResult.setTotal(datasetList.getTotal().longValue());
        pageResult.setList(DifyDatasetRespVO.convertList(datasetList.getData()));
        return success(pageResult);
    }

    @PostMapping("/create")
    @Operation(summary = "创建Dify知识库")
    @PreAuthorize("@ss.hasPermission('infra:file:create')")
    public CommonResult<String> createDifyDataset(@Valid @RequestBody DifyDatasetCreateReqVO createReqVO) {
        return success(fileDifyDatasetService.createDataset(createReqVO));
    }
    
    @DeleteMapping("/delete")
    @Operation(summary = "删除Dify知识库")
    @Parameter(name = "id", description = "知识库ID", required = true)
    @PreAuthorize("@ss.hasPermission('infra:file:delete')")
    public CommonResult<Boolean> deleteDifyDataset(@RequestParam("id") String id) {
        fileDifyDatasetService.deleteDataset(id);
        return success(true);
    }
} 