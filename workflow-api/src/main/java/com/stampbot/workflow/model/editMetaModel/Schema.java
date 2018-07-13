package com.stampbot.workflow.model.editMetaModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Schema {

    @JsonProperty("system")
    private String system;

    @JsonProperty("type")
    private String type;

    @JsonProperty("items")
    private String items;

    @JsonProperty("custom")
    private String custom;

    @JsonProperty("customId")
    private int customId;

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }

    public int getCustomId() {
        return customId;
    }

    public void setCustomId(int customId) {
        this.customId = customId;
    }

    @Override
    public String toString() {
        return
                "Schema{" +
                        "system = '" + system + '\'' +
                        ",type = '" + type + '\'' +
                        ",items = '" + items + '\'' +
                        ",custom = '" + custom + '\'' +
                        ",customId = '" + customId + '\'' +
                        "}";
    }
}