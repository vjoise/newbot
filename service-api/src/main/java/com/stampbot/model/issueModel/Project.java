package com.stampbot.model.issueModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Project {

    @JsonProperty("avatarUrls")
    private AvatarUrls avatarUrls;

    @JsonProperty("projectCategory")
    private ProjectCategory projectCategory;

    @JsonProperty("avatarURI")
    private String avatarURI;

    @JsonProperty("projectTypeKey")
    private String projectTypeKey;

    @JsonProperty("issuetypes")
    private List<Issuetype> issuetypes;

    @JsonProperty("name")
    private String name;

    @JsonProperty("self")
    private String self;

    @JsonProperty("id")
    private String id;

    @JsonProperty("projectId")
    private String projectId;

    @JsonProperty("key")
    private String key;

    public AvatarUrls getAvatarUrls() {
        return avatarUrls;
    }

    public void setAvatarUrls(AvatarUrls avatarUrls) {
        this.avatarUrls = avatarUrls;
    }

    public ProjectCategory getProjectCategory() {
        return projectCategory;
    }

    public void setProjectCategory(ProjectCategory projectCategory) {
        this.projectCategory = projectCategory;
    }

    public List<Issuetype> getIssuetypes() {
        return issuetypes;
    }

    public void setIssuetypes(List<Issuetype> issuetypes) {
        this.issuetypes = issuetypes;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getProjectTypeKey() {
        return projectTypeKey;
    }

    public void setProjectTypeKey(String projectTypeKey) {
        this.projectTypeKey = projectTypeKey;
    }

    public String getAvatarURI() {
        return avatarURI;
    }

    public void setAvatarURI(String avatarURI) {
        this.avatarURI = avatarURI;
    }

    @Override
    public String toString() {
        return "Project{" +
                "avatarUrls=" + avatarUrls +
                ", projectCategory=" + projectCategory +
                ", avatarURI='" + avatarURI + '\'' +
                ", projectTypeKey='" + projectTypeKey + '\'' +
                ", issuetypes=" + issuetypes +
                ", name='" + name + '\'' +
                ", self='" + self + '\'' +
                ", id='" + id + '\'' +
                ", projectId='" + projectId + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}