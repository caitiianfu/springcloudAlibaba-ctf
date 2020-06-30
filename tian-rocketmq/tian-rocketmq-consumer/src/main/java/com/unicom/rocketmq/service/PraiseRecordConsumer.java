package com.unicom.rocketmq.service;

import com.alibaba.fastjson.JSONObject;
import com.unicom.rocketmq.constant.RocketConstant.Topic;
import com.unicom.rocketmq.vo.PraiseRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * @Description TODO
 *
 * @date 2020/6/29
 * @author ctf
 */
@Slf4j
@Service
@RocketMQMessageListener(topic = Topic.PRAISE_TOPIC, consumerGroup = "praise-group")
public class PraiseRecordConsumer implements RocketMQListener<PraiseRecordVO> {

  @Override
  public void onMessage(PraiseRecordVO praiseRecordVO) {
    log.info("receive message : {}", JSONObject.toJSONString(praiseRecordVO));
  }
}
