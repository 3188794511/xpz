package com.lj.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lj.base.Result;
import com.lj.model.blog.Type;
import com.lj.vo.TypeVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lj
 * @since 2022-11-05
 */
public interface TypeService extends IService<Type> {

    List<Type> listByTree();

    List<Type> listSecondLevel();

    Type getTypeById(Long id);

    List<Type> queryByParentId(Long parentId);

    Result removeTypeByIds(List<Long> ids);

    List<TypeVo> listTypeVoTree();

    List<Long> getParentTypeAndChild(Long typeId);
}
