package com.ecell.end_eavour.events.ragstoriches;

public class RagProfile {

    public String credit;
    public String userId;
    public String userName;

    public RagProfile() {
    }

    public RagProfile(String credit, String userId, String userName) {
        this.credit = credit;
        this.userId = userId;
        this.userName = userName;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
