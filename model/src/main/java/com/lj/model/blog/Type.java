package com.lj.model.blog;

import com.lj.base.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Data
@TableName("type")
public class Type extends BaseModel implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 父分类id(没有父分类为-1)
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 分类名
     */
    @TableField("type_name")
    @NotBlank(message = "分类名不能为空")
    private String typeName;

    /**
     * 排序
     */
    @TableField("sorted")
    private Integer sorted;

    //子分类
    @TableField(exist = false)
    private List<Type> sonTypeList;
}

