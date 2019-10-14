package cn.lovingliu.order.convert;

import cn.lovingliu.order.dataobject.OrderDetail;
import cn.lovingliu.order.dto.OrderDTO;
import cn.lovingliu.order.enums.CommonStatusEnum;
import cn.lovingliu.order.exception.OrderException;
import cn.lovingliu.order.form.OrderForm;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-11
 */
@Slf4j
public class OrderFormToOrderDTO {
    public static OrderDTO convert(OrderForm orderForm){
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerName(orderForm.getName());
        orderDTO.setBuyerPhone(orderForm.getPhone());
        orderDTO.setBuyerAddress(orderForm.getAddress());
        orderDTO.setBuyerOpenid(orderForm.getOpenid());
        Gson gson = new Gson();

        List<OrderDetail> orderDetailList;
        try{
            orderDetailList = gson.fromJson(orderForm.getItems(),new TypeToken<List<OrderDetail>>(){}.getType());
        }catch (Exception e){
            log.error("json格式转换错误{}",orderForm.getItems());
            throw new OrderException(CommonStatusEnum.PARAMS_ERROR);
        }

        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }
}
