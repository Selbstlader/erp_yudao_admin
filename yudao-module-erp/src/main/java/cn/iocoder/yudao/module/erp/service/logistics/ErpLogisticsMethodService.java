package cn.iocoder.yudao.module.erp.service.logistics;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.method.ErpLogisticsMethodPageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.method.ErpLogisticsMethodRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.method.ErpLogisticsMethodSaveReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.logistics.ErpLogisticsMethodDO;

import jakarta.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * ERP 物流方式 Service 接口
 *
 * @author 芋道源码
 */
public interface  ErpLogisticsMethodService {

    /**
     * 创建物流方式
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createLogisticsMethod(@Valid ErpLogisticsMethodSaveReqVO createReqVO);

    /**
     * 更新物流方式
     *
     * @param updateReqVO 更新信息
     */
    void updateLogisticsMethod(@Valid ErpLogisticsMethodSaveReqVO updateReqVO);

    /**
     * 删除物流方式
     *
     * @param id 编号
     */
    void deleteLogisticsMethod(Long id);

    /**
     * 获得物流方式
     *
     * @param id 编号
     * @return 物流方式
     */
    ErpLogisticsMethodDO getLogisticsMethod(Long id);

    /**
     * 获得物流方式分页
     *
     * @param pageReqVO 分页查询
     * @return 物流方式分页
     */
    PageResult<ErpLogisticsMethodRespVO> getLogisticsMethodPage(ErpLogisticsMethodPageReqVO pageReqVO);

    /**
     * 获得指定状态的物流方式列表
     *
     * @param status 状态
     * @return 物流方式列表
     */
    List<ErpLogisticsMethodRespVO> getLogisticsMethodListByStatus(Integer status);

    /**
     * 获得物流方式列表
     *
     * @param ids 编号数组
     * @return 物流方式列表
     */
    List<ErpLogisticsMethodRespVO> getLogisticsMethodList(Collection<Long> ids);

    /**
     * 根据服务商编号获得物流方式列表
     *
     * @param providerId 服务商编号
     * @return 物流方式列表
     */
    List<ErpLogisticsMethodRespVO> getLogisticsMethodListByProviderId(Long providerId);

    /**
     * 校验物流方式们的有效性
     *
     * @param ids 编号数组
     * @return 物流方式列表
     */
    List<ErpLogisticsMethodDO> validLogisticsMethodList(Collection<Long> ids);

} 