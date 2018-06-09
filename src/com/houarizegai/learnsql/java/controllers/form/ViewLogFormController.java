package com.houarizegai.learnsql.java.controllers.form;

import com.houarizegai.learnsql.java.controllers.TracController;
import static com.houarizegai.learnsql.java.controllers.TracController.tracUserSelected;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.houarizegai.learnsql.java.dao.TracDao;
import com.houarizegai.learnsql.java.models.UserLogView;
import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.AnchorPane;

public class ViewLogFormController implements Initializable {
    @FXML
    private AnchorPane tablePane, chartPane, barChartPane, pieChartPane;
    @FXML
    private Label errorLabel;
    @FXML
    private Label userSelected;
    @FXML
    private JFXButton btnView;

    /* Start Table Part */
    @FXML
    public JFXTextField searchField;
    @FXML
    public JFXComboBox<String> comboSearchBy;
    @FXML
    private JFXTreeTableView tableViewLog;
    private JFXTreeTableColumn<LogStatTable, String> dateLoginCol, timeLoginCol, durationLoginCol;

    @FXML
    private Label firstDateLogin, firstTimeLogin, lastDateLogin, lastTimeLogin, totalDurationLogin;

    /* End Table Part */
    @FXML
    private JFXComboBox comboYear, comboMonth;
    Map<Integer, List<String>> comboYearMonth;

    @FXML
    private LineChart<String, Double> lineChartLog;
    @FXML
    private CategoryAxis xAxis;
    XYChart.Series series;

    // this variable contain all statistic about user (Number Error, Solved ..)
    List<UserLogView> userLogDao;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboSearchBy.getItems().addAll(new String[]{"Date Login", "Time Login", "Duration Login"});
        initializeTable();
        loadBoxTotal();

        // For combo of line chart (trac user log details)
        comboYearMonth = new TracDao().getComboYearMonthLog();

        // Show the first name & last name of user selected
        userSelected.setText(tracUserSelected.getFirstName().substring(0, 1).toUpperCase()
                + tracUserSelected.getFirstName().substring(1).toLowerCase() + " "
                + tracUserSelected.getLastName().toUpperCase());

