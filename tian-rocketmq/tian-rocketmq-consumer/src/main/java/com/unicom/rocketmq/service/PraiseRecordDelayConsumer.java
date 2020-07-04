package com.unicom.rocketmq.service;

import com.alibaba.fastjson.JSONObject;
import com.unicom.rocketmq.constant.RocketConstant.ConsumerGroup;
import com.unicom.rocketmq.constant.RocketConstant.Topic;
import com.unicom.rocketmq.vo.PraiseRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
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
@RocketMQMessageListener(topic = Topic.TEST_DELAY_TOPIC, consumerGroup = ConsumerGroup
.TEST_DELAY_CONSUMER)
public class PraiseRecordDelayConsumer implements RocketMQListener<PraiseRecordVO>,
    RocketMQPushConsumerLifecycleListener {

  @Override
  public void onMessage(PraiseRecordVO praiseRecordVO) {

    log.info("receive delay message : {}", JSONObject.toJSONString(praiseRecordVO));
  }

  @Override
  public void prepareStart(DefaultMQPushConsumer defaultMQPushConsumer) {
    //每次拉取间隔，单位为毫秒
    defaultMQPushConsumer.setPullInterval(2000);
    //每次从队列拉取的消息数
    defaultMQPushConsumer.setPullBatchSize(16);
  }
}
