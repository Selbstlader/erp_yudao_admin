package cn.iocoder.yudao.module.erp.service.crossborder;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.currency.ErpCurrencyPageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.currency.ErpCurrencyRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.currency.ErpCurrencySaveReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.crossborder.ErpCurrencyDO;
import cn.iocoder.yudao.module.erp.dal.mysql.crossborder.ErpCurrencyMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP 多币种 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ErpCurrencyServiceImpl implements ErpCurrencyService {

    @Resource
    private ErpCurrencyMapper currencyMapper;

    @Override
    public Long createCurrency(ErpCurrencySaveReqVO createReqVO) {
        // 校验币种代码唯一性
        validateCurrencyCodeUnique(null, createReqVO.getCode());
        
        // 如果设置为基础币种，需要取消其他基础币种，并设置汇率为1
        if (Boolean.TRUE.equals(createReqVO.getIsBase())) {
            updateOtherCurrencyNotBase();
            createReqVO.setExchangeRate(BigDecimal.ONE);
        }
        
        // 插入
        ErpCurrencyDO currency = BeanUtils.toBean(createReqVO, ErpCurrencyDO.class);
        currencyMapper.insert(currency);
        // 返回
        return currency.getId();
    }

    @Override
    public void updateCurrency(ErpCurrencySaveReqVO updateReqVO) {
        // 校验存在
        validateCurrencyExists(updateReqVO.getId());
        // 校验币种代码唯一性
        validateCurrencyCodeUnique(updateReqVO.getId(), updateReqVO.getCode());
        
        // 如果设置为基础币种，需要取消其他基础币种，并设置汇率为1
        if (Boolean.TRUE.equals(updateReqVO.getIsBase())) {
            updateOtherCurrencyNotBase();
            updateReqVO.setExchangeRate(BigDecimal.ONE);
        }
        
        // 更新
        ErpCurrencyDO updateObj = BeanUtils.toBean(updateReqVO, ErpCurrencyDO.class);
        currencyMapper.updateById(updateObj);
    }

    @Override
    public void deleteCurrency(Long id) {
        // 校验存在
        ErpCurrencyDO currency = validateCurrencyExists(id);
        
        // 校验是否为基础币种，基础币种不能删除
        if (Boolean.TRUE.equals(currency.getIsBase())) {
            throw exception(CURRENCY_BASE_CANNOT_DELETE);
        }
        
        // 删除
        currencyMapper.deleteById(id);
    }

    private ErpCurrencyDO validateCurrencyExists(Long id) {
        ErpCurrencyDO currency = currencyMapper.selectById(id);
        if (currency == null) {
            throw exception(CURRENCY_NOT_EXISTS);
        }
        return currency;
    }

    private void validateCurrencyCodeUnique(Long id, String code) {
        ErpCurrencyDO currency = currencyMapper.selectByCode(code);
        if (currency == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的币种
        if (id == null) {
            throw exception(CURRENCY_CODE_DUPLICATE, code);
        }
        if (!currency.getId().equals(id)) {
            throw exception(CURRENCY_CODE_DUPLICATE, code);
        }
    }

    private void updateOtherCurrencyNotBase() {
        // 将其他币种设置为非基础币种
        List<ErpCurrencyDO> baseCurrencies = currencyMapper.selectList(ErpCurrencyDO::getIsBase, true);
        for (ErpCurrencyDO currency : baseCurrencies) {
            currency.setIsBase(false);
            currencyMapper.updateById(currency);
        }
    }

    @Override
    public ErpCurrencyDO getCurrency(Long id) {
        return currencyMapper.selectById(id);
    }

    @Override
    public PageResult<ErpCurrencyRespVO> getCurrencyPage(ErpCurrencyPageReqVO pageReqVO) {
        PageResult<ErpCurrencyDO> pageResult = currencyMapper.selectPage(pageReqVO);
        return BeanUtils.toBean(pageResult, ErpCurrencyRespVO.class);
    }

    @Override
    public List<ErpCurrencyRespVO> getCurrencyListByStatus(Integer status) {
        List<ErpCurrencyDO> list = currencyMapper.selectListByStatus(status);
        return BeanUtils.toBean(list, ErpCurrencyRespVO.class);
    }

    @Override
    public List<ErpCurrencyRespVO> getCurrencyList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        List<ErpCurrencyDO> list = currencyMapper.selectBatchIds(ids);
        return BeanUtils.toBean(list, ErpCurrencyRespVO.class);
    }

    @Override
    public List<ErpCurrencyDO> validCurrencyList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        List<ErpCurrencyDO> list = currencyMapper.selectBatchIds(ids);
        if (list.size() != ids.size()) {
            throw exception(CURRENCY_NOT_EXISTS);
        }
        return list;
    }

    @Override
    public ErpCurrencyDO getCurrencyByCode(String code) {
        return currencyMapper.selectByCode(code);
    }

    @Override
    public ErpCurrencyDO getBaseCurrency() {
        return currencyMapper.selectBaseCurrency();
    }

    @Override
    public BigDecimal convertCurrency(BigDecimal amount, String fromCurrencyCode, String toCurrencyCode) {
        if (fromCurrencyCode.equals(toCurrencyCode)) {
            return amount;
        }
        
        ErpCurrencyDO fromCurrency = getCurrencyByCode(fromCurrencyCode);
        ErpCurrencyDO toCurrency = getCurrencyByCode(toCurrencyCode);
        
        if (fromCurrency == null) {
            throw exception(CURRENCY_NOT_EXISTS);
        }
        if (toCurrency == null) {
            throw exception(CURRENCY_NOT_EXISTS);
        }
        
        // 先转换为基础币种，再转换为目标币种
        BigDecimal baseAmount = amount.divide(fromCurrency.getExchangeRate(), 10, RoundingMode.HALF_UP);
        BigDecimal result = baseAmount.multiply(toCurrency.getExchangeRate());
        
        // 根据目标币种的小数位数进行四舍五入
        return result.setScale(toCurrency.getDecimalPlaces(), RoundingMode.HALF_UP);
    }

}
