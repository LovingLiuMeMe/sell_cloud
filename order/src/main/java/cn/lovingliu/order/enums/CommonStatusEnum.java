package cn.lovingliu.order.enums;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-11
 */
public enum CommonStatusEnum implements CodeEnum {
    PARAMS_ERROR(100,"参数错误"),
    CART_EMPTY(101,"购物车为空"),
    SUCCESS(200,"成功"),
    ERROR(400,"失败"),
    NEW_ORDER(0,"新的订单"),
    WAIT_PAY(0,"待支付");

    private final Integer code;
    private final String msg;

    CommonStatusEnum(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
