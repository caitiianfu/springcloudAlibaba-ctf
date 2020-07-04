package com.unicom.rocketmq.config;/**
 *
 **/

import com.alibaba.fastjson.JSONObject;
import com.unicom.rocketmq.constant.RocketConstant;
import com.unicom.rocketmq.constant.RocketConstant.ConsumerGroup;
import com.unicom.rocketmq.constant.RocketConstant.Topic;
import com.unicom.rocketmq.pojo.PraiseRecord;
import com.unicom.rocketmq.vo.PraiseRecordVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description 批量拉取设置
 * @date 2020/7/4
 * @author ctf
 **/
@Slf4j
@Configuration
public class RocketMqBatchConfig {
  @Value("${rocketmq.name-server}")
  private String nameServer;
  @Bean
  public DefaultMQPushConsumer userMQPushConsumer() throws MQClientException {
    DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(ConsumerGroup.TEST_BATCH_CONSUMER);
    consumer.setNamesrvAddr(nameServer);
    consumer.subscribe(Topic.TEST_BATCH_TOPIC, "*");
    // 设置每次消息拉取的时间间隔，单位毫秒
    consumer.setPullInterval(1000);
    // 设置每个队列每次拉取的最大消息数
    consumer.setPullBatchSize(24);
    // 设置消费者单次批量消费的消息数目上限
    consumer.setConsumeMessageBatchMaxSize(12);
    consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context)
        -> {
      List<PraiseRecordVO> praiseRecordVOS = new ArrayList<>(msgs.size());
      Map<Integer, Integer> queueMsgMap = new HashMap<>(8);
      msgs.forEach(msg -> {
        praiseRecordVOS.add(JSONObject.parseObject(msg.getBody(), PraiseRecordVO.class));
        queueMsgMap.compute(msg.getQueueId(), (key, val) -> val == null ? 1 : ++val);
      });
      log.info("praise size: {}, content: {}", praiseRecordVOS.size(), praiseRecordVOS);
        /*
          处理批量消息，如批量插入：userInfoMapper.insertBatch(userInfos);
         */
      return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    });
    consumer.start();
    return consumer;
  }
}
