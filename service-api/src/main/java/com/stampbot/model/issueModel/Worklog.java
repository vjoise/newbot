package com.stampbot.model.issueModel;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stampbot.model.issueModel.WorklogItem;

import javax.annotation.Generated;

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