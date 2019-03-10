package com.houarizegai.learnsql.java.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.houarizegai.learnsql.java.dao.ExerciseDao;
import com.houarizegai.learnsql.java.dao.KeywordsDao;
import com.houarizegai.learnsql.java.models.QueryKeyWords;
import com.houarizegai.learnsql.java.models.Question;
import com.houarizegai.learnsql.java.models.UserInformation;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class ExerciseController implements Initializable {

    @FXML
    private Label errorLabel;

    @FXML
    private JFXComboBox<String> dbTypeComboBox;

    @FXML
    private JFXComboBox<String> nQuestionComboBox;

    @FXML
    private JFXButton btnHelp;

    @FXML
    private TextFlow questionFlow;

    @FXML
    private TextArea answerArea;

    @FXML
    private Label counterLabel;

    @FXML
    private JFXButton btnNext;

    @FXML
    private ListView hintList;

    @FXML
    private Label effectLabel;

    @FXML
    private ImageView imgTables;

    public static Question questionCurrent;

    public Map<String, List<Integer>> comboQuestion;

    @Override
    public void initialize(URL url, ResourceBundle rb) { // This function like constractor it called in the creation of the FXML
        // initialize the variable comboBox (contain the name & number of the Combo)
        comboQuestion = new ExerciseDao().getComboQuestion();

        // get from DB the keyword natural using in help button
        QueryKeyWords.hintKeyword = new KeywordsDao().getHelpKeyword();

        comboQuestion.keySet().forEach((item) -> {
            // this loop initialize the comboBox of question
            String propreName = item.substring(0, 1).toUpperCase() + item.substring(1).toLowerCase();
            dbTypeComboBox.getItems().addAll(propreName); // set it by the name in DB
        });

        answerArea.setOnKeyReleased(event -> { // this event for check max length of the answer area
            if (answerArea.getText().length() + 1 > 350) {
                answerArea.setText(answerArea.getText().substring(0, 350));
                answerArea.positionCaret(answerArea.getText().length());
            }
            counterLabel.setText("" + (350 - answerArea.getText().length()));
            btnNext.setDisable(false);
        });
    }

    @FXML
    private void dbTypeComboAction() { // This function using to enable the Question Combo Box if i choose the database or disable
        btnNext.setText("Check");
        // initialize the number of error
        UserInformation.numberSyntaxE = 0;
        UserInformation.numberSemanticE = 0;
        UserInformation.numberAnalyseE = 0;

        nQuestionComboBox.getItems().clear();

        effectLabel.setText(dbTypeComboBox.getSelectionModel().getSelectedItem());
        comboQuestion.get(dbTypeComboBox.getSelectionModel().getSelectedItem().toLowerCase()).forEach((item) -> {
            nQuestionComboBox.getItems().add("N° " + item); // Get all id of question of this question type
        });

        answerArea.setEditable(false);
        nQuestionComboBox.setDisable(false);
        nQuestionComboBox.setValue(null);

        btnHelp.setDisable(true);
        questionFlow.getChildren().clear();
        errorLabel.setText("");
        hintList.getItems().clear();
        clearAnswerArea();

        try { // Show image 
            imgTables.setImage(new Image("/com/houarizegai/learnsql/resources/images/questionTables/db/"
                    + dbTypeComboBox.getSelectionModel().getSelectedItem().toLowerCase() + ".png"));

        } catch (Exception e) {
            //System.out.println("Error Catch: " + e.getMessage());
            // if image not found
        }
    }

    @FXML
    private void nQuestionAction() { // This function called in the case of user choose the number of question, get The Question selected by the student
        btnNext.setText("Check");
        // initialize the number of error
        UserInformation.numberSyntaxE = 0;
        UserInformation.numberSemanticE = 0;
        UserInformation.numberAnalyseE = 0;

        answerArea.setEditable(true); // Enable to write answer
        try { // Show image 
            Image img = new Image("/com/houarizegai/learnsql/resources/images/questionTables/"
                    + dbTypeComboBox.getSelectionModel().getSelectedItem().toLowerCase() + "/"
                    + nQuestionComboBox.getSelectionModel().getSelectedItem().substring(3) + ".png");
            imgTables.setImage(img);

        } catch (Exception e) {
            try { // Show image 
                imgTables.setImage(new Image("/com/houarizegai/learnsql/resources/images/questionTables/db/"
                        + dbTypeComboBox.getSelectionModel().getSelectedItem().toLowerCase() + ".png"));

            } catch (Exception es) {
                // if image not found make default picture (error.jpg)
                imgTables.setImage(new Image("/com/houarizegai/learnsql/resources/images/questionTables/error.jpg"));
            }
        }

        errorLabel.setText("");
        hintList.getItems().clear();
        btnHelp.setDisable(false);
        clearAnswerArea();

        String dbType = dbTypeComboBox.getSelectionModel().getSelectedItem().toLowerCase();

        if (nQuestionComboBox.getSelectionModel().getSelectedItem() == null) { // this condition using to verify if it is null return
            return;
        }
        String numberQuestion = nQuestionComboBox.getSelectionModel().getSelectedItem().substring(3);

        questionCurrent = new ExerciseDao().getQuestion(dbType, numberQuestion);

        if (questionCurrent == null) {
            errorLabel.setText("Connection Error!");
            return;
        }
        Text txt = new Text(questionCurrent.getQuestion());
        txt.setStyle("-fx-font-size: 18px");
        questionFlow.getChildren().clear();
        questionFlow.getChildren().add(txt);

        new ExerciseDao().createDoExercise();
        btnNext.setDisable(false);

        // Initialize the tables name using in help button
        QueryKeyWords.tableNames = new ExerciseDao().getTableNames(dbTypeComboBox.getSelectionModel().getSelectedItem().toLowerCase());

        // Initialize the attribute name using in help button
        QueryKeyWords.columnNames = new ExerciseDao().getColumnNames(QueryKeyWords.tableNames);

    }

    @FXML
    private void btnHintClicked() { // This Function make Color to the KeyWord of the Question to help the user
        // Change the color of keyword of QuestionArea
        btnHelp.setDisable(true); // make the Button Help Disable
        String words = ((Text) questionFlow.getChildren().get(0)).getText();
        questionFlow.getChildren().clear();

        for (String word : words.split("[ ,;.]")) {
            questionFlow.getChildren().add(getStyleWord(word));
            Text txt = new Text(" ");
            txt.setStyle("-fx-font-size: 18px");
            questionFlow.getChildren().add(txt);
        }
    }

    @FXML
    private void clearAnswerArea() { // This function remove all text of the Answer TextArea
        answerArea.setText("");
        resetColorArea();
        errorLabel.setText("");
    }

    @FXML
    private void btnNextClicked() {
        btnNext.setDisable(true);
        if (btnNext.getText().equals("Next")) {
            if (nQuestionComboBox.getSelectionModel().getSelectedIndex() < nQuestionComboBox.getItems().size() - 1) {
                nQuestionComboBox.getSelectionModel().select(nQuestionComboBox.getSelectionModel().getSelectedIndex() + 1);
                btnNext.setText("Check");
                // initialize the number of error
                UserInformation.numberSyntaxE = 0;
                UserInformation.numberSemanticE = 0;
                UserInformation.numberAnalyseE = 0;
            }
            return;
        }

        resetColorArea();
        hintList.getItems().clear();
        errorLabel.setText("");

        if (answerArea.getText().matches("[ ]*")) {
            return;
        }

        boolean solved = new ExerciseDao().checkAnswer(hintList, answerArea.getText());
        if (solved) {
            btnNext.setText("Next");
            btnNext.setDisable(false);
        }
    }

    @FXML
    private void resetColorArea() { // this function change the color of the Answer Area & Error Label
        answerArea.setStyle("-fx-text-fill: #000");
        errorLabel.setText("");
    }

    public Text getStyleWord(String text) {
        // if the word is key word => change the color of this word 
        Text txt = new Text(text);
        txt.setStyle("-fx-font-size: 18px");

        for (String key : QueryKeyWords.hintKeyword) {
            if (key.equalsIgnoreCase(text)) {
                txt.setFill(Paint.valueOf("#2ECC71"));
                return txt;
            }
        }

        for (String key : QueryKeyWords.tableNames) {
            if (key.equalsIgnoreCase(text)) {
                txt.setFill(Paint.valueOf("#2196f3"));
                return txt;
            }
        }

        for (String key : QueryKeyWords.columnNames) {
            if (key.equalsIgnoreCase(text)) {
                txt.setFill(Paint.valueOf("#2196f3"));
                return txt;
            }
        }

        return txt;
    }

}
