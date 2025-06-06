package cn.iocoder.yudao.module.erp.service.crossborder;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.exchangerate.ErpExchangeRatePageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.exchangerate.ErpExchangeRateRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.exchangerate.ErpExchangeRateSaveReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.crossborder.ErpExchangeRateDO;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * ERP 汇率 Service 接口
 *
 * @author 芋道源码
 */
public interface ErpExchangeRateService {

    /**
     * 创建汇率
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createExchangeRate(@Valid ErpExchangeRateSaveReqVO createReqVO);

    /**
     * 更新汇率
     *
     * @param updateReqVO 更新信息
     */
    void updateExchangeRate(@Valid ErpExchangeRateSaveReqVO updateReqVO);

    /**
     * 删除汇率
     *
     * @param id 编号
     */
    void deleteExchangeRate(Long id);

    /**
     * 获得汇率
     *
     * @param id 编号
     * @return 汇率
     */
    ErpExchangeRateDO getExchangeRate(Long id);

    /**
     * 获得汇率分页
     *
     * @param pageReqVO 分页查询
     * @return 汇率分页
     */
    PageResult<ErpExchangeRateRespVO> getExchangeRatePage(ErpExchangeRatePageReqVO pageReqVO);

    /**
     * 获得指定状态的汇率列表
     *
     * @param status 状态
     * @return 汇率列表
     */
    List<ErpExchangeRateRespVO> getExchangeRateListByStatus(Integer status);

    /**
     * 获得汇率列表
     *
     * @param ids 编号数组
     * @return 汇率列表
     */
    List<ErpExchangeRateRespVO> getExchangeRateList(Collection<Long> ids);

    /**
     * 校验汇率们的有效性
     *
     * @param ids 编号数组
     * @return 汇率列表
     */
    List<ErpExchangeRateDO> validExchangeRateList(Collection<Long> ids);

    /**
     * 获得最新汇率
     *
     * @param fromCurrencyId 源币种编号
     * @param toCurrencyId 目标币种编号
     * @param date 日期
     * @return 汇率
     */
    ErpExchangeRateDO getLatestExchangeRate(Long fromCurrencyId, Long toCurrencyId, LocalDate date);

    /**
     * 获得指定日期的有效汇率列表
     *
     * @param date 日期
     * @return 汇率列表
     */
    List<ErpExchangeRateDO> getEffectiveExchangeRates(LocalDate date);

    /**
     * 根据汇率进行币种转换
     *
     * @param amount 金额
     * @param fromCurrencyId 源币种编号
     * @param toCurrencyId 目标币种编号
     * @param date 日期
     * @return 转换后的金额
     */
    BigDecimal convertCurrencyByRate(BigDecimal amount, Long fromCurrencyId, Long toCurrencyId, LocalDate date);

}
