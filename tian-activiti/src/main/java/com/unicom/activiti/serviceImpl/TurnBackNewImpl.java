package com.unicom.activiti.serviceImpl;

import com.google.protobuf.ServiceException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 1、获取当前任务所在的节点 2、获取所在节点的流出方向 3、记录所在节点的流出方向，并将所在节点的流出方向清空 4、获取目标节点 5、创建新的方向 6、将新的方向set到所在节点的流出方向
 * 7、完成当前任务 8、还原所在节点的流出方向
 *
 * @author by ctf
 */
@Slf4j
@Component
public class TurnBackNewImpl {

  @Autowired private HistoryService historyService;
  @Autowired private RuntimeService runtimeService;
  @Autowired private RepositoryService repositoryService;
  @Autowired private TaskService taskService;

  public boolean revoke(String formId, String applicant) throws Exception {

    Task task = taskService.createTaskQuery().processInstanceBusinessKey(formId).singleResult();
    if (task == null) {
      throw new ServiceException("流程未启动或已执行完成，无法撤回");
    }

    List<HistoricTaskInstance> htiList =
        historyService
            .createHistoricTaskInstanceQuery()
            .processInstanceBusinessKey(formId)
            .orderByTaskCreateTime()
            .asc()
            .list();
    String myTaskId = null;
    HistoricTaskInstance myTask = null;
    for (HistoricTaskInstance hti : htiList) {
      if (applicant.equals(hti.getAssignee())) {
        myTaskId = hti.getId();
        myTask = hti;
        break;
      }
    }
    if (null == myTaskId) {
      throw new ServiceException("该任务非当前用户提交，无法撤回");
    }

    String processDefinitionId = myTask.getProcessDefinitionId();
    ProcessDefinitionEntity processDefinitionEntity =
        (ProcessDefinitionEntity)
            repositoryService
                .createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();
    BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);

    // 变量
    //		Map<String, VariableInstance> variables =
    // runtimeService.getVariableInstances(currentTask.getExecutionId());
    String myActivityId = null;
    List<HistoricActivityInstance> haiList =
        historyService
            .createHistoricActivityInstanceQuery()
            .executionId(myTask.getExecutionId())
            .finished()
            .list();
    for (HistoricActivityInstance hai : haiList) {
      if (myTaskId.equals(hai.getTaskId())) {
        myActivityId = hai.getActivityId();
        break;
      }
    }
    FlowNode myFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(myActivityId);

    Execution execution =
        runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
    String activityId = execution.getActivityId();
    log.warn("------->> activityId:" + activityId);
    FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(activityId);

    // 记录原活动方向
    List<SequenceFlow> oriSequenceFlows = new ArrayList<SequenceFlow>();
    oriSequenceFlows.addAll(flowNode.getOutgoingFlows());

    // 清理活动方向
    flowNode.getOutgoingFlows().clear();
    // 建立新方向
    List<SequenceFlow> newSequenceFlowList = new ArrayList<SequenceFlow>();
    SequenceFlow newSequenceFlow = new SequenceFlow();
    newSequenceFlow.setId("newSequenceFlowId");
    newSequenceFlow.setSourceFlowElement(flowNode);
    newSequenceFlow.setTargetFlowElement(myFlowNode);
    newSequenceFlowList.add(newSequenceFlow);
    flowNode.setOutgoingFlows(newSequenceFlowList);

    Authentication.setAuthenticatedUserId(applicant);
    taskService.addComment(task.getId(), task.getProcessInstanceId(), "撤回");

    Map<String, Object> currentVariables = new HashMap<String, Object>();
    currentVariables.put("applier", applicant);
    // 完成任务
    taskService.complete(task.getId(), currentVariables);
    // 恢复原方向
    flowNode.setOutgoingFlows(oriSequenceFlows);
    return true;
  }

  /**
   * @param task 任务Id
   * @param variables ...
   * @param targetActivityId 节点ID
   * @throws Exception ...
   * @creator ctf
   * @date 2018/2/25 @描述 提交流程至某节点
   */
  private void commitProcess(Task task, Map<String, Object> variables, String targetActivityId) {
    // TODO: 2018/2/25 未测试
    // 获取当前节点Id
    String currentActivityId = task.getTaskDefinitionKey();
    // 获取模型实体
    String processDefinitionId = task.getProcessDefinitionId();
    BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
    // 获取当前节点
    FlowElement currentFlow = bpmnModel.getFlowElement(currentActivityId);
    // 获取目标节点
    FlowElement targetFlow = bpmnModel.getFlowElement(targetActivityId);
    // 创建连线
    String uuid = UUID.randomUUID().toString().replace("-", "");
    SequenceFlow newSequenceFlow = new SequenceFlow();
    newSequenceFlow.setId(uuid);
    newSequenceFlow.setSourceFlowElement(currentFlow);
    newSequenceFlow.setTargetFlowElement(targetFlow);
    // 设置条件
    newSequenceFlow.setConditionExpression("${\"+uuid+\"==\"" + uuid + "\"}");
    // 添加连线至bpmn
    bpmnModel.getMainProcess().addFlowElement(newSequenceFlow);
    // 添加变量（保证这根线独一无二）
    variables.clear(); // 清空变量，防止干扰
    variables.put(uuid, uuid);
    // 提交
    taskService.addComment(task.getId(), task.getProcessInstanceId(), "撤回");
    // 完成任务
    taskService.complete(task.getId(), variables);
    // 删除连线
    bpmnModel.getMainProcess().removeFlowElement(uuid);
  }
}
