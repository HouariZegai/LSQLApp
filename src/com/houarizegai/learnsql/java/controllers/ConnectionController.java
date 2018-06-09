package com.houarizegai.learnsql.java.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import com.houarizegai.learnsql.java.dao.MysqlConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import static javafx.scene.input.KeyCode.ENTER;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class ConnectionController implements Initializable {

    /* Start Connection Part - Attribute */
    @FXML
    public AnchorPane rootPane;
    @FXML // this stackpane using for the Snack Error Msg
    private StackPane stackPane;
    @FXML // This Combo Box using to choose the DBMS using in Exercise
    private JFXComboBox<String> dbmsComboConnection;

    @FXML // This TextField using to type the host name in connection part
    private JFXTextField hostField;
    @FXML // This TextField using to type the name of dataBase in connection part
    private JFXTextField dbNameField;
    @FXML // This TextField using to type the port number of my connection
    private JFXTextField portField;
    @FXML // This TextField using to type the UserName of dataBase in connection part
    private JFXTextField usernameField;
    @FXML // This PasswordField using to input the password of database in connection part
    private JFXPasswordField passwordField;
    private JFXSnackbar toastErrorMsg;
    @FXML
    private AnchorPane exercisePane;

    /* End Connection Part - Attribute */
    @Override
    public void initialize(URL url, ResourceBundle rb) { // This function like constractor it called in the creation of the FXML
        dbmsComboConnection.getItems().addAll("Mysql", "Oracle", "Access", "SQL Server", "SQLite");

        rootPane.setOnKeyPressed(event -> { // If click to the Enter Button Call btnConnectionClicked() method
            if (event.getCode().equals(ENTER)) {
                btnConnect();
            }
        });

        portField.setOnKeyTyped(e -> { // Controls Max Length
            if (portField.getText().length() == 5) {
                e.consume();
            }
        });

        portField.textProperty().addListener(new ChangeListener<String>() { // TextField Accept only Number
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    portField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        toastErrorMsg = new JFXSnackbar(stackPane);
    }

    /* Start Connection Part - Action */
    @FXML
    private void btnConnect() { // This function check the connection if success pass to Tp Exercise Part else show error message
        if (dbmsComboConnection.getSelectionModel().getSelectedItem() == null) {
            toastErrorMsg.show("Please choose DBMS type !", 1500);
            return;
        }
        if (hostField.getText().trim().isEmpty()) {
            toastErrorMsg.show("Please type the Host name or IP address !", 1500);
            return;
        }
        if (dbNameField.getText().trim().isEmpty()) {
            toastErrorMsg.show("Please type the DataBase name !", 1500);
            return;
        }
        if (usernameField.getText().trim().isEmpty()) {
            toastErrorMsg.show("Please type the Username !", 1500);
            return;
        }
        
        if(!hostField.getText().trim().toLowerCase().matches("(127.+)|localhost")) {
            toastErrorMsg.show("Connection failed !", 1500);
            return;
        }

        Connection con = new MysqlConnection().getConnection(hostField.getText().trim().toLowerCase(), 
                portField.getText().trim(), dbNameField.getText().trim(), usernameField.getText().trim().toLowerCase(),
                passwordField.getText().trim());
        
        if (con == null || dbmsComboConnection.getSelectionModel().getSelectedIndex() != 0) {
            toastErrorMsg.show("Connection failed !", 1500);
        } else {
            try {
                exercisePane = FXMLLoader.load(getClass().getResource("/com/houarizegai/learnsql/resources/views/Exercise.fxml"));
            } catch (IOException ex) {
                Logger.getLogger(ConnectionController.class.getName()).log(Level.SEVERE, null, ex);
            }
            rootPane.getChildren().add(exercisePane);
            exercisePane.setVisible(true);
        }
    }

    @FXML
    private void btnClear() { // This function clear all Field & set the ComboBox to the default value in Connection pane
        dbmsComboConnection.setValue(null);
        hostField.setText("");
        portField.setText("");
        dbNameField.setText("");
        usernameField.setText("");
        passwordField.setText("");
    }

    /* End Connection Part - Action */
}
