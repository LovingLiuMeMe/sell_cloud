package cn.lovingliu.product.common;

import lombok.Data;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-14
 */
@Data
public class DecreaseStockInput {

    private String productId;

    private Integer productQuantity;

    public DecreaseStockInput() {
    }

    public DecreaseStockInput(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
