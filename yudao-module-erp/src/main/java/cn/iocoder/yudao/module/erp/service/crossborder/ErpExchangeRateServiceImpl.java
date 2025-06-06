package cn.iocoder.yudao.module.erp.service.crossborder;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.exchangerate.ErpExchangeRatePageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.exchangerate.ErpExchangeRateRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.exchangerate.ErpExchangeRateSaveReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.crossborder.ErpCurrencyDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.crossborder.ErpExchangeRateDO;
import cn.iocoder.yudao.module.erp.dal.mysql.crossborder.ErpExchangeRateMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP 汇率 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ErpExchangeRateServiceImpl implements ErpExchangeRateService {

    @Resource
    private ErpExchangeRateMapper exchangeRateMapper;
    
    @Resource
    private ErpCurrencyService currencyService;

    @Override
    public Long createExchangeRate(ErpExchangeRateSaveReqVO createReqVO) {
        // 校验币种存在
        validateCurrencyExists(createReqVO.getFromCurrencyId(), createReqVO.getToCurrencyId());
        
        // 校验源币种和目标币种不能相同
        if (createReqVO.getFromCurrencyId().equals(createReqVO.getToCurrencyId())) {
            throw exception(EXCHANGE_RATE_SAME_CURRENCY);
        }
        
        // 校验汇率唯一性
        validateExchangeRateUnique(null, createReqVO.getFromCurrencyId(), 
                createReqVO.getToCurrencyId(), createReqVO.getEffectiveDate());
        
        // 插入
        ErpExchangeRateDO exchangeRate = BeanUtils.toBean(createReqVO, ErpExchangeRateDO.class);
        exchangeRateMapper.insert(exchangeRate);
        // 返回
        return exchangeRate.getId();
    }

    @Override
    public void updateExchangeRate(ErpExchangeRateSaveReqVO updateReqVO) {
        // 校验存在
        validateExchangeRateExists(updateReqVO.getId());
        
        // 校验币种存在
        validateCurrencyExists(updateReqVO.getFromCurrencyId(), updateReqVO.getToCurrencyId());
        
        // 校验源币种和目标币种不能相同
        if (updateReqVO.getFromCurrencyId().equals(updateReqVO.getToCurrencyId())) {
            throw exception(EXCHANGE_RATE_SAME_CURRENCY);
        }
        
        // 校验汇率唯一性
        validateExchangeRateUnique(updateReqVO.getId(), updateReqVO.getFromCurrencyId(), 
                updateReqVO.getToCurrencyId(), updateReqVO.getEffectiveDate());
        
        // 更新
        ErpExchangeRateDO updateObj = BeanUtils.toBean(updateReqVO, ErpExchangeRateDO.class);
        exchangeRateMapper.updateById(updateObj);
    }

    @Override
    public void deleteExchangeRate(Long id) {
        // 校验存在
        validateExchangeRateExists(id);
        // 删除
        exchangeRateMapper.deleteById(id);
    }

    private ErpExchangeRateDO validateExchangeRateExists(Long id) {
        ErpExchangeRateDO exchangeRate = exchangeRateMapper.selectById(id);
        if (exchangeRate == null) {
            throw exception(EXCHANGE_RATE_NOT_EXISTS);
        }
        return exchangeRate;
    }

    private void validateCurrencyExists(Long fromCurrencyId, Long toCurrencyId) {
        ErpCurrencyDO fromCurrency = currencyService.getCurrency(fromCurrencyId);
        ErpCurrencyDO toCurrency = currencyService.getCurrency(toCurrencyId);
        if (fromCurrency == null || toCurrency == null) {
            throw exception(CURRENCY_NOT_EXISTS);
        }
    }

    private void validateExchangeRateUnique(Long id, Long fromCurrencyId, Long toCurrencyId, LocalDate effectiveDate) {
        ErpExchangeRateDO exchangeRate = exchangeRateMapper.selectLatestRate(fromCurrencyId, toCurrencyId, effectiveDate);
        if (exchangeRate == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的汇率
        if (id == null) {
            throw exception(EXCHANGE_RATE_DUPLICATE, fromCurrencyId, toCurrencyId, effectiveDate);
        }
        if (!exchangeRate.getId().equals(id)) {
            throw exception(EXCHANGE_RATE_DUPLICATE, fromCurrencyId, toCurrencyId, effectiveDate);
        }
    }

    @Override
    public ErpExchangeRateDO getExchangeRate(Long id) {
        return exchangeRateMapper.selectById(id);
    }

    @Override
    public PageResult<ErpExchangeRateRespVO> getExchangeRatePage(ErpExchangeRatePageReqVO pageReqVO) {
        PageResult<ErpExchangeRateDO> pageResult = exchangeRateMapper.selectPage(pageReqVO);
        return buildExchangeRateVOPageResult(pageResult);
    }

    @Override
    public List<ErpExchangeRateRespVO> getExchangeRateListByStatus(Integer status) {
        List<ErpExchangeRateDO> list = exchangeRateMapper.selectListByStatus(status);
        return buildExchangeRateVOList(list);
    }

    @Override
    public List<ErpExchangeRateRespVO> getExchangeRateList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        List<ErpExchangeRateDO> list = exchangeRateMapper.selectBatchIds(ids);
        return buildExchangeRateVOList(list);
    }

    @Override
    public List<ErpExchangeRateDO> validExchangeRateList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        List<ErpExchangeRateDO> list = exchangeRateMapper.selectBatchIds(ids);
        if (list.size() != ids.size()) {
            throw exception(EXCHANGE_RATE_NOT_EXISTS);
        }
        return list;
    }

    @Override
    public ErpExchangeRateDO getLatestExchangeRate(Long fromCurrencyId, Long toCurrencyId, LocalDate date) {
        return exchangeRateMapper.selectLatestRate(fromCurrencyId, toCurrencyId, date);
    }

    @Override
    public List<ErpExchangeRateDO> getEffectiveExchangeRates(LocalDate date) {
        return exchangeRateMapper.selectEffectiveRates(date);
    }

    @Override
    public BigDecimal convertCurrencyByRate(BigDecimal amount, Long fromCurrencyId, Long toCurrencyId, LocalDate date) {
        if (fromCurrencyId.equals(toCurrencyId)) {
            return amount;
        }
        
        ErpExchangeRateDO exchangeRate = getLatestExchangeRate(fromCurrencyId, toCurrencyId, date);
        if (exchangeRate == null) {
            throw exception(EXCHANGE_RATE_NOT_EXISTS);
        }
        
        return amount.multiply(exchangeRate.getRate()).setScale(2, RoundingMode.HALF_UP);
    }

    private PageResult<ErpExchangeRateRespVO> buildExchangeRateVOPageResult(PageResult<ErpExchangeRateDO> pageResult) {
        if (CollUtil.isEmpty(pageResult.getList())) {
            return PageResult.empty(pageResult.getTotal());
        }
        List<ErpExchangeRateRespVO> voList = buildExchangeRateVOList(pageResult.getList());
        return new PageResult<>(voList, pageResult.getTotal());
    }

    private List<ErpExchangeRateRespVO> buildExchangeRateVOList(List<ErpExchangeRateDO> list) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        
        // 获取币种信息
        List<Long> currencyIds = CollUtil.newArrayList();
        list.forEach(exchangeRate -> {
            currencyIds.add(exchangeRate.getFromCurrencyId());
            currencyIds.add(exchangeRate.getToCurrencyId());
        });
        Map<Long, ErpCurrencyDO> currencyMap = convertMap(currencyService.validCurrencyList(currencyIds), ErpCurrencyDO::getId);
        
        // 构建VO
        return BeanUtils.toBean(list, ErpExchangeRateRespVO.class, vo -> {
            ErpCurrencyDO fromCurrency = currencyMap.get(vo.getFromCurrencyId());
            ErpCurrencyDO toCurrency = currencyMap.get(vo.getToCurrencyId());
            if (fromCurrency != null) {
                vo.setFromCurrencyName(fromCurrency.getName());
            }
            if (toCurrency != null) {
                vo.setToCurrencyName(toCurrency.getName());
            }
        });
    }

}
