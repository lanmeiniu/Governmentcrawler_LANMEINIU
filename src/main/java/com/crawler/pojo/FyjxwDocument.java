package com.crawler.pojo;

import java.util.Date;

public class FyjxwDocument {
    private Integer id;

    private String title;

    private String url;

    private Date date;

    private String editor;

    private String urlAttachment;

    private Date createTime;

    private Date updateTime;

    private String content;

    public FyjxwDocument(Integer id, String title, String url, Date date, String editor, String urlAttachment, Date createTime, Date updateTime, String content) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.date = date;
        this.editor = editor;
        this.urlAttachment = urlAttachment;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.content = content;
    }

    public FyjxwDocument() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor == null ? null : editor.trim();
    }

    public String getUrlAttachment() {
        return urlAttachment;
    }

    public void setUrlAttachment(String urlAttachment) {
        this.urlAttachment = urlAttachment == null ? null : urlAttachment.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}