package cn.lovingliu.order.handle;

import cn.lovingliu.order.common.ServerResponse;
import cn.lovingliu.order.exception.OrderException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-11
 */
@ControllerAdvice
@Slf4j
public class OrderExceptionHandle {
    @ExceptionHandler(OrderException.class)
    @ResponseBody
    public ServerResponse resolveOrderException(Exception e){
        if(e instanceof OrderException){
            OrderException orderException = (OrderException) e;
            log.error("【统一异常处理】=> {}",e.getMessage());
            return  ServerResponse.createByErrorCodeMessage(orderException.getCode(),orderException.getMessage());
        }
        return  ServerResponse.createByErrorMessage(e.getMessage());
    }
}
