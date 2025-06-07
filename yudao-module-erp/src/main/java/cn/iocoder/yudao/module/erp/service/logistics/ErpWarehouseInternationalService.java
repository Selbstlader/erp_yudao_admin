package cn.iocoder.yudao.module.erp.service.logistics;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.warehouse.ErpWarehouseInternationalPageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.warehouse.ErpWarehouseInternationalRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.logistics.vo.warehouse.ErpWarehouseInternationalSaveReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.logistics.ErpWarehouseInternationalDO;

import jakarta.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * ERP 国际仓库 Service 接口
 *
 * @author 芋道源码
 */
public interface ErpWarehouseInternationalService {

    /**
     * 创建国际仓库
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createWarehouseInternational(@Valid ErpWarehouseInternationalSaveReqVO createReqVO);

    /**
     * 更新国际仓库
     *
     * @param updateReqVO 更新信息
     */
    void updateWarehouseInternational(@Valid ErpWarehouseInternationalSaveReqVO updateReqVO);

    /**
     * 删除国际仓库
     *
     * @param id 编号
     */
    void deleteWarehouseInternational(Long id);

    /**
     * 获得国际仓库
     *
     * @param id 编号
     * @return 国际仓库
     */
    ErpWarehouseInternationalDO getWarehouseInternational(Long id);

    /**
     * 获得国际仓库分页
     *
     * @param pageReqVO 分页查询
     * @return 国际仓库分页
     */
    PageResult<ErpWarehouseInternationalRespVO> getWarehouseInternationalPage(ErpWarehouseInternationalPageReqVO pageReqVO);

    /**
     * 获得指定状态的国际仓库列表
     *
     * @param status 状态
     * @return 国际仓库列表
     */
    List<ErpWarehouseInternationalRespVO> getWarehouseInternationalListByStatus(Integer status);

    /**
     * 获得国际仓库列表
     *
     * @param ids 编号数组
     * @return 国际仓库列表
     */
    List<ErpWarehouseInternationalRespVO> getWarehouseInternationalList(Collection<Long> ids);

    /**
     * 根据国家代码获得国际仓库列表
     *
     * @param countryCode 国家代码
     * @return 国际仓库列表
     */
    List<ErpWarehouseInternationalRespVO> getWarehouseInternationalListByCountryCode(String countryCode);

    /**
     * 校验国际仓库们的有效性
     *
     * @param ids 编号数组
     * @return 国际仓库列表
     */
    List<ErpWarehouseInternationalDO> validWarehouseInternationalList(Collection<Long> ids);

} 