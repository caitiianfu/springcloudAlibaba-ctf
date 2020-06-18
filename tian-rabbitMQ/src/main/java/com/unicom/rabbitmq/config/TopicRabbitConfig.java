package com.unicom.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** @author by ctf */
@Configuration
public class TopicRabbitConfig {
  public static final String TOPIC1 = "topic.mess";
  public static final String TOPIC2 = "topic.message";

  @Bean
  public Queue topic1Queue() {
    return new Queue(TOPIC1);
  }

  @Bean
  public Queue topic2Queue() {
    return new Queue(TOPIC2);
  }

  @Bean
  public TopicExchange topicExchange() {
    return new TopicExchange("topicExchange");
  }

  @Bean
  public Binding topic1Binding(Queue topic1Queue, TopicExchange topicExchange) {
    return BindingBuilder.bind(topic1Queue).to(topicExchange).with(TOPIC1);
  }

  @Bean
  public Binding topic2Binding(Queue topic2Queue, TopicExchange topicExchange) {

    return BindingBuilder.bind(topic2Queue).to(topicExchange).with("topic.#");
  }
}
