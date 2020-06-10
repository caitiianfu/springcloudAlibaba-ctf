package com.unicom.rabbitmq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.unicom.generator.entity.MsgLog;
import java.util.List;

/**
 * 消息投递日志 服务类
 *
 * @author ctf
 * @since 2020-05-27
 */
public interface IMsgLogService extends IService<MsgLog> {
  public List<MsgLog> testSelect(MsgLog msgLog);
}
