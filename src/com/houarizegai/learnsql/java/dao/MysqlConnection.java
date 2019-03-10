package com.houarizegai.learnsql.java.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlConnection {
    
    private final static String HOST_NAME = "127.0.0.1";
    private final static String PORT_NUMBER = "3306";
    private final static String DB_NAME = "learn_sql_db";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "";

    public static Connection connectionSystem = null;

    public Connection getConnection() {
        return getConnection(HOST_NAME, PORT_NUMBER, DB_NAME, USERNAME, PASSWORD);
    }

    public Connection getConnection(String HOST_NAME, String PORT_NUMBER, String DB_NAME, String userName, String PASSWORD) {
        try {
            Class.forName("com.mysql.jdbc.Driver"); // 
            connectionSystem = DriverManager.getConnection("jdbc:mysql://" + HOST_NAME + ((PORT_NUMBER.isEmpty()) ? "" : ":" + PORT_NUMBER) + "/" + DB_NAME, userName, PASSWORD);
        } catch (SQLException ex) {
            //System.out.println("Connection Error !");
            return null;
        } catch (ClassNotFoundException ex) {
            //System.out.println("Driver not found Error !");
            return null;
        } finally {
            if (connectionSystem == null) {
                //System.out.println("Connection Failed !");
            }
        }

        return connectionSystem;
    }

    public Connection getConnectionExercise(String DB_NAME) {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/" + DB_NAME, "root", "");
        } catch (SQLException ex) {
            //System.out.println("Connection Error !");
            return null;
        } catch (ClassNotFoundException ex) {
            //System.out.println("Driver not found Error !");
            return null;
        } finally {
            if (con == null) {
                //System.out.println("Connection Other DB Failed !");
            }
        }

        return con;
    }

}
