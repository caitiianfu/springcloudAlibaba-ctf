package com.unicom.rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 邮箱投递 消息队列配置 采用的是 任务调度 进行消息重新投递
 *
 * @author by ctf @Classsname RabbitConfig @Description TODO @Date 2020/5/27 22:40
 */
@Configuration
@Slf4j
public class RabbitConfig {

  /** 消息投递* */
  public static final int MSG_DELIVERING = 0;
  /** 投递成功* */
  public static final int MSG_DELIVERING_SUCCESS = 1;
  /** 消费成功* */
  public static final int MSG_CONSUMER_SUCCESS = 2;
  /** 消费失败* */
  public static final int MSG_CONSUMER_FAILED = 3;

  public static final String MAIL_QUEUE_NAME = "mail.queue";
  public static final String MAIL_ROUTE_KEY = "mail.route";
  public static final String MAIL_EXCHANGE = "mail.exchange";
  @Autowired private CachingConnectionFactory connectionFactory;

  @Bean
  public Jackson2JsonMessageConverter converter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  public RabbitTemplate rabbitTemplate() {
    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(converter());
    return rabbitTemplate;
  }

  @Bean
  public Queue mailQueue() {
    return new Queue(MAIL_QUEUE_NAME, true);
  }

  @Bean
  public DirectExchange mailExchange() {
    return new DirectExchange(MAIL_EXCHANGE, true, false);
  }

  @Bean
  public Binding mailBuilding() {
    return BindingBuilder.bind(mailQueue()).to(mailExchange()).with(MAIL_ROUTE_KEY);
  }
}
