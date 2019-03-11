package com.houarizegai.learnsql.java.dao;

import com.houarizegai.learnsql.java.models.TracQuestion;
import com.houarizegai.learnsql.java.models.TracUser;
import com.houarizegai.learnsql.java.models.UserLogView;
import com.houarizegai.learnsql.java.models.UserStatisticView;
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

public class TracDao {

    public List<TracQuestion> getTracQuestion() {
        Connection con = new MysqlConnection().getConnection();
        if (con == null) {
            return null;
        }

        String sql = "SELECT type_question, id_question, e_syntax, e_semantic, e_analyse, solved, unsolved, "
                + "ROUND(solved * 100 / (solved + unsolved), 2) AS s_percentage "
                + "FROM (SELECT   question.type_question,  do_exercise.id_question, "
                + "SUM(do_exercise.n_syntax_error) AS e_syntax, "
                + "SUM(do_exercise.n_semantic_error) AS e_semantic, "
                + "SUM(do_exercise.n_analyse_error) AS e_analyse, "
                + "SUM(do_exercise.is_solved) AS solved, "
                + "(SELECT COUNT(*) FROM users) - SUM(do_exercise.is_solved) AS unsolved "
                + "FROM do_exercise, question  "
                + "WHERE question.id_question = do_exercise.id_question "
                + "GROUP BY do_exercise.id_question "
                + "UNION ALL "
                + "SELECT  question.type_question, question.id_question, 0 AS e_syntax, "
                + "0 AS e_semantic, 0 AS e_analyse, 0 AS solved, (SELECT COUNT(*) FROM users) AS unsolved "
                + "FROM question "
                + "WHERE question.id_question NOT IN(SELECT id_question FROM do_exercise)) AS a "
                + "ORDER BY id_question;";

        List<TracQuestion> tracQuestion = new ArrayList<>();

        try {
            ResultSet rs = con.createStatement().executeQuery(sql);

            while (rs.next()) {
                tracQuestion.add(new TracQuestion(rs.getString("type_question"),
                        rs.getInt("id_question"),
                        rs.getInt("e_syntax"),
                        rs.getInt("e_semantic"),
                        rs.getInt("e_analyse"),
                        rs.getInt("solved"),
                        rs.getInt("unsolved"),
                        rs.getDouble("s_percentage")
                ));
            }

        } catch (SQLException ex) {
            //Logger.getLogger(TracDao.class.getName()).log(Level.SEVERE, null, ex);
            ////System.out.println("Error SQL");
            return null;
        }

        return tracQuestion;
    }

