package com.ecell.end_eavour.notifications;

public class Notifications_Model {

    String title;
    String body;
    String date;
    String id;

    public Notifications_Model() {
    }

    public Notifications_Model(String title, String body, String date, String id) {
        this.title = title;
        this.body = body;
        this.date = date;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
