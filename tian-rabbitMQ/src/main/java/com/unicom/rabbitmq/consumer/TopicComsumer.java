package com.unicom.rabbitmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/** @author by ctf */
@Component
@Slf4j
public class TopicComsumer {
  @RabbitListener(queues = "topic.mess")
  // @RabbitHandler
  public void topic1(Long id) {
    log.info("topic.mess:  {}", id);
  }

  @RabbitListener(queues = "topic.message")
  // @RabbitHandler
  public void topic1t(Long id) {
    log.info("topic.message:  {}", id);
  }

  /* @RabbitListener(queues = "topic.test")
  @RabbitHandler
  public void test(long id) {
    log.info("topic.test:  {}", id);
  }*/
}
