package cn.iocoder.yudao.module.erp.dal.mysql.logistics;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.method.ErpLogisticsMethodPageReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.logistics.ErpLogisticsMethodDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ERP 物流方式 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ErpLogisticsMethodMapper extends BaseMapperX<ErpLogisticsMethodDO> {

    default PageResult<ErpLogisticsMethodDO> selectPage(ErpLogisticsMethodPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ErpLogisticsMethodDO>()
                .eqIfPresent(ErpLogisticsMethodDO::getProviderId, reqVO.getProviderId())
                .likeIfPresent(ErpLogisticsMethodDO::getName, reqVO.getName())
                .likeIfPresent(ErpLogisticsMethodDO::getCode, reqVO.getCode())
                .eqIfPresent(ErpLogisticsMethodDO::getStatus, reqVO.getStatus())
                .orderByAsc(ErpLogisticsMethodDO::getSort));
    }

    default List<ErpLogisticsMethodDO> selectListByStatus(Integer status) {
        return selectList(new LambdaQueryWrapperX<ErpLogisticsMethodDO>()
                .eq(ErpLogisticsMethodDO::getStatus, status)
                .orderByAsc(ErpLogisticsMethodDO::getSort));
    }

    default List<ErpLogisticsMethodDO> selectListByProviderId(Long providerId) {
        return selectList(new LambdaQueryWrapperX<ErpLogisticsMethodDO>()
                .eq(ErpLogisticsMethodDO::getProviderId, providerId)
                .orderByAsc(ErpLogisticsMethodDO::getSort));
    }

    default ErpLogisticsMethodDO selectByProviderIdAndCode(Long providerId, String code) {
        // 使用selectList而不是selectOne，避免存在多条记录时抛出TooManyResultsException
        List<ErpLogisticsMethodDO> list = selectList(new LambdaQueryWrapperX<ErpLogisticsMethodDO>()
                .eq(ErpLogisticsMethodDO::getProviderId, providerId)
                .eq(ErpLogisticsMethodDO::getCode, code)
                .last("LIMIT 1")); // 只获取第一条记录
        return list.isEmpty() ? null : list.get(0);
    }

} 