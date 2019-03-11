package com.houarizegai.learnsql.java.models;

import java.sql.Date;

public class TracUser {

    private int id;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private int section;
    private int group;
    private int numberSyntaxError; // question error syntaxique
    private int numberSemanticError; // question error symontique
    private int numberAnalyseError; // question error analyse
    private int numberSolved; // question error analyse
    private int numberUnSolved; // question error analyse

    public TracUser() {
        
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getNumberSyntaxError() {
        return numberSyntaxError;
    }

    public void setNumberSyntaxError(int numberSyntaxError) {
        this.numberSyntaxError = numberSyntaxError;
    }

    public int getNumberSemanticError() {
        return numberSemanticError;
    }

    public void setNumberSemanticError(int numberSemanticError) {
        this.numberSemanticError = numberSemanticError;
    }

    public int getNumberAnalyseError() {
        return numberAnalyseError;
    }

    public void setNumberAnalyseError(int numberAnalyseError) {
        this.numberAnalyseError = numberAnalyseError;
    }

    public int getNumberSolved() {
        return numberSolved;
    }

    public void setNumberSolved(int numberSolved) {
        this.numberSolved = numberSolved;
    }

    public int getNumberUnSolved() {
        return numberUnSolved;
    }

    public void setNumberUnSolved(int numberUnSolved) {
        this.numberUnSolved = numberUnSolved;
    }

    public void printInfo() {
        System.out.println("---- Trac User -------");
        System.out.println("Id: " + this.id);
        System.out.println("First Name: " + this.firstName);
        System.out.println("Last Name: " + this.lastName);
        System.out.println("Date of birth: " + this.dateOfBirth.toString());
        System.out.println("Section: " + this.section);
        System.out.println("Group: " + this.group);
        System.out.println("N° Error Syntax: " + this.numberSyntaxError);
        System.out.println("N° Error Symontique: " + this.numberSemanticError);
        System.out.println("N° Error Analyse: " + this.numberAnalyseError);
        System.out.println("N° Solved: " + this.numberSolved);
        System.out.println("N° Un Solved: " + this.numberUnSolved);
        System.out.println("-------------------------");

    }
}
