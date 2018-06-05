package com.stampbot.model.editMetaModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Labels {

    @JsonProperty("schema")
    private Schema schema;

    @JsonProperty("operations")
    private List<String> operations;

    @JsonProperty("name")
    private String name;

    @JsonProperty("autoCompleteUrl")
    private String autoCompleteUrl;

    @JsonProperty("required")
    private boolean required;

    @JsonProperty("key")
    private String key;

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public List<String> getOperations() {
        return operations;
    }

    public void setOperations(List<String> operations) {
        this.operations = operations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAutoCompleteUrl() {
        return autoCompleteUrl;
    }

    public void setAutoCompleteUrl(String autoCompleteUrl) {
        this.autoCompleteUrl = autoCompleteUrl;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return
                "Labels{" +
                        "schema = '" + schema + '\'' +
                        ",operations = '" + operations + '\'' +
                        ",name = '" + name + '\'' +
                        ",autoCompleteUrl = '" + autoCompleteUrl + '\'' +
                        ",required = '" + required + '\'' +
                        ",key = '" + key + '\'' +
                        "}";
    }
}