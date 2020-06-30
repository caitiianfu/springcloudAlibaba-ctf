/**
 * Title: OrderServiceImpl.java Copyright: Copyright (c) 2018 Company: Unicom
 *
 * @author ctf
 * @date 2019年12月2日
 * @version 1.0
 */
package com.unicom.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.unicom.generator.entity.Order;
import com.unicom.generator.mapper.OrderMapper;
import com.unicom.order.service.AccountService;
import com.unicom.order.service.OrderService;
import com.unicom.order.service.StorageService;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** 订单业务实现类 Created by macro on 2019/11/11. */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

  private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

  @Autowired private OrderMapper orderMapper;
  @Autowired private StorageService storageService;
  @Autowired private AccountService accountService;

  /** 创建订单->调用库存服务扣减库存->调用账户服务扣减账户余额->修改订单状态 */
  @Override
  @GlobalTransactional(rollbackFor = Exception.class)
  public void create(Order order) {
    LOGGER.info("------->下单开始");
    // 本应用创建订单
    orderMapper.create(order);

    // 远程调用库存服务扣减库存
    LOGGER.info("------->order-service中扣减库存开始");
    storageService.decrease(order.getProductId(), order.getCount());
    LOGGER.info("------->order-service中扣减库存结束");

    // 远程调用账户服务扣减余额
    LOGGER.info("------->order-service中扣减余额开始");
    accountService.decrease(order.getUserId(), order.getMoney());
    LOGGER.info("------->order-service中扣减余额结束");

    // 修改订单状态为已完成
    LOGGER.info("------->order-service中修改订单状态开始");
    orderMapper.update(order.getUserId(), 0);
    LOGGER.info("------->order-service中修改订单状态结束");

    LOGGER.info("------->下单结束");
  }
}
