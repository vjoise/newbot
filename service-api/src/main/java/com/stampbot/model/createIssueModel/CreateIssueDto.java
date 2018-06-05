package com.stampbot.model.createIssueModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stampbot.model.issueModel.Fields;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateIssueDto {

	@JsonProperty("update")
	private Update update;

	@JsonProperty("fields")
	private Fields fields;

	@JsonProperty("parent")
	private String parent;

    public Update getUpdate() {
		return update;
	}

    public void setUpdate(Update update) {
        this.update = update;
    }

    public Fields getFields() {
		return fields;
	}

    public void setFields(Fields fields) {
        this.fields = fields;
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