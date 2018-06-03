package com.stampbot.model.createIssueModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Add{

	@JsonProperty("timeSpent")
	private String timeSpent;

	@JsonProperty("started")
	private String started;

	public void setTimeSpent(String timeSpent){
		this.timeSpent = timeSpent;
	}

	public String getTimeSpent(){
		return timeSpent;
	}

	public void setStarted(String started){
		this.started = started;
	}

	public String getStarted(){
		return started;
	}

	@Override
 	public String toString(){
		return 
			"Add{" + 
			"timeSpent = '" + timeSpent + '\'' + 
			",started = '" + started + '\'' + 
			"}";
		}
}