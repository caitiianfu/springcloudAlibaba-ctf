package com.unicom.rabbitmq.config;

import com.unicom.rabbitmq.service.IMsgLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author by ctf */
@Component
@Slf4j
public class MailMsgSender {
  @Autowired private IMsgLogService msgLogService;
  @Autowired private RabbitTemplate rabbitTemplate;

  public void mailSender() {
    /*rabbitTemplate.setConfirmCallback(
        (corr, ack, cause) -> {
          if (ack) {
            log.info("消息发送到exchange成功");

            String msgId = corr.getId();
            QueryWrapper<MsgLog> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("msg_id", msgId);
            MsgLog msgLog = msgLogService.getOne(queryWrapper);
            // 发送到exchange成功
            msgLog.setStatus(RabbitConfig.MSG_DELIVERING_SUCCESS);
            msgLogService.update(msgLog, queryWrapper);
          } else {
            log.error("发送到exchange失败,{},cause:{}", corr, cause);
          }
        });

    // 触发setReturnCallback回调必须设置mandatory=true, 否则Exchange没有找到Queue就会丢弃掉消息, 而不会触发回调
    rabbitTemplate.setMandatory(true);
    // 消息是否从Exchange路由到Queue, 注意: 这是一个失败回调, 只有消息从Exchange路由到Queue失败才会回调这个方法
    rabbitTemplate.setReturnCallback(
        ((message, replyCode, replyText, exchange, routingKey) -> {
          log.error(
              "消息从exchange路由到queue失败，exchange {}，route {},replyCode {},replyText {},message {}",
              exchange,
              routingKey,
              replyCode,
              replyText,
              message);
        }));*/
  }
}
