package com.unicom.rabbitmq.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.unicom.common.annotation.DataScopeAnnotation;
import com.unicom.common.api.ResultUtils;
import com.unicom.generator.entity.MsgLog;
import com.unicom.rabbitmq.bo.Mail;
import com.unicom.rabbitmq.config.MailMsgSender;
import com.unicom.rabbitmq.config.RabbitConfig;
import com.unicom.rabbitmq.service.IMsgLogService;
import com.unicom.rabbitmq.util.MessageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** @author by ctf @Classsname MsgLogController @Description TODO @Date 2020/5/27 22:20 */
@RestController
@Slf4j
@Api(tags = "MsgLogController", value = "消息队列投递测试")
@RequestMapping("/test")
public class MsgLogController {

  @Autowired private IMsgLogService iMsgLogService;
  @Autowired private MailMsgSender mailSender;
  @Autowired private RabbitTemplate rabbitTemplate;
  //   @RequestMapping(value = "/send")
  //  @ApiOperation(value = "发送邮箱测试方法",notes = "简单测试")
  // @ApiImplicitParam(value = "传值为mail",name = "mail",dataType ="mail")

  @ApiOperation(value = "发送信息测试")
  @RequestMapping("/send")
  public ResultUtils send(@RequestBody @Validated Mail mail) {
    String msgId = UUID.randomUUID().toString();
    mail.setMsgId(msgId);
    MsgLog msgLog = new MsgLog();
    msgLog.setMsg(JSONObject.toJSONString(mail));
    msgLog.setMsgId(msgId);
    msgLog.setExchange(RabbitConfig.MAIL_EXCHANGE);
    msgLog.setRoutingKey(RabbitConfig.MAIL_ROUTE_KEY);
    msgLog.setStatus(RabbitConfig.MSG_DELIVERING); // 消息投递
    msgLog.setTryCount(0);
    msgLog.setNextTryTime(LocalDateTime.now().plusMinutes(1));
    iMsgLogService.save(msgLog);
    CorrelationData correlationData = new CorrelationData(msgId);
    mailSender.mailSender();
    rabbitTemplate.convertAndSend(
        RabbitConfig.MAIL_EXCHANGE,
        RabbitConfig.MAIL_ROUTE_KEY,
        MessageHelper.objToMsg(mail),
        correlationData);
    return ResultUtils.success("发送成功");
  }

  @ApiOperation(value = "测试")
  @DataScopeAnnotation(Alias = "test")
  @RequestMapping("/ttt")
  public ResultUtils add() {
    MsgLog msgLog = new MsgLog();
    msgLog.setMsg("asdasd");
    QueryWrapper<MsgLog> queryWrapper = new QueryWrapper<>();

    List<MsgLog> msgLogs = iMsgLogService.testSelect(msgLog);

    iMsgLogService.save(msgLog);
    return ResultUtils.success("111");
  }

  @ApiOperation(value = "测试LocalDatatime")
  @RequestMapping("/localDateTime")
  public ResultUtils localDateTime(@RequestBody @Validated Mail mail) {
    String msgId = UUID.randomUUID().toString();
    mail.setMsgId(msgId);
    return ResultUtils.success(mail);
  }
}
