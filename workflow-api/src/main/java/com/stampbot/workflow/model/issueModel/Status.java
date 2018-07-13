package com.stampbot.workflow.model.issueModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Status{

	@JsonProperty("name")
	private String name;

	@JsonProperty("self")
	private String self;

	@JsonProperty("description")
	private String description;

	@JsonProperty("iconUrl")
	private String iconUrl;

	@JsonProperty("id")
	private String id;

	@JsonProperty("statusCategory")
	private StatusCategory statusCategory;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setSelf(String self){
		this.self = self;
	}

	public String getSelf(){
		return self;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setIconUrl(String iconUrl){
		this.iconUrl = iconUrl;
	}

	public String getIconUrl(){
		return iconUrl;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setStatusCategory(StatusCategory statusCategory){
		this.statusCategory = statusCategory;
	}

	public StatusCategory getStatusCategory(){
		return statusCategory;
	}

	@Override
 	public String toString(){
		return 
			"Status{" + 
			"name = '" + name + '\'' + 
			",self = '" + self + '\'' + 
			",description = '" + description + '\'' + 
			",iconUrl = '" + iconUrl + '\'' + 
			",id = '" + id + '\'' + 
			",statusCategory = '" + statusCategory + '\'' + 
			"}";
		}
}