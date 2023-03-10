package com.example.miguelgarciasoftwareiisubission;

import DatabaseAccessObject.UsersAccess;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Locale;

public class LoginScreenController {
    private static int currUserID;
    @FXML
    private TextField userName;
    @FXML
    private TextField password;

    @FXML
    private Label timezone;
    @FXML
    private Label errorLabel;
    @FXML
    private ImageView sadCat;
    @FXML
    private Rectangle errorBox;

    ObservableList<String> languages = FXCollections.observableArrayList("English", "French");
    @FXML
    private ChoiceBox languageBox;

    public LoginScreenController() {
    }

    @FXML
    void initialize () {
        languageBox.setValue("English");
        languageBox.setItems(languages);

        ZoneId zone = ZoneId.systemDefault();
        timezone.setText(zone.getDisplayName(TextStyle.FULL, Locale.ROOT));


    }

    /**
     *
     * @param event
     * @throws Exception
     */
    @FXML
    void signOnButton (ActionEvent event) throws Exception {
        try {
            String currUser = userName.getText();
            String currPass = password.getText();


            /*testing to see if getting user input is there
             *System.out.println(currUser);
             *System.out.print(currPass);
             **/

            int userID = UsersAccess.credentialsValidation(currUser, currPass);

            if (userID > 0) {
                currUserID = userID;
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Object scene = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
                //stage.setScene(new Scene( scene));
                stage.setScene(new Scene((Parent) scene));
                stage.show();
            } else if (userID == 0) {
                errorLabel.setOpacity(1.0);
                sadCat.setOpacity(1.0);
                errorBox.setOpacity(1.0);
            }


        } catch (NumberFormatException e) {
            /*
            DD A NEW WARNING
             */
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input Error");
            alert.setContentText("Form contains blank fields or invalid values.");
            alert.showAndWait();
            return;
        }
    }

    public static int getCurrUserID() {
        return currUserID;
    }

}
