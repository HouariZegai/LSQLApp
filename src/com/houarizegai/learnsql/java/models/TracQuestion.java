package com.houarizegai.learnsql.java.models;

public class TracQuestion {

    /* This class using in traceability part exact in All Student Status
       model for the table of question with status
     */

    private String questionType;
    private int questionNumber;
    private int nSyntaxError;
    private int nSemanticError;
    private int nAnalyseError;
    private int nSolved;
    private int nUnsolved;
    private double solvedPercentage;

    public TracQuestion(String questionType, int questionNumber, int nSyntaxError, int nSemanticError, int nAnalyseError, int nSolved, int nUnsolved, double solvedPercentage) {
        this.questionType = questionType;
        this.questionNumber = questionNumber;
        this.nSyntaxError = nSyntaxError;
        this.nSemanticError = nSemanticError;
        this.nAnalyseError = nAnalyseError;
        this.nSolved = nSolved;
        this.nUnsolved = nUnsolved;
        this.solvedPercentage = solvedPercentage;
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

    public int getnSolved() {
        return nSolved;
    }

    public void setnSolved(int nSolved) {
        this.nSolved = nSolved;
    }

    public int getnUnsolved() {
        return nUnsolved;
    }

    public void setnUnsolved(int nUnsolved) {
        this.nUnsolved = nUnsolved;
    }

    public double getSolvedPercentage() {
        return solvedPercentage;
    }

    public void setSolvedPercentage(double solvedPercentage) {
        this.solvedPercentage = solvedPercentage;
    }

}
