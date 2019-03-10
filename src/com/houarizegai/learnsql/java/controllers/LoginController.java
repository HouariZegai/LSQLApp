package com.houarizegai.learnsql.java.controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import com.houarizegai.learnsql.java.dao.UserDao;
import com.houarizegai.learnsql.java.models.UserInformation;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import static javafx.scene.input.KeyCode.ENTER;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginController implements Initializable {

    @FXML
    private HBox root;
    @FXML
    private VBox boxRight;
    @FXML // This TextField using to type the username of the user
    private JFXTextField usernameField;
    @FXML // This PasswordField using to type the password of the user
    private JFXPasswordField passwordField;
    private JFXSnackbar toastErrorMsg;

    @Override
    public void initialize(URL url, ResourceBundle rb) { // this function like constracotor, called in the creation of the FXML

        boxRight.setOnKeyPressed(event -> { // Add Key Listener, if i click to the Enter Button Call btnLogin() method
            if (event.getCode().equals(ENTER)) {
                btnLogin();
            }
        });
        
        usernameField.setOnKeyReleased(event -> { // this event for check max length of the answer area
            if (usernameField.getText().length() > 25) {
                usernameField.setText(usernameField.getText().substring(0, 25));
                usernameField.positionCaret(usernameField.getText().length());
            }
        });
        passwordField.setOnKeyReleased(event -> { // this event for check max length of the answer area
            if (passwordField.getText().length() > 50) {
                passwordField.setText(passwordField.getText().substring(0, 50));
                passwordField.positionCaret(passwordField.getText().length());
            }
        });
        
        toastErrorMsg = new JFXSnackbar(boxRight);
    }

    @FXML
    private void btnLogin() { // This function check username & password to login to the System        
        if (usernameField.getText().trim().isEmpty()) {
            toastErrorMsg.show("Username is Empty !", 1500);
            return;
        }
        if (!usernameField.getText().trim().matches("[A-Za-z0-9_]{4,}")) {
            toastErrorMsg.show("Username not valid !", 1500);
            return;
        }
        

        if (passwordField.getText().trim().isEmpty()) {
            toastErrorMsg.show("Password is Empty !", 1500);
            return;
        }
        if (passwordField.getText().trim().length() < 8) {
            toastErrorMsg.show("Password not valid !", 1500);
            return;
        }
        
        new UserDao().checkUsernameAndPassword(usernameField.getText().toLowerCase(), passwordField.getText().toLowerCase());
        switch (UserInformation.id) {
            case 0:
                toastErrorMsg.show("Connection Failed !", 1500);
                break;
            case -1:
                toastErrorMsg.show("Username and/or password incorrect !", 1500);
                break;
            default:
                Stage stage;
                HBox rootSystem = null;
                //get reference - stage
                stage = (Stage) usernameField.getScene().getWindow();
                try {
                    //load up other FXML document
                    rootSystem = FXMLLoader.load(getClass().getResource("/com/houarizegai/learnsql/resources/views/System.fxml"));
                } catch (IOException ex) {
                    System.out.println("Error msg: " + ex.getMessage());
                }
                //create a new scene with root and set the stage
                Scene scene = new Scene(rootSystem);
                stage.setScene(scene);
                stage.show();
                stage.setX(30);
                stage.setY(5);
                break;
        }
    }

    @FXML
    private void btnExit() {
        Platform.exit();
    }

}
