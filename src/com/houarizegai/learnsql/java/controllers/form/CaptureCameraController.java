package com.houarizegai.learnsql.java.controllers.form;

import com.github.sarxos.webcam.Webcam;
import com.houarizegai.learnsql.java.controllers.ChatController;
import de.jensd.fx.glyphs.icons525.Icons525;
import de.jensd.fx.glyphs.icons525.Icons525View;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import java.awt.Dimension;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class CaptureCameraController implements Initializable {

    public static int typeOpen; // Select Image => 0/Video => 1/Call => 2
    public static Webcam webcam;
    public static boolean isCapture = false;
    public static ImageView imageSended;

    @FXML
    private HBox bottomHBox;

    @FXML
    private ImageView imageView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        imageSended = null;
        imageView.setImage(null);

        switch (typeOpen) {
            case 0:
                initializeCaptureImage();
                break;
            case 1:
                initializeVideoImage();
                break;

        }
    }

    private void initializeCaptureImage() {
        webcam = Webcam.getDefault();
        webcam.setViewSize(new Dimension(640, 480));
        webcam.open();
        new VideoTacker().start();

        MaterialDesignIconView reloadIcon = new MaterialDesignIconView(MaterialDesignIcon.REPLAY);
        reloadIcon.setSize("38");
        reloadIcon.getStyleClass().add("icon-open-camera");
        reloadIcon.setOnMouseClicked(e -> {
            isCapture = false;
            new VideoTacker().start();
        });

        Icons525View captureImageIcon = new Icons525View(Icons525.CAMERA);
        captureImageIcon.setSize("36");
        captureImageIcon.getStyleClass().add("icon-open-camera");
        captureImageIcon.setOnMouseClicked(e -> {
            isCapture = true;
        });

        Icons525View sendImageIcon = new Icons525View(Icons525.MAIL_SEND);
        sendImageIcon.setSize("36");
        sendImageIcon.getStyleClass().add("icon-open-camera");
        sendImageIcon.setOnMouseClicked(e -> {
            isCapture = false;
            imageSended = new ImageView(imageView.getImage());
            ChatController.captureChooser.close(); // close the dialog
        });

        bottomHBox.getChildren().clear();
        bottomHBox.getChildren().addAll(reloadIcon, captureImageIcon, sendImageIcon);
    }

    private void initializeVideoImage() {
        webcam = Webcam.getDefault();
        webcam.setViewSize(new Dimension(640, 480));
        webcam.open();
        new VideoTacker().start();

        MaterialIconView videoIcon = new MaterialIconView(MaterialIcon.VIDEOCAM);
        videoIcon.setSize("38");
        videoIcon.getStyleClass().add("icon-open-camera");
        videoIcon.setOnMouseClicked(e -> {
            if (videoIcon.getGlyphName().equals(MaterialIcon.VIDEOCAM.name())) {
                videoIcon.setIcon(MaterialIcon.VIDEOCAM_OFF);
            } else {
                videoIcon.setIcon(MaterialIcon.VIDEOCAM);
            }
        });
        MaterialIconView audioIcon = new MaterialIconView(MaterialIcon.VOLUME_UP);
        audioIcon.setSize("38");
        audioIcon.getStyleClass().add("icon-open-camera");
        audioIcon.setOnMouseClicked(e -> {
            if (audioIcon.getGlyphName().equals(MaterialIcon.VOLUME_UP.name())) {
                audioIcon.setIcon(MaterialIcon.VOLUME_OFF);
            } else {
                audioIcon.setIcon(MaterialIcon.VOLUME_UP);
            }
        });

        bottomHBox.getChildren().clear();
        bottomHBox.getChildren().addAll(videoIcon, audioIcon);
    }

    class VideoTacker extends Thread {

        @Override
        public void run() {
            while (!isCapture) {
                try {
                    imageView.setImage(SwingFXUtils.toFXImage(webcam.getImage(), null));
                    sleep(30);
                } catch (InterruptedException ex) {
                    //Logger.getLogger(CaptureCameraController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
