package com.unicom.activiti.service;

import com.unicom.activiti.entity.VacTask;
import com.unicom.activiti.entity.Vacation;
import com.unicom.activiti.util.ActivitiUtil;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/** @author Created by yawn on 2018-01-08 13:44 */
@Service
public class VacationService {

  @Resource private RuntimeService runtimeService;
  @Resource private IdentityService identityService;
  @Resource private TaskService taskService;
  @Resource private HistoryService historyService;

  private static final String PROCESS_DEFINE_KEY = "vacationProcess";

  public Object startVac(String userName, Vacation vac) {

    identityService.setAuthenticatedUserId(userName);
    // 开始流程
    ProcessInstance vacationInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINE_KEY);
    // 查询当前任务
    Task currentTask =
        taskService.createTaskQuery().processInstanceId(vacationInstance.getId()).singleResult();
    // 申明任务
    taskService.claim(currentTask.getId(), userName);

    Map<String, Object> vars = new HashMap<>(4);
    vars.put("applyUser", userName);
    vars.put("days", vac.getDays());
    vars.put("reason", vac.getReason());

    taskService.complete(currentTask.getId(), vars);

    return true;
  }

  public Object myVac(String userName) {
    List<ProcessInstance> instanceList =
        runtimeService.createProcessInstanceQuery().startedBy(userName).list();
    List<Vacation> vacList = new ArrayList<>();
    for (ProcessInstance instance : instanceList) {
      Vacation vac = getVac(instance);
      vacList.add(vac);
    }
    return vacList;
  }

  private Vacation getVac(ProcessInstance instance) {
    Integer days = runtimeService.getVariable(instance.getId(), "days", Integer.class);
    String reason = runtimeService.getVariable(instance.getId(), "reason", String.class);
    Vacation vac = new Vacation();
    vac.setApplyUser(instance.getStartUserId());
    vac.setDays(days);
    vac.setReason(reason);
    Date startTime = instance.getStartTime(); // activiti 6 才有
    vac.setApplyTime(startTime);
    vac.setApplyStatus(instance.isEnded() ? "申请结束" : "等待审批");
    return vac;
  }

  public Object myAudit(String userName) {
    List<Task> taskList =
        taskService
            .createTaskQuery()
            .taskCandidateUser(userName)
            .orderByTaskCreateTime()
            .desc()
            .list();
    //        / 多此一举 taskList中包含了以下内容(用户的任务中包含了所在用户组的任务)
    //        Group group = identityService.createGroupQuery().groupMember(userName).singleResult();
    //        List<Task> list =
    // taskService.createTaskQuery().taskCandidateGroup(group.getId()).list();
    //        taskList.addAll(list);
    List<VacTask> vacTaskList = new ArrayList<>();
    for (Task task : taskList) {
      VacTask vacTask = new VacTask();
      vacTask.setId(task.getId());
      vacTask.setName(task.getName());
      vacTask.setCreateTime(task.getCreateTime());
      String instanceId = task.getProcessInstanceId();
      ProcessInstance instance =
          runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
      Vacation vac = getVac(instance);
      vacTask.setVac(vac);
      vacTaskList.add(vacTask);
    }
    return vacTaskList;
  }

  public Object passAudit(String userName, VacTask vacTask) {
    String taskId = vacTask.getId();
    String result = vacTask.getVac().getResult();
    Map<String, Object> vars = new HashMap<>();
    vars.put("result", result);
    vars.put("auditor", userName);
    vars.put("auditTime", new Date());
    taskService.claim(taskId, userName);
    taskService.complete(taskId, vars);
    return true;
  }

  public Object myVacRecord(String userName) {
    List<HistoricProcessInstance> hisProInstance =
        historyService
            .createHistoricProcessInstanceQuery()
            .processDefinitionKey(PROCESS_DEFINE_KEY)
            .startedBy(userName)
            .finished()
            .orderByProcessInstanceEndTime()
            .desc()
            .list();

    List<Vacation> vacList = new ArrayList<>();
    for (HistoricProcessInstance hisInstance : hisProInstance) {
      Vacation vacation = new Vacation();
      vacation.setApplyUser(hisInstance.getStartUserId());
      vacation.setApplyTime(hisInstance.getStartTime());
      vacation.setApplyStatus("申请结束");
      List<HistoricVariableInstance> varInstanceList =
          historyService
              .createHistoricVariableInstanceQuery()
              .processInstanceId(hisInstance.getId())
              .list();
      ActivitiUtil.setVars(vacation, varInstanceList);
      vacList.add(vacation);
    }
    return vacList;
  }

  public Object myAuditRecord(String userName) {
    List<HistoricProcessInstance> hisProInstance =
        historyService
            .createHistoricProcessInstanceQuery()
            .processDefinitionKey(PROCESS_DEFINE_KEY)
            .involvedUser(userName)
            .finished()
            .orderByProcessInstanceEndTime()
            .desc()
            .list();

    List<String> auditTaskNameList = new ArrayList<>();
    auditTaskNameList.add("经理审批");
    auditTaskNameList.add("总监审批");
    List<Vacation> vacList = new ArrayList<>();
    for (HistoricProcessInstance hisInstance : hisProInstance) {
      List<HistoricTaskInstance> hisTaskInstanceList =
          historyService
              .createHistoricTaskInstanceQuery()
              .processInstanceId(hisInstance.getId())
              .processFinished()
              .taskAssignee(userName)
              .taskNameIn(auditTaskNameList)
              .orderByHistoricTaskInstanceEndTime()
              .desc()
              .list();
      boolean isMyAudit = false;
      for (HistoricTaskInstance taskInstance : hisTaskInstanceList) {
        if (taskInstance.getAssignee().equals(userName)) {
          isMyAudit = true;
        }
      }
      if (!isMyAudit) {
        continue;
      }
      Vacation vacation = new Vacation();
      vacation.setApplyUser(hisInstance.getStartUserId());
      vacation.setApplyStatus("申请结束");
      vacation.setApplyTime(hisInstance.getStartTime());
      List<HistoricVariableInstance> varInstanceList =
          historyService
              .createHistoricVariableInstanceQuery()
              .processInstanceId(hisInstance.getId())
              .list();
      ActivitiUtil.setVars(vacation, varInstanceList);
      vacList.add(vacation);
    }
    return vacList;
  }
}
