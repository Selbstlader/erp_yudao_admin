package cn.iocoder.yudao.module.erp.service.logistics;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.provider.ErpLogisticsProviderPageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.provider.ErpLogisticsProviderRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.provider.ErpLogisticsProviderSaveReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.logistics.ErpLogisticsProviderDO;

import jakarta.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * ERP 物流服务商 Service 接口
 *
 * @author 芋道源码
 */
public interface ErpLogisticsProviderService {

    /**
     * 创建物流服务商
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createLogisticsProvider(@Valid ErpLogisticsProviderSaveReqVO createReqVO);

    /**
     * 更新物流服务商
     *
     * @param updateReqVO 更新信息
     */
    void updateLogisticsProvider(@Valid ErpLogisticsProviderSaveReqVO updateReqVO);

    /**
     * 删除物流服务商
     *
     * @param id 编号
     */
    void deleteLogisticsProvider(Long id);

    /**
     * 获得物流服务商
     *
     * @param id 编号
     * @return 物流服务商
     */
    ErpLogisticsProviderDO getLogisticsProvider(Long id);

    /**
     * 获得物流服务商分页
     *
     * @param pageReqVO 分页查询
     * @return 物流服务商分页
     */
    PageResult<ErpLogisticsProviderRespVO> getLogisticsProviderPage(ErpLogisticsProviderPageReqVO pageReqVO);

    /**
     * 获得指定状态的物流服务商列表
     *
     * @param status 状态
     * @return 物流服务商列表
     */
    List<ErpLogisticsProviderRespVO> getLogisticsProviderListByStatus(Integer status);

    /**
     * 获得物流服务商列表
     *
     * @param ids 编号数组
     * @return 物流服务商列表
     */
    List<ErpLogisticsProviderRespVO> getLogisticsProviderList(Collection<Long> ids);

    /**
     * 根据代码获得物流服务商
     *
     * @param code 代码
     * @return 物流服务商
     */
    ErpLogisticsProviderDO getLogisticsProviderByCode(String code);

    /**
     * 校验物流服务商们的有效性
     *
     * @param ids 编号数组
     * @return 物流服务商列表
     */
    List<ErpLogisticsProviderDO> validLogisticsProviderList(Collection<Long> ids);

    /**
     * 测试物流服务商API连接
     *
     * @param id 编号
     * @return 是否连接成功
     */
    boolean testLogisticsProviderConnection(Long id);

} 