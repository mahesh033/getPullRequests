package com.gitpullrequesttracker.model;

/**
 * Repo
 *
 * @author Mahesh
 */
public class Repo {

    private String id;
    private String title;
    private String html_url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHtmlUrl() {
        return html_url;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.html_url = htmlUrl;
    }
}