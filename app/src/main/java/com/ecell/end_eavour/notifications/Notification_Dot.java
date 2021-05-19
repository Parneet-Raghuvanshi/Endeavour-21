package com.ecell.end_eavour.notifications;

public class Notification_Dot {

    public String dotStatus;
    public String userId;

    public Notification_Dot() {
    }

    public Notification_Dot(String dotStatus, String userId) {
        this.dotStatus = dotStatus;
        this.userId = userId;
    }

    public String getDotStatus() {
        return dotStatus;
    }

    public void setDotStatus(String dotStatus) {
        this.dotStatus = dotStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
