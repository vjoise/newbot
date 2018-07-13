package com.stampbot.workflow.model.createIssueModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Parent {

    @JsonProperty("id")
    private String id;

    @JsonProperty("key")
    private String key;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "Parent{" +
                "id='" + id + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}