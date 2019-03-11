package com.houarizegai.learnsql.java.dao;

import static com.houarizegai.learnsql.java.controllers.ExerciseController.questionCurrent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeywordsDao {
    
    public List<String> getHelpKeyword() {
        List<String> hintKeyword = new ArrayList<>();
        
        Connection con = new MysqlConnection().getConnection();
        if(con == null)
            return null;
        
        String sql = "SELECT help_key FROM keywords;";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while(rs.next()) {
                hintKeyword.add(rs.getString("help_key"));
            }
            
        } catch(SQLException se) {
            //System.out.println("Error msg: " + se.getMessage());
        }
        
        return hintKeyword;
    }

    public Map<Integer, String[]> getKeyword(String sqlOfUser) {
        Map<Integer, String[]> keyword = new HashMap<>(); // This map contain list of sql key => natural hint
        
        Connection con = new MysqlConnection().getConnection();
        if(con == null)
            return null;
        
        String sql = "SELECT sql_key, natural_hint FROM keywords WHERE ? LIKE CONCAT('%',sql_key,'%') AND "
                + "? NOT LIKE CONCAT('%',sql_key,'%');";
        try {
            PreparedStatement prest = con.prepareStatement(sql);
            prest.setString(1, questionCurrent.getAnswer());
            prest.setString(2, sqlOfUser);
            ResultSet rs = prest.executeQuery();
            int i = 0;
            while(rs.next()) {
                keyword.put(i++, new String[]{rs.getString("sql_key"), rs.getString("natural_hint")});
            }
            
        } catch(SQLException se) {
            //System.out.println("Error msg: " + se.getMessage());
        }
        
        return keyword;
    }

}
