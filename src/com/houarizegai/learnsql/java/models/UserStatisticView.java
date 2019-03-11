package com.houarizegai.learnsql.java.models;

// This class using in Trac Part => view statistic about user
public class UserStatisticView {

    String questionType;
    int questionNumber;
    int nSyntaxError;
    int nSemanticError;
    int nAnalyseError;
    boolean solved;

    public UserStatisticView(String questionType, int questionNumber, int nSyntaxError, int nSemanticError, int nAnalyseError, boolean solved) {
        this.questionType = questionType;
        this.questionNumber = questionNumber;
        this.nSyntaxError = nSyntaxError;
        this.nSemanticError = nSemanticError;
        this.nAnalyseError = nAnalyseError;
        this.solved = solved;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public int getnSyntaxError() {
        return nSyntaxError;
    }

    public void setnSyntaxError(int nSyntaxError) {
        this.nSyntaxError = nSyntaxError;
    }

    public int getnSemanticError() {
        return nSemanticError;
    }

    public void setnSemanticError(int nSemanticError) {
        this.nSemanticError = nSemanticError;
    }

    public int getnAnalyseError() {
        return nAnalyseError;
    }

    public void setnAnalyseError(int nAnalyseError) {
        this.nAnalyseError = nAnalyseError;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public void printInfo() {
        System.out.println("---- Trac User View -------");
        System.out.println("Question Type : " + this.questionType);
        System.out.println("Question Number: " + this.questionNumber);
        System.out.println("nSyntaxError: " + this.nSyntaxError);
        System.out.println("nSemanticError: " + this.nSemanticError);
        System.out.println("nAnalyseError: " + this.nAnalyseError);
        System.out.println("Solved: " + this.solved);

        System.out.println("-------------------------");

    }

}
