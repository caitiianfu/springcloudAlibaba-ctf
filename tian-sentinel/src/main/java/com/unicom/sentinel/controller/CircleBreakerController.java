package com.unicom.sentinel.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.unicom.common.api.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author by ctf
 * @description resttemplate熔断调用
 */
@RestController
@RequestMapping("/breaker")
@Slf4j
public class CircleBreakerController {
  @Autowired private RestTemplate restTemplate;

  @Value("${service-url.user-service}")
  private String serviceUrl;

  @RequestMapping("/fallback")
  @SentinelResource(value = "fallback", fallback = "handleFallback")
  public ResultUtils fallback() {
    ResultUtils r = restTemplate.getForObject(serviceUrl + "/test/redisList2", ResultUtils.class);
    return ResultUtils.success("222");
  }

  @RequestMapping("/tt")
  @SentinelResource(value = "tt", fallback = "handleFallback")
  public String tt() {
    return restTemplate.getForObject(serviceUrl + "/test/tt", String.class);
  }

  public ResultUtils handleFallback() {
    return ResultUtils.failedMsg("熔断降级");
  }
}
