package com.unicom.sentinel.handle;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.unicom.common.api.ResultUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author by ctf
 * @description 自定义限流
 */
@Slf4j
public class CustomBlockHandler {
  public ResultUtils handleException(BlockException blockException) {
    log.error("error: {}", blockException);
    return ResultUtils.success("自定义限流");
  }
}
