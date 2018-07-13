package com.stampbot.workflow.model.transitionRequestModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentItem {

    @JsonProperty("add")
    private Add add;

    public Add getAdd() {
        return add;
    }

    public void setAdd(Add add) {
        this.add = add;
    }

    @Override
    public String toString() {
        return
                "CommentItem{" +
                        "add = '" + add + '\'' +
                        "}";
    }
}