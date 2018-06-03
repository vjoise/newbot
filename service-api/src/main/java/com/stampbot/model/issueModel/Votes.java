package com.stampbot.model.issueModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Votes{

	@JsonProperty("hasVoted")
	private boolean hasVoted;

	@JsonProperty("self")
	private String self;

	@JsonProperty("votes")
	private int votes;

	public void setHasVoted(boolean hasVoted){
		this.hasVoted = hasVoted;
	}

	public boolean isHasVoted(){
		return hasVoted;
	}

	public void setSelf(String self){
		this.self = self;
	}

	public String getSelf(){
		return self;
	}

	public void setVotes(int votes){
		this.votes = votes;
	}

	public int getVotes(){
		return votes;
	}

	@Override
 	public String toString(){
		return 
			"Votes{" + 
			"hasVoted = '" + hasVoted + '\'' + 
			",self = '" + self + '\'' + 
			",votes = '" + votes + '\'' + 
			"}";
		}
}