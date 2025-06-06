package cn.iocoder.yudao.module.erp.dal.mysql.crossborder;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.erp.controller.admin.crossborder.vo.language.ErpLanguagePageReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.crossborder.ErpLanguageDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ERP 多语言 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ErpLanguageMapper extends BaseMapperX<ErpLanguageDO> {

    default PageResult<ErpLanguageDO> selectPage(ErpLanguagePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ErpLanguageDO>()
                .likeIfPresent(ErpLanguageDO::getName, reqVO.getName())
                .likeIfPresent(ErpLanguageDO::getCode, reqVO.getCode())
                .eqIfPresent(ErpLanguageDO::getStatus, reqVO.getStatus())
                .orderByAsc(ErpLanguageDO::getSort));
    }

    default List<ErpLanguageDO> selectListByStatus(Integer status) {
        return selectList(new LambdaQueryWrapperX<ErpLanguageDO>()
                .eq(ErpLanguageDO::getStatus, status)
                .orderByAsc(ErpLanguageDO::getSort));
    }

    default ErpLanguageDO selectByCode(String code) {
        return selectOne(ErpLanguageDO::getCode, code);
    }

    default ErpLanguageDO selectDefaultLanguage() {
        return selectOne(ErpLanguageDO::getIsDefault, true);
    }

}
