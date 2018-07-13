package com.stampbot.workflow.model.issueModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL) @JsonIgnoreProperties(ignoreUnknown = true)
public class Type{

	@JsonProperty("inward")
	private String inward;

	@JsonProperty("name")
	private String name;

	@JsonProperty("id")
	private String id;

	@JsonProperty("outward")
	private String outward;

	public void setInward(String inward){
		this.inward = inward;
	}

	public String getInward(){
		return inward;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setOutward(String outward){
		this.outward = outward;
	}

	public String getOutward(){
		return outward;
	}

	@Override
 	public String toString(){
		return 
			"Type{" + 
			"inward = '" + inward + '\'' + 
			",name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			",outward = '" + outward + '\'' + 
			"}";
		}
}