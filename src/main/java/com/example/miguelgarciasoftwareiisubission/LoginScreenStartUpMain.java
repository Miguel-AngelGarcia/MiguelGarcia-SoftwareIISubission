package com.example.miguelgarciasoftwareiisubission;

import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.time.ZoneId;

import java.io.IOException;

/**
 *
 */
public class LoginScreenStartUpMain extends Application {

    @Override/*
    public void start(Stage stage) throws IOException {
        JDBC.openConnection();
        JDBC.closeConnection();
        FXMLLoader fxmlLoader = new FXMLLoader(AddAppointmentController.class.getResource("AddAppointment.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("I love my girlfriend!");
        stage.setScene(scene);
        stage.show();
    }*/

    public void start(Stage stage) throws IOException {
        JDBC.openConnection();
        JDBC.closeConnection();
        FXMLLoader fxmlLoader = new FXMLLoader(LoginScreenController.class.getResource("LoginScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("I love my girlfriend!");
        stage.setMinWidth(533);
        stage.setMinHeight(590);

        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

//Need to get ZONEID class
//need a resource bundle for the language stuff
//https://docs.oracle.com/javase/8/docs/api/java/util/ResourceBundle.html
//only need to translate login form
//to test, in "main method", first line, do Locale.setDefault(newLocale("fr"));
//#5fa4e4 that blue