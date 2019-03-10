package com.houarizegai.learnsql.java.dao;

import static com.houarizegai.learnsql.java.controllers.ExerciseController.questionCurrent;
import static com.houarizegai.learnsql.java.models.QueryKeyWords.columnNames;
import static com.houarizegai.learnsql.java.models.QueryKeyWords.keywords;
import static com.houarizegai.learnsql.java.models.QueryKeyWords.tableNames;
import com.houarizegai.learnsql.java.models.Question;
import com.houarizegai.learnsql.java.models.UserInformation;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import de.jensd.fx.glyphs.octicons.OctIcon;
import de.jensd.fx.glyphs.octicons.OctIconView;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;

public class ExerciseDao {
    
    static int counterNaturalHint = 0;
    static int counterSqlHint = 0;
    static int counterTablesNames = 0;
    static int counterColumnNames = 0;
    
    public List<String> getTableNames(String dbName) {
        // get tables name & attribute of selected database add stocked in key word
        
        Connection con = new MysqlConnection().getConnectionExercise(dbName);
        if(con == null)
            return null;
        
        String sql = "SHOW TABLES WHERE ? LIKE CONCAT('%',Tables_in_" + dbName + ",'%');";
        List<String> tableNames = new ArrayList<>();
        
        try {
            PreparedStatement prest = con.prepareCall(sql);
            prest.setString(1, questionCurrent.getAnswer());
            ResultSet rs = prest.executeQuery();
            
            while(rs.next()) {
                tableNames.add(rs.getString("Tables_in_" + dbName));
            }
            
        } catch(SQLException se) {
            //System.out.println("Error msg (getTableNames): " + se.getMessage());
        }
        
        return tableNames;
    }
    
    public List<String> getColumnNames(List<String> tableNames) {
        Connection con = new MysqlConnection().getConnection();
        if(con == null)
            return null;
        
        List<String> columnNames = new ArrayList<>();
        String sql;
        
        try {
            for(String tableName : tableNames) {
                sql = "SELECT `COLUMN_NAME` FROM `INFORMATION_SCHEMA`.`COLUMNS` WHERE `TABLE_NAME`=? AND ? "
                        + "LIKE CONCAT('%',COLUMN_NAME,'%');";
                PreparedStatement prest = con.prepareCall(sql);
                prest.setString(1, tableName);
                prest.setString(2, questionCurrent.getAnswer());
                ResultSet rs = prest.executeQuery();
                while(rs.next()) {
                    columnNames.add(rs.getString("COLUMN_NAME"));
                }
            }
        } catch(SQLException se) {
            //System.out.println("Error msg (getTableNames): " + se.getMessage());
        }
        
        return columnNames;
    }
    
