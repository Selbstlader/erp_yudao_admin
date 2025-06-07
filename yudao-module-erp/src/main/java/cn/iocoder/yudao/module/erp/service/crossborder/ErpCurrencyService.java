package cn.iocoder.yudao.module.erp.service.crossborder;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.currency.ErpCurrencyPageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.currency.ErpCurrencyRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.currency.ErpCurrencySaveReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.crossborder.ErpCurrencyDO;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * ERP 多币种 Service 接口
 *
 * @author 芋道源码
 */
public interface ErpCurrencyService {

    /**
     * 创建多币种
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCurrency(@Valid ErpCurrencySaveReqVO createReqVO);

    /**
     * 更新多币种
     *
     * @param updateReqVO 更新信息
     */
    void updateCurrency(@Valid ErpCurrencySaveReqVO updateReqVO);

    /**
     * 删除多币种
     *
     * @param id 编号
     */
    void deleteCurrency(Long id);

    /**
     * 获得多币种
     *
     * @param id 编号
     * @return 多币种
     */
    ErpCurrencyDO getCurrency(Long id);

    /**
     * 获得多币种分页
     *
     * @param pageReqVO 分页查询
     * @return 多币种分页
     */
    PageResult<ErpCurrencyRespVO> getCurrencyPage(ErpCurrencyPageReqVO pageReqVO);

    /**
     * 获得指定状态的多币种列表
     *
     * @param status 状态
     * @return 多币种列表
     */
    List<ErpCurrencyRespVO> getCurrencyListByStatus(Integer status);

    /**
     * 获得多币种列表
     *
     * @param ids 编号数组
     * @return 多币种列表
     */
    List<ErpCurrencyRespVO> getCurrencyList(Collection<Long> ids);

    /**
     * 校验币种们的有效性
     *
     * @param ids 编号数组
     * @return 币种列表
     */
    List<ErpCurrencyDO> validCurrencyList(Collection<Long> ids);

    /**
     * 获取币种列表，与validCurrencyList不同，不校验所有ID是否存在
     *
     * @param ids 编号数组
     * @return 存在的币种列表
     */
    List<ErpCurrencyDO> getCurrencyDOList(Collection<Long> ids);

    /**
     * 根据币种代码获得多币种
     *
     * @param code 币种代码
     * @return 多币种
     */
    ErpCurrencyDO getCurrencyByCode(String code);

    /**
     * 获得基础币种
     *
     * @return 基础币种
     */
    ErpCurrencyDO getBaseCurrency();

    /**
     * 币种转换
     *
     * @param amount 金额
     * @param fromCurrencyCode 源币种代码
     * @param toCurrencyCode 目标币种代码
     * @return 转换后的金额
     */
    BigDecimal convertCurrency(BigDecimal amount, String fromCurrencyCode, String toCurrencyCode);

}
