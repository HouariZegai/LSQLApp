package com.houarizegai.learnsql.java.models;

import java.sql.Date;

public class User {
    // This model using in manage account
    
    private int id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String email;
    private boolean isTeacher;
    private int section;
    private int group;

    public User(int id, String username, String password, String firstName, String lastName, Date dateOfBirth, String email, boolean isTeacher, int section, int group) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.isTeacher = isTeacher;
        this.section = section;
        this.group = group;
    }

    public User() {

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIsTeacher(boolean isTeacher) {
        this.isTeacher = isTeacher;
    }

    public boolean getIsTeacher() {
        return isTeacher;
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

    public void printInfo() {
        System.out.println("-------------------------");
        System.out.println("Id: " + this.id);
        System.out.println("Username: " + this.username);
        System.out.println("First Name: " + this.firstName);
        System.out.println("Last Name: " + this.lastName);
        System.out.println("Password: " + this.password);
        System.out.println("Date of birth: " + this.dateOfBirth.toString());
        System.out.println("Email: " + this.email);
        System.out.println("Is Teacher: " + this.isTeacher);
        System.out.println("Section: " + this.section);
        System.out.println("Group: " + this.group);
        System.out.println("-------------------------");

    }
}
