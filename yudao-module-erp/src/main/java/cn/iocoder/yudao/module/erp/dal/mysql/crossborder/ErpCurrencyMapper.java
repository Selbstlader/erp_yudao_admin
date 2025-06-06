package cn.iocoder.yudao.module.erp.dal.mysql.crossborder;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.currency.ErpCurrencyPageReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.crossborder.ErpCurrencyDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ERP 多币种 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ErpCurrencyMapper extends BaseMapperX<ErpCurrencyDO> {

    default PageResult<ErpCurrencyDO> selectPage(ErpCurrencyPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ErpCurrencyDO>()
                .likeIfPresent(ErpCurrencyDO::getName, reqVO.getName())
                .likeIfPresent(ErpCurrencyDO::getCode, reqVO.getCode())
                .eqIfPresent(ErpCurrencyDO::getStatus, reqVO.getStatus())
                .orderByAsc(ErpCurrencyDO::getSort));
    }

    default List<ErpCurrencyDO> selectListByStatus(Integer status) {
        return selectList(new LambdaQueryWrapperX<ErpCurrencyDO>()
                .eq(ErpCurrencyDO::getStatus, status)
                .orderByAsc(ErpCurrencyDO::getSort));
    }

    default ErpCurrencyDO selectByCode(String code) {
        return selectOne(ErpCurrencyDO::getCode, code);
    }

    default ErpCurrencyDO selectBaseCurrency() {
        return selectOne(ErpCurrencyDO::getIsBase, true);
    }

}
