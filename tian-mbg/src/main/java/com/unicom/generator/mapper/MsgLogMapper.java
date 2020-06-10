package com.unicom.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.unicom.generator.entity.MsgLog;
import java.util.List;

/**
 * 消息投递日志 Mapper 接口
 *
 * @author ctf
 * @since 2020-05-27
 */
public interface MsgLogMapper extends BaseMapper<MsgLog> {
  public List<MsgLog> testSelect(MsgLog msgLog);
}
