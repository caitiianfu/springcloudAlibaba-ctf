package com.unicom.rabbitmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/** @author by ctf */
@Component
@Slf4j
@RabbitListener(queues = "queue.order.direct")
public class CancelOrderConsumer {
  @RabbitHandler
  public void consumerOderCancel(Long orderId) {

    log.info("收到延迟消息:  {}", orderId);
  }
}
