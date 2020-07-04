package com.unicom.rocketmq.constant;
/** */

/**
 * @Description TODO
 *
 * @date 2020/6/29
 * @author ctf
 */
public interface RocketConstant {
  interface ConsumerGroup {
    String TEST_NORMAL_CONSUMER = "test-normal-consumer";
    String TEST_DELAY_CONSUMER = "test-delay-consumer";
    String TEST_BATCH_CONSUMER = "test-batch-consumer";
  }

  interface Topic {
    String TEST_NORMALL_TOPIC = "test-normal-topic";
    String TEST_DELAY_TOPIC = "test-delay-topic";
    String TEST_BATCH_TOPIC = "test-batch-topic";
  }
}
