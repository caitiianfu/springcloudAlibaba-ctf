package com.unicom.rabbitmq.config;

import com.unicom.rabbitmq.dto.QueueEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author by ctf */
@Component
@Slf4j
public class CancelOrderSender {
  @Autowired private AmqpTemplate amqpTemplate;

  public void cancel(long orderId, long delayTime) {
    amqpTemplate.convertAndSend(
        QueueEnum.QUEUE_TTL_ORDER_CANCEL.getExchange(),
        QueueEnum.QUEUE_TTL_ORDER_CANCEL.getRouteKey(),
        orderId,
        // 设置延迟时间
        message -> {
          message.getMessageProperties().setExpiration(String.valueOf(delayTime * 1000));
          return message;
        });
    log.info("orderId: {},delayTime: {} ", orderId, delayTime);
  }
}
