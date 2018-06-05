package com.stampbot.model.transitionModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stampbot.model.issueModel.Fields;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransitionsItem {

    @JsonProperty("hasScreen")
    private boolean hasScreen;

    @JsonProperty("name")
    private String name;

    @JsonProperty("isGlobal")
    private boolean isGlobal;

    @JsonProperty("isInitial")
    private boolean isInitial;

    @JsonProperty("id")
    private int id;

    @JsonProperty("to")
    private To to;

    @JsonProperty("isConditional")
    private boolean isConditional;

    @JsonProperty("fields")
    private Fields fields;

    public boolean isHasScreen() {
        return hasScreen;
    }

    public void setHasScreen(boolean hasScreen) {
        this.hasScreen = hasScreen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIsGlobal() {
        return isGlobal;
    }

    public void setIsGlobal(boolean isGlobal) {
        this.isGlobal = isGlobal;
    }

    public boolean isIsInitial() {
        return isInitial;
    }

    public void setIsInitial(boolean isInitial) {
        this.isInitial = isInitial;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public To getTo() {
        return to;
    }

    public void setTo(To to) {
        this.to = to;
    }

    public boolean isIsConditional() {
        return isConditional;
    }

    public void setIsConditional(boolean isConditional) {
        this.isConditional = isConditional;
    }

    public Fields getFields() {
        return fields;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return
                "TransitionsItem{" +
                        "hasScreen = '" + hasScreen + '\'' +
                        ",name = '" + name + '\'' +
                        ",isGlobal = '" + isGlobal + '\'' +
                        ",isInitial = '" + isInitial + '\'' +
                        ",id = '" + id + '\'' +
                        ",to = '" + to + '\'' +
                        ",isConditional = '" + isConditional + '\'' +
                        ",fields = '" + fields + '\'' +
                        "}";
    }
}