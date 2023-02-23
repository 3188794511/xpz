package com.lj.vo;

import lombok.Data;

import java.util.List;

@Data
public class TypeVo {
    private String label;
    private Long value;
    private List<TypeVo> children;
}
