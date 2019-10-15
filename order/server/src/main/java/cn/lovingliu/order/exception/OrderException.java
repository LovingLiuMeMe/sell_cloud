package cn.lovingliu.order.exception;

import cn.lovingliu.order.enums.CommonStatusEnum;
import lombok.Data;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-11
 */
@Data
public class OrderException extends RuntimeException {
    private Integer code;

    public OrderException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public OrderException(CommonStatusEnum commonStatusEnum) {
        super(commonStatusEnum.getMsg());
        this.code = commonStatusEnum.getCode();
    }
}
