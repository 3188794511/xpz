package com.lj.model.blog;

import com.lj.base.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@TableName("tag")
public class Tag extends BaseModel implements Serializable {

    private static final long serialVersionUID=1L;
    /**
     * 标签名
     */
    @TableField("tag_name")
    @NotBlank(message = "标签名不能为空")
    private String tagName;

    /**
     * 标签排序
     */
    @TableField("sort")
    private Integer sort;

}