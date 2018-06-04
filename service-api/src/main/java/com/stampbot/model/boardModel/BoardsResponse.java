package com.stampbot.model.boardModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BoardsResponse {

    @JsonProperty("values")
    private List<Board> values;

    @JsonProperty("total")
    private int total;

    @JsonProperty("isLast")
    private boolean isLast;

    @JsonProperty("maxResults")
    private int maxResults;

    @JsonProperty("startAt")
    private int startAt;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isIsLast() {
        return isLast;
    }

    public void setIsLast(boolean isLast) {
        this.isLast = isLast;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public List<Board> getValues() {
        return values;
    }

    public void setValues(List<Board> values) {
        this.values = values;
    }

    public int getStartAt() {
        return startAt;
    }

    public void setStartAt(int startAt) {
        this.startAt = startAt;
    }

    @Override
    public String toString() {
        return
                "BoardsResponse{" +
                        "total = '" + total + '\'' +
                        ",isLast = '" + isLast + '\'' +
                        ",maxResults = '" + maxResults + '\'' +
                        ",values = '" + values + '\'' +
                        ",startAt = '" + startAt + '\'' +
                        "}";
    }
}