    public List<TracUser> getTracUsers() {
        Connection con = new MysqlConnection().getConnection();
        if (con == null) {
            return null;
        }

        String sql = "(SELECT a.id_user, a.first_name, a.last_name, a.date_of_birth, a.section, a.`group`, "
                + "SUM(a.n_syntax_error) AS 'E.Syntax', SUM(a.n_semantic_error) AS 'E.Semantic', "
                + "SUM(a.n_analyse_error) AS 'E.Analyse', SUM(a.is_solved) AS 'Solved', (SELECT COUNT(question.id_question)"
                + " FROM question) - SUM(a.is_solved) AS 'UnSolved' FROM (SELECT users.id_user, users.first_name, "
                + "users.last_name, users.date_of_birth, users.section, users.`group`, do_exercise.n_syntax_error, "
                + "do_exercise.n_semantic_error, do_exercise.n_analyse_error,  do_exercise.is_solved FROM users, "
                + "do_exercise WHERE users.id_user = do_exercise.id_user AND users.is_teacher = 0 ) AS a "
                + "GROUP BY a.id_user) UNION SELECT id_user, first_name, last_name, date_of_birth, section, `group`, "
                + "0 AS 'E.Syntax', 0 AS 'E.Semantic', 0 AS 'E.Analyse', 0 AS 'E.Solved', 0 AS 'UnSolved' FROM users "
                + "WHERE is_teacher=0 AND id_user NOT IN(SELECT id_user FROM do_exercise) ORDER BY id_user;";

        List<TracUser> tracUsers = new ArrayList<>();

        try {
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            TracUser user;

            while (rs.next()) {
                user = new TracUser();
                user.setId(rs.getInt("id_user"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setDateOfBirth(rs.getDate("date_of_birth"));
                user.setSection(rs.getInt("section"));
                user.setGroup(rs.getInt("group"));
                user.setNumberSyntaxError(rs.getInt("E.Syntax"));
                user.setNumberSemanticError(rs.getInt("E.Semantic"));
                user.setNumberAnalyseError(rs.getInt("E.Analyse"));
                user.setNumberSolved(rs.getInt("Solved"));
                user.setNumberUnSolved(rs.getInt("UnSolved"));

                        
                tracUsers.add(user);
            }

        } catch (SQLException ex) {
            Logger.getLogger(TracDao.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println("Error SQL");
            return null;
        }

        return tracUsers;
    }

    public List<UserStatisticView> getUserStatisticView(int idUser) {
        // This function get The infs about users Statistic
        Connection con = new MysqlConnection().getConnection();
        if (con == null) {
            return null;
        }
        String sql = "SELECT question.type_question, do_exercise.id_question, do_exercise.n_syntax_error, do_exercise.n_semantic_error, do_exercise.n_analyse_error, do_exercise.is_solved FROM do_exercise, question WHERE question.id_question = do_exercise.id_question AND do_exercise.id_user=" + idUser + ";";

        Statement st = null;

        List<UserStatisticView> userStatisticView = new ArrayList<>();
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                userStatisticView.add(new UserStatisticView(rs.getString("type_question"),
                        rs.getInt("id_question"),
                        rs.getInt("n_syntax_error"),
                        rs.getInt("n_semantic_error"),
                        rs.getInt("n_analyse_error"),
                        rs.getBoolean("is_solved")));
            }

        } catch (SQLException ex) {
            //System.out.println("Error get User Statistic !");
            return null;
        }

        ////System.out.println("Passed !");
        return userStatisticView;
    }

    public List<UserLogView> getUserLogView(int idUser) {
        // This function get The infs about users Statistic
        Connection con = new MysqlConnection().getConnection();
        if (con == null) {
            return null;
        }
        String sql = "SELECT id_log, DATE(date_login) AS 'date_login', TIME(date_login) AS 'time_login', "
                + "SEC_TO_TIME(TIMESTAMPDIFF(SECOND, date_login, date_logout)) AS 'duration_login' "
                + "FROM log WHERE log.id_user=?;";

        PreparedStatement prest = null;

        List<UserLogView> userLogView = new ArrayList<>();
        try {
            prest = con.prepareStatement(sql);
            prest.setInt(1, idUser);
            ResultSet rs = prest.executeQuery();

            while (rs.next()) {
                userLogView.add(new UserLogView(rs.getInt("id_log"),
                        rs.getDate("date_login"),
                        rs.getTime("time_login"),
                        rs.getTime("duration_login")));
            }

        } catch (SQLException ex) {
            //System.out.println("Error get User Log !");
            //System.out.println("ErrorMsg: " + ex.getMessage());
            return null;
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (prest != null) {
                    prest.close();
                }

            } catch (SQLException ex) {
                //Logger.getLogger(TracDao.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return userLogView;
    }

    public List<String> getTotalLogInfo(int idUser) {

        Connection con = new MysqlConnection().getConnection();
        if (con == null) {
            return null;
        }
        //System.out.println("dao 1");
        String sql = "SELECT MIN(DATE(date_login)) AS 'first_date_login', "
                + "MIN(TIME(date_login)) AS 'first_time_login', MAX(DATE(date_login)) AS 'last_date_login', "
                + "MAX(TIME(date_login)) AS 'last_time_login', SEC_TO_TIME(SUM(TIMESTAMPDIFF(SECOND, date_login, date_logout)))"
                + " AS 'total_duration_login' FROM log WHERE id_user=?;";

        List<String> totalLogInfo = new ArrayList<>();

        //System.out.println("dao 2");
        PreparedStatement prest = null;
        try {
            prest = con.prepareStatement(sql);
            prest.setInt(1, idUser);
            ResultSet rs = prest.executeQuery();
            if (rs.next()) {
                //System.out.println("dao 3");

                if (rs.getDate("first_date_login") == null) {
                    totalLogInfo.add("00-00-0000");
                } else {
                    totalLogInfo.add(rs.getDate("first_date_login").toString());
                }

                if (rs.getTime("first_time_login") == null) {
                    totalLogInfo.add("00-00-0000");
                } else {
                    totalLogInfo.add(rs.getTime("first_time_login").toString());
                }

//                //System.out.println("First Data Login: " + rs.getDate("first_date_login").toString());
//                //System.out.println("First Time Login: " + rs.getTime("first_time_login").toString());
//                //System.out.println("Last Date Login: " + rs.getDate("last_date_login").toString());
//                //System.out.println("Last Time Login: " + rs.getTime("last_time_login").toString());
                if (rs.getDate("last_date_login") == null) {
                    totalLogInfo.add("00-00-0000");
                } else {
                    totalLogInfo.add(rs.getDate("last_date_login").toString());
                }

                if (rs.getTime("last_time_login") == null) {
                    totalLogInfo.add("00:00");
                } else {
                    totalLogInfo.add(rs.getTime("last_time_login").toString());
                }

                if (rs.getTime("total_duration_login") == null) {
                    totalLogInfo.add("00:00:00");
                } else {
                    totalLogInfo.add(rs.getTime("total_duration_login").toString());
                }

            }

        } catch (SQLException se) {
            //System.out.println("Error into getTotalLogInfo - TracDao");
            //System.out.println("Error: " + se.getMessage());
        }

        return totalLogInfo;
    }

    /* Start Combo auto get year & month for bar chart log show part */
    public List<Integer> getYearLogChart() {
        Connection con = new MysqlConnection().getConnection();
        if (con == null) {
            return null;
        }

        List<Integer> years = new ArrayList<>();
        String sql = "SELECT DISTINCT YEAR(date_login) AS 'years' FROM log;";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                years.add(rs.getInt("years"));
            }

            return years;
        } catch (SQLException se) {
            //System.out.println("Error msg: " + se.getMessage());
            return null;
        }
    }

