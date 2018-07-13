package com.stampbot.workflow.model.boardModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stampbot.workflow.model.issueModel.Project;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Board {

    @JsonProperty("location")
    private Project location;

    @JsonProperty("name")
    private String name;

    @JsonProperty("self")
    private String self;

    @JsonProperty("id")
    private int id;

    @JsonProperty("type")
    private String type;

    public Project getLocation() {
        return location;
    }

    public void setLocation(Project location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Board{" +
                "location=" + location +
                ", name='" + name + '\'' +
                ", self='" + self + '\'' +
                ", id=" + id +
                ", type='" + type + '\'' +
                '}';
    }
}