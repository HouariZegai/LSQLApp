package com.houarizegai.learnsql.java.controllers;

import static com.houarizegai.learnsql.java.controllers.form.ViewStatisticQuestionController.questionStatisticDao;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.houarizegai.learnsql.java.dao.TracDao;
import com.houarizegai.learnsql.java.dao.UserDao;
import com.houarizegai.learnsql.java.models.TracQuestion;
import com.houarizegai.learnsql.java.models.TracUser;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class TracController implements Initializable {

    @FXML
    public StackPane root;
    @FXML // First pane in traceability part
    private AnchorPane choosePane;

    @FXML // using to show or hide tools option Box
    private VBox studentPane, questionPane;
    @FXML // using to show the error msg to the user
    public Label errorLabelUser, errorLabelQuestion;
    @FXML // using the sreach inside the table
    private JFXTextField searchUserField, searchQuestionField;
    @FXML // select the type of selection
    public JFXComboBox<String> comboUserSearchBy, comboQuestionSearchBy;
    @FXML // panel containe the tools option 
    private HBox searchToolsBox;
    @FXML
    private JFXComboBox<String> comboUserSectionFilter, comboUserGroupFilter;

    // This variable contains all section anf group using in advanced search (Tools)
    private Map<String, List<String>> sectionGroup = null;

    @FXML // this table using to show the information about the user
    private JFXTreeTableView tableUserTrac;
    @FXML // Cols of table
    private JFXTreeTableColumn<UserTable, String> idCol, firstNameCol, lastNameCol, dateOfBirthCol, sectionCol, groupCol,
            numberSyntaxErrorCol, numberSemanticErrorCol, numberAnalyseErrorCol, numberSolvedCol, numberUnSolvedCol;
    private boolean boolSearchToolsUser = true; // this variable using in show/hide tools option 

    public static JFXDialog viewStatisticDialog, viewLogDialog;
    public static TracUser tracUserSelected;

    /* Start questionn traceability */
    @FXML // this table using to show the information about question
    private JFXTreeTableView tableQuestionTrac;
    // Cols of table
    private JFXTreeTableColumn<QuestionTable, String> questionTypeCol, questionNumberCol, nSyntaxErrorCol,
            nSemanticErrorCol, nAnalyseErrorCol, nSolvedCol, nUnsolvedCol, percentageSolvedCol;

    @FXML
    private PieChart pieChartSolvedQuestion, pieChartErrorQuestion;

    public static JFXDialog viewTotalQuestionDialog;

    /* End  question traceability */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboUserSearchBy.getItems().addAll("Id", "First name", "Last name", "Date of birth", "Section",
                "Group", "E.Syntax", "E.Semantic", "E.Analyse", "Solved", "UnSolved");

        sectionGroup = new UserDao().getSectionGroupTools();

        comboUserSectionFilter.getItems().clear();
        sectionGroup.keySet().forEach((section) -> {
            comboUserSectionFilter.getItems().add(section);
        });

        comboUserSectionFilter.setOnAction(e -> {
            comboUserGroupFilter.getItems().clear();
            if (comboUserSectionFilter.getSelectionModel().isEmpty()) {
                return;
            }

            sectionGroup.get(comboUserSectionFilter.getSelectionModel().getSelectedItem()).forEach((group) -> {
                comboUserGroupFilter.getItems().add(group);
            });

        });

        comboQuestionSearchBy.getItems().addAll("Type", "Question N°", "E.Syntax", "E.Semantic", "E.Analyse", "Solved",
                "UnSolved", "Percentage Solved");
    }

    /* Start student traceability */
    @FXML
    public void showStudent() {
        btnSearchToolsUser();
        initializeTableUser();

        studentPane.setVisible(true);
        choosePane.setVisible(false);
        FadeTransition ft = new FadeTransition(Duration.millis(1000));
        ft.setNode(studentPane);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
    }

    @FXML
    public void showQuestion() {
        initializeTableQuestion();

        questionPane.setVisible(true);
        choosePane.setVisible(false);
        FadeTransition ft = new FadeTransition(Duration.millis(1000));
        ft.setNode(questionPane);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
    }

    class UserTable extends RecursiveTreeObject<UserTable> {

        StringProperty id;
        StringProperty firstName;
        StringProperty lastName;
        StringProperty dateOfBirth;
        StringProperty section;
        StringProperty group;
        StringProperty numberSyntaxErrorCol; // question error syntaxique
        StringProperty numberSemanticErrorCol; // question error symontic
        StringProperty numberAnalyseErrorCol; // question error analyse
        StringProperty numberSolvedCol;
        StringProperty numberUnSolvedCol;

        public UserTable(int id, String firstName, String lastName, Date dateOfBirth, int section, int group,
                int numberSyntaxError, int numberSemanticError, int numberAnalyseError, int numberSolved,
                int numberUnSolved) {
            this.id = new SimpleStringProperty(String.valueOf(id));
            this.firstName = new SimpleStringProperty(firstName);
            this.lastName = new SimpleStringProperty(lastName);
            this.dateOfBirth = new SimpleStringProperty(dateOfBirth.toString());
            this.section = new SimpleStringProperty(String.valueOf(section));
            this.group = new SimpleStringProperty(String.valueOf(group));
            this.numberSyntaxErrorCol = new SimpleStringProperty(String.valueOf(numberSyntaxError));
            this.numberSemanticErrorCol = new SimpleStringProperty(String.valueOf(numberSemanticError));
            this.numberAnalyseErrorCol = new SimpleStringProperty(String.valueOf(numberAnalyseError));
            this.numberSolvedCol = new SimpleStringProperty(String.valueOf(numberSolved));
            this.numberUnSolvedCol = new SimpleStringProperty(String.valueOf(numberUnSolved));
        }
    }

    public void initializeTableUser() {

        idCol = new JFXTreeTableColumn<>("Id");
        idCol.setPrefWidth(50);
        idCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<UserTable, String> param) -> param.getValue().getValue().id);
        idCol.setContextMenu(null);

        firstNameCol = new JFXTreeTableColumn<>("First Name");
        firstNameCol.setPrefWidth(180);
        firstNameCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<UserTable, String> param) -> param.getValue().getValue().firstName);

        lastNameCol = new JFXTreeTableColumn<>("Last Name");
        lastNameCol.setPrefWidth(150);
        lastNameCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<UserTable, String> param) -> param.getValue().getValue().lastName);

        dateOfBirthCol = new JFXTreeTableColumn<>("Date Of Birth");
        dateOfBirthCol.setPrefWidth(100);
        dateOfBirthCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<UserTable, String> param) -> param.getValue().getValue().dateOfBirth);

        sectionCol = new JFXTreeTableColumn<>("Section");
        sectionCol.setPrefWidth(100);
        sectionCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<UserTable, String> param) -> param.getValue().getValue().section);

        groupCol = new JFXTreeTableColumn<>("Group");
        groupCol.setPrefWidth(100);
        groupCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<UserTable, String> param) -> param.getValue().getValue().group);

        numberSyntaxErrorCol = new JFXTreeTableColumn<>("E.Syntax");
        numberSyntaxErrorCol.setPrefWidth(100);
        numberSyntaxErrorCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<UserTable, String> param) -> param.getValue().getValue().numberSyntaxErrorCol);

        numberSemanticErrorCol = new JFXTreeTableColumn<>("E.Semantic");
        numberSemanticErrorCol.setPrefWidth(100);
        numberSemanticErrorCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<UserTable, String> param) -> param.getValue().getValue().numberSemanticErrorCol);

        numberAnalyseErrorCol = new JFXTreeTableColumn<>("E.Analyse");
        numberAnalyseErrorCol.setPrefWidth(100);
        numberAnalyseErrorCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<UserTable, String> param) -> param.getValue().getValue().numberAnalyseErrorCol);

        numberSolvedCol = new JFXTreeTableColumn<>("Solved");
        numberSolvedCol.setPrefWidth(90);
        numberSolvedCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<UserTable, String> param) -> param.getValue().getValue().numberSolvedCol);

        numberUnSolvedCol = new JFXTreeTableColumn<>("UnSolved");
        numberUnSolvedCol.setPrefWidth(90);
        numberUnSolvedCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<UserTable, String> param) -> param.getValue().getValue().numberUnSolvedCol);

        tableUserTrac.getColumns().addAll(idCol, firstNameCol, lastNameCol, dateOfBirthCol, sectionCol, groupCol,
                numberSyntaxErrorCol, numberSemanticErrorCol, numberAnalyseErrorCol, numberSolvedCol, numberUnSolvedCol);

        searchUserField.textProperty().addListener(e-> {
            filterSearchTableUser();
        });
        comboUserSearchBy.setOnAction(e-> {
            filterSearchTableUser();
        });
        
        updateTableUser(); // Fill table by data imported from data base
    }
    
    @FXML
    public void updateTableUser() { // get data from db and inserted to the table
        errorLabelUser.setText("");
        ObservableList<UserTable> users = FXCollections.observableArrayList();

        List<TracUser> tracUsers = new TracDao().getTracUsers(); // Get users from DB
        if (tracUsers == null) {
            errorLabelUser.setText("Connection Failed");
        } else {
            tracUsers.forEach((user) -> {
                users.add(new UserTable(user.getId(), user.getFirstName().substring(0, 1).toUpperCase() + user.getFirstName().substring(1), user.getLastName().toUpperCase(), user.getDateOfBirth(), user.getSection(), user.getGroup(), user.getNumberSyntaxError(), user.getNumberSemanticError(),
                        user.getNumberAnalyseError(), user.getNumberSolved(), user.getNumberUnSolved()));
            });
        }

        final TreeItem<UserTable> treeItem = new RecursiveTreeItem<>(users, RecursiveTreeObject::getChildren);
        tableUserTrac.setShowRoot(false);
        try {
            tableUserTrac.setRoot(treeItem);
        } catch (Exception ex) {
            System.out.println("Error catched !");
        }
    }

    @FXML
    public void btnSearchToolsUser() { // Show/Hide Search Tools HBox
        searchUserField.setText("");
        filterSearchTableUser();
        
        if (boolSearchToolsUser) {
            studentPane.getChildren().remove(searchToolsBox);
            boolSearchToolsUser = false;
        } else {
            studentPane.getChildren().add(2, searchToolsBox);
            boolSearchToolsUser = true;

            comboUserSectionFilter.getItems().clear();
            sectionGroup.keySet().forEach((section) -> {
                comboUserSectionFilter.getItems().add(section);
            });
        }
    }

    @FXML
    public void btnFilterUser() {
        tableUserTrac.setPredicate(new Predicate<TreeItem<UserTable>>() {
            @Override
            public boolean test(TreeItem<UserTable> user) {
                String sectionSelected = comboUserSectionFilter.getSelectionModel().getSelectedItem();
                String groupSelected = comboUserGroupFilter.getSelectionModel().getSelectedItem();

                if (sectionSelected == null && groupSelected == null) {
                    return true;
                }

                if (groupSelected == null) {
                    return user.getValue().section.getValue().equals(sectionSelected);
                }
                if (sectionSelected == null) {
                    return user.getValue().group.getValue().equals(groupSelected);
                }

                return user.getValue().section.getValue().equals(sectionSelected) && user.getValue().group.getValue().equals(groupSelected);
            }
        });
    }

    @FXML
    private void btnViewStatisticUser() {
        errorLabelUser.setText("");

        int index = tableUserTrac.getSelectionModel().getSelectedIndex(); // selected index
        String id = idCol.getCellData(index);
        if (id == null) {
            System.out.println("Index is null !");
            return;
        }

        tracUserSelected = new TracUser();
        
        tracUserSelected.setId(Integer.parseInt(id));
        tracUserSelected.setFirstName(firstNameCol.getCellData(index));
        tracUserSelected.setLastName(lastNameCol.getCellData(index));
        tracUserSelected.setDateOfBirth(Date.valueOf(dateOfBirthCol.getCellData(index)));
        tracUserSelected.setSection(Integer.parseInt(sectionCol.getCellData(index)));
        tracUserSelected.setGroup(Integer.parseInt(groupCol.getCellData(index)));
        tracUserSelected.setNumberSyntaxError(Integer.parseInt(numberSyntaxErrorCol.getCellData(index)));
        tracUserSelected.setNumberSemanticError(Integer.parseInt(numberSemanticErrorCol.getCellData(index)));
        tracUserSelected.setNumberAnalyseError(Integer.parseInt(numberAnalyseErrorCol.getCellData(index)));
        tracUserSelected.setNumberSolved(Integer.parseInt(numberSolvedCol.getCellData(index)));
        tracUserSelected.setNumberUnSolved(Integer.parseInt(numberUnSolvedCol.getCellData(index)));
        
        
        AnchorPane viewStatistic = null;
        try {
            viewStatistic = FXMLLoader.load(getClass().getResource("/com/houarizegai/learnsql/resources/views/form/viewStatisticForm.fxml"));
            viewStatisticDialog = new JFXDialog(root, viewStatistic, JFXDialog.DialogTransition.CENTER);
            viewStatisticDialog.show();
        } catch (IOException ex) {
            errorLabelUser.setText("Connection failed !");
            System.out.println("Error Here exception load fxml !");
            System.out.println("Massage : " + ex.getMessage());
        }
    }

    @FXML
    private void btnViewLogUser() {
        errorLabelUser.setText("");
        int index = tableUserTrac.getSelectionModel().getSelectedIndex(); // selected index
        String id = idCol.getCellData(index);
        if (id == null) { // Index is null !;
            return;
        }

        tracUserSelected = new TracUser();

        tracUserSelected.setId(Integer.parseInt(id));
        tracUserSelected.setFirstName(firstNameCol.getCellData(index));
        tracUserSelected.setLastName(lastNameCol.getCellData(index));
        tracUserSelected.setDateOfBirth(Date.valueOf(dateOfBirthCol.getCellData(index)));
        tracUserSelected.setSection(Integer.parseInt(sectionCol.getCellData(index)));
        tracUserSelected.setGroup(Integer.parseInt(groupCol.getCellData(index)));
        tracUserSelected.setNumberSyntaxError(Integer.parseInt(numberSyntaxErrorCol.getCellData(index)));
        tracUserSelected.setNumberSemanticError(Integer.parseInt(numberSemanticErrorCol.getCellData(index)));
        tracUserSelected.setNumberAnalyseError(Integer.parseInt(numberAnalyseErrorCol.getCellData(index)));
        tracUserSelected.setNumberSolved(Integer.parseInt(numberSolvedCol.getCellData(index)));
        tracUserSelected.setNumberUnSolved(Integer.parseInt(numberUnSolvedCol.getCellData(index)));
        
        try {
            AnchorPane viewLog = FXMLLoader.load(getClass().getResource("/com/houarizegai/learnsql/resources/views/form/viewLogForm.fxml"));
            viewLogDialog = new JFXDialog(root, viewLog, JFXDialog.DialogTransition.CENTER);
            viewLogDialog.show();
        } catch (IOException ex) {
            errorLabelUser.setText("Connection failed !");
        }
    }

    /* End each student traceability */
    
    /* Start Question traceability */
    
    class QuestionTable extends RecursiveTreeObject<QuestionTable> {

        StringProperty questionType;
        StringProperty questionNumber;
        StringProperty nSyntaxError; // Number question error syntax
        StringProperty nSemanticError; // Number question error symontic
        StringProperty nAnalyseError; // Number question error analyse
        StringProperty nSolved;
        StringProperty nUnsolved;
        StringProperty percentageSolved;

        public QuestionTable(String questionType, int questionNumber, int nSyntaxError, int nSemanticError,
                int nAnalyseError, int nSolved, int nUnsolved, double percentageSolved) {
            this.questionType = new SimpleStringProperty(questionType);
            this.questionNumber = new SimpleStringProperty(String.valueOf(questionNumber));
            this.nSyntaxError = new SimpleStringProperty(String.valueOf(nSyntaxError));
            this.nSemanticError = new SimpleStringProperty(String.valueOf(nSemanticError));
            this.nAnalyseError = new SimpleStringProperty(String.valueOf(nAnalyseError));
            this.nSolved = new SimpleStringProperty(String.valueOf(nSolved));
            this.nUnsolved = new SimpleStringProperty(String.valueOf(nUnsolved));
            this.percentageSolved = new SimpleStringProperty(percentageSolved + " %");
        }
    }

    public void initializeTableQuestion() {

        questionTypeCol = new JFXTreeTableColumn<>("Question Type");
        questionTypeCol.setPrefWidth(120);
        questionTypeCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<QuestionTable, String> param) -> param.getValue().getValue().questionType);
        questionTypeCol.setContextMenu(null);

        questionNumberCol = new JFXTreeTableColumn<>("Question N°");
        questionNumberCol.setPrefWidth(100);
        questionNumberCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<QuestionTable, String> param) -> param.getValue().getValue().questionNumber);

        nSyntaxErrorCol = new JFXTreeTableColumn<>("E.Syntax");
        nSyntaxErrorCol.setPrefWidth(100);
        nSyntaxErrorCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<QuestionTable, String> param) -> param.getValue().getValue().nSyntaxError);

        nSemanticErrorCol = new JFXTreeTableColumn<>("E.Semantic");
        nSemanticErrorCol.setPrefWidth(100);
        nSemanticErrorCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<QuestionTable, String> param) -> param.getValue().getValue().nSemanticError);

        nAnalyseErrorCol = new JFXTreeTableColumn<>("E.Analyse");
        nAnalyseErrorCol.setPrefWidth(100);
        nAnalyseErrorCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<QuestionTable, String> param) -> param.getValue().getValue().nAnalyseError);

        nSolvedCol = new JFXTreeTableColumn<>("Solved");
        nSolvedCol.setPrefWidth(100);
        nSolvedCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<QuestionTable, String> param) -> param.getValue().getValue().nSolved);

        nUnsolvedCol = new JFXTreeTableColumn<>("Unsolved");
        nUnsolvedCol.setPrefWidth(100);
        nUnsolvedCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<QuestionTable, String> param) -> param.getValue().getValue().nUnsolved);

        percentageSolvedCol = new JFXTreeTableColumn<>("Solved %");
        percentageSolvedCol.setPrefWidth(100);
        percentageSolvedCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<QuestionTable, String> param) -> param.getValue().getValue().percentageSolved);

        tableQuestionTrac.getColumns().addAll(questionTypeCol, questionNumberCol, nSyntaxErrorCol, nSemanticErrorCol,
                nAnalyseErrorCol, nSolvedCol, nUnsolvedCol, percentageSolvedCol);

        searchQuestionField.textProperty().addListener(e -> {
            filterSearchTableQuestion();
        });
        
        comboQuestionSearchBy.setOnAction(e-> {
            filterSearchTableQuestion();
        });
        
        updateTableQuestion(); // Fill table by data imported from data base
    }
    
    public void filterSearchTableQuestion() {
        // This method using to filter row in table by the key entered in SearchField
        tableQuestionTrac.setPredicate(new Predicate<TreeItem<QuestionTable>>() {
                @Override
                public boolean test(TreeItem<QuestionTable> question) {
                    switch (comboQuestionSearchBy.getSelectionModel().getSelectedIndex()) {
                        case 0:
                            return question.getValue().questionType.getValue().toLowerCase().contains(searchQuestionField.getText().toLowerCase());
                        case 1:
                            return question.getValue().questionNumber.getValue().toLowerCase().contains(searchQuestionField.getText().toLowerCase());
                        case 2:
                            return question.getValue().nSyntaxError.getValue().toLowerCase().contains(searchQuestionField.getText().toLowerCase());
                        case 3:
                            return question.getValue().nSemanticError.getValue().toLowerCase().contains(searchQuestionField.getText().toLowerCase());
                        case 4:
                            return question.getValue().nAnalyseError.getValue().toLowerCase().contains(searchQuestionField.getText().toLowerCase());
                        case 5:
                            return question.getValue().nSolved.getValue().toLowerCase().contains(searchQuestionField.getText().toLowerCase());
                        case 6:
                            return question.getValue().nUnsolved.getValue().toLowerCase().contains(searchQuestionField.getText().toLowerCase());
                        case 7:
                            return question.getValue().percentageSolved.getValue().toLowerCase().contains(searchQuestionField.getText().toLowerCase());

                        default:
                            return question.getValue().questionType.getValue().toLowerCase().contains(searchQuestionField.getText().toLowerCase())
                                    || question.getValue().questionNumber.getValue().toLowerCase().contains(searchQuestionField.getText().toLowerCase())
                                    || question.getValue().nSyntaxError.getValue().toLowerCase().contains(searchQuestionField.getText().toLowerCase())
                                    || question.getValue().nSemanticError.getValue().toLowerCase().contains(searchQuestionField.getText().toLowerCase())
                                    || question.getValue().nAnalyseError.getValue().toLowerCase().contains(searchQuestionField.getText().toLowerCase())
                                    || question.getValue().nSolved.getValue().toLowerCase().contains(searchQuestionField.getText().toLowerCase())
                                    || question.getValue().nUnsolved.getValue().toLowerCase().contains(searchQuestionField.getText().toLowerCase())
                                    || question.getValue().percentageSolved.getValue().toLowerCase().contains(searchQuestionField.getText().toLowerCase());
                    }
                }
            });
    }

    public void filterSearchTableUser() {
        // This method using to filter row in table by the key entered in SearchField
        tableUserTrac.setPredicate(new Predicate<TreeItem<UserTable>>() {
                @Override
                public boolean test(TreeItem<UserTable> user) {
                    switch (comboUserSearchBy.getSelectionModel().getSelectedIndex()) {
                        case 0:
                            return user.getValue().id.getValue().toLowerCase().contains(searchUserField.getText().toLowerCase());
                        case 1:
                            return user.getValue().firstName.getValue().toLowerCase().contains(searchUserField.getText().toLowerCase());
                        case 2:
                            return user.getValue().lastName.getValue().toLowerCase().contains(searchUserField.getText().toLowerCase());
                        case 3:
                            return user.getValue().dateOfBirth.getValue().toLowerCase().contains(searchUserField.getText().toLowerCase());
                        case 4:
                            return user.getValue().section.getValue().toLowerCase().contains(searchUserField.getText().toLowerCase());
                        case 5:
                            return user.getValue().group.getValue().toLowerCase().contains(searchUserField.getText().toLowerCase());
                        case 6:
                            return user.getValue().numberSyntaxErrorCol.getValue().toLowerCase().contains(searchUserField.getText().toLowerCase());
                        case 7:
                            return user.getValue().numberSemanticErrorCol.getValue().toLowerCase().contains(searchUserField.getText().toLowerCase());
                        case 8:
                            return user.getValue().numberAnalyseErrorCol.getValue().toLowerCase().contains(searchUserField.getText().toLowerCase());
                        case 9:
                            return user.getValue().numberSolvedCol.getValue().toLowerCase().contains(searchUserField.getText().toLowerCase());
                        case 10:
                            return user.getValue().numberUnSolvedCol.getValue().toLowerCase().contains(searchUserField.getText().toLowerCase());

                        default:
                            return user.getValue().id.getValue().toLowerCase().contains(searchUserField.getText().toLowerCase())
                                    || user.getValue().firstName.getValue().toLowerCase().contains(searchUserField.getText().toLowerCase())
                                    || user.getValue().lastName.getValue().toLowerCase().contains(searchUserField.getText().toLowerCase())
                                    || user.getValue().dateOfBirth.getValue().toLowerCase().contains(searchUserField.getText().toLowerCase())
                                    || user.getValue().section.getValue().toLowerCase().contains(searchUserField.getText().toLowerCase())
                                    || user.getValue().group.getValue().toLowerCase().contains(searchUserField.getText().toLowerCase())
                                    || user.getValue().numberSyntaxErrorCol.getValue().toLowerCase().contains(searchUserField.getText().toLowerCase())
                                    || user.getValue().numberSemanticErrorCol.getValue().toLowerCase().contains(searchUserField.getText().toLowerCase())
                                    || user.getValue().numberAnalyseErrorCol.getValue().toLowerCase().contains(searchUserField.getText().toLowerCase())
                                    || user.getValue().numberSolvedCol.getValue().toLowerCase().contains(searchUserField.getText().toLowerCase())
                                    || user.getValue().numberUnSolvedCol.getValue().toLowerCase().contains(searchUserField.getText().toLowerCase());
                    }
                }
            });
    }

    @FXML
    public void updateTableQuestion() { // get data from db and inserted to the table
        errorLabelQuestion.setText("");
        ObservableList<QuestionTable> questionList = FXCollections.observableArrayList();

        List<TracQuestion> tracQuestionDao = new TracDao().getTracQuestion(); // Get users from DB
        if (tracQuestionDao == null) {
            errorLabelQuestion.setText("Connection Failed");
        } else {
            for (TracQuestion question : tracQuestionDao) {
                questionList.add(new QuestionTable(question.getQuestionType(), question.getQuestionNumber(),
                        question.getnSyntaxError(), question.getnSemanticError(), question.getnAnalyseError(), question.getnSolved(),
                        question.getnUnsolved(), question.getSolvedPercentage()));
            }
        }

        final TreeItem<QuestionTable> treeItem = new RecursiveTreeItem<>(questionList, RecursiveTreeObject::getChildren);
        tableQuestionTrac.setShowRoot(false);
        try {
            tableQuestionTrac.setRoot(treeItem);
        } catch (Exception ex) {
            System.out.println("Error catched Table Question !");
        }
    }

    @FXML
    private void viewChartQuestion() {
        errorLabelUser.setText("");

        int index = tableQuestionTrac.getSelectionModel().getSelectedIndex(); // selected index
        String questionNumber = questionNumberCol.getCellData(index);
        if (questionNumber == null) {
            System.out.println("Index is null - Question Table!");
            return;
        }

        // Initialize pie chart
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        data.add(new PieChart.Data("Solved", Integer.parseInt(nSolvedCol.getCellData(index))));
        data.add(new PieChart.Data("UnSolved", Integer.parseInt(nUnsolvedCol.getCellData(index))));

        pieChartSolvedQuestion.setData(data);
        pieChartSolvedQuestion.setLabelsVisible(false);
        pieChartSolvedQuestion.setTitle("Total Solved/UnSolved Question" + Integer.parseInt(questionNumber));
        data.forEach(d
                -> d.nameProperty().bind(
                        Bindings.concat((d.pieValueProperty() + "").replaceFirst(".*?(\\d+).*", "$1"), " ", d.getName())
                )
        );

        ObservableList<PieChart.Data> data2 = FXCollections.observableArrayList();
        data2.add(new PieChart.Data("Syntax Error", Integer.parseInt(nSyntaxErrorCol.getCellData(index))));
        data2.add(new PieChart.Data("Semantic Error", Integer.parseInt(nSemanticErrorCol.getCellData(index))));
        data2.add(new PieChart.Data("Analyse Error", Integer.parseInt(nAnalyseErrorCol.getCellData(index))));

        pieChartErrorQuestion.setTitle("Total number of error");
        pieChartErrorQuestion.setLabelsVisible(false);
        pieChartErrorQuestion.setData(data2);
        data2.forEach(d
                -> d.nameProperty().bind(
                        Bindings.concat((d.pieValueProperty() + "").replaceFirst(".*?(\\d+).*", "$1"), " ", d.getName())
                )
        );
    }

    @FXML
    private void viewTotalQuestion() {
        errorLabelQuestion.setText("");
        questionStatisticDao = new TracDao().getTracQuestion();
        if (questionStatisticDao == null) {
            errorLabelQuestion.setText("Connection Failed !");
            return;
        }

        try {
            AnchorPane totalQuestionPane = FXMLLoader.load(getClass().getResource("/com/houarizegai/learnsql/resources/views/form/viewStatisticQuestion.fxml"));
            viewTotalQuestionDialog = new JFXDialog(root, totalQuestionPane, JFXDialog.DialogTransition.CENTER);
            viewTotalQuestionDialog.show();
        } catch (IOException ex) {
            System.out.println("Error Load View Total Question Statistic");
            System.out.println("Error Msg: " + ex.getMessage());
            Logger.getLogger(TracController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /* End Question traceability */
}
