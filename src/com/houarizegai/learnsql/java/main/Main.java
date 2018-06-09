package com.houarizegai.learnsql.java.main;

import com.houarizegai.learnsql.java.dao.LogDao;
import com.houarizegai.learnsql.java.models.UserInformation;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/houarizegai/learnsql/resources/views/Login.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("LSQL Application");
        stage.getIcons().add(new Image("/com/houarizegai/learnsql/resources/images/iconApp.png"));
        stage.show();
    }

    @Override
    public void stop() { // This method called in the time when we close the window
        if (UserInformation.id != 0) {
            new LogDao().setLog(); // Save information ( Login )
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
