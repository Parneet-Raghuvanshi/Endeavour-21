package com.ecell.end_eavour.events.detail;

public class EventsRound_Model {

    String title;
    String content;
    String heading;

    public EventsRound_Model() {
    }

    public EventsRound_Model(String title, String content, String heading) {
        this.title = title;
        this.content = content;
        this.heading = heading;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }
}
