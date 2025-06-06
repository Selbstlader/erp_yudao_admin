package cn.iocoder.yudao.module.erp.service.crossborder;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.language.ErpLanguagePageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.language.ErpLanguageRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.language.ErpLanguageSaveReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.crossborder.ErpLanguageDO;
import cn.iocoder.yudao.module.erp.dal.mysql.crossborder.ErpLanguageMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP 多语言 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ErpLanguageServiceImpl implements ErpLanguageService {

    @Resource
    private ErpLanguageMapper languageMapper;

    @Override
    public Long createLanguage(ErpLanguageSaveReqVO createReqVO) {
        // 校验语言代码唯一性
        validateLanguageCodeUnique(null, createReqVO.getCode());
        
        // 如果设置为默认语言，需要取消其他默认语言
        if (Boolean.TRUE.equals(createReqVO.getIsDefault())) {
            updateOtherLanguageNotDefault();
        }
        
        // 插入
        ErpLanguageDO language = BeanUtils.toBean(createReqVO, ErpLanguageDO.class);
        languageMapper.insert(language);
        // 返回
        return language.getId();
    }

    @Override
    public void updateLanguage(ErpLanguageSaveReqVO updateReqVO) {
        // 校验存在
        validateLanguageExists(updateReqVO.getId());
        // 校验语言代码唯一性
        validateLanguageCodeUnique(updateReqVO.getId(), updateReqVO.getCode());
        
        // 如果设置为默认语言，需要取消其他默认语言
        if (Boolean.TRUE.equals(updateReqVO.getIsDefault())) {
            updateOtherLanguageNotDefault();
        }
        
        // 更新
        ErpLanguageDO updateObj = BeanUtils.toBean(updateReqVO, ErpLanguageDO.class);
        languageMapper.updateById(updateObj);
    }

    @Override
    public void deleteLanguage(Long id) {
        // 校验存在
        ErpLanguageDO language = validateLanguageExists(id);
        
        // 校验是否为默认语言，默认语言不能删除
        if (Boolean.TRUE.equals(language.getIsDefault())) {
            throw exception(LANGUAGE_DEFAULT_CANNOT_DELETE);
        }
        
        // 删除
        languageMapper.deleteById(id);
    }

    private ErpLanguageDO validateLanguageExists(Long id) {
        ErpLanguageDO language = languageMapper.selectById(id);
        if (language == null) {
            throw exception(LANGUAGE_NOT_EXISTS);
        }
        return language;
    }

    private void validateLanguageCodeUnique(Long id, String code) {
        ErpLanguageDO language = languageMapper.selectByCode(code);
        if (language == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的语言
        if (id == null) {
            throw exception(LANGUAGE_CODE_DUPLICATE, code);
        }
        if (!language.getId().equals(id)) {
            throw exception(LANGUAGE_CODE_DUPLICATE, code);
        }
    }

    private void updateOtherLanguageNotDefault() {
        // 将其他语言设置为非默认
        List<ErpLanguageDO> defaultLanguages = languageMapper.selectList(ErpLanguageDO::getIsDefault, true);
        for (ErpLanguageDO language : defaultLanguages) {
            language.setIsDefault(false);
            languageMapper.updateById(language);
        }
    }

    @Override
    public ErpLanguageDO getLanguage(Long id) {
        return languageMapper.selectById(id);
    }

    @Override
    public PageResult<ErpLanguageRespVO> getLanguagePage(ErpLanguagePageReqVO pageReqVO) {
        PageResult<ErpLanguageDO> pageResult = languageMapper.selectPage(pageReqVO);
        return BeanUtils.toBean(pageResult, ErpLanguageRespVO.class);
    }

    @Override
    public List<ErpLanguageRespVO> getLanguageListByStatus(Integer status) {
        List<ErpLanguageDO> list = languageMapper.selectListByStatus(status);
        return BeanUtils.toBean(list, ErpLanguageRespVO.class);
    }

    @Override
    public List<ErpLanguageRespVO> getLanguageList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        List<ErpLanguageDO> list = languageMapper.selectBatchIds(ids);
        return BeanUtils.toBean(list, ErpLanguageRespVO.class);
    }

    @Override
    public List<ErpLanguageDO> validLanguageList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        List<ErpLanguageDO> list = languageMapper.selectBatchIds(ids);
        if (list.size() != ids.size()) {
            throw exception(LANGUAGE_NOT_EXISTS);
        }
        return list;
    }

    @Override
    public ErpLanguageDO getLanguageByCode(String code) {
        return languageMapper.selectByCode(code);
    }

    @Override
    public ErpLanguageDO getDefaultLanguage() {
        return languageMapper.selectDefaultLanguage();
    }

}
