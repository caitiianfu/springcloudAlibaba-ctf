package com.unicom.activiti.controller;

import com.unicom.activiti.serviceImpl.TurnBackNewImpl;
import com.unicom.common.api.ResultUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** @author by ctf */
@Api(tags = "TurnBackController", description = "流程退回")
@RestController
@RequestMapping("/trunBack")
public class TurnBackController {
  @Autowired private TurnBackNewImpl turnBackNew;

  @RequestMapping("/test")
  public ResultUtils test(String formId, String applicant) throws Exception {
    turnBackNew.revoke(formId, applicant);
    return ResultUtils.success("ok");
  }

  @RequestMapping("/test2")
  public ResultUtils test2(String formId, String applicant) throws Exception {
    turnBackNew.revoke(formId, applicant);
    return ResultUtils.success("ok");
  }
}
