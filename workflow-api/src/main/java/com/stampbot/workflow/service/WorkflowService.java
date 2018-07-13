package com.stampbot.workflow.service;

import com.stampbot.workflow.model.WorkflowContext;
import com.stampbot.workflow.service.provider.WorkflowServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class WorkflowService {

	@Autowired
	@Qualifier(WorkflowServiceProvider.CAMUNDA)
	private WorkflowServiceProvider provider;

	public void process(Map<String, Object> context){
		WorkflowContext workflowContext = new WorkflowContext();
		workflowContext.setWorkflowName(String.class.cast(context.get("WORKFLOW_ID")));
		workflowContext.setUserId(Long.class.cast(context.get("USER_ID")));
		workflowContext.setConversationId(String.class.cast(context.get("CONVERSATION_ID")));
		provider.process(workflowContext);
	}

}
