package com.houarizegai.learnsql.java.controllers;

import com.houarizegai.learnsql.java.dao.UserDao;
import com.jfoenix.controls.JFXComboBox;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SettingsController implements Initializable {

    @FXML
    private VBox changeUsernamePane, changeEmailPane, changePasswordPane, changeLanguagePane, changeThemePane;
    @FXML
    private HBox usernameOption, emailOption, passwordOption, languageOption, themeOption;
    @FXML
    private JFXComboBox<String> comboTheme, comboLanguage;

    @FXML // This label showing in top of each settings
    private Label headerLabel, contentLabel;

    // Change username part
    @FXML
    private TextField newUsernameUserPart;
    @FXML
    private PasswordField currentPasswordUserPart;

    // Change Email Part
    @FXML
    private TextField newEmailEmailPart;
    @FXML
    private PasswordField currentPasswordEmailPart;

    // Change password part
    @FXML
    private PasswordField currentPasswordPassPart, newPasswordPassPart, verifyPasswordPassPart;
    @FXML
    private FontAwesomeIconView iconValid;

    @FXML
    private HBox boxError;
    @FXML
    private Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboTheme.getItems().addAll("Blue", "Red", "Green", "Dark");
        comboTheme.getSelectionModel().select(0);

        comboLanguage.getItems().addAll("English", "Français", "العربية");
        comboLanguage.getSelectionModel().select(0);

        addListenerOption();
        
        // This code below check the max value length of password field
        
        // max length of username
        newUsernameUserPart.setOnKeyReleased(event -> { // this event for check max length of the answer area
            if (newUsernameUserPart.getText().length() > 25) {
                newUsernameUserPart.setText(newUsernameUserPart.getText().substring(0, 25));
                newUsernameUserPart.positionCaret(newUsernameUserPart.getText().length());
            }
        });
        
        /* missing code here for ... */
        
        // max length for password
        currentPasswordUserPart.setOnKeyReleased(event -> { // this event for check max length of the answer area
            if (currentPasswordUserPart.getText().length() > 25) {
                currentPasswordUserPart.setText(currentPasswordUserPart.getText().substring(0, 25));
                currentPasswordUserPart.positionCaret(currentPasswordUserPart.getText().length());
            }
        });
        
        currentPasswordEmailPart.setOnKeyReleased(event -> { // this event for check max length of the answer area
            if (currentPasswordEmailPart.getText().length() > 25) {
                currentPasswordEmailPart.setText(currentPasswordEmailPart.getText().substring(0, 25));
                currentPasswordEmailPart.positionCaret(currentPasswordEmailPart.getText().length());
            }
        });
        
        currentPasswordPassPart.setOnKeyReleased(event -> { // this event for check max length of the answer area
            if (currentPasswordPassPart.getText().length() > 50) {
                currentPasswordPassPart.setText(currentPasswordPassPart.getText().substring(0, 50));
                currentPasswordPassPart.positionCaret(currentPasswordPassPart.getText().length());
            }
        });
        
        newPasswordPassPart.setOnKeyReleased(event -> { // this event for check max length of the answer area
            if (newPasswordPassPart.getText().length() > 50) {
                newPasswordPassPart.setText(newPasswordPassPart.getText().substring(0, 50));
                newPasswordPassPart.positionCaret(newPasswordPassPart.getText().length());
            }
            showIconPassPart();
        });
        
        verifyPasswordPassPart.setOnKeyReleased(event -> { // this event for check max length of the answer area
            if (verifyPasswordPassPart.getText().length() > 50) {
                verifyPasswordPassPart.setText(verifyPasswordPassPart.getText().substring(0, 50));
                verifyPasswordPassPart.positionCaret(verifyPasswordPassPart.getText().length());
            }
            showIconPassPart();
        });
        
    }

    private void addListenerOption() {
        usernameOption.setOnMouseClicked(e -> {
            // Show the content of selected option
            changeUsernamePane.setVisible(true);
            changeEmailPane.setVisible(false);
            changePasswordPane.setVisible(false);
            changeLanguagePane.setVisible(false);
            changeThemePane.setVisible(false);

            // Make label option selected bold and reset other option to normal
            usernameOption.getChildren().get(0).setStyle("-fx-font-weight: bold");
            emailOption.getChildren().get(0).setStyle("-fx-font-weight: normal");
            passwordOption.getChildren().get(0).setStyle("-fx-font-weight: normal");
            languageOption.getChildren().get(0).setStyle("-fx-font-weight: normal");
            themeOption.getChildren().get(0).setStyle("-fx-font-weight: normal");
            btnCloseErrorMsg();
            changePasswordPane.setPrefHeight(0);

            // Change the text in top selected box
            headerLabel.setText("Username");
            contentLabel.setText("Change your username");
            
            // Remove value from field
            newUsernameUserPart.setText("");
            currentPasswordUserPart.setText("");
        });
        emailOption.setOnMouseClicked(e -> {
            changeUsernamePane.setVisible(false);
            changeEmailPane.setVisible(true);
            changePasswordPane.setVisible(false);
            changeLanguagePane.setVisible(false);
            changeThemePane.setVisible(false);
            usernameOption.getChildren().get(0).setStyle("-fx-font-weight: normal");
            emailOption.getChildren().get(0).setStyle("-fx-font-weight: bold");
            passwordOption.getChildren().get(0).setStyle("-fx-font-weight: normal");
            languageOption.getChildren().get(0).setStyle("-fx-font-weight: normal");
            themeOption.getChildren().get(0).setStyle("-fx-font-weight: normal");
            btnCloseErrorMsg();
            changePasswordPane.setPrefHeight(0);

            // Change the text in top selected box
            headerLabel.setText("Email");
            contentLabel.setText("Change your email");
            
            // Remove value from field
            newEmailEmailPart.setText("");
            currentPasswordEmailPart.setText("");
        });
        passwordOption.setOnMouseClicked(e -> {
            changeUsernamePane.setVisible(false);
            changeEmailPane.setVisible(false);
            changePasswordPane.setVisible(true);
            changeLanguagePane.setVisible(false);
            changeThemePane.setVisible(false);
            usernameOption.getChildren().get(0).setStyle("-fx-font-weight: normal");
            emailOption.getChildren().get(0).setStyle("-fx-font-weight: normal");
            passwordOption.getChildren().get(0).setStyle("-fx-font-weight: bold");
            languageOption.getChildren().get(0).setStyle("-fx-font-weight: normal");
            themeOption.getChildren().get(0).setStyle("-fx-font-weight: normal");
            btnCloseErrorMsg();
            changePasswordPane.setPrefHeight(252);

            // Change the text in top selected box
            headerLabel.setText("Password");
            contentLabel.setText("Change your password");
            
            // Remove value from field
            newPasswordPassPart.setText("");
            currentPasswordPassPart.setText("");
            verifyPasswordPassPart.setText("");
        });
        languageOption.setOnMouseClicked(e -> {
            changeUsernamePane.setVisible(false);
            changeEmailPane.setVisible(false);
            changePasswordPane.setVisible(false);
            changeLanguagePane.setVisible(true);
            changeThemePane.setVisible(false);
            usernameOption.getChildren().get(0).setStyle("-fx-font-weight: normal");
            emailOption.getChildren().get(0).setStyle("-fx-font-weight: normal");
            passwordOption.getChildren().get(0).setStyle("-fx-font-weight: normal");
            languageOption.getChildren().get(0).setStyle("-fx-font-weight: bold");
            themeOption.getChildren().get(0).setStyle("-fx-font-weight: normal");
            btnCloseErrorMsg();
            changePasswordPane.setPrefHeight(0);

            // Change the text in top selected box
            headerLabel.setText("Language");
            contentLabel.setText("Change the language of this application");
            
            comboLanguage.getSelectionModel().select("English");
        });
        themeOption.setOnMouseClicked(e -> {
            changeUsernamePane.setVisible(false);
            changeEmailPane.setVisible(false);
            changePasswordPane.setVisible(false);
            changeLanguagePane.setVisible(false);
            changeThemePane.setVisible(true);
            usernameOption.getChildren().get(0).setStyle("-fx-font-weight: normal");
            emailOption.getChildren().get(0).setStyle("-fx-font-weight: normal");
            passwordOption.getChildren().get(0).setStyle("-fx-font-weight: normal");
            languageOption.getChildren().get(0).setStyle("-fx-font-weight: normal");
            themeOption.getChildren().get(0).setStyle("-fx-font-weight: bold");
            btnCloseErrorMsg();
            changePasswordPane.setPrefHeight(0);

            // Change the text in top selected box
            headerLabel.setText("Theme");
            contentLabel.setText("Change the theme of this application");
            
            comboTheme.getSelectionModel().select("Blue");
        });

    }

    @FXML
    private void btnSave() {
        if (changeUsernamePane.isVisible()) {
            saveUsername();
        } else if (changeEmailPane.isVisible()) {
            saveEmail();
        } else if (changePasswordPane.isVisible()) {
            savePassword();
        } else if (changeLanguagePane.isVisible()) {
            saveLanguage();
        } else if (changeThemePane.isVisible()) {
            saveTheme();
        }
    }

    private void saveUsername() {
        if (!newUsernameUserPart.getText().trim().toLowerCase().matches("[a-z0-9_]{4,}")) {
            showErrorMsg("New username error !");
            return;
        }
        if (currentPasswordUserPart.getText().length() < 8) {
            showErrorMsg("Password error !");
            return;
        }

        // Change Username
        int status = new UserDao().changeUsername(newUsernameUserPart.getText(), currentPasswordUserPart.getText());

        switch (status) {
            case -2:
                showErrorMsg("This username already exists !");
                break;
            case -1:
                showErrorMsg("Connection failed !");
                break;
            case 0:
                showErrorMsg("Password incorrect !");
                break;
            case 1:
                showErrorMsg("Successfuly save change !");
                newUsernameUserPart.setText("");
                currentPasswordUserPart.setText("");
                break;
        }
    }

    private void saveEmail() {
        if (!newEmailEmailPart.getText().trim().matches("[a-zA-Z_][\\w]*[-]{0,4}[\\w]+@[a-zA-Z0-9]+.[a-zA-Z]{2,6}")) {
            showErrorMsg("New email incorrect !");
            return;
        }
        if (currentPasswordEmailPart.getText().length() < 8) {
            showErrorMsg("Password  incorrect !");
            return;
        }

        // Change Username
        int status = new UserDao().changeEmail(newEmailEmailPart.getText(), currentPasswordEmailPart.getText());

        switch (status) {
            case -2:
                showErrorMsg("This email already exists !");
                break;
            case -1:
                showErrorMsg("Connection failed !");
                break;
            case 0:
                showErrorMsg("Password Incorrect !");
                break;
            case 1:
                showErrorMsg("Successfuly save change !");
                newEmailEmailPart.setText("");
                currentPasswordEmailPart.setText("");
                break;
        }
    }

    private void savePassword() {
        if (currentPasswordPassPart.getText().length() < 8) {
            showErrorMsg("Current password incorrect !");
            return;
        }
        if (newPasswordPassPart.getText().length() < 8) {
            showErrorMsg("New password incorrect !");
            return;
        }

        if (verifyPasswordPassPart.getText().length() < 8 || !newPasswordPassPart.getText().equals(verifyPasswordPassPart.getText())) {
            showErrorMsg("Verify password incorrect !");
            return;
        }

        // Change Username
        int status = new UserDao().changePassword(currentPasswordPassPart.getText(), newPasswordPassPart.getText());

        switch (status) {
            case -1:
                showErrorMsg("Connection failed !");
                break;
            case 0:
                showErrorMsg("Current password incorrect !");
                break;
            case 1: {
                showErrorMsg("Successfuly save change !");
                currentPasswordPassPart.setText("");
                newPasswordPassPart.setText("");
                verifyPasswordPassPart.setText("");
                break;
            }
        }
    }

    private void saveLanguage() {
        showErrorMsg("Coming soon !");
    }

    private void saveTheme() {
        showErrorMsg("Coming soon !");
    }
    
    private void showIconPassPart() {
        // This method show / hide password
        if (newPasswordPassPart.getText().isEmpty() || verifyPasswordPassPart.getText().isEmpty()) {
            iconValid.setVisible(false);
            return;
        }
        if (newPasswordPassPart.getText().equals(verifyPasswordPassPart.getText())) {
            iconValid.setVisible(true);
        } else {
            iconValid.setVisible(false);
        }
    }

    private void showErrorMsg(String msg) {
        errorLabel.setText(msg);
        boxError.setPrefHeight(48);
        boxError.setVisible(true);
    }

    @FXML
    private void btnCloseErrorMsg() {
        boxError.setPrefHeight(0);
        boxError.setVisible(false);
    }

}