    public List<String> getMonthLogChart(int year) {
        Connection con = new MysqlConnection().getConnection();
        if (con == null) {
            return null;
        }

        List<String> month = new ArrayList<>();
        String sql = "SELECT DISTINCT MONTH(date_login) AS 'month' FROM log WHERE YEAR(date_login) =" + year + " ORDER BY `month` ASC;";
        try {
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                switch (rs.getInt("month")) {
                    case 1:
                        month.add("January");
                        break;
                    case 2:
                        month.add("February");
                        break;
                    case 3:
                        month.add("March");
                        break;
                    case 4:
                        month.add("April");
                        break;
                    case 5:
                        month.add("May");
                        break;
                    case 6:
                        month.add("June");
                        break;
                    case 7:
                        month.add("July");
                        break;
                    case 8:
                        month.add("August");
                        break;
                    case 9:
                        month.add("September");
                        break;
                    case 10:
                        month.add("October");
                        break;
                    case 11:
                        month.add("November");
                        break;
                    case 12:
                        month.add("December");
                        break;
                }
            }

            return month;
        } catch (SQLException se) {
            //System.out.println("Error msg: " + se.getMessage());
            return null;
        }
    }

    public Map<Integer, List<String>> getComboYearMonthLog() {
        // This function return the exercise type with each exercises
        Map<Integer, List<String>> comboYearMonth = new HashMap<>();

        List<Integer> years = getYearLogChart();

        years.forEach(year -> comboYearMonth.put(year, getMonthLogChart(year)));

        return comboYearMonth;
    }

    /* End Combo auto get year & month for bar chart log show part*/
    // Start LineChart Time active part
    public Map<Integer, Double> getTimeActive(int idUserSelected, int year, int month) {
        Map<Integer, Double> listTimeActive = new HashMap<>();

        Connection con = new MysqlConnection().getConnection();
        if (con == null) {
            return null;
        }

        String sql = "SELECT DAY(date_login) AS 'day_login', SUM(TIMESTAMPDIFF(SECOND, date_login, date_logout)) / 60"
                + " AS 'duration_login' FROM (SELECT * FROM log WHERE id_user=?) AS T WHERE YEAR(date_login) = ? AND "
                + "MONTH(date_login) = ? GROUP BY DATE(date_login);";

        try {
            PreparedStatement prest = con.prepareStatement(sql);
            prest.setInt(1, idUserSelected);
            prest.setInt(2, year);
            prest.setInt(3, month);
            ResultSet rs = prest.executeQuery();
            while (rs.next()) {
                listTimeActive.put(rs.getInt("day_login"), rs.getDouble("duration_login"));
            }

        } catch (SQLException se) {
            //System.out.println("Error Msg (TracDao > getTimeActive()) : " + se.getMessage());
            return null;
        }

        return listTimeActive;
    }

    public Map<Integer, Double> getTimeActive(int idUserSelected, int year) {
        Map<Integer, Double> listTimeActive = new HashMap<>();

        Connection con = new MysqlConnection().getConnection();
        if (con == null) {
            return null;
        }

        String sql = "SELECT MONTH(date_login) AS 'month', SUM(TIMESTAMPDIFF(SECOND, date_login, date_logout)) / 60 "
                + "AS 'duration_login' FROM (SELECT * FROM log WHERE id_user=?) AS T WHERE YEAR(date_login)=? "
                + "GROUP BY MONTH(date_login);";

        try {
            PreparedStatement prest = con.prepareStatement(sql);
            prest.setInt(1, idUserSelected);
            prest.setInt(2, year);
            ResultSet rs = prest.executeQuery();
            while (rs.next()) {
                listTimeActive.put(rs.getInt("month"), rs.getDouble("duration_login"));
            }

        } catch (SQLException se) {
            //System.out.println("Error Msg (TracDao > getTimeActive()) : " + se.getMessage());
            return null;
        }

        return listTimeActive;
    }

    // End LineChart Time active part
}
