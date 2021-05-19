package com.ecell.end_eavour.events.faq;

public class EventsFaq_Model {

    public String question;
    public String answer;

    public EventsFaq_Model() {
    }

    public EventsFaq_Model(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
