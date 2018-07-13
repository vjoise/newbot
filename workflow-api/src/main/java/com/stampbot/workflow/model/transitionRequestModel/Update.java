package com.stampbot.workflow.model.transitionRequestModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Update {

    @JsonProperty("comment")
    private List<CommentItem> comment;

    public List<CommentItem> getComment() {
        return comment;
    }

    public void setComment(List<CommentItem> comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return
                "Update{" +
                        "comment = '" + comment + '\'' +
                        "}";
    }
}