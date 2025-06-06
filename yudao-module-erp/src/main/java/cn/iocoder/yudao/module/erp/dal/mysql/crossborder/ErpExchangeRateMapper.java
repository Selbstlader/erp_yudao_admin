package cn.iocoder.yudao.module.erp.dal.mysql.crossborder;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.exchangerate.ErpExchangeRatePageReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.crossborder.ErpExchangeRateDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

/**
 * ERP 汇率 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ErpExchangeRateMapper extends BaseMapperX<ErpExchangeRateDO> {

    default PageResult<ErpExchangeRateDO> selectPage(ErpExchangeRatePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ErpExchangeRateDO>()
                .eqIfPresent(ErpExchangeRateDO::getFromCurrencyId, reqVO.getFromCurrencyId())
                .eqIfPresent(ErpExchangeRateDO::getToCurrencyId, reqVO.getToCurrencyId())
                .eqIfPresent(ErpExchangeRateDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(ErpExchangeRateDO::getEffectiveDate, reqVO.getEffectiveDate())
                .orderByDesc(ErpExchangeRateDO::getEffectiveDate));
    }

    default List<ErpExchangeRateDO> selectListByStatus(Integer status) {
        return selectList(new LambdaQueryWrapperX<ErpExchangeRateDO>()
                .eq(ErpExchangeRateDO::getStatus, status)
                .orderByDesc(ErpExchangeRateDO::getEffectiveDate));
    }

    default ErpExchangeRateDO selectLatestRate(Long fromCurrencyId, Long toCurrencyId, LocalDate date) {
        return selectOne(new LambdaQueryWrapperX<ErpExchangeRateDO>()
                .eq(ErpExchangeRateDO::getFromCurrencyId, fromCurrencyId)
                .eq(ErpExchangeRateDO::getToCurrencyId, toCurrencyId)
                .eq(ErpExchangeRateDO::getStatus, 1) // 启用状态
                .le(ErpExchangeRateDO::getEffectiveDate, date)
                .and(wrapper -> wrapper.isNull(ErpExchangeRateDO::getExpiryDate)
                        .or().ge(ErpExchangeRateDO::getExpiryDate, date))
                .orderByDesc(ErpExchangeRateDO::getEffectiveDate)
                .last("LIMIT 1"));
    }

    default List<ErpExchangeRateDO> selectEffectiveRates(LocalDate date) {
        return selectList(new LambdaQueryWrapperX<ErpExchangeRateDO>()
                .eq(ErpExchangeRateDO::getStatus, 1) // 启用状态
                .le(ErpExchangeRateDO::getEffectiveDate, date)
                .and(wrapper -> wrapper.isNull(ErpExchangeRateDO::getExpiryDate)
                        .or().ge(ErpExchangeRateDO::getExpiryDate, date))
                .orderByDesc(ErpExchangeRateDO::getEffectiveDate));
    }

}
