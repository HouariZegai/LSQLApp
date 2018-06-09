package com.houarizegai.learnsql.java.controllers;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class GuideController implements Initializable {

    @FXML
    private ImageView imageView;
    private String folderName = "guide";
    private int pictureNumber = 0;
    private final Map<String, Integer> MAX_PICTURE = new HashMap();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MAX_PICTURE.put("guide", 2);
        MAX_PICTURE.put("doExercise", 5);
        MAX_PICTURE.put("trac", 10);
        MAX_PICTURE.put("manageQuestion", 5);
        MAX_PICTURE.put("manageAccount", 4);
        MAX_PICTURE.put("chat", 0);
        MAX_PICTURE.put("settings", 5);

        showImage();
    }

    @FXML
    private void btnGuide() {
        if (folderName.equals("guide")) {
            return;
        }
        folderName = "guide";
        pictureNumber = 0;
        showImage();
    }

    @FXML
    private void btnDoExercise() {
        if (folderName.equals("doExercise")) {
            return;
        }
        folderName = "doExercise";
        pictureNumber = 0;
        showImage();
    }

    @FXML
    private void btnTraceability() {
        if (folderName.equals("trac")) {
            return;
        }
        folderName = "trac";
        pictureNumber = 0;
        showImage();
    }

    @FXML
    private void btnManageQuestion() {
        if (folderName.equals("manageQuestion")) {
            return;
        }
        folderName = "manageQuestion";
        pictureNumber = 0;
        showImage();
    }

    @FXML
    private void btnManageAccount() {
        if (folderName.equals("manageAccount")) {
            return;
        }
        folderName = "manageAccount";
        pictureNumber = 0;
        showImage();
    }

    @FXML
    private void btnChat() {
        if (folderName.equals("chat")) {
            return;
        }
        folderName = "chat";
        pictureNumber = 0;
        showImage();
    }

    @FXML
    private void btnSettings() {
        if (folderName.equals("settings")) {
            return;
        }
        folderName = "settings";
        pictureNumber = 0;
        showImage();
    }

    @FXML
    private void moveFirst() {
        if (pictureNumber == 0) {
            return;
        }

        pictureNumber = 0;
        showImage();
    }

    @FXML
    private void moveEnd() {
        if (pictureNumber == MAX_PICTURE.get(folderName)) {
            return;
        }
        pictureNumber = MAX_PICTURE.get(folderName);
        showImage();
    }

    @FXML
    private void movePrevious() {
        if (pictureNumber == 0) {
            return;
        }
        pictureNumber--;
        showImage();
    }

    @FXML
    private void moveNext() {
        if (pictureNumber == MAX_PICTURE.get(folderName)) {
            return;
        }
        pictureNumber++;
        showImage();
    }

    public void showImage() {
        imageView.setImage(new Image("com/houarizegai/learnsql/resources/images/guide/" + folderName + "/" + pictureNumber + ".png"));

        FadeTransition ft = new FadeTransition();
        ft.setNode(imageView);
        ft.setDuration(Duration.seconds(1));
        ft.setFromValue(0.3);
        ft.setToValue(1.0);
        ft.setCycleCount(0);
        ft.setAutoReverse(true);
        ft.play();
    }
}