        initializeChartPart();
        btnView.setOnAction(e -> {
            if (btnView.getText().equals("View Table")) {
                tablePane.setVisible(true);
                btnView.setText("View Chart");
                chartPane.setVisible(false);

            } else {
                tablePane.setVisible(false);
                btnView.setText("View Table");
                // ComboBox Log BarChart
                chartPane.setVisible(true);
            }
        });

    }

    class LogStatTable extends RecursiveTreeObject<LogStatTable> {

        StringProperty dateLogin;
        StringProperty timeLogin;
        StringProperty durationLogin;

        public LogStatTable(String dateLogin, String timeLogin, String durationLogin) {
            this.dateLogin = new SimpleStringProperty(dateLogin);
            this.timeLogin = new SimpleStringProperty(timeLogin);
            this.durationLogin = new SimpleStringProperty(durationLogin);
        }
    }

    private void initializeTable() {
        dateLoginCol = new JFXTreeTableColumn<>("Login Date");
        dateLoginCol.setPrefWidth(102);
        dateLoginCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<LogStatTable, String> param) -> param.getValue().getValue().dateLogin);

        timeLoginCol = new JFXTreeTableColumn<>("Login Time");
        timeLoginCol.setPrefWidth(102);
        timeLoginCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<LogStatTable, String> param) -> param.getValue().getValue().timeLogin);

        durationLoginCol = new JFXTreeTableColumn<>("Login Duration");
        durationLoginCol.setPrefWidth(104);
        durationLoginCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<LogStatTable, String> param) -> param.getValue().getValue().durationLogin);

        updateTable(); // Fill table by data imported from data base

        tableViewLog.getColumns().addAll(dateLoginCol, timeLoginCol, durationLoginCol);
        tableViewLog.setShowRoot(false);

        searchField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            tableViewLog.setPredicate(new Predicate<TreeItem<LogStatTable>>() {
                @Override
                public boolean test(TreeItem<LogStatTable> user) {
                    switch (comboSearchBy.getSelectionModel().getSelectedIndex()) {
                        case 0:
                            return user.getValue().dateLogin.getValue().contains(newValue);
                        case 1:
                            return user.getValue().timeLogin.getValue().contains(newValue);
                        case 2:
                            return user.getValue().durationLogin.getValue().contains(newValue);

                        default:
                            return user.getValue().dateLogin.getValue().contains(newValue)
                                    || user.getValue().timeLogin.getValue().contains(newValue)
                                    || user.getValue().durationLogin.getValue().contains(newValue);
                    }
                }
            });
        });
    }

    @FXML
    private void updateTable() { // get data from db and inserted to the table
        errorLabel.setText("");
        ObservableList<LogStatTable> userLogStat = FXCollections.observableArrayList();

        userLogDao = new TracDao().getUserLogView(tracUserSelected.getId()); // Get users from DB

        if (!userLogDao.isEmpty()) {
            for (UserLogView userLog : userLogDao) // store data to ObservableList for after added to the table
            {
                userLogStat.add(new LogStatTable(userLog.getDateLog().toString(), userLog.getTimeLog().toString(), userLog.getDurationLogin().toString()));
            }
        }

        final TreeItem<LogStatTable> treeItem = new RecursiveTreeItem<>(userLogStat, RecursiveTreeObject::getChildren);
        try { // insert data into thte table
            tableViewLog.setRoot(treeItem);
        } catch (Exception ex) {
            //System.out.println("Error catched !");
        }
    }

    private void loadBoxTotal() {
        // This function load the total information of the Login of user
        //System.out.println("Passed 1");
        List<String> totalLogInfo = new TracDao().getTotalLogInfo(tracUserSelected.getId());
        //System.out.println("Passed 2");

        firstDateLogin.setText(totalLogInfo.get(0));
        firstTimeLogin.setText(totalLogInfo.get(1));
        lastDateLogin.setText(totalLogInfo.get(2));
        lastTimeLogin.setText(totalLogInfo.get(3));
        totalDurationLogin.setText(totalLogInfo.get(4));
        //System.out.println("Passed 3");
    }

    @FXML
    private void btnClose() {
        TracController.viewLogDialog.close();
    }

    private void initializeChartPart() {
        // Load data to ComboBox (year & month) from DB
        
        comboYearMonth.keySet().forEach(item -> {
            // this loop initialize the comboBox of year
            comboYear.getItems().addAll(item); // set it by the Year 
        });

        comboYear.setOnAction(e -> {
            comboMonth.getItems().clear();

            for (Object item : comboYearMonth.get(comboYear.getSelectionModel().getSelectedItem())) {
                comboMonth.getItems().add(item.toString()); // set it by the Year
            }
        });

        // Initalize LibeChart
        lineChartLog.setTitle("Time active into the application");

    }

    @FXML
    private void btnShowLog() {
        if (comboYear.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        if (comboMonth.getSelectionModel().getSelectedItem() == null) {
            xAxis.setLabel("Month");

            //defining a series
            series = new XYChart.Series();
            series.setName("Time Active in " + comboYear.getSelectionModel().getSelectedItem().toString());

            lineChartLog.getData().clear();
            lineChartLog.getData().add(series);

            for (int i = 1; i <= 12; i++) {
                series.getData().add(new XYChart.Data<>(getMonthString(i), 0.0));
            }

            Map<Integer, Double> data = new TracDao().getTimeActive(tracUserSelected.getId(), (int) comboYear.getSelectionModel().getSelectedItem());

            //populating the series with data
            data.keySet().forEach(key -> series.getData().set(key - 1, new XYChart.Data(getMonthString(key), data.get(key))));

        } else {
            int year = (int) comboYear.getSelectionModel().getSelectedItem();
            int month = getMonthInNumber(comboMonth.getSelectionModel().getSelectedItem().toString());

            int m = 0;
            switch (month) {
                case 2:
                    m = 29;
                    break;
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    m = 31;
                    break;
                default:
                    m = 30;
                    break;
            }

            xAxis.setLabel("Days");

            //defining a series
            series = new XYChart.Series();
            series.setName("Time Active in " + comboMonth.getSelectionModel().getSelectedItem().toString() + " "
                    + comboYear.getSelectionModel().getSelectedItem().toString());

            lineChartLog.getData().clear();
            lineChartLog.getData().add(series);

            for (int i = 1; i <= m; i++) {
                series.getData().add(new XYChart.Data<>(i + "", 0.0));
            }

            Map<Integer, Double> data = new TracDao().getTimeActive(tracUserSelected.getId(), year, month);

            //populating the series with data
            data.keySet().forEach(key -> series.getData().set(key - 1, new XYChart.Data(key + "", data.get(key))));

        }
    }

    private int getMonthInNumber(String month) {
        switch (month.toLowerCase()) {
            case "january":
                return 1;
            case "february":
                return 2;
            case "march":
                return 3;
            case "april":
                return 4;
            case "may":
                return 5;
            case "june":
                return 6;
            case "july":
                return 7;
            case "august":
                return 8;
            case "september":
                return 9;
            case "october":
                return 10;
            case "november":
                return 11;
            case "december":
                return 12;
        }

        return 0;
    }

    private String getMonthString(int month) {
        switch (month) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
        }
        return "";
    }
}
