package com.stampbot.workflow.model.editMetaModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Fields {

    @JsonProperty("summary")
    private Summary summary;

    @JsonProperty("issuetype")
    private Issuetype issuetype;

    @JsonProperty("components")
    private Components components;

    @JsonProperty("description")
    private Description description;

    @JsonProperty("reporter")
    private Reporter reporter;

    @JsonProperty("customfield_10010")
    private Customfield10010 customfield10010;

    @JsonProperty("fixVersions")
    private FixVersions fixVersions;

    @JsonProperty("priority")
    private Priority priority;

    @JsonProperty("labels")
    private Labels labels;

    @JsonProperty("environment")
    private Environment environment;

    @JsonProperty("customfield_10008")
    private Customfield10008 customfield10008;

    @JsonProperty("attachment")
    private Attachment attachment;

    @JsonProperty("versions")
    private Versions versions;

    @JsonProperty("issuelinks")
    private Issuelinks issuelinks;

    @JsonProperty("comment")
    private Comment comment;

    @JsonProperty("assignee")
    private Assignee assignee;

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public Issuetype getIssuetype() {
        return issuetype;
    }

    public void setIssuetype(Issuetype issuetype) {
        this.issuetype = issuetype;
    }

    public Components getComponents() {
        return components;
    }

    public void setComponents(Components components) {
        this.components = components;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public Reporter getReporter() {
        return reporter;
    }

    public void setReporter(Reporter reporter) {
        this.reporter = reporter;
    }

    public Customfield10010 getCustomfield10010() {
        return customfield10010;
    }

    public void setCustomfield10010(Customfield10010 customfield10010) {
        this.customfield10010 = customfield10010;
    }

    public FixVersions getFixVersions() {
        return fixVersions;
    }

    public void setFixVersions(FixVersions fixVersions) {
        this.fixVersions = fixVersions;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Labels getLabels() {
        return labels;
    }

    public void setLabels(Labels labels) {
        this.labels = labels;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Customfield10008 getCustomfield10008() {
        return customfield10008;
    }

    public void setCustomfield10008(Customfield10008 customfield10008) {
        this.customfield10008 = customfield10008;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    public Versions getVersions() {
        return versions;
    }

    public void setVersions(Versions versions) {
        this.versions = versions;
    }

    public Issuelinks getIssuelinks() {
        return issuelinks;
    }

    public void setIssuelinks(Issuelinks issuelinks) {
        this.issuelinks = issuelinks;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Assignee getAssignee() {
        return assignee;
    }

    public void setAssignee(Assignee assignee) {
        this.assignee = assignee;
    }

    @Override
    public String toString() {
        return
                "Fields{" +
                        "summary = '" + summary + '\'' +
                        ",issuetype = '" + issuetype + '\'' +
                        ",components = '" + components + '\'' +
                        ",description = '" + description + '\'' +
                        ",reporter = '" + reporter + '\'' +
                        ",customfield_10010 = '" + customfield10010 + '\'' +
                        ",fixVersions = '" + fixVersions + '\'' +
                        ",priority = '" + priority + '\'' +
                        ",labels = '" + labels + '\'' +
                        ",environment = '" + environment + '\'' +
                        ",customfield_10008 = '" + customfield10008 + '\'' +
                        ",attachment = '" + attachment + '\'' +
                        ",versions = '" + versions + '\'' +
                        ",issuelinks = '" + issuelinks + '\'' +
                        ",comment = '" + comment + '\'' +
                        ",assignee = '" + assignee + '\'' +
                        "}";
    }
}