package com.stampbot.model.issueModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AttachmentItem {

    @JsonProperty("thumbnail")
    private String thumbnail;

    @JsonProperty("filename")
    private String filename;

    @JsonProperty("size")
    private int size;

    @JsonProperty("author")
    private Author author;

    @JsonProperty("created")
    private String created;

    @JsonProperty("self")
    private String self;

    @JsonProperty("mimeType")
    private String mimeType;

    @JsonProperty("content")
    private String content;

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Author getAuthor() {
        return author;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreated() {
        return created;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getSelf() {
        return self;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return
                "AttachmentItem{" +
                        "thumbnail = '" + thumbnail + '\'' +
                        ",filename = '" + filename + '\'' +
                        ",size = '" + size + '\'' +
                        ",author = '" + author + '\'' +
                        ",created = '" + created + '\'' +
                        ",self = '" + self + '\'' +
                        ",mimeType = '" + mimeType + '\'' +
                        ",content = '" + content + '\'' +
                        "}";
    }
}