package com.stampbot.workflow.model.issueModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Version {

    @JsonProperty("archived")
    private boolean archived;

    @JsonProperty("releaseDate")
    private String releaseDate;

    @JsonProperty("name")
    private String name;

    @JsonProperty("self")
    private String self;

    @JsonProperty("userReleaseDate")
    private String userReleaseDate;

    @JsonProperty("id")
    private String id;

    @JsonProperty("projectId")
    private int projectId;

    @JsonProperty("released")
    private boolean released;

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getReleaseDate() {
        return releaseDate;
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

    public void setUserReleaseDate(String userReleaseDate) {
        this.userReleaseDate = userReleaseDate;
    }

    public String getUserReleaseDate() {
        return userReleaseDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setReleased(boolean released) {
        this.released = released;
    }

    public boolean isReleased() {
        return released;
    }

    @Override
    public String toString() {
        return
                "Version{" +
                        "archived = '" + archived + '\'' +
                        ",releaseDate = '" + releaseDate + '\'' +
                        ",name = '" + name + '\'' +
                        ",self = '" + self + '\'' +
                        ",userReleaseDate = '" + userReleaseDate + '\'' +
                        ",id = '" + id + '\'' +
                        ",projectId = '" + projectId + '\'' +
                        ",released = '" + released + '\'' +
                        "}";
    }
}