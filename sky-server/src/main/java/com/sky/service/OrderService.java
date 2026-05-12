package com.sky.service;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderSubmitVO;

public interface OrderService {
    /**
     * 用户下单
     * @param orderSubmitDTO 订单提交DTO
     * @return 订单提交VO
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO orderSubmitDTO);
}
