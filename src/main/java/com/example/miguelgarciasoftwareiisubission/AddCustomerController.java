package com.example.miguelgarciasoftwareiisubission;
import DatabaseAccessObject.*;
import Model.*;
import helper.JDBC;
import helper.TimeLogicConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ChoiceBoxListCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ListIterator;

public class AddCustomerController {

    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField phone;
    @FXML
    private TextField streetAddress;
    @FXML
    private TextField streetAddress2;
    @FXML
    private TextField city;
    @FXML
    private ComboBox stateProvince;
    @FXML
    private ComboBox country;
    @FXML
    private TextField postalCode;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label customerIDLabel;
    @FXML
    private Label userIDLabel;

    //private int customerID;

    @FXML
    void initialize() throws SQLException {

        int customerID = CustomersAccess.generateCustomerID();
        customerIDLabel.setText(String.valueOf(customerID));

        ObservableList<FirstLevelDivisions> allFirstLevelDivisionsList = FirstLevelDivisionsAccess.getFirstLevelDivisions();
        ObservableList<Countries> allCountriesList = CountriesAccess.getCountries();

        //make countries names list to add to combo box
        ObservableList<String> allCountriesNamesList = FXCollections.observableArrayList();
        allCountriesList.stream().map(Countries::getCounty).forEach(allCountriesNamesList::add);
        country.setItems(allCountriesNamesList);

        int currUserID = LoginScreenController.getCurrUserID();
        userIDLabel.setText(String.valueOf(currUserID));


    }

    @FXML
    void addCustomerSaveButton(ActionEvent event) throws IOException, SQLException {

        String insert_statement = "INSERT INTO client_schedule.customers (Customer_ID, Customer_Name, " +
                "Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?)";

        JDBC.openConnection();
        JDBC.setPreparedStatement(JDBC.connection, insert_statement);
        PreparedStatement ps = JDBC.getPreparedStatement();

        int currCustID = Integer.parseInt(customerIDLabel.getText());
        //name string
        String custFirstName = firstName.getText().trim();
        String custLastName = lastName.getText().trim();
        String currCustName = custFirstName + " " + custLastName;

        String currCustPhone = phone.getText();
        //address string
        String address1 = streetAddress.getText().trim();
        String address2 = streetAddress2.getText().trim();
        boolean address2Flag = (address2.length() > 0);
        String currCustCity = city.getText();
        String currCustAddress = address1 + ", " + currCustCity;
        if (address2Flag) {
            currCustAddress = address1 + ", " + address2 + ", " + currCustCity;
        }

        String currCustPostal = postalCode.getText();
        //gets Division_ID from divisionName
        int currCustFivisionID = FirstLevelDivisionsAccess.getDivisionIDfromDivisionName(stateProvince.getValue().toString());

        ps.setInt(1, currCustID);
        ps.setString(2, currCustName);
        ps.setString(3, currCustAddress);
        ps.setString(4, currCustPostal);
        ps.setString(5, currCustPhone);
        ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(7, userIDLabel.getText());
        ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(9, userIDLabel.getText());
        ps.setInt(10, currCustFivisionID);


        ps.execute();

        JDBC.closeConnection();

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();


    }

    @FXML
    void setStateProvinces(ActionEvent event) throws SQLException {
        stateProvince.setDisable(false);
        ObservableList<DivisionsCountries> allDivisionsInCountryList = DivisionsCountriesAccess.getDivisionFromCountryName(country.getValue().toString());
        ObservableList<String> divivsionsFromCountryName = FXCollections.observableArrayList();

        allDivisionsInCountryList.stream().map(DivisionsCountries::getDivision).forEach(divivsionsFromCountryName::add);

        stateProvince.setItems(divivsionsFromCountryName);
        //set state/province combo box with said divisions.

    }

    @FXML public void addCustomerCancelButton (ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }



}
