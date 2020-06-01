package com.unicom.rabbitmq.controller;

import com.alibaba.fastjson.JSONObject;
import com.unicom.common.api.ResultUtils;
import com.unicom.common.exception.Assert;
import com.unicom.generator.entity.MsgLog;
import com.unicom.rabbitmq.bo.Mail;
import com.unicom.rabbitmq.config.RabbitConfig;
import com.unicom.rabbitmq.service.IMsgLogService;
import com.unicom.rabbitmq.util.MessageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.net.www.MessageHeader;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * @author by ctf
 * @Classsname MsgLogController
 * @Description TODO
 * @Date 2020/5/27 22:20
 **/
@RestController
@Slf4j
@Api(tags = "MsgLogController",value = "消息队列投递测试")
@RequestMapping("/test")
public class MsgLogController {

        @Autowired
        private IMsgLogService iMsgLogService;

        @Autowired
        private RabbitTemplate rabbitTemplate;
     //   @RequestMapping(value = "/send")
      //  @ApiOperation(value = "发送邮箱测试方法",notes = "简单测试")
       // @ApiImplicitParam(value = "传值为mail",name = "mail",dataType ="mail")

        @ApiOperation(value = "发送信息测试")
        @RequestMapping("/send")
        public ResultUtils send(@Validated Mail mail){
            String msgId= UUID.randomUUID().toString();
            mail.setMsgId(msgId);
            MsgLog msgLog=new MsgLog();
            msgLog.setMsg(JSONObject.toJSONString(mail));
            msgLog.setMsgId(msgId);
            msgLog.setExchange(RabbitConfig.MAIL_EXCHANGE);
            msgLog.setRoutingKey(RabbitConfig.MAIL_ROUTE_KEY);
            msgLog.setStatus(RabbitConfig.MSG_DELIVERING);//消息投递
            msgLog.setTryCount(0);
            msgLog.setNextTryTime(LocalDateTime.now().plusMinutes(1));
            iMsgLogService.save(msgLog);
            CorrelationData correlationData=new CorrelationData(msgId);
            rabbitTemplate.convertAndSend(RabbitConfig.MAIL_EXCHANGE,RabbitConfig.MAIL_ROUTE_KEY, MessageHelper.objToMsg(mail),correlationData);
            return ResultUtils.success("发送成功");
        }
        @ApiOperation(value = "测试")
        @RequestMapping("/ttt")
    public ResultUtils add(){
            MsgLog msgLog=new MsgLog();
            msgLog.setMsg("asdasd");
            iMsgLogService.save(msgLog);
            return ResultUtils.success("111");
        }
}
