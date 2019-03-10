package com.houarizegai.learnsql.java.dao;

import com.houarizegai.learnsql.java.models.User;
import com.houarizegai.learnsql.java.models.UserInformation;
import com.houarizegai.learnsql.java.security.MD5Hashing;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDao {

    public void checkUsernameAndPassword(String user, String pass) {

        UserInformation.id = -1;
        if (!user.matches("[a-zA-Z0-9_]+") || pass.length() < 4) {
            return;
        }

        String sql = "SELECT `id_user`, `is_teacher` FROM `users` WHERE username=? AND password =?;";
        Connection con = null;
        PreparedStatement prest = null;

        try {
            con = new MysqlConnection().getConnection();

            if (con == null) {
                UserInformation.id = 0;
                return;
            }

            prest = null;
            prest = con.prepareStatement(sql);
            prest.setString(1, user);
            prest.setString(2, MD5Hashing.getHash(pass));
            ResultSet rs = prest.executeQuery();

            if (rs.next()) {
                UserInformation.id = rs.getInt("id_user");
                UserInformation.isTeacher = rs.getBoolean("is_teacher");
            }

        } catch (SQLException ex) {
            ////System.out.println("Error msg: " + ex.getMessage());
            //ex.printStackTrace();
            // user not found & return null
        } finally {
            try {
                if (prest != null) {
                    prest.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public List<User> getUsers() {
        Connection con = new MysqlConnection().getConnection();
        if (con == null) {
            return null;
        }

        String sql = "SELECT * FROM `users`";
        List<User> users = new ArrayList<>();

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                users.add(new User(rs.getInt("id_user"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("date_of_birth"),
                        rs.getString("email"),
                        rs.getBoolean("is_teacher"),
                        rs.getInt("section"),
                        rs.getInt("group")
                ));
            }

        } catch (SQLException ex) {
            //Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
            ////System.out.println("Error SQL in Manage Account Dao");
            return null;
        }

        return users;
    }

    public int addUser(User user) { // Add user to DB
        StringBuilder sql = new StringBuilder("INSERT INTO `users` (`username`, `password`, `first_name`, `last_name`, `date_of_birth`, `email`, `is_teacher`");
        Connection con = null;
        Statement st = null;

        try {
            con = new MysqlConnection().getConnection();

            if (con == null) {
                return -1;
            }
            
            if (isExistsUser(user.getUsername()))
                return 2;
                
            st = con.createStatement();
            if (!user.getIsTeacher()) {
                sql.append(", `section`, `group`");
            }

            sql.append(") values ('").append(user.getUsername()).append("', '");
            sql.append(MD5Hashing.getHash(user.getPassword())).append("', '");
            sql.append(user.getFirstName()).append("', '");
            sql.append(user.getLastName()).append("', '");
            sql.append(user.getDateOfBirth().toString()).append("', '");
            sql.append(user.getEmail()).append("', ");
            sql.append((user.getIsTeacher()) ? 1 : 0);

            if (!user.getIsTeacher()) {
                sql.append(", ").append(user.getSection()).append(", ").append(user.getGroup());
            }

            sql.append(");");

            int n = st.executeUpdate(sql.toString());
        } catch (SQLException ex) {
            ////System.out.println("SQLException msg: " + ex.getMessage());
            //ex.printStackTrace();
            return 0;
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 1;
    }

    public int editUser(User user) { // Edit user to DB

        StringBuilder sql = new StringBuilder("UPDATE `users` SET ");
        Connection con = null;
        Statement st = null;

        try {
            con = new MysqlConnection().getConnection();

            if (con == null) {
                return -1;
            }
            
            if (isExistsUser(user.getUsername()))
                return 2;
              
            
            st = con.createStatement();

            sql.append("`username`='").append(user.getUsername());
            sql.append("', `first_name`='").append(user.getFirstName());
            sql.append("', `last_name`='").append(user.getLastName());
            sql.append("', `date_of_birth`='").append(user.getDateOfBirth().toString());
            sql.append("', `email`='").append(user.getEmail());
            sql.append("', `is_teacher`=").append((user.getIsTeacher()) ? 1 : 0);

            if (user.getIsTeacher()) {
                sql.append(", `section`=0, `group`=0");
            } else {
                sql.append(", `section`=").append(user.getSection()).append(", `group`=").append(user.getGroup());
            }

            sql.append(" WHERE `id_user`=").append(user.getId()).append(";");
            user.printInfo();

            int status = st.executeUpdate(sql.toString());
            //System.out.println("Status : " + status);
        } catch (SQLException ex) {
            //System.out.println("SQL Exception code: " + ex.getErrorCode());
            //ex.printStackTrace();
            return 0;
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 1;
    }

    public int deleteUser(String id) {

        Connection con = new MysqlConnection().getConnection();
        if (con == null) // if connection failed
        {
            return -1;
        }
        Statement st = null;

        try {
            st = con.createStatement();
            String sql = "DELETE FROM `do_exercise` WHERE `id_user`=" + id + ";";
            st.executeUpdate(sql);
            sql = "DELETE FROM `log` WHERE `id_user`=" + id + ";";
            st.executeUpdate(sql);
            sql = "DELETE FROM `users` WHERE `id_user`=" + id + ";";
            st.executeUpdate(sql);
            return 1;
        } catch (SQLException e) {
            //System.out.println("Error : " + e.getErrorCode());
            //e.printStackTrace();
            return 0;
        }
    }

    public boolean isExistsUser(String username) {
        Connection con = new MysqlConnection().getConnection();
        if (con == null) // if connection failed
        {
            return true;
        }
        String sql = "SELECT * FROM `users` WHERE username=?;";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true; // Username already exists
            }
            return false;
        } catch(SQLException se) {
            System.out.println("Error msg: " + se.getMessage());
            return true;
        }
    }

    public int changeUsername(String newUsername, String password) {
        Connection con = new MysqlConnection().getConnection();
        if (con == null) // if connection failed
        {
            return -1;
        }

        try {
            String sql = "SELECT * FROM `users` WHERE username=?;";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, newUsername);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return -2; // Username already exists
            }
            sql = "UPDATE `users` SET `username`=? WHERE `id_user`=? AND `password`=?;";
            PreparedStatement prest = con.prepareStatement(sql);
            prest.setString(1, newUsername);
            prest.setInt(2, UserInformation.id);
            prest.setString(3, MD5Hashing.getHash(password));
            return prest.executeUpdate();

        } catch (SQLException e) {
            //System.out.println("Error : " + e.getErrorCode());
            //e.printStackTrace();
            return -1;
        }
    }

    public int changeEmail(String newEmail, String password) {
        Connection con = new MysqlConnection().getConnection();
        if (con == null) // if connection failed
        {
            return -1;
        }

        try {
            String sql = "SELECT * FROM `users` WHERE email=?;";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, newEmail);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return -2; // Username already exists
            }

            sql = "UPDATE `users` SET `email`=? WHERE `id_user`=? AND `password`=?;";
            PreparedStatement prest = con.prepareStatement(sql);
            prest.setString(1, newEmail);
            prest.setInt(2, UserInformation.id);
            prest.setString(3, MD5Hashing.getHash(password));
            return prest.executeUpdate();

        } catch (SQLException e) {
            //System.out.println("Error Code : " + e.getErrorCode());
            //System.out.println("Error Msg : " + e.getMessage());
            return -1;
        }
    }

    public int changePassword(String oldPassword, String newPassword) {
        Connection con = new MysqlConnection().getConnection();
        if (con == null) // if connection failed
        {
            return -1;
        }

        try {
            String sql = "UPDATE `users` SET `password`=? WHERE `id_user`=? AND `password`=?;";
            PreparedStatement prest = con.prepareStatement(sql);
            prest.setString(1, MD5Hashing.getHash(newPassword));
            prest.setInt(2, UserInformation.id);
            prest.setString(3, MD5Hashing.getHash(oldPassword));
            return prest.executeUpdate();

        } catch (SQLException e) {
            //System.out.println("Error msg : " + e.getMessage());
            //e.printStackTrace();
            return -1;
        }
    }

    public Map<String, List<String>> getSectionGroupTools() {
        // This function get Section and Group using in Tools Option (advanced search)
        Map<String, List<String>> sectionGroup = new HashMap<>();

        // get section
        String sql = "SELECT DISTINCT section from users WHERE section IS NOT NULL ORDER BY section;";
        Connection con = new MysqlConnection().getConnection();
        if (con == null) {
            return null;
        }
        List<Integer> section = new ArrayList<>();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                section.add(rs.getInt("section"));
            }

            sql = "SELECT DISTINCT `group` from users WHERE section=? ORDER BY `group`;";
            List<String> group = null;
            for (int sec : section) {
                PreparedStatement prest = con.prepareStatement(sql);
                prest.setInt(1, sec);
                ResultSet rsG = prest.executeQuery();
                group = new ArrayList<>();
                while (rsG.next()) {
                    group.add(rsG.getInt("group") + "");
                }

                sectionGroup.put(sec + "", group);
            }

        } catch (SQLException ex) {
            //System.out.println("Error msg : " + ex.getMessage());
        }

        return sectionGroup;
    }

}
