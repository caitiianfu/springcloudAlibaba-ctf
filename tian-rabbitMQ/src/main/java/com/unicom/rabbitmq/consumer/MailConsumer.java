package com.unicom.rabbitmq.consumer;

/**
 * @author by ctf
 * @Classsname MailConsumer
 * @Description TODO
 * @Date 2020/5/28 10:24
 **/

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbitmq.client.Channel;
import com.unicom.generator.entity.MsgLog;
import com.unicom.rabbitmq.bo.Mail;
import com.unicom.rabbitmq.config.RabbitConfig;
import com.unicom.rabbitmq.service.IMsgLogService;
import com.unicom.rabbitmq.util.MailUtil;
import com.unicom.rabbitmq.util.MessageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
@Slf4j
public class MailConsumer {
    @Autowired
    private IMsgLogService iMsgLogService;
    @Autowired
    private MailUtil mailUtil;
    @RabbitListener(queues = RabbitConfig.MAIL_QUEUE_NAME)
    public void consumer(Message message, Channel channel) throws IOException {
        Mail mail= MessageHelper.msgToObj(message,Mail.class);
        log.info("收到消息  {}",mail.toString());
        QueryWrapper<MsgLog> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("msg_id",mail.getMsgId());
        MsgLog msgLog=iMsgLogService.getOne(queryWrapper);
        if (msgLog==null||msgLog.getStatus()==RabbitConfig.MSG_CONSUMER_SUCCESS){
            log.info("重复消费 {} ",mail.getMsgId());
            return;
        }
        MessageProperties messageProperties=new MessageProperties();
        long tag=messageProperties.getDeliveryTag();
        boolean success=mailUtil.sendMail(mail);
        if (success) {
            msgLog.setStatus(RabbitConfig.MSG_CONSUMER_SUCCESS);
            iMsgLogService.update(msgLog,queryWrapper);
            channel.basicAck(tag,false);//消费确认
        }else{
            channel.basicNack(tag,false,true);
        }

    }
}
