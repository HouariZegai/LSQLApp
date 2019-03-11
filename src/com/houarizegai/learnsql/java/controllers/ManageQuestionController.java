package com.houarizegai.learnsql.java.controllers;

import com.houarizegai.learnsql.java.controllers.form.EditQuestionFormController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.houarizegai.learnsql.java.dao.QuestionDao;
import com.houarizegai.learnsql.java.models.Question;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import static com.houarizegai.learnsql.java.controllers.form.EditQuestionFormController.questionSelected;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class ManageQuestionController implements Initializable {

    @FXML
    public StackPane root;
    @FXML
    private Label titleLabel;
    @FXML
    public Label errorLabel;
    @FXML
    public JFXTextField searchField;
    @FXML
    public JFXComboBox<String> comboSearchBy;
    @FXML
    public JFXTreeTableView treeTableView;
    @FXML // Cols of table
    public JFXTreeTableColumn<QuestionTable, String> idCol, questionTypeCol, questionCol, answerCol;

    public static JFXDialog addQuestionDialog, editQuestionDialog;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        comboSearchBy.getItems().addAll(new String[]{"Id", "Question Type", "Question", "Answer"});
        editQuestionDialog = new JFXDialog();

        initializeTable();

    }

    class QuestionTable extends RecursiveTreeObject<QuestionTable> {

        StringProperty id;
        StringProperty questionType;
        StringProperty question;
        StringProperty answer;

        public QuestionTable(int id, String questionType, String question, String answer) {
            this.id = new SimpleStringProperty(String.valueOf(id));
            this.questionType = new SimpleStringProperty(questionType);
            this.question = new SimpleStringProperty(question);
            this.answer = new SimpleStringProperty(answer);
        }
    }

    public void initializeTable() {
        idCol = new JFXTreeTableColumn<>("Id");
        idCol.setPrefWidth(50);
        idCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<QuestionTable, String> param) -> param.getValue().getValue().id);

        questionTypeCol = new JFXTreeTableColumn<>("Type");
        questionTypeCol.setPrefWidth(90);
        questionTypeCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<QuestionTable, String> param) -> param.getValue().getValue().questionType);

        questionCol = new JFXTreeTableColumn<>("Question");
        questionCol.setPrefWidth(550);
        questionCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<QuestionTable, String> param) -> param.getValue().getValue().question);

        answerCol = new JFXTreeTableColumn<>("Answer");
        answerCol.setPrefWidth(450);
        answerCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<QuestionTable, String> param) -> param.getValue().getValue().answer);

        updateTable(); // Fill table by data imported from data base

        treeTableView.getColumns().addAll(idCol, questionTypeCol, questionCol, answerCol);
        treeTableView.setShowRoot(false);

        searchField.textProperty().addListener(e -> {
            filterSearchTable();
        });
        comboSearchBy.setOnAction(e -> {
            filterSearchTable();
        });
    }
    
    public void filterSearchTable() {
        treeTableView.setPredicate(new Predicate<TreeItem<QuestionTable>>() {
                @Override
                public boolean test(TreeItem<QuestionTable> user) {
                    switch (comboSearchBy.getSelectionModel().getSelectedIndex()) {
                        case 0:
                            return user.getValue().id.getValue().toLowerCase().contains(searchField.getText().toLowerCase());
                        case 1:
                            return user.getValue().questionType.getValue().toLowerCase().contains(searchField.getText().toLowerCase());
                        case 2:
                            return user.getValue().question.getValue().toLowerCase().contains(searchField.getText().toLowerCase());
                        case 3:
                            return user.getValue().answer.getValue().toLowerCase().contains(searchField.getText().toLowerCase());

                        default:
                            return user.getValue().id.getValue().toLowerCase().contains(searchField.getText().toLowerCase())
                                    || user.getValue().questionType.getValue().toLowerCase().contains(searchField.getText().toLowerCase())
                                    || user.getValue().question.getValue().toLowerCase().contains(searchField.getText().toLowerCase())
                                    || user.getValue().answer.getValue().toLowerCase().contains(searchField.getText().toLowerCase());
                    }
                }
            });
        
    }

    @FXML
    public void updateTable() { // get data from db and inserted to the table
        errorLabel.setText("");
        ObservableList<QuestionTable> questions = FXCollections.observableArrayList();

        List<Question> questionsDao = new QuestionDao().getQuestion(); // Get users from DB
        if (questionsDao == null) {
            errorLabel.setText("Connection Failed !");
        } else {
            for (Question question : questionsDao) {
                questions.add(new QuestionTable(question.getId(), question.getQuestionType().substring(0, 1).toUpperCase() + question.getQuestionType().substring(1), question.getQuestion(), question.getAnswer()));
            }
        }

        final TreeItem<QuestionTable> treeItem = new RecursiveTreeItem<>(questions, RecursiveTreeObject::getChildren);
        try {
            treeTableView.setRoot(treeItem);
        } catch (Exception ex) {
            System.out.println("Error catched !");
        }
    }

    @FXML
    public void btnEdit() { // if i click to the edit button
        
        errorLabel.setText("");
        int index = treeTableView.getSelectionModel().getSelectedIndex(); // selected index
        String id = idCol.getCellData(index);
        if (id == null) {
            //System.out.println("Index is null !");
            return;
        }

        questionSelected = new Question(Integer.parseInt(id), questionTypeCol.getCellData(index),
                questionCol.getCellData(index), answerCol.getCellData(index));

        AnchorPane editQuestionPane = null;
        try {
            editQuestionPane = FXMLLoader.load(getClass().getResource("/com/houarizegai/learnsql/resources/views/form/EditQuestionForm.fxml"));
        } catch (IOException ex) {
            //System.out.println("Catched error FXML loader!");
            Logger.getLogger(EditQuestionFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        editQuestionDialog = getSpecialDialog(editQuestionPane);
        editQuestionDialog.show();
    }

    @FXML
    public void btnRemove() { // if i click to the remove button
        errorLabel.setText("");
        String id = idCol.getCellData(treeTableView.getSelectionModel().getSelectedIndex());
        if (id == null) {
            System.out.println("Index is null !");
            return;
        }

        JFXDialogLayout content = new JFXDialogLayout();
        Text headerText = new Text("Confirmation");
        Text contentText = new Text("Are you sure to delete this question ?");
        headerText.setStyle("-fx-font-size: 19px");
        contentText.setStyle("-fx-font-size: 18px");

        content.setHeading(headerText);
        content.setBody(contentText);

        JFXDialog dialog = new JFXDialog(root, content, JFXDialog.DialogTransition.CENTER);

        JFXButton btnOk = new JFXButton("Yes");
        btnOk.setOnAction(e -> {
            int status = new QuestionDao().deleteQuestion(id);
            System.out.println("status : " + status);
            if (status == -1) {
                errorLabel.setText("Connection Failed !");
            } else {
                Notifications.create()
                        .title("You Successfuly removed question !")
                        .graphic(new ImageView(new Image("/com/houarizegai/learnsql/resources/images/icons/valid.png")))
                        .hideAfter(Duration.millis(2000))
                        .position(Pos.BOTTOM_RIGHT)
                        .darkStyle()
                        .show();
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
    public void btnAdd() {
        
        errorLabel.setText("");
        
        AnchorPane addQuestionPane = null;
        try {
            addQuestionPane = FXMLLoader.load(getClass().getResource("/com/houarizegai/learnsql/resources/views/form/AddQuestionForm.fxml"));
            addQuestionDialog = getSpecialDialog(addQuestionPane);
            addQuestionDialog.show();
        } catch (IOException ex) {
            Logger.getLogger(ManageAccountController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public JFXDialog getSpecialDialog(AnchorPane content) {
        JFXDialog dialog = new JFXDialog(root, content, JFXDialog.DialogTransition.CENTER);
        dialog.setOnDialogClosed((event) -> {
            System.out.println("Passed ..!");
            updateTable();
        });
        return dialog;
    }
}
