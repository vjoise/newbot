package com.stampbot.model.editIssueRequestModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Update {

    @JsonProperty("comment")
    private List<CommentItem> comment;

    @JsonProperty("assignee")
    private List<AssigneeItem> assignee;

    public List<CommentItem> getComment() {
        return comment;
    }

    public void setComment(List<CommentItem> comment) {
        this.comment = comment;
    }

    public List<AssigneeItem> getAssignee() {
        return assignee;
    }

    public void setAssignee(List<AssigneeItem> assignee) {
        this.assignee = assignee;
    }

    @Override
    public String toString() {
        return
                "Update{" +
                        "comment = '" + comment + '\'' +
                        ",assignee = '" + assignee + '\'' +
                        "}";
    }
}