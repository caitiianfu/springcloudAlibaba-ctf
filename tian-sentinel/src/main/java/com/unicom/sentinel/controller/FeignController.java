package com.unicom.sentinel.controller;

import com.unicom.common.api.ResultUtils;
import com.unicom.sentinel.service.FeignService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by ctf
 * @descript feign熔断监控
 */
@RestController
@RequestMapping("/feign")
@Api(tags = "FeignController", description = "feign测试")
public class FeignController {
  @Autowired private FeignService feignService;

  @RequestMapping("/test")
  public ResultUtils test() {
    return feignService.redisList2();
  }

  @RequestMapping("/tt")
  public String tt() {
    return feignService.tt();
  }

  @RequestMapping("/tso")
  public String testSetObj() {
    return feignService.testSetObj();
  }
}
