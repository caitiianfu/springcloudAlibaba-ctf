package com.unicom.rabbitmq.config;

import com.unicom.rabbitmq.dto.QueueEnum;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** @author by ctf */
@Configuration
public class DeathRabbitConfig {

  /**
   * 订单队列
   *
   * @return
   */
  @Bean
  public Queue orderQueue() {
    return new Queue(QueueEnum.QUEUE_ORDER_CANCEL.getQueueName());
  }

  /**
   * 订单延迟（死信） 队列
   *
   * @return
   */
  @Bean
  public Queue orderTtlQueue() {

    return QueueBuilder.durable(QueueEnum.QUEUE_TTL_ORDER_CANCEL.getQueueName())
        .withArgument(
            "x-dead-letter-exchange", QueueEnum.QUEUE_ORDER_CANCEL.getExchange()) // 订单超时时转到该交换机
        .withArgument(
            "x-dead-letter-routing-key", QueueEnum.QUEUE_ORDER_CANCEL.getRouteKey()) // 订单超时转到该路由键
        .build();
  }

  /**
   * 订单交换机
   *
   * @return
   */
  @Bean
  public DirectExchange orderExchange() {
    return (DirectExchange)
        ExchangeBuilder.directExchange(QueueEnum.QUEUE_ORDER_CANCEL.getExchange())
            .durable(true)
            .build();
  }

  /**
   * 订单延迟（死信）交换机
   *
   * @return
   */
  @Bean
  public DirectExchange orderTtlExchange() {
    return (DirectExchange)
        ExchangeBuilder.directExchange(QueueEnum.QUEUE_TTL_ORDER_CANCEL.getExchange())
            .durable(true)
            .build();
  }

  /**
   * 订单 队列绑定交换机
   *
   * @return
   */
  @Bean
  public Binding orderBinding() {
    return BindingBuilder.bind(orderQueue())
        .to(orderExchange())
        .with(QueueEnum.QUEUE_ORDER_CANCEL.getRouteKey());
  }

  /**
   * 将订单延迟队列绑定到交换机
   *
   * @return
   */
  @Bean
  public Binding orderTtlBinding() {
    return BindingBuilder.bind(orderTtlQueue())
        .to(orderTtlExchange())
        .with(QueueEnum.QUEUE_TTL_ORDER_CANCEL.getRouteKey());
  }
}
