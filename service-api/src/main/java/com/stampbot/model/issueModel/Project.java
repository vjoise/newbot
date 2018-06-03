package com.stampbot.model.issueModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Project{

	@JsonProperty("avatarUrls")
	private AvatarUrls avatarUrls;

	@JsonProperty("projectCategory")
	private ProjectCategory projectCategory;

	@JsonProperty("projectTypeKey")
	private String projectTypeKey;

	@JsonProperty("issuetypes")
	private List<Issuetype> issuetypes;

	@JsonProperty("name")
	private String name;

	@JsonProperty("self")
	private String self;

	@JsonProperty("id")
	private String id;

	@JsonProperty("key")
	private String key;

	public void setAvatarUrls(AvatarUrls avatarUrls){
		this.avatarUrls = avatarUrls;
	}

	public AvatarUrls getAvatarUrls(){
		return avatarUrls;
	}

	public void setProjectCategory(ProjectCategory projectCategory){
		this.projectCategory = projectCategory;
	}

	public ProjectCategory getProjectCategory(){
		return projectCategory;
	}

	public List<Issuetype> getIssuetypes() {
		return issuetypes;
	}

	public void setIssuetypes(List<Issuetype> issuetypes) {
		this.issuetypes = issuetypes;
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

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setKey(String key){
		this.key = key;
	}

	public String getKey(){
		return key;
	}

	public void setProjectTypeKey(String projectTypeKey){
		this.projectTypeKey = projectTypeKey;
	}

	public String getProjectTypeKey(){
		return projectTypeKey;
	}

	@Override
 	public String toString(){
		return 
			"Project{" + 
			"avatarUrls = '" + avatarUrls + '\'' + 
			",projectCategory = '" + projectCategory + '\'' + 
			",name = '" + name + '\'' + 
			",self = '" + self + '\'' + 
			",id = '" + id + '\'' + 
			",key = '" + key + '\'' + 
			"}";
		}
}