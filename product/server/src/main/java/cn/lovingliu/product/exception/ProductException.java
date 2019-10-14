package cn.lovingliu.product.exception;

import cn.lovingliu.product.enums.CommonStatusEnum;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-11
 */
public class ProductException extends RuntimeException {
    private Integer code;

    public ProductException(Integer code,String msg){
        super(msg);
        this.code = code;
    }

    public ProductException(CommonStatusEnum commonStatusEnum){
        super(commonStatusEnum.getMsg());
        this.code = commonStatusEnum.getCode();
    }
}
