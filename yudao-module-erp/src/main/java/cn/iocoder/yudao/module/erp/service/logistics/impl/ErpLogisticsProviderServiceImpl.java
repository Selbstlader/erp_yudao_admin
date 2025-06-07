package cn.iocoder.yudao.module.erp.service.logistics.impl;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.provider.ErpLogisticsProviderPageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.provider.ErpLogisticsProviderRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.provider.ErpLogisticsProviderSaveReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.logistics.ErpLogisticsProviderDO;
import cn.iocoder.yudao.module.erp.dal.mysql.logistics.ErpLogisticsProviderMapper;
import cn.iocoder.yudao.module.erp.service.logistics.ErpLogisticsProviderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
 * ERP 物流服务商 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class ErpLogisticsProviderServiceImpl implements ErpLogisticsProviderService {

    @Resource
    private ErpLogisticsProviderMapper logisticsProviderMapper;

    @Override
    public Long createLogisticsProvider(ErpLogisticsProviderSaveReqVO createReqVO) {
        // 校验编码是否重复
        validateLogisticsProviderCodeDuplicate(createReqVO.getCode(), null);

        // 插入
        ErpLogisticsProviderDO logisticsProvider = BeanUtils.toBean(createReqVO, ErpLogisticsProviderDO.class);
        logisticsProviderMapper.insert(logisticsProvider);
        // 返回
        return logisticsProvider.getId();
    }

    @Override
    public void updateLogisticsProvider(ErpLogisticsProviderSaveReqVO updateReqVO) {
        // 校验存在
        validateLogisticsProviderExists(updateReqVO.getId());
        // 校验编码是否重复
        validateLogisticsProviderCodeDuplicate(updateReqVO.getCode(), updateReqVO.getId());

        // 更新
        ErpLogisticsProviderDO updateObj = BeanUtils.toBean(updateReqVO, ErpLogisticsProviderDO.class);
        logisticsProviderMapper.updateById(updateObj);
    }

    @Override
    public void deleteLogisticsProvider(Long id) {
        // 校验存在
        validateLogisticsProviderExists(id);
        // 删除
        logisticsProviderMapper.deleteById(id);
    }

    private void validateLogisticsProviderExists(Long id) {
        if (logisticsProviderMapper.selectById(id) == null) {
            throw exception(ERP_LOGISTICS_PROVIDER_NOT_EXISTS);
        }
    }

    private void validateLogisticsProviderCodeDuplicate(String code, Long id) {
        ErpLogisticsProviderDO provider = logisticsProviderMapper.selectByCode(code);
        if (provider == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的物流服务商
        if (id == null) {
            throw exception(ERP_LOGISTICS_PROVIDER_CODE_DUPLICATE);
        }
        if (!provider.getId().equals(id)) {
            throw exception(ERP_LOGISTICS_PROVIDER_CODE_DUPLICATE);
        }
    }

    @Override
    public ErpLogisticsProviderDO getLogisticsProvider(Long id) {
        return logisticsProviderMapper.selectById(id);
    }

    @Override
    public PageResult<ErpLogisticsProviderRespVO> getLogisticsProviderPage(ErpLogisticsProviderPageReqVO pageReqVO) {
        PageResult<ErpLogisticsProviderDO> pageResult = logisticsProviderMapper.selectPage(pageReqVO);
        return BeanUtils.toBean(pageResult, ErpLogisticsProviderRespVO.class);
    }

    @Override
    public List<ErpLogisticsProviderRespVO> getLogisticsProviderListByStatus(Integer status) {
        List<ErpLogisticsProviderDO> list = logisticsProviderMapper.selectListByStatus(status);
        return BeanUtils.toBean(list, ErpLogisticsProviderRespVO.class);
    }

    @Override
    public List<ErpLogisticsProviderRespVO> getLogisticsProviderList(Collection<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }
        List<ErpLogisticsProviderDO> list = logisticsProviderMapper.selectBatchIds(ids);
        return BeanUtils.toBean(list, ErpLogisticsProviderRespVO.class);
    }

    @Override
    public ErpLogisticsProviderDO getLogisticsProviderByCode(String code) {
        return logisticsProviderMapper.selectByCode(code);
    }

    @Override
    public List<ErpLogisticsProviderDO> validLogisticsProviderList(Collection<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }
        // 获取物流服务商列表
        List<ErpLogisticsProviderDO> list = logisticsProviderMapper.selectBatchIds(ids);
        if (list.size() != ids.size()) {
            // 校验物流服务商
            List<Long> notExistIds = ids.stream()
                    .filter(id -> list.stream().noneMatch(logisticsProvider -> logisticsProvider.getId().equals(id)))
                    .collect(Collectors.toList());
            if (!notExistIds.isEmpty()) {
                throw exception(ERP_LOGISTICS_PROVIDER_NOT_EXISTS);
            }
        }
        return list;
    }

    @Override
    public boolean testLogisticsProviderConnection(Long id) {
        // 校验存在
        ErpLogisticsProviderDO provider = logisticsProviderMapper.selectById(id);
        if (provider == null) {
            throw exception(ERP_LOGISTICS_PROVIDER_NOT_EXISTS);
        }
        
        // TODO 实现实际的API连接测试逻辑
        // 这里仅做示例，实际应该根据provider.getApiConfig()中的配置信息进行连接测试
        log.info("[testLogisticsProviderConnection][物流服务商({}) 进行连接测试]", id);
        return true;
    }
} 