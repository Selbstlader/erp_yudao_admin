package cn.iocoder.yudao.module.erp.service.crossborder;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.language.ErpLanguagePageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.language.ErpLanguageRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.language.ErpLanguageSaveReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.crossborder.ErpLanguageDO;

import jakarta.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * ERP 多语言 Service 接口
 *
 * @author 芋道源码
 */
public interface ErpLanguageService {

    /**
     * 创建多语言
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createLanguage(@Valid ErpLanguageSaveReqVO createReqVO);

    /**
     * 更新多语言
     *
     * @param updateReqVO 更新信息
     */
    void updateLanguage(@Valid ErpLanguageSaveReqVO updateReqVO);

    /**
     * 删除多语言
     *
     * @param id 编号
     */
    void deleteLanguage(Long id);

    /**
     * 获得多语言
     *
     * @param id 编号
     * @return 多语言
     */
    ErpLanguageDO getLanguage(Long id);

    /**
     * 获得多语言分页
     *
     * @param pageReqVO 分页查询
     * @return 多语言分页
     */
    PageResult<ErpLanguageRespVO> getLanguagePage(ErpLanguagePageReqVO pageReqVO);

    /**
     * 获得指定状态的多语言列表
     *
     * @param status 状态
     * @return 多语言列表
     */
    List<ErpLanguageRespVO> getLanguageListByStatus(Integer status);

    /**
     * 获得多语言列表
     *
     * @param ids 编号数组
     * @return 多语言列表
     */
    List<ErpLanguageRespVO> getLanguageList(Collection<Long> ids);

    /**
     * 校验多语言们的有效性
     *
     * @param ids 编号数组
     * @return 多语言列表
     */
    List<ErpLanguageDO> validLanguageList(Collection<Long> ids);

    /**
     * 根据语言代码获得多语言
     *
     * @param code 语言代码
     * @return 多语言
     */
    ErpLanguageDO getLanguageByCode(String code);

    /**
     * 获得默认语言
     *
     * @return 默认语言
     */
    ErpLanguageDO getDefaultLanguage();

}
