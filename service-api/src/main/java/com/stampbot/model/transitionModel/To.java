package com.stampbot.model.transitionModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class To {

    @JsonProperty("name")
    private String name;

    @JsonProperty("self")
    private String self;

    @JsonProperty("description")
    private String description;

    @JsonProperty("iconUrl")
    private String iconUrl;

    @JsonProperty("id")
    private String id;

    @JsonProperty("statusCategory")
    private StatusCategory statusCategory;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public StatusCategory getStatusCategory() {
        return statusCategory;
    }

    public void setStatusCategory(StatusCategory statusCategory) {
        this.statusCategory = statusCategory;
    }

    @Override
    public String toString() {
        return
                "To{" +
                        "name = '" + name + '\'' +
                        ",self = '" + self + '\'' +
                        ",description = '" + description + '\'' +
                        ",iconUrl = '" + iconUrl + '\'' +
                        ",id = '" + id + '\'' +
                        ",statusCategory = '" + statusCategory + '\'' +
                        "}";
    }
}