package cn.iocoder.yudao.module.erp.dal.mysql.logistics;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.warehouse.ErpWarehouseInternationalPageReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.logistics.ErpWarehouseInternationalDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ERP 国际仓库 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ErpWarehouseInternationalMapper extends BaseMapperX<ErpWarehouseInternationalDO> {

    default PageResult<ErpWarehouseInternationalDO> selectPage(ErpWarehouseInternationalPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ErpWarehouseInternationalDO>()
                .likeIfPresent(ErpWarehouseInternationalDO::getCountryCode, reqVO.getCountryCode())
                .eqIfPresent(ErpWarehouseInternationalDO::getWarehouseId, reqVO.getWarehouseId())
                .eqIfPresent(ErpWarehouseInternationalDO::getIsBonded, reqVO.getIsBonded())
                .eqIfPresent(ErpWarehouseInternationalDO::getStatus, reqVO.getStatus())
                .orderByDesc(ErpWarehouseInternationalDO::getId));
    }

    default List<ErpWarehouseInternationalDO> selectListByStatus(Integer status) {
        return selectList(new LambdaQueryWrapperX<ErpWarehouseInternationalDO>()
                .eq(ErpWarehouseInternationalDO::getStatus, status));
    }

    default List<ErpWarehouseInternationalDO> selectListByCountryCode(String countryCode) {
        return selectList(new LambdaQueryWrapperX<ErpWarehouseInternationalDO>()
                .eq(ErpWarehouseInternationalDO::getCountryCode, countryCode));
    }

} 