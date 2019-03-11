package com.houarizegai.learnsql.java.controllers.form;

import com.houarizegai.learnsql.java.controllers.TracController;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import static com.houarizegai.learnsql.java.controllers.TracController.tracUserSelected;
import com.houarizegai.learnsql.java.dao.TracDao;
import com.houarizegai.learnsql.java.models.UserStatisticView;
import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.AnchorPane;

public class ViewStatisticFormController implements Initializable {

    @FXML
    private AnchorPane tablePane, chartPane, barChartPane, pieChartPane;
    @FXML
    private Label errorLabel;
    @FXML
    private Label userSelected;
    @FXML
    private JFXButton btnView, btnChartType;

    /* Start Chart Part */
    @FXML
    private BarChart statisticUserBarChart;
    @FXML
    CategoryAxis xAxis;
    @FXML
    NumberAxis yAxis;

    @FXML
    private PieChart totalSolvedPieChart, numberErrorPieChart;

    /* Start Table Part */
    @FXML
    public JFXTextField searchField;
    @FXML
    public JFXComboBox<String> comboSearchBy;
    @FXML
    private JFXTreeTableView tableView;
    private JFXTreeTableColumn<StatisticTable, String> questionTypeCol, questionNumberCol, nSyntaxErrorCol, nSemanticErrorCol, nAnalyseErrorCol,
            solvedCol;

    // this variable contain all statistic about user (Number Error, Solved ..)
    List<UserStatisticView> userStatisticDao;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboSearchBy.getItems().addAll(new String[]{"Question Type", "Question N°", "E.Syntax", "E.Semantic ", "E.Analyse", "Solved"});

        initializeTable();
        initializePieChart();
        initializeBarChart();

        userSelected.setText(tracUserSelected.getFirstName().substring(0, 1).toUpperCase()
                + tracUserSelected.getFirstName().substring(1).toLowerCase() + " "
                + tracUserSelected.getLastName().toUpperCase());

        btnView.setOnAction(e -> {
            if (btnView.getText().equals("View Table")) {
                tablePane.setVisible(true);
                btnView.setText("View Chart");

                chartPane.setVisible(false);
                btnChartType.setVisible(false);

            } else {
                tablePane.setVisible(false);
                btnView.setText("View Table");

                // initilize chart part 
                chartPane.setVisible(true);
                btnChartType.setText("Total Stat");
                btnChartType.setVisible(true);
                barChartPane.setVisible(true);
                pieChartPane.setVisible(false);
            }
        });

