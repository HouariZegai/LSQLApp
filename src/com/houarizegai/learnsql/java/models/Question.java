package com.houarizegai.learnsql.java.models;

public class Question { // Question Model

    private int id;
    private String questionType;
    private String question;
    private String answer;

    public Question(int id, String questionType, String question, String answer) {
        this.id = id;
        this.questionType = questionType;
        this.question = question;
        this.answer = answer;
    }

    public Question() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
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
