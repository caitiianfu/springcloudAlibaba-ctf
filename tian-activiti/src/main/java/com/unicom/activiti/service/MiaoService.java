package com.unicom.activiti.service;

import com.unicom.generator.entity.User;
import com.unicom.generator.entity.VacationForm;
import java.util.HashMap;
import java.util.List;

public interface MiaoService {

  public VacationForm writeForm(String title, String content, String applicant);

  public boolean giveupVacation(String formId, String operator);

  public boolean applyVacation(String formId, String operator);

  public boolean approverVacation(String formId, String operator);

  public void completeProcess(String formId, String operator, String input);

  public HashMap<String, String> getCurrentState(String formId);

  public List<VacationForm> formList();

  public User loginSuccess(String user);

  public List historyState(String formId);
}
