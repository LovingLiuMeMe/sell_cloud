package cn.lovingliu.product.dto;

import lombok.Data;

/**
 * @Author：LovingLiu
 * @Description: 购物车
 * @Date：Created in 2019-10-11
 */
@Data
public class CartDTO {
    private String productId;
    private Integer productQuantity;

    public CartDTO(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
