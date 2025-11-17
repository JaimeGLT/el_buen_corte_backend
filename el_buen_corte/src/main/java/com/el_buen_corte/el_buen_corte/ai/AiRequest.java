package com.el_buen_corte.el_buen_corte.ai;

public class AiRequest {
    private String question;

    public AiRequest() {
    }

    public AiRequest(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
