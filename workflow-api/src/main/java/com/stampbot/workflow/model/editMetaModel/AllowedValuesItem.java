package com.stampbot.workflow.model.editMetaModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AllowedValuesItem {

    @JsonProperty("archived")
    private boolean archived;

    @JsonProperty("name")
    private String name;

    @JsonProperty("self")
    private String self;

    @JsonProperty("id")
    private String id;

    @JsonProperty("projectId")
    private int projectId;

    @JsonProperty("released")
    private boolean released;

    @JsonProperty("overdue")
    private boolean overdue;

    @JsonProperty("releaseDate")
    private String releaseDate;

    @JsonProperty("userReleaseDate")
    private String userReleaseDate;

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public boolean isReleased() {
        return released;
    }

    public void setReleased(boolean released) {
        this.released = released;
    }

    public boolean isOverdue() {
        return overdue;
    }

    public void setOverdue(boolean overdue) {
        this.overdue = overdue;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getUserReleaseDate() {
        return userReleaseDate;
    }

    public void setUserReleaseDate(String userReleaseDate) {
        this.userReleaseDate = userReleaseDate;
    }

    @Override
    public String toString() {
        return
                "AllowedValuesItem{" +
                        "archived = '" + archived + '\'' +
                        ",name = '" + name + '\'' +
                        ",self = '" + self + '\'' +
                        ",id = '" + id + '\'' +
                        ",projectId = '" + projectId + '\'' +
                        ",released = '" + released + '\'' +
                        ",overdue = '" + overdue + '\'' +
                        ",releaseDate = '" + releaseDate + '\'' +
                        ",userReleaseDate = '" + userReleaseDate + '\'' +
                        "}";
    }
}