package com.unicom.rabbitmq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.unicom.generator.entity.MsgLog;
import com.unicom.generator.mapper.MsgLogMapper;
import com.unicom.rabbitmq.service.IMsgLogService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 消息投递日志 服务实现类
 *
 * @author ctf
 * @since 2020-05-27
 */
@Service
public class MsgLogServiceImpl extends ServiceImpl<MsgLogMapper, MsgLog> implements IMsgLogService {
  @Autowired private MsgLogMapper msgLogMapper;

  @Override
  public List<MsgLog> testSelect(MsgLog msgLog) {
    return msgLogMapper.testSelect(msgLog);
  }
}
