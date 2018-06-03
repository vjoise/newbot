
package com.stampbot.model.createMetaModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stampbot.model.issueModel.Project;

import java.util.List;

//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectMetaData {

    @JsonProperty("expand")
    private String mExpand;
    @JsonProperty("projects")
    private List<Project> mProjects;

    public String getExpand() {
        return mExpand;
    }

    public void setExpand(String expand) {
        mExpand = expand;
    }

    public List<Project> getProjects() {
        return mProjects;
    }

    public void setProjects(List<Project> projects) {
        mProjects = projects;
    }

    @Override
    public String toString() {
        return "ProjectMetaData{" +
                "mExpand='" + mExpand + '\'' +
                ", mProjects=" + mProjects +
                '}';
    }
}
