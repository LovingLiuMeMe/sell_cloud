package cn.lovingliu.order.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-11
 */
@Data
public class OrderForm {
    @NotEmpty(message = "姓名必须填写")

    private String name;
    @NotEmpty(message = "收货电话必须填写")

    private String phone;
    @NotEmpty(message = "收货地址必须填写")
    private String address;

    @NotEmpty(message = "openid必须填写")
    private String openid;

    @NotEmpty(message = "购物车不能空")
    private String items;
}
