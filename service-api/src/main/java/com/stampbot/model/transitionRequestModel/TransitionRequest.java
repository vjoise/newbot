package com.stampbot.model.transitionRequestModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stampbot.model.issueModel.Fields;
import com.stampbot.model.transitionModel.TransitionsItem;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransitionRequest {

    @JsonProperty("historyMetadata")
    private HistoryMetadata historyMetadata;

    @JsonProperty("update")
    private Update update;

    @JsonProperty("fields")
    private Fields fields;

    @JsonProperty("transition")
    private TransitionsItem transition;

    public HistoryMetadata getHistoryMetadata() {
        return historyMetadata;
    }

    public void setHistoryMetadata(HistoryMetadata historyMetadata) {
        this.historyMetadata = historyMetadata;
    }

    public Update getUpdate() {
        return update;
    }

    public void setUpdate(Update update) {
        this.update = update;
    }

    public Fields getFields() {
        return fields;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }

    public TransitionsItem getTransition() {
        return transition;
    }

    public void setTransition(TransitionsItem transition) {
        this.transition = transition;
    }

    @Override
    public String toString() {
        return
                "TransitionRequest{" +
                        "historyMetadata = '" + historyMetadata + '\'' +
                        ",update = '" + update + '\'' +
                        ",fields = '" + fields + '\'' +
                        ",transition = '" + transition + '\'' +
                        "}";
    }
}