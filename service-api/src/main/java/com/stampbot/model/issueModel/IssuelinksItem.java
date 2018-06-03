package com.stampbot.model.issueModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL) @JsonIgnoreProperties(ignoreUnknown = true)
public class IssuelinksItem{

	@JsonProperty("id")
	private String id;

	@JsonProperty("inwardIssue")
	private InwardIssue inwardIssue;

	@JsonProperty("type")
	private Type type;

	@JsonProperty("outwardIssue")
	private OutwardIssue outwardIssue;

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setInwardIssue(InwardIssue inwardIssue){
		this.inwardIssue = inwardIssue;
	}

	public InwardIssue getInwardIssue(){
		return inwardIssue;
	}

	public void setType(Type type){
		this.type = type;
	}

	public Type getType(){
		return type;
	}

	public void setOutwardIssue(OutwardIssue outwardIssue){
		this.outwardIssue = outwardIssue;
	}

	public OutwardIssue getOutwardIssue(){
		return outwardIssue;
	}

	@Override
 	public String toString(){
		return 
			"IssuelinksItem{" + 
			"id = '" + id + '\'' + 
			",inwardIssue = '" + inwardIssue + '\'' + 
			",type = '" + type + '\'' + 
			",outwardIssue = '" + outwardIssue + '\'' + 
			"}";
		}
}