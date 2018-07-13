package com.stampbot.workflow.model.issueModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Worklog{

	@JsonProperty("total")
	private int total;

	@JsonProperty("maxResults")
	private int maxResults;

	@JsonProperty("startAt")
	private int startAt;

	@JsonProperty("worklogs")
	private List<WorklogItem> worklogs;

	public void setTotal(int total){
		this.total = total;
	}

	public int getTotal(){
		return total;
	}

	public void setMaxResults(int maxResults){
		this.maxResults = maxResults;
	}

	public int getMaxResults(){
		return maxResults;
	}

	public void setStartAt(int startAt){
		this.startAt = startAt;
	}

	public int getStartAt(){
		return startAt;
	}

	public void setWorklogs(List<WorklogItem> worklogs){
		this.worklogs = worklogs;
	}

	public List<WorklogItem> getWorklogs(){
		return worklogs;
	}

	@Override
 	public String toString(){
		return 
			"Worklog{" + 
			"total = '" + total + '\'' + 
			",maxResults = '" + maxResults + '\'' + 
			",startAt = '" + startAt + '\'' + 
			",worklogs = '" + worklogs + '\'' + 
			"}";
		}
}