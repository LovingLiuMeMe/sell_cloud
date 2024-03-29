package cn.lovingliu.product.enums;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-11
 */
public enum CommonStatusEnum implements CodeEnum {
    UP(0, "上架"),
    PRODUCT_NOT_EXIT(201, "商品不存在"),
    PRODUCT_STOCK_ERROR(202, "商品库存错误"),
    SUCCESS(200, "成功"),
    ERROR(400, "失败"),
    DOWN(1, "下架");

    private final Integer code;
    private final String msg;

    CommonStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
