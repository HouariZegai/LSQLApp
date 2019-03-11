package com.houarizegai.learnsql.java.controllers.form;

import com.houarizegai.learnsql.java.controllers.ManageAccountController;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import com.houarizegai.learnsql.java.dao.UserDao;
import com.houarizegai.learnsql.java.models.User;
import java.net.URL;
import java.sql.Date;
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

public class AddUserFormController implements Initializable {

    /* Start Add Account Pane */
    @FXML
    private VBox root;
    @FXML
    private JFXTextField userNameField;
    @FXML
    private JFXPasswordField passwordField;
    @FXML
    private JFXTextField firstNameField;
    @FXML
    private JFXTextField lastNameField;
    @FXML
    private JFXDatePicker datePicker;
    @FXML
    private JFXTextField emailField;
    @FXML
    private JFXToggleButton isTeacherToggleButton;
    @FXML
    private JFXComboBox<String> comboSection, comboGroup;
    private JFXSnackbar toastErrorMsg; // Error Message

    /* End Add Account Pane */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // This ComboBox for Add Account
        comboSection.getItems().addAll(new String[]{"1", "2", "3", "4"});
        comboGroup.getItems().addAll(new String[]{"1", "2", "3"});

        /* End ComboBox */
        root.setOnKeyPressed(event -> { // Add Key Listener, if i click to the Enter Button Call btnLogin() method
            if (event.getCode().equals(ENTER)) {
                btnAdd();
            }
        });
        toastErrorMsg = new JFXSnackbar(root);
        
        userNameField.setOnKeyReleased(event -> { // this event for check max length of the answer area
            if (userNameField.getText().length() > 25) {
                userNameField.setText(userNameField.getText().substring(0, 25));
                userNameField.positionCaret(userNameField.getText().length());
            }
        });
        passwordField.setOnKeyReleased(event -> { // this event for check max length of the answer area
            if (passwordField.getText().length() > 50) {
                passwordField.setText(passwordField.getText().substring(0, 50));
                passwordField.positionCaret(passwordField.getText().length());
            }
        });
    }

    /* Start Add Account Action Part */
    @FXML
    public void btnClear() {
        userNameField.setText("");
        passwordField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        datePicker.setValue(null);
        emailField.setText("");
        isTeacherToggleButton.setSelected(false);
        comboSection.setVisible(true);
        comboGroup.setVisible(true);
        comboSection.getSelectionModel().select(null);
        comboGroup.getSelectionModel().select(null);
    }

    @FXML
    public void btnAdd() { // Add New Student
        if (!userNameField.getText().trim().toLowerCase().matches("[a-z0-9_]{4,}")) {
            toastErrorMsg.show("Username error !", 1500);
            return;
        }
        if (passwordField.getText().length() < 8) {
            toastErrorMsg.show("Password error !", 1500);
            return;
        }
        if (!firstNameField.getText().trim().toLowerCase().matches("[a-z]{3,}")) {
            toastErrorMsg.show("First name error !", 1500);
            return;
        }
        if (!lastNameField.getText().trim().toLowerCase().matches("[a-z]{3,}")) {
            toastErrorMsg.show("Last name error !", 1500);
            return;
        }
        if (datePicker.getValue() == null) {
            toastErrorMsg.show("Please fill Date of Birth !", 1500);
            return;
        }
        if (!emailField.getText().trim().matches("[a-zA-Z_][\\w]*[-]{0,4}[\\w]+@[a-zA-Z0-9]+.[a-zA-Z]{2,6}")) {
            toastErrorMsg.show("Email error !", 1500);
            return;
        }
        if (!isTeacherToggleButton.isSelected()) {
            if (comboSection.getSelectionModel().getSelectedItem() == null) {
                toastErrorMsg.show("Please fill section !", 1500);
                return;
            }
            if (comboGroup.getSelectionModel().getSelectedItem() == null) {
                toastErrorMsg.show("Please fill group !", 1500);
                return;
            }
        }

        User user = new User();
        user.setUsername(userNameField.getText().trim().toLowerCase());
        user.setPassword(passwordField.getText());
        user.setFirstName(firstNameField.getText().trim().toLowerCase());
        user.setLastName(lastNameField.getText().trim().toLowerCase());

        System.out.println("Date : " + datePicker.getValue().toString());
        user.setDateOfBirth(Date.valueOf(datePicker.getValue()));

        user.setEmail(emailField.getText().trim().toLowerCase());
        user.setIsTeacher(isTeacherToggleButton.isSelected());
        if (!isTeacherToggleButton.isSelected()) {
            user.setSection(Integer.parseInt(comboSection.getSelectionModel().getSelectedItem()));
            user.setGroup(Integer.parseInt(comboGroup.getSelectionModel().getSelectedItem()));
        }
        int status = new UserDao().addUser(user);
        switch (status) {
            case -1:
                toastErrorMsg.show("Connection failed :", 1500);
                break;
            case 0:
                toastErrorMsg.show("User not added :", 1500);
                break;
            case 2:
                toastErrorMsg.show("Username aleady exists !", 1500);
                break;
            case 1: {
                Notifications.create()
                        .title("You Successfuly added new user !")
                        .graphic(new ImageView(new Image("/com/houarizegai/learnsql/resources/images/icons/valid.png")))
                        .hideAfter(Duration.millis(2000))
                        .position(Pos.BOTTOM_RIGHT)
                        .darkStyle()
                        .show();

                ManageAccountController.addUserDialog.close();
            }
        }
    }

    @FXML
    public void actionToggleButton() {
        if (isTeacherToggleButton.isSelected()) {
            comboSection.setVisible(false);
            comboGroup.setVisible(false);
        } else {
            comboSection.setVisible(true);
            comboGroup.setVisible(true);
        }
    }

    @FXML
    private void btnClose() {
        ManageAccountController.addUserDialog.close();
    }
}
