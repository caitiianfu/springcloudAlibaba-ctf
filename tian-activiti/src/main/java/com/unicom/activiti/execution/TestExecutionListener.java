package com.unicom.activiti.execution;/**
 *
 **/

import java.util.ArrayList;
import java.util.List;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.el.JuelExpression;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description TODO
 * @date 2020/6/30
 * @author ctf
 **/
public class TestExecutionListener implements ExecutionListener {

  @Override
  public void notify(DelegateExecution execution) {
    //获取传过来的参数
    SequenceFlow element= (SequenceFlow) execution.getCurrentFlowElement();
    String preName=element.getSourceFlowElement().getName();
    UserTask userTask= (UserTask) element.getTargetFlowElement();
    String nextName=userTask.getName();
    Process process= (Process) userTask.getParentContainer();
    List<FlowElement> flowElementList= (List<FlowElement>) process.getFlowElements();
    flowElementList.forEach(flowElement -> {
     System.out.println( flowElement.getName());
    });
  }
}
