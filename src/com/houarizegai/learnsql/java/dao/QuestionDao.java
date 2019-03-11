package com.houarizegai.learnsql.java.dao;

import com.houarizegai.learnsql.java.models.Question;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuestionDao {

    public List<Question> getQuestion() {
        // This function return the question a traver database type
        String sql = "SELECT `id_question`, `type_question`, `question`, `answer` FROM `question`;";

        List<Question> questions = new ArrayList<>();

        Connection con = null;
        Statement statement = null;

        try {
            con = new MysqlConnection().getConnection();

            if (con == null) {
                return null;
            }
            statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                questions.add(new Question(rs.getInt("id_question"), rs.getString("type_question"),
                        rs.getString("question"), rs.getString("answer")));
            }

        } catch (SQLException ex) {
            ////System.out.println("Error Code: " + ex.getErrorCode());
            //Logger.getLogger(ExerciseDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException ex) {
                //Logger.getLogger(ExerciseDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return questions;
    }

    public int addQuestion(Question question) { // Add user to DB

        Connection con = null;
        PreparedStatement prest = null;
        String sql = "INSERT INTO `question` (`id_question`, `type_question`, `question`, `answer`) VALUES (?, ?, ?, ?)";
        try {
            con = new MysqlConnection().getConnection();

            if (con == null) {
                return -1;
            }
            prest = con.prepareStatement(sql);

            prest.setInt(1, question.getId());
            prest.setString(2, question.getQuestionType());
            prest.setString(3, question.getQuestion());
            prest.setString(4, question.getAnswer());
            prest.executeUpdate();

        } catch (SQLException ex) {
            //System.out.println("SQL Exception code: " + ex.getErrorCode());
            //ex.printStackTrace();
            return 0;
        } finally {
            try {
                if (prest != null) {
                    prest.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                //Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 1;
    }

    public int editQuestion(Question question) { // Edit user to DB

        Connection con = null;
        PreparedStatement prest = null;

        try {
            con = new MysqlConnection().getConnection();

            if (con == null) {
                return -1;
            }
            String sql = "UPDATE `question` SET `type_question`=?, `question`=?, `answer`=? WHERE `id_question`=?";

            prest = con.prepareStatement(sql);
            prest.setString(1, question.getQuestionType());
            prest.setString(2, question.getQuestion());
            prest.setString(3, question.getAnswer());
            prest.setInt(4, question.getId());
            prest.executeUpdate();
        } catch (SQLException ex) {
            //System.out.println("SQL Exception code: " + ex.getErrorCode());
            //ex.printStackTrace();
            return 0;
        } finally {
            try {
                if (prest != null) {
                    prest.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                //Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 1;
    }

    public int deleteQuestion(String id) {

        Connection con = new MysqlConnection().getConnection();
        if (con == null) // if connection failed
        {
            return -1;
        }
        Statement st = null;

        try {
            st = con.createStatement();
            String sql = "DELETE FROM `do_exercise` WHERE `id_question`=" + id + ";";
            st.executeUpdate(sql);
            sql = "DELETE FROM `question` WHERE `id_question`=" + id + ";";
            st.executeUpdate(sql);

        } catch (SQLException e) {
            //System.out.println("Error : " + e.getErrorCode());
            //e.printStackTrace();
            return 0;
        }
        return 1;
    }

}
