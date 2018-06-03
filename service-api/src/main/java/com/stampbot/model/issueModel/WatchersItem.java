package com.stampbot.model.issueModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WatchersItem {

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("name")
    private String name;

    @JsonProperty("self")
    private String self;

    @JsonProperty("active")
    private boolean active;

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getSelf() {
        return self;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public String toString() {
        return
                "WatchersItem{" +
                        "displayName = '" + displayName + '\'' +
                        ",name = '" + name + '\'' +
                        ",self = '" + self + '\'' +
                        ",active = '" + active + '\'' +
                        "}";
    }
}