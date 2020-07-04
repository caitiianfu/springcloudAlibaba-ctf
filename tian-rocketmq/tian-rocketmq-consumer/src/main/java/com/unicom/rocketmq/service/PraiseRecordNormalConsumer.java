package com.unicom.rocketmq.service;

import com.alibaba.fastjson.JSONObject;
import com.unicom.rocketmq.constant.RocketConstant;
import com.unicom.rocketmq.constant.RocketConstant.ConsumerGroup;
import com.unicom.rocketmq.constant.RocketConstant.Topic;
import com.unicom.rocketmq.vo.PraiseRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * @Description 测试接收信息
 *RocketMQ的消息发送方式主要含
 * syncSend()同步发送、asyncSend()异步发送、
 * sendOneWay()三种方式，
 * sendOneWay()也是异步发送，
 * 区别在于不需等待Broker返回确认，
 * 所以可能会存在信息丢失的状况，
 * 但吞吐量更高，具体需根据业务情况选用。
 * @date 2020/6/29
 * @author ctf
 */
@Slf4j
@Service
@RocketMQMessageListener(topic = Topic.TEST_NORMALL_TOPIC, consumerGroup = ConsumerGroup
.TEST_NORMAL_CONSUMER)
public class PraiseRecordNormalConsumer implements RocketMQListener<PraiseRecordVO> {

  @Override
  public void onMessage(PraiseRecordVO praiseRecordVO) {
    log.info("receive normal message : {}", JSONObject.toJSONString(praiseRecordVO));
  }
}
