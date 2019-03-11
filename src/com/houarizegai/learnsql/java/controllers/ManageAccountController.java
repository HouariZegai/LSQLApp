package com.houarizegai.learnsql.java.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import static com.houarizegai.learnsql.java.controllers.form.EditUserFormController.userSelected;
import com.houarizegai.learnsql.java.dao.UserDao;
import com.houarizegai.learnsql.java.models.User;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class ManageAccountController implements Initializable {

    @FXML
    public StackPane root;
    @FXML
    private Label titleLabel;
    @FXML
    public Label errorLabel;
    @FXML
    public JFXTextField searchField;
    @FXML
    public JFXComboBox<String> combo;
    @FXML
    public JFXTreeTableView treeTableView;
    @FXML // Cols of table
    public JFXTreeTableColumn<UserTable, String> idCol, usernameCol, firstNameCol, lastNameCol,
            dateOfBirthCol, emailCol, userTypeCol, sectionCol, groupCol;

    public static JFXDialog addUserDialog, editUserDialog;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        combo.getItems().addAll(new String[]{"Id", "Username", "First Name", "Last Name", "Date of birth", "Email", "Type", "Section", "Group"});

        initializeTable();

    }
    
    class UserTable extends RecursiveTreeObject<UserTable> {
    
        StringProperty id;
        StringProperty username;
        StringProperty firstName;
        StringProperty lastName;
        StringProperty dateOfBirth;
        StringProperty email;
        StringProperty studentOrTeacher;
        StringProperty section;
        StringProperty group;

        public UserTable(int id, String username, String firstName, String lastName, Date dateOfBirth, String email, boolean isTeacher, int section, int group) {
            this.id = new SimpleStringProperty(String.valueOf(id));
            this.username = new SimpleStringProperty(username);
            this.firstName = new SimpleStringProperty(firstName);
            this.lastName = new SimpleStringProperty(lastName);
            this.dateOfBirth = new SimpleStringProperty(dateOfBirth.toString());
            this.email = new SimpleStringProperty(email);
            this.studentOrTeacher = new SimpleStringProperty((isTeacher) ? "Teacher" : "Student");
            this.section = new SimpleStringProperty(String.valueOf(section));
            this.group = new SimpleStringProperty(String.valueOf(group));
        }
    }

    public void initializeTable() {
        idCol = new JFXTreeTableColumn<>("Id");
        idCol.setPrefWidth(50);
        idCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<UserTable, String> param) -> param.getValue().getValue().id);

        usernameCol = new JFXTreeTableColumn<>("Username");
        usernameCol.setPrefWidth(150);
        usernameCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<UserTable, String> param) -> param.getValue().getValue().username);

        firstNameCol = new JFXTreeTableColumn<>("First Name");
        firstNameCol.setPrefWidth(200);
        firstNameCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<UserTable, String> param) -> param.getValue().getValue().firstName);

        lastNameCol = new JFXTreeTableColumn<>("Last Name");
        lastNameCol.setPrefWidth(150);
        lastNameCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<UserTable, String> param) -> param.getValue().getValue().lastName);

        dateOfBirthCol = new JFXTreeTableColumn<>("Date Of Birth");
        dateOfBirthCol.setPrefWidth(100);
        dateOfBirthCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<UserTable, String> param) -> param.getValue().getValue().dateOfBirth);

        emailCol = new JFXTreeTableColumn<>("Email");
        emailCol.setPrefWidth(200);
        emailCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<UserTable, String> param) -> param.getValue().getValue().email);

        userTypeCol = new JFXTreeTableColumn<>("Type");
        userTypeCol.setPrefWidth(95);
        userTypeCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<UserTable, String> param) -> param.getValue().getValue().studentOrTeacher);

        sectionCol = new JFXTreeTableColumn<>("Section");
        sectionCol.setPrefWidth(100);
        sectionCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<UserTable, String> param) -> param.getValue().getValue().section);

        groupCol = new JFXTreeTableColumn<>("Group");
        groupCol.setPrefWidth(100);
        groupCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<UserTable, String> param) -> param.getValue().getValue().group);

        updateTable(); // Fill table by data imported from data base

        treeTableView.getColumns().addAll(idCol, usernameCol, firstNameCol, lastNameCol, dateOfBirthCol, emailCol, userTypeCol, sectionCol, groupCol);
        treeTableView.setShowRoot(false);
        // this line bellow to select multi rows
        //treeTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        searchField.textProperty().addListener(e -> {
            filterSearchTable(); 
        });
        
        combo.setOnAction(e-> {
            filterSearchTable();
        });
        
    }

    public void filterSearchTable() {
        treeTableView.setPredicate(new Predicate<TreeItem<UserTable>>() {
                @Override
                public boolean test(TreeItem<UserTable> user) {
                    switch (combo.getSelectionModel().getSelectedIndex()) {
                        case 0:
                            return user.getValue().id.getValue().toLowerCase().contains(searchField.getText().toLowerCase());
                        case 1:
                            return user.getValue().username.getValue().toLowerCase().contains(searchField.getText().toLowerCase());
                        case 2:
                            return user.getValue().firstName.getValue().toLowerCase().contains(searchField.getText().toLowerCase());
                        case 3:
                            return user.getValue().lastName.getValue().toLowerCase().contains(searchField.getText().toLowerCase());
                        case 4:
                            return user.getValue().dateOfBirth.getValue().toLowerCase().contains(searchField.getText().toLowerCase());
                        case 5:
                            return user.getValue().email.getValue().toLowerCase().contains(searchField.getText().toLowerCase());
                        case 6:
                            return user.getValue().studentOrTeacher.getValue().toLowerCase().contains(searchField.getText().toLowerCase());
                        case 7:
                            return user.getValue().section.getValue().toLowerCase().contains(searchField.getText().toLowerCase());
                        case 8:
                            return user.getValue().group.getValue().toLowerCase().contains(searchField.getText().toLowerCase());
                        default:
                            return user.getValue().id.getValue().toLowerCase().contains(searchField.getText().toLowerCase())
                                    || user.getValue().username.getValue().toLowerCase().contains(searchField.getText().toLowerCase())
                                    || user.getValue().firstName.getValue().toLowerCase().contains(searchField.getText().toLowerCase())
                                    || user.getValue().lastName.getValue().toLowerCase().contains(searchField.getText().toLowerCase())
                                    || user.getValue().dateOfBirth.getValue().toLowerCase().contains(searchField.getText().toLowerCase())
                                    || user.getValue().email.getValue().toLowerCase().contains(searchField.getText().toLowerCase())
                                    || user.getValue().studentOrTeacher.getValue().toLowerCase().contains(searchField.getText().toLowerCase())
                                    || user.getValue().section.getValue().toLowerCase().contains(searchField.getText().toLowerCase())
                                    || user.getValue().group.getValue().toLowerCase().contains(searchField.getText().toLowerCase());
                    }
                }
            });
    }
    
    @FXML
    public void updateTable() { // get data from db and inserted to the table
        errorLabel.setText("");
        ObservableList<UserTable> users = FXCollections.observableArrayList();

        List<User> usersDao = new UserDao().getUsers(); // Get users from DB
        if (usersDao == null) {
            errorLabel.setText("Connection Failed !");
        } else {
            for (User user : usersDao) {
                users.add(new UserTable(user.getId(), user.getUsername(), user.getFirstName().substring(0, 1).toUpperCase() + user.getFirstName().substring(1),
                        user.getLastName().toUpperCase(), user.getDateOfBirth(), user.getEmail(), user.getIsTeacher(),
                        user.getSection(), user.getGroup()));
            }
        }

        final TreeItem<UserTable> treeItem = new RecursiveTreeItem<>(users, RecursiveTreeObject::getChildren);
        try {
            treeTableView.setRoot(treeItem);
        } catch (Exception ex) {
            System.out.println("Error catched !");
        }
    }

    @FXML
    public void editUser() { // if i click to the edit button
        errorLabel.setText("");
        int index = treeTableView.getSelectionModel().getSelectedIndex(); // selected index
        String id = idCol.getCellData(index);
        if (id == null) {
            return;
        }

        userSelected = new User();
        userSelected.setId(Integer.parseInt(id));
        userSelected.setUsername(usernameCol.getCellData(index));
        userSelected.setFirstName(firstNameCol.getCellData(index));
        userSelected.setLastName(lastNameCol.getCellData(index));
        userSelected.setDateOfBirth(Date.valueOf(dateOfBirthCol.getCellData(index)));
        userSelected.setEmail(emailCol.getCellData(index));
        userSelected.setIsTeacher(userTypeCol.getCellData(index).toLowerCase().equals("teacher"));
        if (userTypeCol.getCellData(index).toLowerCase().equals("student")) {
            userSelected.setSection(Integer.parseInt(sectionCol.getCellData(index)) - 1);
            userSelected.setGroup(Integer.parseInt(groupCol.getCellData(index)) - 1);
        }

        AnchorPane editUserPane = null;
        try {
            editUserPane = FXMLLoader.load(getClass().getResource("/com/houarizegai/learnsql/resources/views/form/EditUserForm.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(ManageAccountController.class.getName()).log(Level.SEVERE, null, ex);
        }
        editUserDialog = getSpecialDialog(editUserPane);
        editUserDialog.show();
    }

    @FXML
    public void removeUser() { // if i click to the remove button
        errorLabel.setText("");
        String id = idCol.getCellData(treeTableView.getSelectionModel().getSelectedIndex());
        if (id == null) {
            System.out.println("Index is null !");
            return;
        }

        JFXDialogLayout content = new JFXDialogLayout();
        Text headerText = new Text("Confirmation");
        Text contentText = new Text("Are you sure to delete this user ?");
        headerText.setStyle("-fx-font-size: 19px");
        contentText.setStyle("-fx-font-size: 18px");

        content.setHeading(headerText);
        content.setBody(contentText);

        JFXDialog dialog = new JFXDialog(root, content, JFXDialog.DialogTransition.CENTER);

        JFXButton btnOk = new JFXButton("Yes");
        btnOk.setOnAction(e -> {
            int status = new UserDao().deleteUser(id);
            System.out.println("status : " + status);
            if (status == -1) {
                errorLabel.setText("Connection Failed !");
            } else {
//                Notifications notification = Notifications.create()
//                        .title("You Successfuly removed user !")
//                        .graphic(new ImageView(new Image("/com/houarizegai/learnsql/resources/images/icons/valid.png")))
//                        .hideAfter(Duration.millis(2000))
//                        .position(Pos.BOTTOM_RIGHT);
//                notification.darkStyle();
//                notification.show();
                updateTable();
            }
            dialog.close();
        });

        JFXButton btnNo = new JFXButton("No");
        btnOk.setPrefSize(120, 40);
        btnNo.setPrefSize(120, 40);
        btnOk.setStyle("-fx-font-size: 18px");
        btnNo.setStyle("-fx-font-size: 18px");

        content.setActions(btnOk, btnNo);
        StackPane stackpane = new StackPane();

        dialog.getStylesheets().add("/com/houarizegai/learnsql/resources/css/main.css");
        btnNo.setOnAction(e -> {
            dialog.close();
        });
        dialog.show();
    }

    @FXML
    public void addUser() {
        errorLabel.setText("");
        AnchorPane addUserPane = null;
        try {
            addUserPane = FXMLLoader.load(getClass().getResource("/com/houarizegai/learnsql/resources/views/form/AddUserForm.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(ManageAccountController.class.getName()).log(Level.SEVERE, null, ex);
        }
        addUserDialog = getSpecialDialog(addUserPane);
        addUserDialog.show();
    }

    public JFXDialog getSpecialDialog(AnchorPane content) {
        JFXDialog dialog = new JFXDialog(root, content, JFXDialog.DialogTransition.CENTER);
        dialog.setOnDialogClosed((event) -> {
            updateTable();
        });
        return dialog;
    }

}
