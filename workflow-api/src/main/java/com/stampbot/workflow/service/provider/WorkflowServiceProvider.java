package com.stampbot.workflow.service.provider;

import com.stampbot.workflow.model.WorkflowContext;

public interface WorkflowServiceProvider {

	String BASIC = "basicWorkflowProvider";
	String CAMUNDA = "camundaWorkflowProvider";

	void process(WorkflowContext context);

}
