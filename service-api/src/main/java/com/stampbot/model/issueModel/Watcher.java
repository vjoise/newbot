package com.stampbot.model.issueModel;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL) @JsonIgnoreProperties(ignoreUnknown = true)
public class Watcher{

	@JsonProperty("self")
	private String self;

	@JsonProperty("watchers")
	private List<WatchersItem> watchers;

	@JsonProperty("isWatching")
	private boolean isWatching;

	@JsonProperty("watchCount")
	private int watchCount;

	public void setSelf(String self){
		this.self = self;
	}

	public String getSelf(){
		return self;
	}

	public void setWatchers(List<WatchersItem> watchers){
		this.watchers = watchers;
	}

	public List<WatchersItem> getWatchers(){
		return watchers;
	}

	public void setIsWatching(boolean isWatching){
		this.isWatching = isWatching;
	}

	public boolean isIsWatching(){
		return isWatching;
	}

	public void setWatchCount(int watchCount){
		this.watchCount = watchCount;
	}

	public int getWatchCount(){
		return watchCount;
	}

	@Override
 	public String toString(){
		return 
			"Watcher{" + 
			"self = '" + self + '\'' + 
			",watchers = '" + watchers + '\'' + 
			",isWatching = '" + isWatching + '\'' + 
			",watchCount = '" + watchCount + '\'' + 
			"}";
		}
}