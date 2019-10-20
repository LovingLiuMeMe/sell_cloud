package cn.lovingliu.order.service;

import cn.lovingliu.order.dto.OrderDTO;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-11
 */
public interface OrderService {
    OrderDTO createrOrder(OrderDTO orderDTO);
    OrderDTO finish(String orderId);
}
