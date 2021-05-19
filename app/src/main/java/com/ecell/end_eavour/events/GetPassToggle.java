package com.ecell.end_eavour.events;

public class GetPassToggle {

    String getPassToggle;
    String hackathonLink;

    public GetPassToggle() {
    }

    public GetPassToggle(String getPassToggle, String hackathonLink) {
        this.getPassToggle = getPassToggle;
        this.hackathonLink = hackathonLink;
    }

    public String getGetPassToggle() {
        return getPassToggle;
    }

    public void setGetPassToggle(String getPassToggle) {
        this.getPassToggle = getPassToggle;
    }

    public String getHackathonLink() {
        return hackathonLink;
    }

    public void setHackathonLink(String hackathonLink) {
        this.hackathonLink = hackathonLink;
    }
}
