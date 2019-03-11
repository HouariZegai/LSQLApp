package com.houarizegai.learnsql.java.controllers.form;

import com.houarizegai.learnsql.java.controllers.ManageQuestionController;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.houarizegai.learnsql.java.dao.QuestionDao;
import com.houarizegai.learnsql.java.models.Question;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static javafx.scene.input.KeyCode.ENTER;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class EditQuestionFormController implements Initializable {

    @FXML
    private VBox root;
    @FXML
    private JFXTextField questionTypeField;
    @FXML
    private JFXTextArea questionArea;
    @FXML
    private JFXTextArea answerArea;

    private JFXSnackbar toastErrorMsg; // Error Message

    public static Question questionSelected; // this variable using to fill the edit form

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        root.setOnKeyPressed(event -> { // Add Key Listener, if i click to the Enter Button Call btnLogin() method
            if (event.getCode().equals(ENTER)) {
                btnEdit();
            }
        });
        System.out.println("Question Selected: " + questionSelected.getQuestion());
        questionTypeField.setText(questionSelected.getQuestionType());
        questionArea.setText(questionSelected.getQuestion());
        answerArea.setText(questionSelected.getAnswer());

        toastErrorMsg = new JFXSnackbar(root);
        toastErrorMsg.setPrefWidth(600);
    }

    @FXML
    public void btnEdit() { // Add New Student
        if (!questionTypeField.getText().trim().toLowerCase().matches("[a-z0-9_]{3,}")) {
            toastErrorMsg.show("Question type error", 1500);
            return;
        }
        if (questionArea.getText().trim().length() < 5) {
            toastErrorMsg.show("Question area error!", 1500);
            return;
        }
        if (answerArea.getText().trim().length() < 4) {
            toastErrorMsg.show("Answer area error!", 1500);
            return;
        }

        Question question = new Question();
        question.setId(questionSelected.getId());
        question.setQuestionType(questionTypeField.getText().trim().toLowerCase());
        question.setQuestion(questionArea.getText().trim().toLowerCase());
        question.setAnswer(answerArea.getText().trim().toLowerCase());

        int status = new QuestionDao().editQuestion(question);
        switch (status) {
            case -1:
                toastErrorMsg.show("Connection failed !", 1500);
                break;
            case 0:
                toastErrorMsg.show("Question not edited !", 1500);
                break;
            case 1: {
                Notifications.create()
                        .title("You Successfuly edited question !")
                        .graphic(new ImageView(new Image("/com/houarizegai/learnsql/resources/images/icons/valid.png")))
                        .hideAfter(Duration.millis(2000))
                        .position(Pos.BOTTOM_RIGHT)
                        .darkStyle().show();

                ManageQuestionController.editQuestionDialog.close();
            }
        }
    }

    @FXML
    public void btnReset() {
        questionTypeField.setText(questionSelected.getQuestionType());
        questionArea.setText(questionSelected.getQuestion());
        answerArea.setText(questionSelected.getAnswer());
    }

    @FXML
    public void btnClose() {
        ManageQuestionController.editQuestionDialog.close();
    }
}
