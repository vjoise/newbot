package com.stampbot.workflow.model.issueModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL) @JsonIgnoreProperties(ignoreUnknown = true)
public class CommentItem{

	@JsonProperty("visibility")
	private Visibility visibility;

	@JsonProperty("author")
	private Author author;

	@JsonProperty("created")
	private String created;

	@JsonProperty("updateAuthor")
	private UpdateAuthor updateAuthor;

	@JsonProperty("self")
	private String self;

	@JsonProperty("id")
	private String id;

	@JsonProperty("body")
	private String body;

	@JsonProperty("updated")
	private String updated;

	public void setVisibility(Visibility visibility){
		this.visibility = visibility;
	}

	public Visibility getVisibility(){
		return visibility;
	}

	public void setAuthor(Author author){
		this.author = author;
	}

	public Author getAuthor(){
		return author;
	}

	public void setCreated(String created){
		this.created = created;
	}

	public String getCreated(){
		return created;
	}

	public void setUpdateAuthor(UpdateAuthor updateAuthor){
		this.updateAuthor = updateAuthor;
	}

	public UpdateAuthor getUpdateAuthor(){
		return updateAuthor;
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

	public void setBody(String body){
		this.body = body;
	}

	public String getBody(){
		return body;
	}

	public void setUpdated(String updated){
		this.updated = updated;
	}

	public String getUpdated(){
		return updated;
	}

	@Override
 	public String toString(){
		return 
			"CommentItem{" + 
			"visibility = '" + visibility + '\'' + 
			",author = '" + author + '\'' + 
			",created = '" + created + '\'' + 
			",updateAuthor = '" + updateAuthor + '\'' + 
			",self = '" + self + '\'' + 
			",id = '" + id + '\'' + 
			",body = '" + body + '\'' + 
			",updated = '" + updated + '\'' + 
			"}";
		}
}