package com.houarizegai.learnsql.java.controllers.form;

import com.houarizegai.learnsql.java.controllers.SystemController;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class AboutController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void btnClose() {
        SystemController.aboutDialog.close();
    }
    
    @FXML
    void goGithub() {
        try {
            Desktop.getDesktop().browse(new URI("https://www.github.com/HouariZegai"));
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(AboutController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void goFacebook() {
        try {
            Desktop.getDesktop().browse(new URI("https://www.facebook.com/HouariZegai"));
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(AboutController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void goYoutube() {
        try {
            Desktop.getDesktop().browse(new URI("https://www.youtube.com/HouariZegai"));
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(AboutController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void goLinkedIn() {
        try {
            Desktop.getDesktop().browse(new URI("https://www.linkedin.com/in/HouariZegai"));
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(AboutController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void goTwitter() {
        try {
            Desktop.getDesktop().browse(new URI("https://www.twitter.com/HouariZegai"));
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(AboutController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void goGooglePlus() {
        try {
            Desktop.getDesktop().browse(new URI("https://plus.google.com/+HouariZegaiTv"));
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(AboutController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void goInstagram() {
        try {
            Desktop.getDesktop().browse(new URI("https://www.instagram.com/HouariZegai"));
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(AboutController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
