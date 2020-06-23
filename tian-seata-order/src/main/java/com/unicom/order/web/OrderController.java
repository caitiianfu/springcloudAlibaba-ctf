/**
 * Title: OrderController.java Copyright: Copyright (c) 2018 Company: Unicom
 *
 * @author ctf
 * @date 2019年12月2日
 * @version 1.0
 */
package com.unicom.order.web;

import com.unicom.common.api.ResultUtils;
import com.unicom.generator.entity.Order;
import com.unicom.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/order")
public class OrderController {

  @Autowired private OrderService orderService;

  /** 创建订单 */
  @GetMapping("/create")
  public ResultUtils create(Order order) {
    orderService.create(order);
    return ResultUtils.successMsg("订单创建成功!", 200);
  }
}
