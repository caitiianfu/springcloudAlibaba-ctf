package com.unicom.rabbitmq.dto;

import lombok.Getter;

/** @author by ctf */
@Getter
public enum QueueEnum {
  QUEUE_ORDER_CANCEL("queue.order.direct", "queue.order.cancel", "queue.order.cancel"),
  QUEUE_TTL_ORDER_CANCEL(
      "queue.order.ttl.direct", "queue.order.ttl.cancel", "queue.order.ttl.cancel");
  /** 交换机 */
  private String exchange;
  /** 队列 */
  private String queueName;
  /** 路由键 */
  private String routeKey;

  private QueueEnum(String queueName, String exchange, String routeKey) {
    this.queueName = queueName;
    this.exchange = exchange;
    this.routeKey = routeKey;
  }
}
