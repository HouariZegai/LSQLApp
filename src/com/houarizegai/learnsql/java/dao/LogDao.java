package com.houarizegai.learnsql.java.dao;

import com.houarizegai.learnsql.java.models.UserInformation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class LogDao {

    public void setLog() {
        Connection con = new MysqlConnection().getConnection();
        if (con == null) {
            return;
        }
        String sql = "INSERT INTO `log` (`date_login`, `date_logout`, `id_user`) VALUES(?, ?, ?);";
        try {
            PreparedStatement pstat = con.prepareStatement(sql);
            pstat.setString(1, UserInformation.dateLogin);
            pstat.setString(2, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
            pstat.setInt(3, UserInformation.id);
            int stat = pstat.executeUpdate();
        } catch (SQLException se) {
            //System.out.println("Error Code: " + se.getErrorCode());
            //System.out.println("Massage : " + se.getMessage());
            //se.printStackTrace();
        }
    }
}
