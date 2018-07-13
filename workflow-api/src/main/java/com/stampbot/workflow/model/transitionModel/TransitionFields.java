package com.stampbot.workflow.model.transitionModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransitionFields {

    @JsonProperty("expand")
    private String expand;

    @JsonProperty("transitions")
    private List<TransitionsItem> transitions;

    public String getExpand() {
        return expand;
    }

    public void setExpand(String expand) {
        this.expand = expand;
    }

    public List<TransitionsItem> getTransitions() {
        return transitions;
    }

    public void setTransitions(List<TransitionsItem> transitions) {
        this.transitions = transitions;
    }

    @Override
    public String toString() {
        return
                "TransitionFields{" +
                        "expand = '" + expand + '\'' +
                        ",transitions = '" + transitions + '\'' +
                        "}";
    }
}