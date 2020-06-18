package com.unicom.sentinel.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.unicom.common.api.ResultUtils;
import com.unicom.sentinel.handle.CustomBlockHandler;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description 限流,在sentinel指定规则，真实场景一般不这么用
 * @author by ctf
 */
@RestController
@RequestMapping("/rateLimit")
@Api(tags = "RateLimitController", description = "限流设置")
public class RateLimitController {
  /** 按资源名称限流，需要指定限流处理逻辑 value:对应sentinel设置的资源 blockHandler：失败执行的方法 */
  @GetMapping("/byResource")
  @SentinelResource(value = "byResource", blockHandler = "handleException")
  public ResultUtils byResource() {
    return ResultUtils.success("按资源名称限流");
  }

  /** 按URL限流，有默认的限流处理逻辑 Blocked by Sentinel (flow limiting) */
  @GetMapping("/byUrl")
  @SentinelResource(value = "byUrl", blockHandler = "handleException")
  public ResultUtils byUrl() {
    return ResultUtils.success("按url限流");
  }

  public ResultUtils handleException(BlockException exception) {
    return ResultUtils.failedMsg(exception.getClass().getCanonicalName());
  }

  /** 自定义通用的限流处理逻辑 */
  @GetMapping("/customBlockHandler")
  @SentinelResource(
      value = "customBlockHandler",
      blockHandler = "handleException",
      blockHandlerClass = CustomBlockHandler.class)
  public ResultUtils blockHandler() {
    return ResultUtils.success("自定义限流成功");
  }
}