    public Question getQuestion(String dbType, String numberQuestion) {
        // This function return the question a traver database type
        String sql = "SELECT * FROM `question` WHERE `id_question`=" + numberQuestion + " AND `type_question`='" + dbType + "';";

        Question question = new Question();

        Connection con = null;
        Statement statement = null;

        try {
            con = new MysqlConnection().getConnection();

            if (con == null) {
                return null;
            }
            statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            if (rs.next()) {
                question.setId(rs.getInt("id_question"));
                question.setQuestionType(rs.getString("type_question"));
                question.setQuestion(rs.getString("question"));
                question.setAnswer(rs.getString("answer"));
            }

        } catch (SQLException ex) {
            //System.out.println("Error Code: " + ex.getErrorCode());
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

        return question;
    }
    
    public Map<String, List<Integer>> getComboQuestion() {
        // This function return the exercise type with each exercises
        Map<String, List<Integer>> comboQuestion = new HashMap<>();

        List<Integer> item = new ArrayList<>();

        Connection con = MysqlConnection.connectionSystem;

        ArrayList<String> dbName = getNameOfDBType();

        for (String dbType : dbName) {
            //System.out.println(dbType);
            comboQuestion.put(dbType, null);
        }

        for (int i = 0; i < dbName.size(); i++) {
            comboQuestion.replace(dbName.get(i), getNumberOfQuestion(dbName.get(i)));
        }
        //System.out.println(comboQuestion);
        return comboQuestion;
    }
    
    public ArrayList<String> getNameOfDBType() {
        // This function return array of exercise name
        Connection con = null;

        String sql = "SELECT DISTINCT `type_question` FROM `question`;";
        Statement st = null;
        ResultSet rs = null;
        ArrayList<String> dbName = new ArrayList<>();

        try {
            con = MysqlConnection.connectionSystem;
            if (con == null) {
                return null;
            }

            st = con.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                dbName.add(rs.getString("type_question"));
            }

        } catch (SQLException ex) {
            //Logger.getLogger(ExerciseDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return dbName;
    }

    public ArrayList<Integer> getNumberOfQuestion(String dbType) {
        // This function return array of question of the exercise type passed in argument

        String sql = "SELECT `id_question` FROM `question` WHERE `type_question`='" + dbType.toLowerCase() + "' "
                + "AND id_question NOT IN(SELECT id_question FROM do_exercise WHERE id_user=" + UserInformation.id + " "
                + "AND is_solved=1) ORDER BY id_question;";
        Statement st = null;
        ResultSet rs = null;
        ArrayList<Integer> questionNumber = new ArrayList<>();

        Connection con = null;
        try {
            con = MysqlConnection.connectionSystem;
            if (con == null) {
                return null;
            }

            st = con.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                questionNumber.add(rs.getInt("id_question"));
            }

        } catch (SQLException ex) {
            //Logger.getLogger(ExerciseDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return questionNumber;
    }

    public boolean checkAnswer(ListView hintList, String sqlOfUser) {
        // This function check the query typed by the student
        
        if (sqlOfUser.toLowerCase().contains("delete")) {
            showHintMsg(hintList, 1, "Don't use DELETE keyword");
            return false;
        }
        if (sqlOfUser.toLowerCase().contains("drop")) {
            showHintMsg(hintList, 1, "Don't use DROP keyword");
            return false;
        }
        if (sqlOfUser.toLowerCase().contains("insert")) {
            showHintMsg(hintList, 1, "Don't use INSERT keyword");
            return false;
        }
        if (sqlOfUser.toLowerCase().contains("update")) {
            showHintMsg(hintList, 1, "Don't use UPDATE keyword");
            return false;
        }
        
        
        
        hintList.getItems().clear(); // clear hint Area

        // Get connection a traver the name of the DB selected by the user
        Connection con = new MysqlConnection().getConnectionExercise(questionCurrent.getQuestionType());
        
        if (con == null) {
            hintList.getItems().clear();
            showHintMsg(hintList, 1, "Connection failed");
            return false;
        }

        try {
            // this ResultSet of query typed by the user
            ResultSet rs = con.createStatement().executeQuery(sqlOfUser);
            showHintMsg(hintList, 3, "Syntax Right");
            showHintMsg(hintList, 3, "Semantic Right");
            
            // Check if it is true
            // this ResultSet of query typed by the teacher (from db)
            ResultSet rsTeacher = con.createStatement().executeQuery(questionCurrent.getAnswer()); // this query of the teacher

            if (!ifErrorMetaData(hintList, rs, rsTeacher, sqlOfUser)) {
                if (!ifErrorContent(hintList, rs, rsTeacher, sqlOfUser)) {
                    
                    showHintMsg(hintList, 3, "Correct");
                    try {
                        String sql = "UPDATE `do_exercise` SET `is_solved`=1 WHERE `id_user`=? AND `id_question`=?;";
                        PreparedStatement prest = new MysqlConnection().getConnection().prepareStatement(sql);
                        prest.setInt(1, UserInformation.id);
                        prest.setInt(2, questionCurrent.getId());
                        prest.executeUpdate();
                    } catch (SQLException e) {
                        //System.out.println("Error is_solved ! Code: " + e.getErrorCode());
                        //e.printStackTrace();
                    }
                    return true;
                }
            }

        } catch (SQLException ex) {
            //System.out.println("Error msg: " + ex.getMessage());
            
            if (!ifErrorSyntax(hintList, ex.getErrorCode(), sqlOfUser)) {
                ifErrorSemantic(hintList, ex.getErrorCode(), sqlOfUser);
            }
        }
        return false;
    }

    private boolean ifErrorSyntax(ListView hintList, int errorCode, String sqlOfUser) {
        // Check syntex if is correct return true else return false
        if (errorCode == 1064) {
            hintList.getItems().clear();
            showHintMsg(hintList, 1, "Syntax Error");
            // increment the number of the syntax error & call the function for print the hint
            ++UserInformation.numberSyntaxE;

            if (UserInformation.numberSyntaxE >= 10) {
                sendHintSQL(hintList, sqlOfUser);
            } else if (UserInformation.numberSyntaxE >= 5) {
                sendHintNatural(hintList, sqlOfUser);
            }

            updateNumberError("n_syntax_error"); // add +1 of the N° of syntax error to the DB
            return true;
        } else {
            hintList.getItems().clear();
            showHintMsg(hintList, 3, "Syntax Correct");
            return false;
        }
    }

    private void ifErrorSemantic(ListView hintList, int errorCode, String sqlOfUser) {
        // check symontic if it is true
        
        showHintMsg(hintList, 1, "Semantic Error");

        UserInformation.numberSemanticE++;

        if (UserInformation.numberSemanticE >= 10) {
            sendHintSQL(hintList, sqlOfUser);
        } else if (UserInformation.numberSemanticE >= 6) {
            sendHintNatural(hintList, sqlOfUser);
        } else if (UserInformation.numberSemanticE >= 3) {
            switch (errorCode) {
                case 1054:
                    showHintMsg(hintList, 2, "Can't find Column");
                    break;
                case 1146:
                    showHintMsg(hintList, 2, "Table selected doesn't exist");
                    break;
            }
        }

        updateNumberError("n_semantic_error"); // add +1 of the N° of semantic error to the DB
    }

    private boolean ifErrorMetaData(ListView hintList, ResultSet rs1, ResultSet rs2, String sqlOfUser) {
        // This method check if the metadata if it's same in two ResultSet (number column, name column, data type of column)
        boolean ifError = false;
        
        try {
            ResultSetMetaData rsmd1 = rs1.getMetaData();
            ResultSetMetaData rsmd2 = rs2.getMetaData();

            ifError = ifError || rsmd1.getColumnCount() != rsmd2.getColumnCount(); // compaire the col count
            if (ifError) {
                showHintMsg(hintList, 1, "Analyse error");
                if (UserInformation.numberAnalyseE >= 3)
                    showHintMsg(hintList, 2, "Meta Data Error: Number of column not same");
                return true;
            }

            for (int i = 1; i <= rsmd1.getColumnCount() && !ifError; i++) {
                ifError = !rsmd1.getColumnName(i).equals(rsmd2.getColumnName(i)); // compaire the col name

                if (ifError) {
                    showHintMsg(hintList, 1, "Analyse error");
                    if (UserInformation.numberAnalyseE >= 3)
                        showHintMsg(hintList, 2, "Meta Data Error: Column name not same");
                    return true;
                }
                ifError = ifError || rsmd1.getColumnType(i) != rsmd2.getColumnType(i); // compaire the col type
                if (ifError) {
                    showHintMsg(hintList, 1, "Analyse error");
                    if (UserInformation.numberAnalyseE >= 3)
                        showHintMsg(hintList, 2, "Meta Data Error: Column Data type not same");
                    return true;
                }
            }

        } catch (SQLException ex) {
            showHintMsg(hintList, 1, "Connection error");
            return true;
        } finally {

            if (ifError) {
                // increment the number of the Anaylse error & call the function for print the hint
                ++UserInformation.numberAnalyseE;
                
                if (UserInformation.numberAnalyseE >= 10) {
                    sendHintSQL(hintList, sqlOfUser);
                } else if (UserInformation.numberAnalyseE >= 5) {
                    sendHintNatural(hintList, sqlOfUser);
                }

                updateNumberError("n_analyse_error"); // add +1 of the N° of analyse error to the DB
            }
        }
        return ifError;
    }

    private boolean ifErrorContent(ListView hintList, ResultSet rs1, ResultSet rs2, String sqlOfUser) {
        // This method check the value of record if it's same in two ResultSet (number records & values)
        boolean ifError = false;
        String errorMsg1 = "";
        try {
            rs1.beforeFirst(); // move the cursor before the first record
            rs2.beforeFirst();
            boolean rs1bool = rs1.next();
            boolean rs2bool = rs2.next();

            while (rs1bool && rs2bool && !ifError) {  // this loop using to move between records of the ResultSet
                for (int i = 1; i <= rs1.getMetaData().getColumnCount() && !ifError; i++) {
                    // this loop above using to move between the nodes of record
                    switch (rs1.getMetaData().getColumnType(i)) { // get the type of this row to compaire
                        case Types.CHAR:
                        case Types.VARCHAR:
                            ifError = !rs1.getString(i).equals(rs2.getString(i));
                            //System.out.println("String He Passed " + i + " : " + ifError);
                            break;
                        case Types.BOOLEAN:
                            ifError = rs1.getBoolean(i) != rs2.getBoolean(i);
                            //System.out.println("Boolean He Passed " + i + " : " + ifError);
                            break;
                        case Types.INTEGER:
                            ifError = rs1.getInt(i) != rs2.getInt(i);
                            //System.out.println("Integer He Passed " + i + " : " + ifError);
                            break;
                        case Types.FLOAT:
                            ifError = rs1.getFloat(i) != rs2.getFloat(i);
                            break;
                        case Types.REAL:
                        case Types.DOUBLE:
                            ifError = rs1.getDouble(i) != rs2.getDouble(i);
                            break;
                        case Types.DATE:
                            ifError = !rs1.getDate(i).equals(rs2.getDate(i));
                            break;
                        case Types.TIME:
                            ifError = rs1.getTime(i) != rs2.getTime(i);
                            break;
                    }
                }
                rs1bool = rs1.next();
                rs2bool = rs2.next();
            }
            if (rs1bool || rs2bool) { // number of record not same (One of the ResultSet != null after move)
                showHintMsg(hintList, 1, "Analyse error");
                if (UserInformation.numberAnalyseE >= 3)
                    showHintMsg(hintList, 2, "Number of records not same");
                ifError = true;
                return true;
            }
            if (ifError) {
                showHintMsg(hintList, 1, "Analyse error");
                if (UserInformation.numberAnalyseE >= 3)
                    showHintMsg(hintList, 2, "The records values not same");
                return true;
            }

        } catch (SQLException ex) {
            showHintMsg(hintList, 1, "Connection failed");
            return true;
        } finally {
            if (ifError) {
                // increment the number of the Semantic error & call the function for print the hint
                ++UserInformation.numberAnalyseE;
                if (UserInformation.numberAnalyseE >= 10) {
                    sendHintSQL(hintList, sqlOfUser);
                } else if (UserInformation.numberAnalyseE >= 5) {
                    sendHintNatural(hintList, sqlOfUser);
                }

                updateNumberError("n_analyse_error"); // add +1 of the N° of analyse error to the DB
            }

        }
        return ifError;
    }

    private void updateNumberError(String typeError) { // This function add +1 to the number of error
        //System.out.println("Entered to the update number syntax error function !");

        String sql = "UPDATE `do_exercise` SET `" + typeError + "`=`" + typeError + "` + 1 WHERE `id_user`=? AND `id_question`=?;";
        try {
            Connection con = new MysqlConnection().getConnection();
            PreparedStatement prest = con.prepareStatement(sql);
            prest.setInt(1, UserInformation.id);
            prest.setInt(2, questionCurrent.getId());

            prest.executeUpdate();
        } catch (SQLException e) {
            //System.out.println("Add Check Error");
            //System.out.println("Error Code: " + e.getErrorCode());
        }

    }

    public void createDoExercise() {
        // This function using to create the relation between user and exercise (insert into the table do_exercise)
        Connection con = new MysqlConnection().getConnection();
        if (con == null) {
            return;
        }
        String sql = "INSERT INTO `do_exercise` (`id_user`, `id_question`) VALUES (?, ?)";
        PreparedStatement prest = null;
        try {
            prest = con.prepareStatement(sql);
            prest.setInt(1, UserInformation.id);
            prest.setInt(2, questionCurrent.getId());
            int status = prest.executeUpdate();
        } catch (SQLException e) {
            
        }
    }

    public void sendHintNatural(ListView hintList, String sqlOfUser) {
        // This function print the hint to the user
        
        // Get Table of Hint
        keywords = new KeywordsDao().getKeyword(sqlOfUser);
        
        if(keywords.isEmpty()) { // No keyword missing in query of user
            if(counterTablesNames >= tableNames.size())
                counterTablesNames = 0;
            
            if(tableNames.isEmpty()) { // No table name missing in query of user
                System.out.println("Table name is empty !");
                if(counterColumnNames >= columnNames.size())
                    counterColumnNames = 0;
                if(columnNames.isEmpty()) { // No column name missing in query of user
                    System.out.println("Table column is empty !");
                    return;
                }
                showHintMsg(hintList, 2, "Use column " + columnNames.get(counterColumnNames));
                
                return;
            }
            
            showHintMsg(hintList, 2, "Use table " + tableNames.get(counterTablesNames));
            return;
        }
        
        if(counterNaturalHint >= keywords.size())
            counterNaturalHint = 0;
        
        showHintMsg(hintList, 2, keywords.get(counterNaturalHint++)[1]);
    }

    public void sendHintSQL(ListView hintList, String sqlOfUser) {
        // This function print the hint to the user
        
        // Get Table of Hint
        keywords = new KeywordsDao().getKeyword(sqlOfUser);
        
        if(keywords.isEmpty()) { // No keyword missing in query of user
            if(counterTablesNames >= tableNames.size())
                counterTablesNames = 0;
            
            if(tableNames.isEmpty()) { // No table name missing in query of user
                System.out.println("Table name is empty !");
                if(counterColumnNames >= columnNames.size())
                    counterColumnNames = 0;
                if(columnNames.isEmpty()) { // No column name missing in query of user
                    System.out.println("Table column is empty !");
                    return;
                }
                showHintMsg(hintList, 2, "Use column " + columnNames.get(counterColumnNames));
                
                return;
            }
            
            showHintMsg(hintList, 2, "Use table " + tableNames.get(counterTablesNames));
            return;
        }
        
        if(counterSqlHint >= keywords.size())
            counterSqlHint = 0;
        
        showHintMsg(hintList, 2, "You can use " + keywords.get(counterSqlHint++)[0]);
        
    }

    private void showHintMsg(ListView hintList, int msgType, String text) {
        HBox hbox = new HBox();
        hbox.setSpacing(8);

        hbox.setAlignment(Pos.CENTER_LEFT);
        Label lbl = new Label(text + " !");

        switch (msgType) {
            case 1: // Warning & Error
                FontAwesomeIconView iconWarning = new FontAwesomeIconView(FontAwesomeIcon.WARNING);
                iconWarning.setSize("15px");
                iconWarning.setFill(Paint.valueOf("#F00"));
                lbl.setStyle("-fx-text-fill: #F00");
                
                MaterialIconView iconSad = new MaterialIconView(MaterialIcon.SENTIMENT_DISSATISFIED);
                iconSad.setSize("16px");
                iconSad.setFill(Paint.valueOf("#F00"));
                hbox.getChildren().addAll(iconWarning, lbl, iconSad);
                break;
            case 2: // Hint
                OctIconView iconHint = new OctIconView(OctIcon.INFO);
                iconHint.setSize("16px");
                iconHint.setFill(Paint.valueOf("#00F"));
                lbl.setStyle("-fx-text-fill: #00F");
                hbox.getChildren().addAll(iconHint, lbl);
                break;
            case 3: // Correct
                MaterialIconView iconValid = new MaterialIconView(MaterialIcon.CHECK_CIRCLE);
                iconValid.setSize("16px");
                iconValid.setFill(Paint.valueOf("#080"));
                lbl.setStyle("-fx-text-fill: #080");

                MaterialIconView iconSmile = new MaterialIconView(MaterialIcon.SENTIMENT_SATISFIED);
                iconSmile.setSize("16px");
                iconSmile.setFill(Paint.valueOf("#080"));
                hbox.getChildren().addAll(iconValid, lbl, iconSmile);
                break;
        }
        hintList.getItems().add(hbox);

    }
    
    public boolean sendHintTableNames(ListView hintList) {
        if(counterTablesNames >= tableNames.size())
                counterTablesNames = 0;
            
        if(tableNames.isEmpty()) { // No table name missing in query of user
            return false;
        }

        showHintMsg(hintList, 2, "Use table " + tableNames.get(counterTablesNames));
        return true;
    }
}
