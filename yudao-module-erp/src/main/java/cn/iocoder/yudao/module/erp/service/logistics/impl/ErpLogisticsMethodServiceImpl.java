package cn.iocoder.yudao.module.erp.service.logistics.impl;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.method.ErpLogisticsMethodPageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.method.ErpLogisticsMethodRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.method.ErpLogisticsMethodSaveReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.logistics.ErpLogisticsMethodDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.logistics.ErpLogisticsProviderDO;
import cn.iocoder.yudao.module.erp.dal.mysql.logistics.ErpLogisticsMethodMapper;
import cn.iocoder.yudao.module.erp.service.logistics.ErpLogisticsMethodService;
import cn.iocoder.yudao.module.erp.service.logistics.ErpLogisticsProviderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP 物流方式 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class ErpLogisticsMethodServiceImpl implements ErpLogisticsMethodService {

    @Resource
    private ErpLogisticsMethodMapper logisticsMethodMapper;
    
    @Resource
    private ErpLogisticsProviderService logisticsProviderService;

    @Override
    public Long createLogisticsMethod(ErpLogisticsMethodSaveReqVO createReqVO) {
        // 校验服务商存在
        validateLogisticsProviderExists(createReqVO.getProviderId());
        // 校验编码是否重复
        validateLogisticsMethodCodeDuplicate(createReqVO.getProviderId(), createReqVO.getCode(), null);

        // 插入
        ErpLogisticsMethodDO logisticsMethod = BeanUtils.toBean(createReqVO, ErpLogisticsMethodDO.class);
        logisticsMethodMapper.insert(logisticsMethod);
        // 返回
        return logisticsMethod.getId();
    }

    @Override
    public void updateLogisticsMethod(ErpLogisticsMethodSaveReqVO updateReqVO) {
        // 校验存在
        validateLogisticsMethodExists(updateReqVO.getId());
        // 校验服务商存在
        validateLogisticsProviderExists(updateReqVO.getProviderId());
        // 校验编码是否重复
        validateLogisticsMethodCodeDuplicate(updateReqVO.getProviderId(), updateReqVO.getCode(), updateReqVO.getId());

        // 更新
        ErpLogisticsMethodDO updateObj = BeanUtils.toBean(updateReqVO, ErpLogisticsMethodDO.class);
        logisticsMethodMapper.updateById(updateObj);
    }

    @Override
    public void deleteLogisticsMethod(Long id) {
        // 校验存在
        validateLogisticsMethodExists(id);
        // 删除
        logisticsMethodMapper.deleteById(id);
    }

    private void validateLogisticsMethodExists(Long id) {
        if (logisticsMethodMapper.selectById(id) == null) {
            throw exception(ERP_LOGISTICS_METHOD_NOT_EXISTS);
        }
    }
    
    private void validateLogisticsProviderExists(Long providerId) {
        logisticsProviderService.validLogisticsProviderList(List.of(providerId));
    }

    private void validateLogisticsMethodCodeDuplicate(Long providerId, String code, Long id) {
        try {
            ErpLogisticsMethodDO method = logisticsMethodMapper.selectByProviderIdAndCode(providerId, code);
            if (method == null) {
                return;
            }
            // 如果 id 为空，说明不用比较是否为相同 id 的物流方式
            if (id == null) {
                log.warn("[validateLogisticsMethodCodeDuplicate][存在重复的物流方式 providerId({}) code({}) 已存在]", providerId, code);
                throw exception(ERP_LOGISTICS_METHOD_CODE_DUPLICATE);
            }
            if (!method.getId().equals(id)) {
                log.warn("[validateLogisticsMethodCodeDuplicate][存在重复的物流方式 providerId({}) code({}) id({}) 已存在]", providerId, code, method.getId());
                throw exception(ERP_LOGISTICS_METHOD_CODE_DUPLICATE);
            }
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("TooManyResultsException")) {
                // 处理可能存在的多条记录的情况
                log.error("[validateLogisticsMethodCodeDuplicate][存在多条重复的物流方式记录 providerId({}) code({})]", providerId, code, e);
                throw exception(ERP_LOGISTICS_METHOD_CODE_DUPLICATE);
            }
            throw e;
        }
    }

    @Override
    public ErpLogisticsMethodDO getLogisticsMethod(Long id) {
        return logisticsMethodMapper.selectById(id);
    }

    @Override
    public PageResult<ErpLogisticsMethodRespVO> getLogisticsMethodPage(ErpLogisticsMethodPageReqVO pageReqVO) {
        PageResult<ErpLogisticsMethodDO> pageResult = logisticsMethodMapper.selectPage(pageReqVO);
        if (pageResult.getList().isEmpty()) {
            return PageResult.empty(pageResult.getTotal());
        }
        
        try {
            // 获取服务商信息 - 使用try-catch包装，避免因服务商不存在导致整个查询失败
            Set<Long> providerIds = pageResult.getList().stream()
                    .map(ErpLogisticsMethodDO::getProviderId)
                    .filter(id -> id != null) // 过滤掉可能为null的providerId
                    .collect(Collectors.toSet());
                    
            final Map<Long, String> providerMap;
            if (!providerIds.isEmpty()) {
                List<ErpLogisticsProviderDO> providers = logisticsProviderService.validLogisticsProviderList(providerIds);
                providerMap = providers.stream()
                        .collect(Collectors.toMap(ErpLogisticsProviderDO::getId, ErpLogisticsProviderDO::getName));
            } else {
                providerMap = Collections.emptyMap();
            }
            
            // 拼接结果
            List<ErpLogisticsMethodRespVO> respVOList = BeanUtils.toBean(pageResult.getList(), ErpLogisticsMethodRespVO.class);
            respVOList.forEach(vo -> vo.setProviderName(vo.getProviderId() != null ? providerMap.get(vo.getProviderId()) : null));
            return new PageResult<>(respVOList, pageResult.getTotal());
        } catch (Exception e) {
            log.error("[getLogisticsMethodPage][物流方式分页查询异常]", e);
            // 出现异常时，返回不含服务商名称的结果
            List<ErpLogisticsMethodRespVO> respVOList = BeanUtils.toBean(pageResult.getList(), ErpLogisticsMethodRespVO.class);
            return new PageResult<>(respVOList, pageResult.getTotal());
        }
    }

    @Override
    public List<ErpLogisticsMethodRespVO> getLogisticsMethodListByStatus(Integer status) {
        List<ErpLogisticsMethodDO> list = logisticsMethodMapper.selectListByStatus(status);
        if (list.isEmpty()) {
            return List.of();
        }
        
        try {
            // 获取服务商信息 - 使用try-catch包装，避免因服务商不存在导致整个查询失败
            Set<Long> providerIds = list.stream()
                    .map(ErpLogisticsMethodDO::getProviderId)
                    .filter(id -> id != null) // 过滤掉可能为null的providerId
                    .collect(Collectors.toSet());
                    
            final Map<Long, String> providerMap;
            if (!providerIds.isEmpty()) {
                List<ErpLogisticsProviderDO> providers = logisticsProviderService.validLogisticsProviderList(providerIds);
                providerMap = providers.stream()
                        .collect(Collectors.toMap(ErpLogisticsProviderDO::getId, ErpLogisticsProviderDO::getName));
            } else {
                providerMap = Collections.emptyMap();
            }
            
            // 拼接结果
            List<ErpLogisticsMethodRespVO> respVOList = BeanUtils.toBean(list, ErpLogisticsMethodRespVO.class);
            respVOList.forEach(vo -> vo.setProviderName(vo.getProviderId() != null ? providerMap.get(vo.getProviderId()) : null));
            return respVOList;
        } catch (Exception e) {
            log.error("[getLogisticsMethodListByStatus][物流方式列表查询异常]", e);
            // 出现异常时，返回不含服务商名称的结果
            List<ErpLogisticsMethodRespVO> respVOList = BeanUtils.toBean(list, ErpLogisticsMethodRespVO.class);
            return respVOList;
        }
    }

    @Override
    public List<ErpLogisticsMethodRespVO> getLogisticsMethodList(Collection<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }
        List<ErpLogisticsMethodDO> list = logisticsMethodMapper.selectBatchIds(ids);
        if (list.isEmpty()) {
            return List.of();
        }
        
        try {
            // 获取服务商信息 - 使用try-catch包装，避免因服务商不存在导致整个查询失败
            Set<Long> providerIds = list.stream()
                    .map(ErpLogisticsMethodDO::getProviderId)
                    .filter(id -> id != null) // 过滤掉可能为null的providerId
                    .collect(Collectors.toSet());
                    
            final Map<Long, String> providerMap;
            if (!providerIds.isEmpty()) {
                List<ErpLogisticsProviderDO> providers = logisticsProviderService.validLogisticsProviderList(providerIds);
                providerMap = providers.stream()
                        .collect(Collectors.toMap(ErpLogisticsProviderDO::getId, ErpLogisticsProviderDO::getName));
            } else {
                providerMap = Collections.emptyMap();
            }
            
            // 拼接结果
            List<ErpLogisticsMethodRespVO> respVOList = BeanUtils.toBean(list, ErpLogisticsMethodRespVO.class);
            respVOList.forEach(vo -> vo.setProviderName(vo.getProviderId() != null ? providerMap.get(vo.getProviderId()) : null));
            return respVOList;
        } catch (Exception e) {
            log.error("[getLogisticsMethodList][物流方式列表查询异常]", e);
            // 出现异常时，返回不含服务商名称的结果
            List<ErpLogisticsMethodRespVO> respVOList = BeanUtils.toBean(list, ErpLogisticsMethodRespVO.class);
            return respVOList;
        }
    }

    @Override
    public List<ErpLogisticsMethodRespVO> getLogisticsMethodListByProviderId(Long providerId) {
        List<ErpLogisticsMethodDO> list = logisticsMethodMapper.selectListByProviderId(providerId);
        if (list.isEmpty()) {
            return List.of();
        }
        
        try {
            // 获取服务商信息
            final ErpLogisticsProviderDO provider = logisticsProviderService.getLogisticsProvider(providerId);
            
            // 拼接结果
            List<ErpLogisticsMethodRespVO> respVOList = BeanUtils.toBean(list, ErpLogisticsMethodRespVO.class);
            respVOList.forEach(vo -> vo.setProviderName(provider != null ? provider.getName() : null));
            return respVOList;
        } catch (Exception e) {
            log.error("[getLogisticsMethodListByProviderId][根据服务商编号获取物流方式列表异常][providerId={}]", providerId, e);
            // 出现异常时，返回不含服务商名称的结果
            List<ErpLogisticsMethodRespVO> respVOList = BeanUtils.toBean(list, ErpLogisticsMethodRespVO.class);
            return respVOList;
        }
    }

    @Override
    public List<ErpLogisticsMethodDO> validLogisticsMethodList(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        // 获取物流方式列表
        List<ErpLogisticsMethodDO> list = logisticsMethodMapper.selectBatchIds(ids);
        if (list.size() != ids.size()) {
            // 校验物流方式
            final List<ErpLogisticsMethodDO> finalList = list; // 创建final变量供lambda表达式使用
            List<Long> notExistIds = ids.stream()
                    .filter(id -> finalList.stream().noneMatch(method -> method.getId().equals(id)))
                    .collect(Collectors.toList());
            if (!notExistIds.isEmpty()) {
                throw exception(ERP_LOGISTICS_METHOD_NOT_EXISTS);
            }
        }
        return list;
    }
} 