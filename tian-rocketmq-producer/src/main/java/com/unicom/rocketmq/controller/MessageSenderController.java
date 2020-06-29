package com.unicom.rocketmq.controller;
/** */
import com.unicom.common.api.ResultUtils;
import com.unicom.rocketmq.constant.RocketConstant;
import com.unicom.rocketmq.vo.PraiseRecordVO;
import javax.annotation.Resource;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description TODO
 *
 * @date 2020/6/29
 * @author ctf
 */
@RestController
@RequestMapping("/send")
public class MessageSenderController {
  @Resource private RocketMQTemplate rocketMQTemplate;

  @Value("${rocketmq.topic.user}")
  private String userTopic;

  @Value("${rocketmq.topic.order}")
  private String orderTopic;

  @PostMapping("/praise")
  public ResultUtils praise(@RequestBody PraiseRecordVO vo) {
    rocketMQTemplate.sendOneWay(
        RocketConstant.Topic.PRAISE_TOPIC, MessageBuilder.withPayload(vo).build());
    return ResultUtils.success("success");
  }

  @GetMapping("/user")
  public String sendUser(@RequestParam String name) {
    //  rocketMQTemplate.send(
    //      userTopic, MessageBuilder.withPayload(new UserInfo().setId(1).setName(name)).build());
    return "send user success";
  }

  @GetMapping("/order")
  public String sendOrder(@RequestParam String msg) {
    // rocketMQTemplate.send(orderTopic, MessageBuilder.withPayload(msg).build());
    return "send order success";
  }
}
