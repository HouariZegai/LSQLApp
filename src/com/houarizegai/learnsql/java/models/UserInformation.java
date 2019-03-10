package com.houarizegai.learnsql.java.models;

public class UserInformation { // This Class Containe the information of the user entered in the system

    public static int id; // this variable save the id of the user (for known the user active)
    public static boolean isTeacher; // this variable using for the prevélège
    public static String dateLogin; // this variable save the date & time of login in the DB
    public static int numberSyntaxE = 0; // number syntax error using for help student (show hint)
    public static int numberSemanticE = 0; // number semantic error using for help student (show hint)
    public static int numberAnalyseE = 0; // number analyse error using for help student (show hint)

}
