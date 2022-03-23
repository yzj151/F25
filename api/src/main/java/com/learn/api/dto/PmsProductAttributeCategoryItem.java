package com.learn.api.dto;


import com.learn.api.model.PmsProductAttribute;
import com.learn.api.model.PmsProductAttributeCategory;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 包含有分类下属性的dto
 * Created by macro on 2018/5/24.
 */
@Data
public class PmsProductAttributeCategoryItem extends PmsProductAttributeCategory implements Serializable {



    private List<PmsProductAttribute> productAttributeList;//每个种类下都有多个PmsProductAttribute


}
