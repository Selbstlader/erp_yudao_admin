package cn.iocoder.yudao.module.erp.dal.mysql.logistics;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.provider.ErpLogisticsProviderPageReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.logistics.ErpLogisticsProviderDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ERP 物流服务商 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ErpLogisticsProviderMapper extends BaseMapperX<ErpLogisticsProviderDO> {

    default PageResult<ErpLogisticsProviderDO> selectPage(ErpLogisticsProviderPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ErpLogisticsProviderDO>()
                .likeIfPresent(ErpLogisticsProviderDO::getName, reqVO.getName())
                .likeIfPresent(ErpLogisticsProviderDO::getCode, reqVO.getCode())
                .eqIfPresent(ErpLogisticsProviderDO::getStatus, reqVO.getStatus())
                .orderByDesc(ErpLogisticsProviderDO::getId));
    }

    default List<ErpLogisticsProviderDO> selectListByStatus(Integer status) {
        return selectList(new LambdaQueryWrapperX<ErpLogisticsProviderDO>()
                .eq(ErpLogisticsProviderDO::getStatus, status)
                .orderByAsc(ErpLogisticsProviderDO::getSort));
    }

    default ErpLogisticsProviderDO selectByCode(String code) {
        return selectOne(ErpLogisticsProviderDO::getCode, code);
    }

} 