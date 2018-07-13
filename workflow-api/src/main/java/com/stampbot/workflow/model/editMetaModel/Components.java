package com.stampbot.workflow.model.editMetaModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Components {

    @JsonProperty("schema")
    private Schema schema;

    @JsonProperty("allowedValues")
    private List<Object> allowedValues;

    @JsonProperty("operations")
    private List<String> operations;

    @JsonProperty("name")
    private String name;

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

    public List<Object> getAllowedValues() {
        return allowedValues;
    }

    public void setAllowedValues(List<Object> allowedValues) {
        this.allowedValues = allowedValues;
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
                "Components{" +
                        "schema = '" + schema + '\'' +
                        ",allowedValues = '" + allowedValues + '\'' +
                        ",operations = '" + operations + '\'' +
                        ",name = '" + name + '\'' +
                        ",required = '" + required + '\'' +
                        ",key = '" + key + '\'' +
                        "}";
    }
}