package com.stampbot.workflow.model.createIssueModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stampbot.workflow.model.issueModel.WorklogItem;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Update {

    @JsonProperty("worklog")
    private List<WorklogItem> worklog;

    public void setWorklog(List<WorklogItem> worklog) {
        this.worklog = worklog;
    }

    public List<WorklogItem> getWorklog() {
        return worklog;
    }

    @Override
    public String toString() {
        return
                "Update{" +
                        "worklog = '" + worklog + '\'' +
                        "}";
    }
}