package cn.lovingliu.order.controller;

import cn.lovingliu.order.common.ServerResponse;
import cn.lovingliu.order.convert.OrderFormToOrderDTO;
import cn.lovingliu.order.dto.OrderDTO;
import cn.lovingliu.order.enums.CommonStatusEnum;
import cn.lovingliu.order.exception.OrderException;
import cn.lovingliu.order.form.OrderForm;
import cn.lovingliu.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-11
 */
@RestController
@RequestMapping("order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("create")
    public ServerResponse<Map<String, String>> create(@Valid OrderForm orderForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("参数不正确,参数不正确{}", orderForm);
            throw new OrderException(CommonStatusEnum.PARAMS_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }

        OrderDTO orderDTO = OrderFormToOrderDTO.convert(orderForm);
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("【创建订单购物车不能为空】");
            throw new OrderException(CommonStatusEnum.CART_EMPTY);
        }

        OrderDTO resultDto = orderService.createrOrder(orderDTO);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("orderId", resultDto.getOrderId());
        return ServerResponse.createBySuccess(resultMap);
    }
}
