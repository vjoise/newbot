package com.stampbot.model.transitionRequestModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HistoryMetadata {

    @JsonProperty("actor")
    private Actor actor;

    @JsonProperty("extraData")
    private ExtraData extraData;

    @JsonProperty("activityDescriptionKey")
    private String activityDescriptionKey;

    @JsonProperty("descriptionKey")
    private String descriptionKey;

    @JsonProperty("description")
    private String description;

    @JsonProperty("generator")
    private Generator generator;

    @JsonProperty("cause")
    private Cause cause;

    @JsonProperty("activityDescription")
    private String activityDescription;

    @JsonProperty("type")
    private String type;

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public ExtraData getExtraData() {
        return extraData;
    }

    public void setExtraData(ExtraData extraData) {
        this.extraData = extraData;
    }

    public String getActivityDescriptionKey() {
        return activityDescriptionKey;
    }

    public void setActivityDescriptionKey(String activityDescriptionKey) {
        this.activityDescriptionKey = activityDescriptionKey;
    }

    public String getDescriptionKey() {
        return descriptionKey;
    }

    public void setDescriptionKey(String descriptionKey) {
        this.descriptionKey = descriptionKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Generator getGenerator() {
        return generator;
    }

    public void setGenerator(Generator generator) {
        this.generator = generator;
    }

    public Cause getCause() {
        return cause;
    }

    public void setCause(Cause cause) {
        this.cause = cause;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return
                "HistoryMetadata{" +
                        "actor = '" + actor + '\'' +
                        ",extraData = '" + extraData + '\'' +
                        ",activityDescriptionKey = '" + activityDescriptionKey + '\'' +
                        ",descriptionKey = '" + descriptionKey + '\'' +
                        ",description = '" + description + '\'' +
                        ",generator = '" + generator + '\'' +
                        ",cause = '" + cause + '\'' +
                        ",activityDescription = '" + activityDescription + '\'' +
                        ",type = '" + type + '\'' +
                        "}";
    }
}