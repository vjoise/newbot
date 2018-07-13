package com.stampbot.workflow.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompleteTestParams {

    @JsonProperty("issueKey")
    private String issueKey;

    @JsonProperty("assignToReporter")
    private boolean assignToReporter;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("status")
    private String status;

    public String getIssueKey() {
        return issueKey;
    }

    public void setIssueKey(String issueKey) {
        this.issueKey = issueKey;
    }

    public boolean isAssignToReporter() {
        return assignToReporter;
    }

    public void setAssignToReporter(boolean assignToReporter) {
        this.assignToReporter = assignToReporter;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return
                "CompleteTestParams{" +
                        "issueKey = '" + issueKey + '\'' +
                        ",assignToReporter = '" + assignToReporter + '\'' +
                        ",comment = '" + comment + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}