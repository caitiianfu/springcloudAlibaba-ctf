/**
 * Title: OrderService.java Copyright: Copyright (c) 2018 Company: Unicom
 *
 * @author ctf
 * @date 2019年12月2日
 * @version 1.0
 */
package com.unicom.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.unicom.generator.entity.Order;

public interface OrderService extends IService<Order> {

  /** 创建订单 */
  void create(Order order);
}
