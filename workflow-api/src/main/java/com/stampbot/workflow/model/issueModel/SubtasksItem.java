package com.stampbot.workflow.model.issueModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubtasksItem{

	@JsonProperty("outwardIssue")
	private OutwardIssue outwardIssue;

	@JsonProperty("self")
	private String self;

	@JsonProperty("id")
	private String id;

	@JsonProperty("fields")
	private Fields fields;

	@JsonProperty("key")
	private String key;

	@JsonProperty("type")
	private Type type;

	public void setSelf(String self){
		this.self = self;
	}

	public String getSelf(){
		return self;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setFields(Fields fields){
		this.fields = fields;
	}

	public Fields getFields(){
		return fields;
	}

	public void setKey(String key){
		this.key = key;
	}

	public String getKey(){
		return key;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public OutwardIssue getOutwardIssue() {

		return outwardIssue;
	}

	public void setOutwardIssue(OutwardIssue outwardIssue) {
		this.outwardIssue = outwardIssue;
	}

	@Override
 	public String toString(){
		return 
			"SubtasksItem{" + 
			"self = '" + self + '\'' + 
			",id = '" + id + '\'' + 
			",fields = '" + fields + '\'' + 
			",key = '" + key + '\'' + 
			"}";
		}
}