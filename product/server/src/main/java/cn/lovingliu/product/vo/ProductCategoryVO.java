package cn.lovingliu.product.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-11
 */
@Data
public class ProductCategoryVO {

    @JsonProperty("name")
    private String categoryName;//类目名称
    @JsonProperty("type")
    private Integer categoryType;//类目编号
    @JsonProperty("foods")
    private List<ProductInfoVO> productInfoVOList;
}
