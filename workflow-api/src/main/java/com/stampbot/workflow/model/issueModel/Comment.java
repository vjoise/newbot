package com.stampbot.workflow.model.issueModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment{

	@JsonProperty("total")
	private int total;

	@JsonProperty("comments")
	private List<CommentItem> comments;

	@JsonProperty("maxResults")
	private int maxResults;

	@JsonProperty("startAt")
	private int startAt;

	public void setTotal(int total){
		this.total = total;
	}

	public int getTotal(){
		return total;
	}

	public void setComments(List<CommentItem> comments){
		this.comments = comments;
	}

	public List<CommentItem> getComments(){
		return comments;
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

	@Override
 	public String toString(){
		return 
			"Comment{" + 
			"total = '" + total + '\'' + 
			",comments = '" + comments + '\'' + 
			",maxResults = '" + maxResults + '\'' + 
			",startAt = '" + startAt + '\'' + 
			"}";
		}
}