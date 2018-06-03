package com.stampbot.model.issueModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AvatarUrls {

    @JsonProperty("48x48")
    private String jsonMember48x48;

    @JsonProperty("24x24")
    private String jsonMember24x24;

    @JsonProperty("16x16")
    private String jsonMember16x16;

    @JsonProperty("32x32")
    private String jsonMember32x32;

    public void setJsonMember48x48(String jsonMember48x48) {
        this.jsonMember48x48 = jsonMember48x48;
    }

    public String getJsonMember48x48() {
        return jsonMember48x48;
    }

    public void setJsonMember24x24(String jsonMember24x24) {
        this.jsonMember24x24 = jsonMember24x24;
    }

    public String getJsonMember24x24() {
        return jsonMember24x24;
    }

    public void setJsonMember16x16(String jsonMember16x16) {
        this.jsonMember16x16 = jsonMember16x16;
    }

    public String getJsonMember16x16() {
        return jsonMember16x16;
    }

    public void setJsonMember32x32(String jsonMember32x32) {
        this.jsonMember32x32 = jsonMember32x32;
    }

    public String getJsonMember32x32() {
        return jsonMember32x32;
    }

    @Override
    public String toString() {
        return
                "AvatarUrls{" +
                        "48x48 = '" + jsonMember48x48 + '\'' +
                        ",24x24 = '" + jsonMember24x24 + '\'' +
                        ",16x16 = '" + jsonMember16x16 + '\'' +
                        ",32x32 = '" + jsonMember32x32 + '\'' +
                        "}";
    }
}