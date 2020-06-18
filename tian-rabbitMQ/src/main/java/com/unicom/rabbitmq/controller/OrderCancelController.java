package com.unicom.rabbitmq.controller;

import com.unicom.common.api.ResultUtils;
import com.unicom.rabbitmq.config.CancelOrderSender;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** @author by ctf */
@RestController
@Api(tags = "OrderCancelController", description = "订单延迟(死信)队列模拟")
@RequestMapping("/order")
public class OrderCancelController {
  @Autowired private CancelOrderSender cancelOrderSender;

  @GetMapping("/cancel")
  @ApiOperation(value = "取消订单延迟队列")
  public ResultUtils cancel(long orderId, long delayTime) {
    cancelOrderSender.cancel(orderId, delayTime);
    return ResultUtils.success("取消订单成功");
  }
}
