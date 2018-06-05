package com.stampbot.model.transitionRequestModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtraData {

    @JsonProperty("goes")
    private String goes;

    @JsonProperty("keyvalue")
    private String keyvalue;

    public String getGoes() {
        return goes;
    }

    public void setGoes(String goes) {
        this.goes = goes;
    }

    public String getKeyvalue() {
        return keyvalue;
    }

    public void setKeyvalue(String keyvalue) {
        this.keyvalue = keyvalue;
    }

    @Override
    public String toString() {
        return
                "ExtraData{" +
                        "goes = '" + goes + '\'' +
                        ",keyvalue = '" + keyvalue + '\'' +
                        "}";
    }
}