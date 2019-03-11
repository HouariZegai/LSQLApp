package com.houarizegai.learnsql.java.controllers;

import com.houarizegai.learnsql.java.controllers.form.CaptureCameraController;
import com.jfoenix.controls.JFXDialog;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class ChatController implements Initializable {

    @FXML
    private StackPane root;
    @FXML
    private ScrollPane scrollPaneMessageBox;
    @FXML
    private VBox messageBox;
    @FXML
    private TextArea msgTypedArea;
    @FXML
    private ScrollPane contactBox, groupBox, newContactBox;

    public static JFXDialog captureChooser;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    public void fileChooser() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Files (*.*)", "*.*");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(root.getScene().getWindow());
        System.out.println(file);
    }

    @FXML
    public void imageChooser() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Images (*.png, *.jpg, *.jpeg, *.gif)", "*.png", "*.jpg", "*.jpeg", "*.gif");
        fileChooser.getExtensionFilters().add(extFilter);
        File f = fileChooser.showOpenDialog(root.getScene().getWindow());

        if (f == null) {
            return;
        }

        //Image image = new Image(f.toURI().toString());
        sendImage(f.toURI().toString());

        // Move to the bottom of the box (change to max value => ScrollPane)
        scrollPaneMessageBox.vvalueProperty().bind(messageBox.heightProperty());

        System.out.println("Reading complete.");

    }

    @FXML
    public void cameraChooser() {
        // Initialize the camera Dialog
        try {
            CaptureCameraController.typeOpen = 0;
            VBox capturePane = FXMLLoader.load(getClass().getResource("/com/houarizegai/learnsql/resources/views/form/CaptureCamera.fxml"));
            captureChooser = new JFXDialog(root, capturePane, JFXDialog.DialogTransition.TOP);

        } catch (IOException ex) {
            Logger.getLogger(ChatController.class.getName()).log(Level.SEVERE, null, ex);
        }
        captureChooser.show();
        captureChooser.setOnDialogClosed(e -> {
            CaptureCameraController.isCapture = true;
            if (CaptureCameraController.webcam.isOpen()) {
                CaptureCameraController.webcam.close();
            }

            if (CaptureCameraController.imageSended != null) {
                sendImage(CaptureCameraController.imageSended);
            }
        });
    }

    public void sendImage(String imageSelectedUrl) {
        HBox hBox = new HBox();
        hBox.setPrefSize(809, 79);
        hBox.setMinHeight(79);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        hBox.setPadding(new Insets(0, 0, 0, 10));

        ImageView iconUser = new ImageView(new Image("com/houarizegai/learnsql/resources/images/chat/user.png"));

        ImageView imageSended = new ImageView(imageSelectedUrl);
        imageSended.setFitWidth(100);
        imageSended.setFitHeight(100);

        hBox.getChildren().addAll(iconUser, imageSended);
        hBox.setSpacing(19);
        messageBox.getChildren().add(hBox);
    }

    public void sendImage(ImageView imageViewSelected) {
        HBox hBox = new HBox();
        hBox.setPrefSize(809, 79);
        hBox.setMinHeight(79);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        hBox.setPadding(new Insets(0, 0, 0, 10));

        ImageView iconUser = new ImageView(new Image("com/houarizegai/learnsql/resources/images/chat/user.png"));

        ImageView imageSended = new ImageView(imageViewSelected.getImage());
        imageSended.setFitWidth(100);
        imageSended.setFitHeight(100);

        hBox.getChildren().addAll(iconUser, imageSended);
        hBox.setSpacing(19);
        messageBox.getChildren().add(hBox);
    }

    @FXML
    private void SendMessage() {
        if (msgTypedArea.getText().matches("[ ]*")) {
            return;
        }

        HBox hBox = new HBox();
        hBox.setPrefWidth(809);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        hBox.setPadding(new Insets(0, 0, 0, 10));

        ImageView iconUser = new ImageView(new Image("com/houarizegai/learnsql/resources/images/chat/user.png"));

        TextArea contentArea = new TextArea();
        contentArea.getStyleClass().addAll("box-msg-src", "box-msg-area");
        contentArea.setWrapText(true);

        contentArea.setMinSize(78, 55);
        contentArea.setPrefSize(450, 600);
        
        contentArea.setText(msgTypedArea.getText());

        hBox.getChildren().addAll(iconUser, contentArea);
        hBox.setSpacing(19);
        messageBox.getChildren().add(hBox);

        // clear Inbox Typed by the user
        msgTypedArea.setText("");
        // Move Scroll to the bottom
        scrollPaneMessageBox.vvalueProperty().bind(messageBox.heightProperty());

    }

    @FXML
    private void moveToContact() {
        contactBox.setVisible(true);
        groupBox.setVisible(false);
        newContactBox.setVisible(false);
    }

    @FXML
    private void moveToGroup() {
        contactBox.setVisible(false);
        groupBox.setVisible(true);
        newContactBox.setVisible(false);
    }

    @FXML
    private void moveToNewContact() {
        contactBox.setVisible(false);
        groupBox.setVisible(false);
        newContactBox.setVisible(true);
    }

    @FXML
    private void callVideo() {

        // Initialize the camera Dialog
        try {
            CaptureCameraController.typeOpen = 1;
            VBox capturePane = FXMLLoader.load(getClass().getResource("/com/houarizegai/learnsql/resources/views/form/CaptureCamera.fxml"));
            captureChooser = new JFXDialog(root, capturePane, JFXDialog.DialogTransition.TOP);

        } catch (IOException ex) {
            Logger.getLogger(ChatController.class.getName()).log(Level.SEVERE, null, ex);
        }
        captureChooser.show();
        captureChooser.setOnDialogClosed(e -> {
            if (CaptureCameraController.webcam.isOpen()) {
                CaptureCameraController.webcam.close();
            }

        });
    }
}
