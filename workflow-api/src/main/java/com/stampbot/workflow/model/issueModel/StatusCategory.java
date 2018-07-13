package com.stampbot.workflow.model.issueModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatusCategory{

	@JsonProperty("colorName")
	private String colorName;

	@JsonProperty("name")
	private String name;

	@JsonProperty("self")
	private String self;

	@JsonProperty("id")
	private int id;

	@JsonProperty("key")
	private String key;

	public void setColorName(String colorName){
		this.colorName = colorName;
	}

	public String getColorName(){
		return colorName;
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

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
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
			"StatusCategory{" + 
			"colorName = '" + colorName + '\'' + 
			",name = '" + name + '\'' + 
			",self = '" + self + '\'' + 
			",id = '" + id + '\'' + 
			",key = '" + key + '\'' + 
			"}";
		}
}