package com.houarizegai.learnsql.java.controllers.form;

import com.houarizegai.learnsql.java.controllers.TracController;
import com.houarizegai.learnsql.java.models.TracQuestion;
import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;

public class ViewStatisticQuestionController implements Initializable {

    @FXML
    private JFXButton toggleBtnView;

    @FXML
    private AnchorPane barChartPane, stackedBarChartPane;

    @FXML
    private BarChart totalNumberErrorBarChart, percentageSolvedBarChart;
    
    //--------
    @FXML
    private StackedBarChart stackedBarChart;
    @FXML
    private CategoryAxis xAxisStacked;
    @FXML
    private NumberAxis yAxisStacked;
    
    // this variable contain all statistic about user (Number Error, Solved ..)
    public static List<TracQuestion> questionStatisticDao;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        toggleBtnView.setOnAction(e -> {
            if (toggleBtnView.getText().equals("View Total Error")) {
                barChartPane.setVisible(false);
                stackedBarChartPane.setVisible(true);
                toggleBtnView.setText("View Total Solved %");
            } else {
                barChartPane.setVisible(true);
                stackedBarChartPane.setVisible(false);
                toggleBtnView.setText("View Total Error");
            }
        });

        initializeBarChartPercentage();
        initializeStackedBarChart();
    }

    private void initializeBarChartPercentage() {

        XYChart.Series xySolved = new XYChart.Series();
        xySolved.setName("Solved Question");
        
        questionStatisticDao.forEach(q ->
            xySolved.getData().add(new XYChart.Data(q.getQuestionType() + " " + q.getQuestionNumber(), q.getSolvedPercentage()))
        );

        percentageSolvedBarChart.getData().addAll(xySolved);

    }

    private void initializeStackedBarChart() {
        xAxisStacked.setLabel("Question");
        yAxisStacked.setLabel("Number of error");
        
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        XYChart.Series<String, Number> series3 = new XYChart.Series<>();
        
        series1.setName("Syntax Error");
        series2.setName("Semantic Error");
        series3.setName("Analyse Error");
        for(TracQuestion qStat : questionStatisticDao) {
            series1.getData().add(new XYChart.Data<String, Number>(qStat.getQuestionType() + " " + qStat.getQuestionNumber(), qStat.getnSyntaxError()));
            series2.getData().add(new XYChart.Data<String, Number>(qStat.getQuestionType() + " " + qStat.getQuestionNumber(), qStat.getnSemanticError()));
            series3.getData().add(new XYChart.Data<String, Number>(qStat.getQuestionType() + " " + qStat.getQuestionNumber(), qStat.getnAnalyseError()));
        }
        
        stackedBarChart.getData().addAll(series1, series2, series3);
    }
    
    @FXML
    public void btnClose() {
        TracController.viewTotalQuestionDialog.close();
    }
}
