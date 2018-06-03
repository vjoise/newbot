package com.stampbot.model.issueModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stampbot.model.createIssueModel.*;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Fields {

    @JsonProperty("issuetype")
    private Issuetype issuetype;

    @JsonProperty("timespent")
    private Object timespent;

    @JsonProperty("project")
    private Project project;

    @JsonProperty("parent")
    private Parent parent;

    @JsonProperty("fixVersions")
    private List<FixVersionsItem> fixVersions;

    @JsonProperty("aggregatetimespent")
    private Object aggregatetimespent;

    @JsonProperty("resolution")
    private Resolution resolution;

    @JsonProperty("resolutiondate")
    private String resolutiondate;

    @JsonProperty("workratio")
    private Object workratio;

    @JsonProperty("lastViewed")
    private Object lastViewed;

    @JsonProperty("watches")
    private Watches watches;

    @JsonProperty("watcher")
    private Watcher watcher;

    @JsonProperty("created")
    private String created;

    @JsonProperty("priority")
    private Priority priority;

    @JsonProperty("labels")
    private List<String> labels;

    @JsonProperty("customfield_10017")
    private Object customfield10017;

    @JsonProperty("timeestimate")
    private Object timeestimate;

    @JsonProperty("aggregatetimeoriginalestimate")
    private Object aggregatetimeoriginalestimate;

    @JsonProperty("versions")
    private List<VersionsItem> versions;

    @JsonProperty("issuelinks")
    private List<IssuelinksItem> issuelinks;

    @JsonProperty("assignee")
    private Assignee assignee;

    @JsonProperty("updated")
    private String updated;

    @JsonProperty("status")
    private Status status;

    @JsonProperty("components")
    private List<ComponentsItem> components;

    @JsonProperty("timeoriginalestimate")
    private Object timeoriginalestimate;

    @JsonProperty("description")
    private String description;

    @JsonProperty("customfield_10000")
    private String customfield10000;

    @JsonProperty("customfield_10008")
    private Object customfield10008;

    @JsonProperty("customfield_10009")
    private Object customfield10009;

    @JsonProperty("customfield_10010")
    private List<String> customfield10010;

    @JsonProperty("customfield_10011")
    private String customfield10011;

    @JsonProperty("customfield_10012")
    private Object customfield10012;

    @JsonProperty("customfield_10013")
    private String customfield10013;

    @JsonProperty("customfield_10014")
    private Object customfield10014;

    @JsonProperty("customfield_20000")
    private String customfield20000;

    @JsonProperty("customfield_30000")
    private List<String> customfield30000;

    @JsonProperty("customfield_40000")
    private String customfield40000;

    @JsonProperty("customfield_50000")
    private String customfield50000;

    @JsonProperty("customfield_60000")
    private String customfield60000;

    @JsonProperty("customfield_70000")
    private List<String> customfield70000;

    @JsonProperty("customfield_80000")
    private Customfield80000 customfield80000;

    @JsonProperty("timetracking")
    private Timetracking timetracking;

    @JsonProperty("security")
    private Security security;

    @JsonProperty("aggregatetimeestimate")
    private Object aggregatetimeestimate;

    @JsonProperty("attachment")
    private List<AttachmentItem> attachment;

    @JsonProperty("summary")
    private String summary;

    @JsonProperty("creator")
    private Creator creator;

    @JsonProperty("subtasks")
    private List<SubtasksItem> subtasks;

    @JsonProperty("reporter")
    private Reporter reporter;

    @JsonProperty("aggregateprogress")
    private Aggregateprogress aggregateprogress;

    @JsonProperty("customfield_10001")
    private Object customfield10001;

    @JsonProperty("customfield_10004")
    private Object customfield10004;

    @JsonProperty("environment")
    private String environment;

    @JsonProperty("duedate")
    private String duedate;

    @JsonProperty("progress")
    private Progress progress;

    @JsonProperty("votes")
    private Votes votes;

    @JsonProperty("comment")
    private Comment comment;

    @JsonProperty("worklog")
    private Worklog worklog;

    public void setIssuetype(Issuetype issuetype) {
        this.issuetype = issuetype;
    }

    public Issuetype getIssuetype() {
        return issuetype;
    }

    public void setTimespent(Object timespent) {
        this.timespent = timespent;
    }

    public Object getTimespent() {
        return timespent;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }

    public void setFixVersions(List<FixVersionsItem> fixVersions) {
        this.fixVersions = fixVersions;
    }

    public List<FixVersionsItem> getFixVersions() {
        return fixVersions;
    }

    public void setAggregatetimespent(Object aggregatetimespent) {
        this.aggregatetimespent = aggregatetimespent;
    }

    public Object getAggregatetimespent() {
        return aggregatetimespent;
    }

    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }

    public Resolution getResolution() {
        return resolution;
    }

    public void setResolutiondate(String resolutiondate) {
        this.resolutiondate = resolutiondate;
    }

    public String getResolutiondate() {
        return resolutiondate;
    }

    public void setWorkratio(Object workratio) {
        this.workratio = workratio;
    }

    public Object getWorkratio() {
        return workratio;
    }

    public void setLastViewed(Object lastViewed) {
        this.lastViewed = lastViewed;
    }

    public Object getLastViewed() {
        return lastViewed;
    }

    public void setWatches(Watches watches) {
        this.watches = watches;
    }

    public Watches getWatches() {
        return watches;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreated() {
        return created;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setCustomfield10017(Object customfield10017) {
        this.customfield10017 = customfield10017;
    }

    public Object getCustomfield10017() {
        return customfield10017;
    }

    public void setTimeestimate(Object timeestimate) {
        this.timeestimate = timeestimate;
    }

    public Object getTimeestimate() {
        return timeestimate;
    }

    public void setAggregatetimeoriginalestimate(Object aggregatetimeoriginalestimate) {
        this.aggregatetimeoriginalestimate = aggregatetimeoriginalestimate;
    }

    public Object getAggregatetimeoriginalestimate() {
        return aggregatetimeoriginalestimate;
    }

    public void setVersions(List<VersionsItem> versions) {
        this.versions = versions;
    }

    public List<VersionsItem> getVersions() {
        return versions;
    }

    public void setIssuelinks(List<IssuelinksItem> issuelinks) {
        this.issuelinks = issuelinks;
    }

    public List<IssuelinksItem> getIssuelinks() {
        return issuelinks;
    }

    public void setAssignee(Assignee assignee) {
        this.assignee = assignee;
    }

    public Assignee getAssignee() {
        return assignee;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getUpdated() {
        return updated;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setComponents(List<ComponentsItem> components) {
        this.components = components;
    }

    public List<ComponentsItem> getComponents() {
        return components;
    }

    public void setTimeoriginalestimate(Object timeoriginalestimate) {
        this.timeoriginalestimate = timeoriginalestimate;
    }

    public Object getTimeoriginalestimate() {
        return timeoriginalestimate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setCustomfield10010(List<String> customfield10010) {
        this.customfield10010 = customfield10010;
    }

    public List<String> getCustomfield10010() {
        return customfield10010;
    }

    public void setCustomfield10011(String customfield10011) {
        this.customfield10011 = customfield10011;
    }

    public String getCustomfield10011() {
        return customfield10011;
    }

    public void setCustomfield10012(Object customfield10012) {
        this.customfield10012 = customfield10012;
    }

    public Object getCustomfield10012() {
        return customfield10012;
    }

    public void setCustomfield10013(String customfield10013) {
        this.customfield10013 = customfield10013;
    }

    public String getCustomfield10013() {
        return customfield10013;
    }

    public void setCustomfield10014(Object customfield10014) {
        this.customfield10014 = customfield10014;
    }

    public Object getCustomfield10014() {
        return customfield10014;
    }

    public void setTimetracking(Timetracking timetracking) {
        this.timetracking = timetracking;
    }

    public Timetracking getTimetracking() {
        return timetracking;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public Security getSecurity() {
        return security;
    }

    public void setCustomfield10008(Object customfield10008) {
        this.customfield10008 = customfield10008;
    }

    public Object getCustomfield10008() {
        return customfield10008;
    }

    public void setCustomfield10009(Object customfield10009) {
        this.customfield10009 = customfield10009;
    }

    public Object getCustomfield10009() {
        return customfield10009;
    }

    public void setAggregatetimeestimate(Object aggregatetimeestimate) {
        this.aggregatetimeestimate = aggregatetimeestimate;
    }

    public Object getAggregatetimeestimate() {
        return aggregatetimeestimate;
    }

    public void setAttachment(List<AttachmentItem> attachment) {
        this.attachment = attachment;
    }

    public List<AttachmentItem> getAttachment() {
        return attachment;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSummary() {
        return summary;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    public Creator getCreator() {
        return creator;
    }

    public void setSubtasks(List<SubtasksItem> subtasks) {
        this.subtasks = subtasks;
    }

    public List<SubtasksItem> getSubtasks() {
        return subtasks;
    }

    public void setReporter(Reporter reporter) {
        this.reporter = reporter;
    }

    public Reporter getReporter() {
        return reporter;
    }

    public void setAggregateprogress(Aggregateprogress aggregateprogress) {
        this.aggregateprogress = aggregateprogress;
    }

    public Aggregateprogress getAggregateprogress() {
        return aggregateprogress;
    }

    public void setCustomfield10000(String customfield10000) {
        this.customfield10000 = customfield10000;
    }

    public String getCustomfield10000() {
        return customfield10000;
    }

    public void setCustomfield10001(Object customfield10001) {
        this.customfield10001 = customfield10001;
    }

    public Object getCustomfield10001() {
        return customfield10001;
    }

    public void setCustomfield10004(Object customfield10004) {
        this.customfield10004 = customfield10004;
    }

    public Object getCustomfield10004() {
        return customfield10004;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }

    public String getDuedate() {
        return duedate;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    public Progress getProgress() {
        return progress;
    }

    public void setVotes(Votes votes) {
        this.votes = votes;
    }

    public Votes getVotes() {
        return votes;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Comment getComment() {
        return comment;
    }

    public void setWorklog(Worklog worklog) {
        this.worklog = worklog;
    }

    public Worklog getWorklog() {
        return worklog;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public Watcher getWatcher() {
        return watcher;
    }

    public void setWatcher(Watcher watcher) {
        this.watcher = watcher;
    }

    public String getCustomfield20000() {
        return customfield20000;
    }

    public void setCustomfield20000(String customfield20000) {
        this.customfield20000 = customfield20000;
    }

    public List<String> getCustomfield30000() {
        return customfield30000;
    }

    public void setCustomfield30000(List<String> customfield30000) {
        this.customfield30000 = customfield30000;
    }

    public String getCustomfield40000() {
        return customfield40000;
    }

    public void setCustomfield40000(String customfield40000) {
        this.customfield40000 = customfield40000;
    }

    public String getCustomfield50000() {
        return customfield50000;
    }

    public void setCustomfield50000(String customfield50000) {
        this.customfield50000 = customfield50000;
    }

    public String getCustomfield60000() {
        return customfield60000;
    }

    public void setCustomfield60000(String customfield60000) {
        this.customfield60000 = customfield60000;
    }

    public List<String> getCustomfield70000() {
        return customfield70000;
    }

    public void setCustomfield70000(List<String> customfield70000) {
        this.customfield70000 = customfield70000;
    }

    public Customfield80000 getCustomfield80000() {
        return customfield80000;
    }

    public void setCustomfield80000(Customfield80000 customfield80000) {
        this.customfield80000 = customfield80000;
    }

    @Override
    public String toString() {
        return
                "Fields{" +
                        "issuetype = '" + issuetype + '\'' +
                        ",timespent = '" + timespent + '\'' +
                        ",project = '" + project + '\'' +
                        ",fixVersions = '" + fixVersions + '\'' +
                        ",aggregatetimespent = '" + aggregatetimespent + '\'' +
                        ",resolution = '" + resolution + '\'' +
                        ",resolutiondate = '" + resolutiondate + '\'' +
                        ",workratio = '" + workratio + '\'' +
                        ",lastViewed = '" + lastViewed + '\'' +
                        ",watches = '" + watches + '\'' +
                        ",created = '" + created + '\'' +
                        ",priority = '" + priority + '\'' +
                        ",labels = '" + labels + '\'' +
                        ",customfield_10017 = '" + customfield10017 + '\'' +
                        ",timeestimate = '" + timeestimate + '\'' +
                        ",aggregatetimeoriginalestimate = '" + aggregatetimeoriginalestimate + '\'' +
                        ",versions = '" + versions + '\'' +
                        ",issuelinks = '" + issuelinks + '\'' +
                        ",assignee = '" + assignee + '\'' +
                        ",updated = '" + updated + '\'' +
                        ",status = '" + status + '\'' +
                        ",components = '" + components + '\'' +
                        ",timeoriginalestimate = '" + timeoriginalestimate + '\'' +
                        ",description = '" + description + '\'' +
                        ",customfield_10010 = '" + customfield10010 + '\'' +
                        ",customfield_10011 = '" + customfield10011 + '\'' +
                        ",customfield_10012 = '" + customfield10012 + '\'' +
                        ",customfield_10013 = '" + customfield10013 + '\'' +
                        ",customfield_10014 = '" + customfield10014 + '\'' +
                        ",timetracking = '" + timetracking + '\'' +
                        ",security = '" + security + '\'' +
                        ",customfield_10008 = '" + customfield10008 + '\'' +
                        ",customfield_10009 = '" + customfield10009 + '\'' +
                        ",aggregatetimeestimate = '" + aggregatetimeestimate + '\'' +
                        ",attachment = '" + attachment + '\'' +
                        ",summary = '" + summary + '\'' +
                        ",creator = '" + creator + '\'' +
                        ",subtasks = '" + subtasks + '\'' +
                        ",reporter = '" + reporter + '\'' +
                        ",aggregateprogress = '" + aggregateprogress + '\'' +
                        ",customfield_10000 = '" + customfield10000 + '\'' +
                        ",customfield_10001 = '" + customfield10001 + '\'' +
                        ",customfield_10004 = '" + customfield10004 + '\'' +
                        ",environment = '" + environment + '\'' +
                        ",duedate = '" + duedate + '\'' +
                        ",progress = '" + progress + '\'' +
                        ",votes = '" + votes + '\'' +
                        ",comment = '" + comment + '\'' +
                        ",worklog = '" + worklog + '\'' +
                        "}";
    }
}