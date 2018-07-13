package com.stampbot.workflow.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class Sprint {

    @JsonProperty("originBoardId")
    private int originBoardId;

    @JsonProperty("endDate")
    private String endDate;

    @JsonProperty("name")
    private String name;

    @JsonProperty("self")
    private String self;

    @JsonProperty("id")
    private int id;

    @JsonProperty("state")
    private String state;

    @JsonProperty("startDate")
    private String startDate;

    @JsonProperty("completeDate")
    private String completeDate;

    public int getOriginBoardId() {
        return originBoardId;
    }

    public void setOriginBoardId(int originBoardId) {
        this.originBoardId = originBoardId;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(String completeDate) {
        this.completeDate = completeDate;
    }

    @Override
    public String toString() {
        return
                "Sprint{" +
                        "originBoardId = '" + originBoardId + '\'' +
                        ",endDate = '" + endDate + '\'' +
                        ",name = '" + name + '\'' +
                        ",self = '" + self + '\'' +
                        ",id = '" + id + '\'' +
                        ",state = '" + state + '\'' +
                        ",startDate = '" + startDate + '\'' +
                        ",completeDate = '" + completeDate + '\'' +
                        "}";
    }
}