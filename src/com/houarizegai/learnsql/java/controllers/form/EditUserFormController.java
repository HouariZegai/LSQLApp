package com.houarizegai.learnsql.java.controllers.form;

import com.houarizegai.learnsql.java.controllers.ManageAccountController;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
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

public class EditUserFormController implements Initializable {

    @FXML
    private VBox root;
    @FXML
    private JFXTextField userNameField;
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

    public static User userSelected; // this variable using to fill the edit form

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // This ComboBox for Edit Account
        comboSection.getItems().addAll(new String[]{"1", "2", "3", "4"});
        comboGroup.getItems().addAll(new String[]{"1", "2", "3"});

        root.setOnKeyPressed(event -> { // Add Key Listener, if i click to the Enter Button Call btnLogin() method
            if (event.getCode().equals(ENTER)) {
                btnEdit();
            }
        });

        userNameField.setText(userSelected.getUsername());
        firstNameField.setText(userSelected.getFirstName());
        lastNameField.setText(userSelected.getLastName());
        datePicker.setValue(userSelected.getDateOfBirth().toLocalDate());
        emailField.setText(userSelected.getEmail());
        isTeacherToggleButton.setSelected(userSelected.getIsTeacher());
        if (userSelected.getIsTeacher()) {
            actionToggleButton(); // for hide the Section & group comboBox
        }
        if (!userSelected.getIsTeacher()) {
            comboSection.getSelectionModel().select(userSelected.getSection());
            comboGroup.getSelectionModel().select(userSelected.getGroup());
        }

        toastErrorMsg = new JFXSnackbar(root);
        
        userNameField.setOnKeyReleased(event -> { // this event for check max length of the answer area
            if (userNameField.getText().length() > 25) {
                userNameField.setText(userNameField.getText().substring(0, 25));
                userNameField.positionCaret(userNameField.getText().length());
            }
        });
    }

    @FXML
    public void btnEdit() { // Add New Student
        if (!userNameField.getText().trim().toLowerCase().matches("[a-z0-9_]{4,}")) {
            toastErrorMsg.show("Username error", 1500);
            return;
        }
        if (!firstNameField.getText().trim().toLowerCase().matches("[a-z]{4,}")) {
            toastErrorMsg.show("First name error!", 1500);

            return;
        }
        if (!lastNameField.getText().trim().toLowerCase().matches("[a-z]{4,}")) {
            toastErrorMsg.show("Last name error!", 1500);
            return;
        }
        if (datePicker.getValue() == null) {
            toastErrorMsg.show("Please fill Date of Birth!", 1500);
            return;
        }
        if (!emailField.getText().trim().matches("[a-zA-Z_][\\w]*[-]{0,4}[\\w]+@[a-zA-Z0-9]+.[a-zA-Z]{2,6}")) {
            toastErrorMsg.show("Email error!", 1500);
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
        user.setId(userSelected.getId());
        user.setUsername(userNameField.getText().trim().toLowerCase());
        user.setFirstName(firstNameField.getText().trim().toLowerCase());
        user.setLastName(lastNameField.getText().trim().toLowerCase());

        user.setDateOfBirth(Date.valueOf(datePicker.getValue()));

        user.setEmail(emailField.getText().trim().toLowerCase());
        user.setIsTeacher(isTeacherToggleButton.isSelected());
        if (!isTeacherToggleButton.isSelected()) {
            user.setSection(Integer.parseInt(comboSection.getSelectionModel().getSelectedItem()));
            user.setGroup(Integer.parseInt(comboGroup.getSelectionModel().getSelectedItem()));
        }
        int status = new UserDao().editUser(user);
        switch (status) {
            case -1:
                toastErrorMsg.show("Connection failed !", 1500);
                break;
            case 0:
                toastErrorMsg.show("User not edited !", 1500);
                break;
            case 2:
                toastErrorMsg.show("Userame already exists !", 1500);
                break;    
            case 1: {
                Notifications.create()
                        .title("You Successfuly edited user !")
                        .graphic(new ImageView(new Image("/com/houarizegai/learnsql/resources/images/icons/valid.png")))
                        .hideAfter(Duration.millis(2000))
                        .position(Pos.BOTTOM_RIGHT)
                        .darkStyle()
                        .show();

                ManageAccountController.editUserDialog.close();
            }
        }
    }

    @FXML
    public void btnReset() {
        userNameField.setText(userSelected.getUsername());
        firstNameField.setText(userSelected.getFirstName());
        lastNameField.setText(userSelected.getLastName());
        datePicker.setValue(userSelected.getDateOfBirth().toLocalDate());
        emailField.setText(userSelected.getEmail());
        isTeacherToggleButton.setSelected(userSelected.getIsTeacher());
        if (userSelected.getIsTeacher()) {
            comboSection.setVisible(false);
            comboGroup.setVisible(false);
        } else {
            comboSection.setVisible(true);
            comboGroup.setVisible(true);
            comboSection.getSelectionModel().select(userSelected.getSection());
            comboGroup.getSelectionModel().select(userSelected.getGroup());
        }
    }

    @FXML
    public void btnCancel() {
        ManageAccountController.editUserDialog.close();
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
        ManageAccountController.editUserDialog.close();
    }

}
