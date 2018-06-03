package com.stampbot.model.issueModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stampbot.model.createIssueModel.Add;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorklogItem {

    @JsonProperty("issueId")
    private String issueId;

    @JsonProperty("timeSpentSeconds")
    private int timeSpentSeconds;

    @JsonProperty("visibility")
    private Visibility visibility;

    @JsonProperty("timeSpent")
    private String timeSpent;

    @JsonProperty("author")
    private Author author;

    @JsonProperty("updateAuthor")
    private UpdateAuthor updateAuthor;

    @JsonProperty("self")
    private String self;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("started")
    private String started;

    @JsonProperty("id")
    private String id;

    @JsonProperty("updated")
    private String updated;

    @JsonProperty("add")
    private Add add;

    public void setAdd(Add add) {
        this.add = add;
    }

    public Add getAdd() {
        return add;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setTimeSpentSeconds(int timeSpentSeconds) {
        this.timeSpentSeconds = timeSpentSeconds;
    }

    public int getTimeSpentSeconds() {
        return timeSpentSeconds;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setTimeSpent(String timeSpent) {
        this.timeSpent = timeSpent;
    }

    public String getTimeSpent() {
        return timeSpent;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Author getAuthor() {
        return author;
    }

    public void setUpdateAuthor(UpdateAuthor updateAuthor) {
        this.updateAuthor = updateAuthor;
    }

    public UpdateAuthor getUpdateAuthor() {
        return updateAuthor;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getSelf() {
        return self;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setStarted(String started) {
        this.started = started;
    }

    public String getStarted() {
        return started;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getUpdated() {
        return updated;
    }

    @Override
    public String toString() {
        return "WorklogItem{" +
                "issueId='" + issueId + '\'' +
                ", timeSpentSeconds=" + timeSpentSeconds +
                ", visibility=" + visibility +
                ", timeSpent='" + timeSpent + '\'' +
                ", author=" + author +
                ", updateAuthor=" + updateAuthor +
                ", self='" + self + '\'' +
                ", comment='" + comment + '\'' +
                ", started='" + started + '\'' +
                ", id='" + id + '\'' +
                ", updated='" + updated + '\'' +
                ", add=" + add +
                '}';
    }
}