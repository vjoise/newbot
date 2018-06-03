package com.stampbot.model.issueModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL) @JsonIgnoreProperties(ignoreUnknown = true)
public class Names{

	@JsonProperty("watcher")
	private String watcher;

	@JsonProperty("attachment")
	private String attachment;

	@JsonProperty("sub-tasks")
	private String subTasks;

	@JsonProperty("description")
	private String description;

	@JsonProperty("project")
	private String project;

	@JsonProperty("comment")
	private String comment;

	@JsonProperty("issuelinks")
	private String issuelinks;

	@JsonProperty("worklog")
	private String worklog;

	@JsonProperty("updated")
	private String updated;

	@JsonProperty("timetracking")
	private String timetracking;

	public void setWatcher(String watcher){
		this.watcher = watcher;
	}

	public String getWatcher(){
		return watcher;
	}

	public void setAttachment(String attachment){
		this.attachment = attachment;
	}

	public String getAttachment(){
		return attachment;
	}

	public void setSubTasks(String subTasks){
		this.subTasks = subTasks;
	}

	public String getSubTasks(){
		return subTasks;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setProject(String project){
		this.project = project;
	}

	public String getProject(){
		return project;
	}

	public void setComment(String comment){
		this.comment = comment;
	}

	public String getComment(){
		return comment;
	}

	public void setIssuelinks(String issuelinks){
		this.issuelinks = issuelinks;
	}

	public String getIssuelinks(){
		return issuelinks;
	}

	public void setWorklog(String worklog){
		this.worklog = worklog;
	}

	public String getWorklog(){
		return worklog;
	}

	public void setUpdated(String updated){
		this.updated = updated;
	}

	public String getUpdated(){
		return updated;
	}

	public void setTimetracking(String timetracking){
		this.timetracking = timetracking;
	}

	public String getTimetracking(){
		return timetracking;
	}

	@Override
 	public String toString(){
		return 
			"Names{" + 
			"watcher = '" + watcher + '\'' + 
			",attachment = '" + attachment + '\'' + 
			",sub-tasks = '" + subTasks + '\'' + 
			",description = '" + description + '\'' + 
			",project = '" + project + '\'' + 
			",comment = '" + comment + '\'' + 
			",issuelinks = '" + issuelinks + '\'' + 
			",worklog = '" + worklog + '\'' + 
			",updated = '" + updated + '\'' + 
			",timetracking = '" + timetracking + '\'' + 
			"}";
		}
}