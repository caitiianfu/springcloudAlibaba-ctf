package com.unicom.sentinel.service;

import com.unicom.common.api.ResultUtils;
import org.springframework.stereotype.Component;

/** @author by ctf */
@Component
public class FeignFallbackService implements FeignService {

  @Override
  public ResultUtils redisList2() {
    return ResultUtils.failedMsg("远程调用失败");
  }

  @Override
  public String tt() {
    return "tt错误";
  }

  @Override
  public String testSetObj() {
    return "redistemplate错误";
  }
}
