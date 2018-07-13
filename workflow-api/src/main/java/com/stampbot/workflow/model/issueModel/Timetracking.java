package com.stampbot.workflow.model.issueModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Timetracking {

    @JsonProperty("originalEstimateSeconds")
    private int originalEstimateSeconds;

    @JsonProperty("timeSpentSeconds")
    private int timeSpentSeconds;

    @JsonProperty("timeSpent")
    private String timeSpent;

    @JsonProperty("remainingEstimate")
    private String remainingEstimate;

    @JsonProperty("remainingEstimateSeconds")
    private int remainingEstimateSeconds;

    @JsonProperty("originalEstimate")
    private String originalEstimate;

    public void setOriginalEstimateSeconds(int originalEstimateSeconds) {
        this.originalEstimateSeconds = originalEstimateSeconds;
    }

    public int getOriginalEstimateSeconds() {
        return originalEstimateSeconds;
    }

    public void setTimeSpentSeconds(int timeSpentSeconds) {
        this.timeSpentSeconds = timeSpentSeconds;
    }

    public int getTimeSpentSeconds() {
        return timeSpentSeconds;
    }

    public void setTimeSpent(String timeSpent) {
        this.timeSpent = timeSpent;
    }

    public String getTimeSpent() {
        return timeSpent;
    }

    public void setRemainingEstimate(String remainingEstimate) {
        this.remainingEstimate = remainingEstimate;
    }

    public String getRemainingEstimate() {
        return remainingEstimate;
    }

    public void setRemainingEstimateSeconds(int remainingEstimateSeconds) {
        this.remainingEstimateSeconds = remainingEstimateSeconds;
    }

    public int getRemainingEstimateSeconds() {
        return remainingEstimateSeconds;
    }

    public void setOriginalEstimate(String originalEstimate) {
        this.originalEstimate = originalEstimate;
    }

    public String getOriginalEstimate() {
        return originalEstimate;
    }

    @Override
    public String toString() {
        return
                "Timetracking{" +
                        "originalEstimateSeconds = '" + originalEstimateSeconds + '\'' +
                        ",timeSpentSeconds = '" + timeSpentSeconds + '\'' +
                        ",timeSpent = '" + timeSpent + '\'' +
                        ",remainingEstimate = '" + remainingEstimate + '\'' +
                        ",remainingEstimateSeconds = '" + remainingEstimateSeconds + '\'' +
                        ",originalEstimate = '" + originalEstimate + '\'' +
                        "}";
    }
}