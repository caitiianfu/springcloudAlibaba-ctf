package com.unicom.sentinel.service;

import com.unicom.common.api.ResultUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/** @author by ctf */
@FeignClient(value = "tian-redis", fallback = FeignFallbackService.class)
public interface FeignService {
  @GetMapping("/test/redisList2")
  public ResultUtils redisList2();

  @GetMapping("/test/tt")
  public String tt();

  @GetMapping("/test-set-obj")
  public String testSetObj();
}
