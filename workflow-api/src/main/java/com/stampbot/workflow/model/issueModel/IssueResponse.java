package com.stampbot.workflow.model.issueModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IssueResponse {

    @JsonProperty("schema")
    private Schema schema;

    @JsonProperty("expand")
    private String expand;

    @JsonProperty("names")
    private Names names;

    @JsonProperty("self")
    private String self;

    @JsonProperty("id")
    private String id;

    @JsonProperty("fields")
    private Fields fields;

    @JsonProperty("key")
    private String key;

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setExpand(String expand) {
        this.expand = expand;
    }

    public String getExpand() {
        return expand;
    }

    public void setNames(Names names) {
        this.names = names;
    }

    public Names getNames() {
        return names;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getSelf() {
        return self;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }

    public Fields getFields() {
        return fields;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return
                "IssueResponse{" +
                        "schema = '" + schema + '\'' +
                        ",expand = '" + expand + '\'' +
                        ",names = '" + names + '\'' +
                        ",self = '" + self + '\'' +
                        ",id = '" + id + '\'' +
                        ",fields = '" + fields + '\'' +
                        ",key = '" + key + '\'' +
                        "}";
    }
}