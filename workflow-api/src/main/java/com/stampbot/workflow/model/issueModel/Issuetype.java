package com.stampbot.workflow.model.issueModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Issuetype{

	@JsonProperty("avatarId")
	private String avatarId;

	@JsonProperty("name")
	private String name;

	@JsonProperty("self")
	private String self;

	@JsonProperty("description")
	private String description;

	@JsonProperty("id")
	private String id;

	@JsonProperty("iconUrl")
	private String iconUrl;

	@JsonProperty("subtask")
	private boolean subtask;

	public void setAvatarId(String avatarId){
		this.avatarId = avatarId;
	}

	public String getAvatarId(){
		return avatarId;
	}

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

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setIconUrl(String iconUrl){
		this.iconUrl = iconUrl;
	}

	public String getIconUrl(){
		return iconUrl;
	}

	public void setSubtask(boolean subtask){
		this.subtask = subtask;
	}

	public boolean isSubtask(){
		return subtask;
	}

	@Override
 	public String toString(){
		return 
			"Issuetype{" + 
			"avatarId = '" + avatarId + '\'' + 
			",name = '" + name + '\'' + 
			",self = '" + self + '\'' + 
			",description = '" + description + '\'' + 
			",id = '" + id + '\'' + 
			",iconUrl = '" + iconUrl + '\'' + 
			",subtask = '" + subtask + '\'' + 
			"}";
		}
}