        btnChartType.setOnAction(e -> {
            if (btnChartType.getText().equals("Total Stat")) {
                pieChartPane.setVisible(true);
                barChartPane.setVisible(false);
                btnChartType.setText("Error Stat");
            } else {
                barChartPane.setVisible(true);
                pieChartPane.setVisible(false);
                btnChartType.setText("Total Stat");
            }
        });

    }

    class StatisticTable extends RecursiveTreeObject<StatisticTable> {

        StringProperty questionType;
        StringProperty questionNumber;
        StringProperty nSyntaxError;
        StringProperty nSemanticError;
        StringProperty nAnalyseError;
        StringProperty solved;

        public StatisticTable(String questionType, int questionNumber, int nSyntaxError, int nSemanticError,
                int nAnalyseError, boolean solved) {
            this.questionType = new SimpleStringProperty(questionType);
            this.questionNumber = new SimpleStringProperty(String.valueOf(questionNumber));
            this.nSyntaxError = new SimpleStringProperty(String.valueOf(nSyntaxError));
            this.nSemanticError = new SimpleStringProperty(String.valueOf(nSemanticError));
            this.nAnalyseError = new SimpleStringProperty(String.valueOf(nAnalyseError));
            this.solved = new SimpleStringProperty(String.valueOf(solved).toUpperCase());
        }
    }

    public void initializeTable() {

        questionTypeCol = new JFXTreeTableColumn<>("Question Type");
        questionTypeCol.setPrefWidth(100);
        questionTypeCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<StatisticTable, String> param) -> param.getValue().getValue().questionType);

        questionNumberCol = new JFXTreeTableColumn<>("Question N°");
        questionNumberCol.setPrefWidth(100);
        questionNumberCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<StatisticTable, String> param) -> param.getValue().getValue().questionNumber);

        nSyntaxErrorCol = new JFXTreeTableColumn<>("E.Syntax");
        nSyntaxErrorCol.setPrefWidth(100);
        nSyntaxErrorCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<StatisticTable, String> param) -> param.getValue().getValue().nSyntaxError);

        nSemanticErrorCol = new JFXTreeTableColumn<>("E.Semantic");
        nSemanticErrorCol.setPrefWidth(100);
        nSemanticErrorCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<StatisticTable, String> param) -> param.getValue().getValue().nSemanticError);

        nAnalyseErrorCol = new JFXTreeTableColumn<>("E.Analyse");
        nAnalyseErrorCol.setPrefWidth(100);
        nAnalyseErrorCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<StatisticTable, String> param) -> param.getValue().getValue().nAnalyseError);

        solvedCol = new JFXTreeTableColumn<>("Solved");
        solvedCol.setPrefWidth(100);
        solvedCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<StatisticTable, String> param) -> param.getValue().getValue().solved);

        updateTable(); // Fill table by data imported from data base

        tableView.getColumns().addAll(questionTypeCol, questionNumberCol, nSyntaxErrorCol, nSemanticErrorCol, nAnalyseErrorCol,
                solvedCol);
        tableView.setShowRoot(false);

        searchField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            tableView.setPredicate(new Predicate<TreeItem<StatisticTable>>() {
                @Override
                public boolean test(TreeItem<StatisticTable> user) {
                    switch (comboSearchBy.getSelectionModel().getSelectedIndex()) {
                        case 0:
                            return user.getValue().questionType.getValue().contains(newValue);
                        case 1:
                            return user.getValue().questionNumber.getValue().contains(newValue);
                        case 2:
                            return user.getValue().nSyntaxError.getValue().contains(newValue);
                        case 3:
                            return user.getValue().nSemanticError.getValue().contains(newValue);
                        case 4:
                            return user.getValue().nAnalyseError.getValue().contains(newValue);
                        case 5:
                            return user.getValue().solved.getValue().contains(newValue);

                        default:
                            return user.getValue().questionType.getValue().contains(newValue)
                                    || user.getValue().questionNumber.getValue().contains(newValue)
                                    || user.getValue().nSyntaxError.getValue().contains(newValue)
                                    || user.getValue().nSemanticError.getValue().contains(newValue)
                                    || user.getValue().nAnalyseError.getValue().contains(newValue)
                                    || user.getValue().solved.getValue().contains(newValue);
                    }
                }
            });
        });
    }

    @FXML
    public void updateTable() { // get data from db and inserted to the table
        errorLabel.setText("");
        ObservableList<StatisticTable> usersStatistic = FXCollections.observableArrayList();

        userStatisticDao = new TracDao().getUserStatisticView(tracUserSelected.getId()); // Get users from DB
        if (userStatisticDao == null) {
            errorLabel.setText("Connection Failed !");
        } else {
            for (UserStatisticView userStatistic : userStatisticDao) {
                usersStatistic.add(new StatisticTable(userStatistic.getQuestionType(), userStatistic.getQuestionNumber(),
                        userStatistic.getnSyntaxError(), userStatistic.getnSemanticError(), userStatistic.getnAnalyseError(),
                        userStatistic.isSolved()));
            }
        }

        final TreeItem<StatisticTable> treeItem = new RecursiveTreeItem<>(usersStatistic, RecursiveTreeObject::getChildren);
        try { // insert data into thte table
            tableView.setRoot(treeItem);
        } catch (Exception ex) {
            System.out.println("Error catched !");
        }
    }

    private void initializePieChart() {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        data.add(new PieChart.Data("UnSolved", tracUserSelected.getNumberUnSolved()));
        data.add(new PieChart.Data("Solved", tracUserSelected.getNumberSolved()));

        totalSolvedPieChart.setData(data);
        totalSolvedPieChart.setTitle("Total number Solved/UnSolved Question");
        data.forEach(d
                -> d.nameProperty().bind(
                        Bindings.concat((d.pieValueProperty() + "").replaceFirst(".*?(\\d+).*", "$1"), " ", d.getName())
                )
        );

        ObservableList<PieChart.Data> data2 = FXCollections.observableArrayList();
        data2.add(new PieChart.Data("Syntax Error", tracUserSelected.getNumberSyntaxError()));
        data2.add(new PieChart.Data("Semantic Error", tracUserSelected.getNumberSemanticError()));
        data2.add(new PieChart.Data("Analyse Error", tracUserSelected.getNumberAnalyseError()));

        numberErrorPieChart.setTitle("Total number of error");
        numberErrorPieChart.setData(data2);
        data2.forEach(d
                -> d.nameProperty().bind(
                        Bindings.concat((d.pieValueProperty() + "").replaceFirst(".*?(\\d+).*", "$1"), " ", d.getName())
                )
        );

    }

    private void initializeBarChart() {
        xAxis.setLabel("Type of Error");
        //yAxis.setLabel("Number Of Error");

//        // List of Question Chart xY
//        List<XYChart.Series> qChart = new ArrayList<>();
        XYChart.Series xySyntax = new XYChart.Series();
        XYChart.Series xySemantic = new XYChart.Series();
        XYChart.Series xyAnalyse = new XYChart.Series();

        xySyntax.setName("E.Syntax");
        xySemantic.setName("E.Semantic");
        xyAnalyse.setName("E.Analyse");

        for (UserStatisticView stat : userStatisticDao) {
            xySyntax.getData().add(new XYChart.Data("Question " + stat.getQuestionNumber(), stat.getnSyntaxError()));
            xySemantic.getData().add(new XYChart.Data("Question " + stat.getQuestionNumber(), stat.getnSemanticError()));
            xyAnalyse.getData().add(new XYChart.Data("Question " + stat.getQuestionNumber(), stat.getnAnalyseError()));
        }

        statisticUserBarChart.getData().addAll(xySyntax, xySemantic, xyAnalyse);

    }

    @FXML
    public void btnClose() {
        TracController.viewStatisticDialog.close();
    }

}
