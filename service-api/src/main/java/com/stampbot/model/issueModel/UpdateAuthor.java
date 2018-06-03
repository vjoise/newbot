package com.stampbot.model.issueModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stampbot.model.issueModel.AvatarUrls;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateAuthor{

	@JsonProperty("accountId")
	private String accountId;

	@JsonProperty("emailAddress")
	private String emailAddress;

	@JsonProperty("avatarUrls")
	private AvatarUrls avatarUrls;

	@JsonProperty("displayName")
	private String displayName;

	@JsonProperty("name")
	private String name;

	@JsonProperty("self")
	private String self;

	@JsonProperty("active")
	private boolean active;

	@JsonProperty("timeZone")
	private String timeZone;

	@JsonProperty("key")
	private String key;

	public void setAccountId(String accountId){
		this.accountId = accountId;
	}

	public String getAccountId(){
		return accountId;
	}

	public void setEmailAddress(String emailAddress){
		this.emailAddress = emailAddress;
	}

	public String getEmailAddress(){
		return emailAddress;
	}

	public void setAvatarUrls(AvatarUrls avatarUrls){
		this.avatarUrls = avatarUrls;
	}

	public AvatarUrls getAvatarUrls(){
		return avatarUrls;
	}

	public void setDisplayName(String displayName){
		this.displayName = displayName;
	}

	public String getDisplayName(){
		return displayName;
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

	public void setActive(boolean active){
		this.active = active;
	}

	public boolean isActive(){
		return active;
	}

	public void setTimeZone(String timeZone){
		this.timeZone = timeZone;
	}

	public String getTimeZone(){
		return timeZone;
	}

	public void setKey(String key){
		this.key = key;
	}

	public String getKey(){
		return key;
	}

	@Override
 	public String toString(){
		return 
			"UpdateAuthor{" + 
			"accountId = '" + accountId + '\'' + 
			",emailAddress = '" + emailAddress + '\'' + 
			",avatarUrls = '" + avatarUrls + '\'' + 
			",displayName = '" + displayName + '\'' + 
			",name = '" + name + '\'' + 
			",self = '" + self + '\'' + 
			",active = '" + active + '\'' + 
			",timeZone = '" + timeZone + '\'' + 
			",key = '" + key + '\'' + 
			"}";
		}
}