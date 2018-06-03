package com.stampbot.model.issueModel;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stampbot.model.issueModel.CommentItem;

import javax.annotation.Generated;

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