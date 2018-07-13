package com.stampbot.workflow.model.issueModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Progress{

	@JsonProperty("total")
	private int total;

	@JsonProperty("progress")
	private int progress;

	public void setTotal(int total){
		this.total = total;
	}

	public int getTotal(){
		return total;
	}

	public void setProgress(int progress){
		this.progress = progress;
	}

	public int getProgress(){
		return progress;
	}

	@Override
 	public String toString(){
		return 
			"Progress{" + 
			"total = '" + total + '\'' + 
			",progress = '" + progress + '\'' + 
			"}";
		}
}