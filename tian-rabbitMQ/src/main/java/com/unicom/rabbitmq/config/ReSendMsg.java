package com.unicom.rabbitmq.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.unicom.generator.entity.MsgLog;
import com.unicom.rabbitmq.service.IMsgLogService;
import com.unicom.rabbitmq.util.MessageHelper;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/** @author by ctf @Classsname ReSendMsg @Description TODO @Date 2020/5/28 11:29 */
// @Component
@Slf4j
public class ReSendMsg {
  @Autowired private static final int MAX_RETRY_TIMES = 3;
  @Autowired private IMsgLogService msgLogService;
  @Autowired private RabbitTemplate rabbitTemplate;
  // @Scheduled(cron = "0/30 * * * * ?")
  // @Scheduled(cron = "? 0/30 * * * ?")
  public void reSend() {
    QueryWrapper<MsgLog> queryWrapper = new QueryWrapper<>();
    queryWrapper.lt("next_try_time", LocalDateTime.now());
    queryWrapper.eq("status", RabbitConfig.MSG_DELIVERING);
    List<MsgLog> list = msgLogService.list(queryWrapper);
    list.forEach(
        (o) -> {
          String msgId = o.getMsgId();
          if (o.getTryCount() > MAX_RETRY_TIMES) {
            QueryWrapper<MsgLog> wrapper = new QueryWrapper<>();
            wrapper.eq("msg_id", msgId);
            MsgLog msgLog = msgLogService.getOne(wrapper);
            msgLog.setStatus(RabbitConfig.MSG_CONSUMER_FAILED);
            msgLogService.update(msgLog, wrapper);
            log.error("重试超过最大次数 msgId:{}", msgId);
          } else {
            QueryWrapper<MsgLog> wrapper = new QueryWrapper<>();
            wrapper.eq("msg_id", msgId);
            MsgLog msgLog = msgLogService.getOne(wrapper);
            msgLog.setNextTryTime(msgLog.getNextTryTime().plusMinutes(1));
            msgLog.setTryCount(msgLog.getTryCount() + 1);
            msgLogService.update(msgLog, wrapper);
            CorrelationData correlationData = new CorrelationData(msgId);
            rabbitTemplate.convertAndSend(
                RabbitConfig.MAIL_EXCHANGE,
                RabbitConfig.MAIL_ROUTE_KEY,
                MessageHelper.objToMsg(msgLog),
                correlationData); // 重新投递
            log.info("重试 第" + (msgLog.getTryCount() + 1) + "次投递消息");
          }
        });
    log.info("重新投递消息任务 结束");
  }
}
