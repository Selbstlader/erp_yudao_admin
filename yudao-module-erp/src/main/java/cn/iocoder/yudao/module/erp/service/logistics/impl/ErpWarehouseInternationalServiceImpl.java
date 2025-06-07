package cn.iocoder.yudao.module.erp.service.logistics.impl;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.warehouse.ErpWarehouseInternationalPageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.warehouse.ErpWarehouseInternationalRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.warehouse.ErpWarehouseInternationalSaveReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.logistics.ErpWarehouseInternationalDO;
import cn.iocoder.yudao.module.erp.dal.mysql.logistics.ErpWarehouseInternationalMapper;
import cn.iocoder.yudao.module.erp.service.logistics.ErpWarehouseInternationalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP 国际仓库 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class ErpWarehouseInternationalServiceImpl implements ErpWarehouseInternationalService {

    @Resource
    private ErpWarehouseInternationalMapper warehouseInternationalMapper;

    @Override
    public Long createWarehouseInternational(ErpWarehouseInternationalSaveReqVO createReqVO) {
        // 插入
        ErpWarehouseInternationalDO warehouseInternational = BeanUtils.toBean(createReqVO, ErpWarehouseInternationalDO.class);
        warehouseInternationalMapper.insert(warehouseInternational);
        // 返回
        return warehouseInternational.getId();
    }

    @Override
    public void updateWarehouseInternational(ErpWarehouseInternationalSaveReqVO updateReqVO) {
        // 校验存在
        validateWarehouseInternationalExists(updateReqVO.getId());

        // 更新
        ErpWarehouseInternationalDO updateObj = BeanUtils.toBean(updateReqVO, ErpWarehouseInternationalDO.class);
        warehouseInternationalMapper.updateById(updateObj);
    }

    @Override
    public void deleteWarehouseInternational(Long id) {
        // 校验存在
        validateWarehouseInternationalExists(id);
        // 删除
        warehouseInternationalMapper.deleteById(id);
    }

    private void validateWarehouseInternationalExists(Long id) {
        if (warehouseInternationalMapper.selectById(id) == null) {
            throw exception(ERP_WAREHOUSE_INTERNATIONAL_NOT_EXISTS);
        }
    }

    @Override
    public ErpWarehouseInternationalDO getWarehouseInternational(Long id) {
        return warehouseInternationalMapper.selectById(id);
    }

    @Override
    public PageResult<ErpWarehouseInternationalRespVO> getWarehouseInternationalPage(ErpWarehouseInternationalPageReqVO pageReqVO) {
        PageResult<ErpWarehouseInternationalDO> pageResult = warehouseInternationalMapper.selectPage(pageReqVO);
        return BeanUtils.toBean(pageResult, ErpWarehouseInternationalRespVO.class);
    }

    @Override
    public List<ErpWarehouseInternationalRespVO> getWarehouseInternationalListByStatus(Integer status) {
        List<ErpWarehouseInternationalDO> list = warehouseInternationalMapper.selectListByStatus(status);
        return BeanUtils.toBean(list, ErpWarehouseInternationalRespVO.class);
    }

    @Override
    public List<ErpWarehouseInternationalRespVO> getWarehouseInternationalList(Collection<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }
        List<ErpWarehouseInternationalDO> list = warehouseInternationalMapper.selectBatchIds(ids);
        return BeanUtils.toBean(list, ErpWarehouseInternationalRespVO.class);
    }

    @Override
    public List<ErpWarehouseInternationalRespVO> getWarehouseInternationalListByCountryCode(String countryCode) {
        List<ErpWarehouseInternationalDO> list = warehouseInternationalMapper.selectListByCountryCode(countryCode);
        return BeanUtils.toBean(list, ErpWarehouseInternationalRespVO.class);
    }

    @Override
    public List<ErpWarehouseInternationalDO> validWarehouseInternationalList(Collection<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }
        // 获取国际仓库列表
        List<ErpWarehouseInternationalDO> list = warehouseInternationalMapper.selectBatchIds(ids);
        if (list.size() != ids.size()) {
            // 校验国际仓库
            List<Long> notExistIds = ids.stream()
                    .filter(id -> list.stream().noneMatch(warehouse -> warehouse.getId().equals(id)))
                    .collect(Collectors.toList());
            if (!notExistIds.isEmpty()) {
                throw exception(ERP_WAREHOUSE_INTERNATIONAL_NOT_EXISTS);
            }
        }
        return list;
    }
} 