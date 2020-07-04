package com.unicom.rocketmq.controller;
/** */
import com.unicom.common.api.ResultUtils;
import com.unicom.rocketmq.constant.RocketConstant;
import com.unicom.rocketmq.constant.RocketConstant.Topic;
import com.unicom.rocketmq.pojo.PraiseRecord;
import com.unicom.rocketmq.vo.PraiseRecordVO;
import javax.annotation.Resource;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
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
  /*

    @Value("${rocketmq.topic.user}")
    private String userTopic;

    @Value("${rocketmq.topic.order}")
    private String orderTopic;
  */
  /**
   *
   * @Description 普通发送消息
   * @param vo
   * @return com.unicom.common.api.ResultUtils
   * @date 2020/7/4
   * @author ctf
   **/

  @PostMapping("/testNormal")
  public String testNormal(@RequestBody PraiseRecordVO vo) {
    rocketMQTemplate.sendOneWay(
        Topic.TEST_NORMALL_TOPIC, MessageBuilder.withPayload(vo).build());
    return "send testNormal success";
  }
  /**
   *
   * @Description 延迟发送消息
   * @param name
   * @return java.lang.String
   * @date 2020/7/4
   * @author ctf
   **/

  @PostMapping("/testDelay")
  public String testDelay(@RequestBody PraiseRecordVO praiseRecord) {
      rocketMQTemplate.send(
         Topic.TEST_DELAY_TOPIC, MessageBuilder.withPayload(praiseRecord).build());
    return "send testDelay success";
  }

  @PostMapping("/testBatch")
  public String testBatch(@RequestBody PraiseRecordVO praiseRecord) {
     rocketMQTemplate.send(Topic.TEST_BATCH_TOPIC, MessageBuilder.withPayload(praiseRecord).build());
    return "send testBatch success";
  }
}
