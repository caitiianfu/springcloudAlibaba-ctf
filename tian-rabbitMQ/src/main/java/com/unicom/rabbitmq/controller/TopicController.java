package com.unicom.rabbitmq.controller;

import com.unicom.common.api.ResultUtils;
import com.unicom.rabbitmq.config.TopicRabbitConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** @author by ctf */
@RestController
@Api(tags = "TopicController", description = "topic模式测试")
@RequestMapping("/topic")
public class TopicController {
  @Autowired private RabbitTemplate rabbitTemplate;

  @GetMapping("/test1")
  @ApiOperation(value = "通配符匹配")
  public ResultUtils test1(Long id) {
    rabbitTemplate.convertAndSend("topicExchange", TopicRabbitConfig.TOPIC1, id);
    return ResultUtils.success(TopicRabbitConfig.TOPIC1);
  }

  @GetMapping("/test2")
  @ApiOperation(value = "匹配所有")
  public ResultUtils test2(Long id) {
    rabbitTemplate.convertAndSend("topicExchange", TopicRabbitConfig.TOPIC2, id);
    return ResultUtils.success(TopicRabbitConfig.TOPIC2);
  }
}
