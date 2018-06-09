package com.houarizegai.learnsql.java.controllers;

import com.jfoenix.controls.JFXDialog;
import com.houarizegai.learnsql.java.dao.LogDao;
import com.houarizegai.learnsql.java.models.UserInformation;
import static com.houarizegai.learnsql.java.models.UserInformation.dateLogin;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.icons525.Icons525View;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import de.jensd.fx.glyphs.octicons.OctIconView;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SystemController implements Initializable {

    @FXML
    private HBox parent;
    @FXML
    private AnchorPane holderPane;
    @FXML
    private VBox sidebar; // Menu Left of my System

    /* Start Home Part */
    @FXML
    private VBox homePane;
    @FXML
    private VBox conBox, tracBox, manageQuestionBox, manageAccountBox;

    /* End Home Part */
    private VBox guidePane;
    private AnchorPane connectionPane, gradesPane, addQuestionPane, settingsPane;
    private StackPane tracPane, manageQuestionPane, manageAccountPane, chatPane;
    @FXML // this pane using for the Dialog of about
    private StackPane rightPane;

    /* Start Icon Option */
    @FXML
    private HBox boxHome, boxCon, boxGrades, boxTrac, boxQuestion, boxAccount, boxChat, boxSettings, boxGuide;
    @FXML
    private FontAwesomeIconView iconHome, iconChat, iconAccount;
    @FXML
    private MaterialDesignIconView iconGuide, iconAbout;
    @FXML
    private Icons525View iconCon, iconSettings;
    @FXML
    private OctIconView iconTrac, iconGrades, iconQuestion;
    /* End Icon Option */

    public static JFXDialog aboutDialog; // this for show the about Dialog

    @FXML
    private ImageView imgSlider;
    @FXML
    private Label dateLabel;
    // counter Number of image using in slider
    private final byte NUMBER_IMAGE_SLIDER = 3;
    private int counter = 1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        styleBox(0); // for changing the color of Home Icon

        // Initialize the image (to fill parent)
        imgSlider.fitWidthProperty().bind(holderPane.widthProperty());
        imgSlider.fitHeightProperty().bind(holderPane.heightProperty());

        // Make auto change the slider in duration
        sliderAutoChangePictures();

        // Load FXML in variable
        try {
            connectionPane = FXMLLoader.load(getClass().getResource("/com/houarizegai/learnsql/resources/views/Connection.fxml"));
            manageQuestionPane = FXMLLoader.load(getClass().getResource("/com/houarizegai/learnsql/resources/views/ManageQuestion.fxml"));
            manageAccountPane = FXMLLoader.load(getClass().getResource("/com/houarizegai/learnsql/resources/views/ManageAccount.fxml"));
            settingsPane = FXMLLoader.load(getClass().getResource("/com/houarizegai/learnsql/resources/views/Settings.fxml"));
            chatPane = FXMLLoader.load(getClass().getResource("/com/houarizegai/learnsql/resources/views/Chat.fxml"));
            guidePane = FXMLLoader.load(getClass().getResource("/com/houarizegai/learnsql/resources/views/Guide.fxml"));
            // load Dialog
            AnchorPane aboutPane = FXMLLoader.load(getClass().getResource("/com/houarizegai/learnsql/resources/views/form/About.fxml"));
            aboutDialog = new JFXDialog(rightPane, aboutPane, JFXDialog.DialogTransition.TOP);

        } catch (IOException ex) {
            Logger.getLogger(SystemController.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Check if the user if teacher for activate the button of Managment
        if (UserInformation.isTeacher) {
            boxCon.setDisable(true);
            conBox.setDisable(true);

            boxTrac.setDisable(false);
            boxQuestion.setDisable(false);
            boxAccount.setDisable(false);
            tracBox.setDisable(false);
            manageQuestionBox.setDisable(false);
            manageAccountBox.setDisable(false);

        }

        // save the date of login
        dateLogin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
    }

    private void sliderAutoChangePictures() {
        // Make auto change the slider in duration

        Timeline sliderTimer = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            FadeTransition ft = new FadeTransition();
            ft.setNode(imgSlider);
            ft.setDuration(new Duration(4000));
            ft.setFromValue(1.0);
            ft.setToValue(0.3);
            ft.setCycleCount(0);
            ft.setAutoReverse(true);
            ft.play();
            imgSlider.setImage(new Image("com/houarizegai/learnsql/resources/images/slider/" + counter + ".png"));
            if (++counter > NUMBER_IMAGE_SLIDER) {
                counter = 1;
            }
        }),
                new KeyFrame(Duration.seconds(4))
        );
        sliderTimer.setCycleCount(Animation.INDEFINITE);
        sliderTimer.play();

        // initialize Clock Showing in home
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy      HH:mm:ss");
            Date date = new Date();
            dateLabel.setText(dateFormat.format(date));
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    @FXML
    private void expandSidebar() {
        sidebar.setPrefWidth((sidebar.getWidth() == 50) ? 250 : 50);
    }

    private void setNode(Node node) {
        homePane.setVisible(false);
        holderPane.getChildren().clear();
        holderPane.getChildren().add((Node) node);
        sidebar.setPrefWidth(50);
        FadeTransition ft = new FadeTransition(Duration.millis(1000));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
    }

    @FXML
    private void homeClicked() {
        styleBox(0);
        homePane.setVisible(true);
        sidebar.setPrefWidth(50);
    }

    @FXML
    private void connectionClicked() {
        styleBox(1);
        try {
            // This function show The Connection pane (move to Connection part)
            connectionPane = FXMLLoader.load(getClass().getResource("/com/houarizegai/learnsql/resources/views/Connection.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(SystemController.class.getName()).log(Level.SEVERE, null, ex);
        }
        setNode(connectionPane);
    }

    @FXML
    private void gradesClicked() {
        styleBox(2);
        try {
            gradesPane = FXMLLoader.load(getClass().getResource("/com/houarizegai/learnsql/resources/views/Grades.fxml"));
            setNode(gradesPane);
        } catch (IOException ex) {
            Logger.getLogger(SystemController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void traceabilityClicked() {
        styleBox(3);
        try {
            tracPane = FXMLLoader.load(getClass().getResource("/com/houarizegai/learnsql/resources/views/Trac.fxml"));
            setNode(tracPane);
        } catch (IOException ex) {
            Logger.getLogger(SystemController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void questionClicked() {
        styleBox(4);
        setNode(manageQuestionPane);
    }

    @FXML
    private void accountClicked() {
        styleBox(5);
        setNode(manageAccountPane);
    }

    @FXML
    private void chatClicked() {
        styleBox(6);
        setNode(chatPane);
    }

    @FXML
    private void settingsClicked() {
        styleBox(7);
        setNode(settingsPane);
    }

    @FXML
    private void logoutClicked() {
        new LogDao().setLog();
        UserInformation.id = 0;
        //System.out.println("Logout clicked");
        Stage stage;
        Parent root = null;
        //get reference - stage         
        stage = (Stage) holderPane.getScene().getWindow();
        try {
            //load up other FXML document
            root = FXMLLoader.load(getClass().getResource("/com/houarizegai/learnsql/resources/views/Login.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(SystemController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //create a new scene with root and set the stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void aboutClicked() {
        if (aboutDialog.isVisible()) {
            return;
        }

        aboutDialog.show();

    }

    @FXML
    private void guideClicked() {
        styleBox(8);
        setNode(guidePane);
    }

    private void styleBox(int index) {
        // This function change the style+color of the menu (Menu Item Selected)
        iconHome.setFill(Paint.valueOf("#4a4949"));
        iconCon.setFill(Paint.valueOf("#4a4949"));
        iconGrades.setFill(Paint.valueOf("#4a4949"));
        iconTrac.setFill(Paint.valueOf("#4a4949"));
        iconQuestion.setFill(Paint.valueOf("#4a4949"));
        iconAccount.setFill(Paint.valueOf("#4a4949"));
        iconChat.setFill(Paint.valueOf("#4a4949"));
        iconSettings.setFill(Paint.valueOf("#4a4949"));
        iconGuide.setFill(Paint.valueOf("#4a4949"));

        boxHome.setStyle("-fx-border: 0");
        boxCon.setStyle("-fx-border: 0");
        boxGrades.setStyle("-fx-border: 0");
        boxTrac.setStyle("-fx-border: 0");
        boxQuestion.setStyle("-fx-border: 0");
        boxAccount.setStyle("-fx-border: 0");
        boxChat.setStyle("-fx-border: 0");
        boxSettings.setStyle("-fx-border: 0");
        boxGuide.setStyle("-fx-border: 0");

        switch (index) {
            case 0:
                boxHome.setStyle("-fx-background-color: #f2f2f2;-fx-border-color: #0078D7;-fx-border-width: 0px 0px 0px 3px;-fx-border-style: solid;");
                iconHome.setFill(Paint.valueOf("#2196f3"));
                break;
            case 1:
                boxCon.setStyle("-fx-background-color: #f2f2f2;-fx-border-color: #0078D7;-fx-border-width: 0px 0px 0px 3px;-fx-border-style: solid;");
                iconCon.setFill(Paint.valueOf("#2196f3"));
                break;
            case 2:
                boxGrades.setStyle("-fx-background-color: #f2f2f2;-fx-border-color: #0078D7;-fx-border-width: 0px 0px 0px 3px;-fx-border-style: solid;");
                iconGrades.setFill(Paint.valueOf("#2196f3"));
                break;
            case 3:
                boxTrac.setStyle("-fx-background-color: #f2f2f2;-fx-border-color: #0078D7;-fx-border-width: 0px 0px 0px 3px;-fx-border-style: solid;");
                iconTrac.setFill(Paint.valueOf("#2196f3"));
                break;
            case 4:
                boxQuestion.setStyle("-fx-background-color: #f2f2f2;-fx-border-color: #0078D7;-fx-border-width: 0px 0px 0px 3px;-fx-border-style: solid;");
                iconQuestion.setFill(Paint.valueOf("#2196f3"));
                break;
            case 5:
                boxAccount.setStyle("-fx-background-color: #f2f2f2;-fx-border-color: #0078D7;-fx-border-width: 0px 0px 0px 3px;-fx-border-style: solid;");
                iconAccount.setFill(Paint.valueOf("#2196f3"));
                break;
            case 6:
                boxChat.setStyle("-fx-background-color: #f2f2f2;-fx-border-color: #0078D7;-fx-border-width: 0px 0px 0px 3px;-fx-border-style: solid;");
                iconChat.setFill(Paint.valueOf("#2196f3"));
                break;
            case 7:
                boxSettings.setStyle("-fx-background-color: #f2f2f2;-fx-border-color: #0078D7;-fx-border-width: 0px 0px 0px 3px;-fx-border-style: solid;");
                iconSettings.setFill(Paint.valueOf("#2196f3"));
                break;
            case 8:
                boxGuide.setStyle("-fx-background-color: #f2f2f2;-fx-border-color: #0078D7;-fx-border-width: 0px 0px 0px 3px;-fx-border-style: solid;");
                iconGuide.setFill(Paint.valueOf("#2196f3"));
                break;
        }
    }

}
