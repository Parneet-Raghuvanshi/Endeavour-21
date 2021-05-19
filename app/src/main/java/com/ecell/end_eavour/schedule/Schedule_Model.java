package com.ecell.end_eavour.schedule;

public class Schedule_Model {

    public String eventTitle;
    public String eventDesc;
    public String eventVenue;
    public String eventTime;
    public String eventIcon;

    public Schedule_Model() {
    }

    public Schedule_Model(String eventTitle, String eventDesc, String eventVenue, String eventTime, String eventIcon) {
        this.eventTitle = eventTitle;
        this.eventDesc = eventDesc;
        this.eventVenue = eventVenue;
        this.eventTime = eventTime;
        this.eventIcon = eventIcon;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public String getEventVenue() {
        return eventVenue;
    }

    public void setEventVenue(String eventVenue) {
        this.eventVenue = eventVenue;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventIcon() {
        return eventIcon;
    }

    public void setEventIcon(String eventIcon) {
        this.eventIcon = eventIcon;
    }
}
