package com.stampbot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stampbot.model.createIssueModel.Update;
import com.stampbot.model.issueModel.Fields;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateIssueDto{

	@JsonProperty("update")
	private Update update;

	@JsonProperty("fields")
	private Fields fields;

	@JsonProperty("parent")
	private String parent;

	public void setUpdate(Update update){
		this.update = update;
	}

	public Update getUpdate(){
		return update;
	}

	public void setFields(Fields fields){
		this.fields = fields;
	}

	public Fields getFields(){
		return fields;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		return "CreateIssueDto{" +
				"update=" + update +
				", fields=" + fields +
				", parent=" + parent +
				'}';
	}
}