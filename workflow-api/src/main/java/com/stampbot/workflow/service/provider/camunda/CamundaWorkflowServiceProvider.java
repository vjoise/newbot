package com.stampbot.workflow.service.provider.camunda;

import camundafeel.de.odysseus.el.ObjectValueExpression;
import camundafeel.javax.el.Expression;
import com.google.common.collect.Maps;
import com.stampbot.workflow.model.WorkflowContext;
import com.stampbot.workflow.service.provider.WorkflowServiceProvider;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.jobexecutor.JobExecutor;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.camunda.bpm.spring.boot.starter.event.PreUndeployEvent;
import org.camunda.bpm.spring.boot.starter.property.CamundaBpmProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

@Component(WorkflowServiceProvider.CAMUNDA)
@EnableProcessApplication
@Slf4j
class CamundaWorkflowServiceProvider implements WorkflowServiceProvider {

	@Autowired
	private JobExecutor jobExecutor;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private ConfigurableApplicationContext context;

	@Autowired
	private CamundaBpmProperties camundaBpmProperties;

	@Autowired
	private ProcessEngine processEngine;


	@Value("${org.camunda.bpm.spring.boot.starter.example.simple.SimpleApplication.exitWhenFinished:true}")
	private boolean exitWhenFinished;

	@EventListener
	public void onPostDeploy(PostDeployEvent event) {
		log.info("postDeploy: {}", event);
	}

	private Boolean processApplicationStopped = false;

	@EventListener
	public void onPreUndeploy(PreUndeployEvent event) {
		log.info("preUndeploy: {}", event);
		processApplicationStopped = true;
	}


	@Scheduled(fixedDelay = 1500L)
	public void exitApplicationWhenProcessIsFinished() {
		Assert.isTrue(!((ProcessEngineConfigurationImpl) processEngine.getProcessEngineConfiguration()).isDbMetricsReporterActivate());

		String processInstanceId = "123";

		if (processInstanceId == null) {
			log.info("processInstance not yet started!");
			return;
		}

		if (isProcessInstanceFinished()) {
			log.info("processinstance ended!");

			if (exitWhenFinished) {
				jobExecutor.shutdown();
				SpringApplication.exit(context, () -> 0);
			}
			return;
		}
		log.info("processInstance not yet ended!");
	}

	public boolean isProcessInstanceFinished() {
		final HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId("123").singleResult();

		return historicProcessInstance != null && historicProcessInstance.getEndTime() != null;
	}

	@Override
	public void process(WorkflowContext context) {
		String workflowId = context.getWorkflowName();
		log.info("Workflow ID :: " + workflowId);
		Map<String, Object> variablesMap = Maps.newHashMap();
		variablesMap.put("approved", true);
		ProcessInstance dev_workflow = processEngine.getRuntimeService().startProcessInstanceByKey("dev_workflow", variablesMap);

		String processInstanceId = dev_workflow.getProcessInstanceId();
		log.info("Process Instance ID :: {}", processInstanceId);
		TaskService taskService = processEngine.getTaskService();
		Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
		processEngine.getRuntimeService().createExecutionQuery().processInstanceId("198282f2-85d2-11e8-9704-e470b899ad63").list();
		taskService.complete(task.getId());
	}
}
