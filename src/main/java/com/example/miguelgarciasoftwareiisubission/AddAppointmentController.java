package com.example.miguelgarciasoftwareiisubission;
import DatabaseAccessObject.AppointmentsAccess;
import DatabaseAccessObject.CustomersAccess;
import Model.Appointments;
import Model.Customers;
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

/*
get current time, round to nearest hour
Then autofill?
*/

public class AddAppointmentController {

    @FXML private Label appointmentIDLabel;
    @FXML private Label userIDLabel;
    @FXML private TextField appointmentTitleField;
    //make a combo box, should only select current customers, not create new ones
    //@FXML private TextField customerNameField;
    @FXML private ComboBox customerNameField;
    @FXML private TextField customerIDField;
    //@FXML private ComboBox customerIDField;
    @FXML private TextField contactField;
    @FXML private DatePicker startDatePicker;
    @FXML private ChoiceBox startHourChoice;
    @FXML private ChoiceBox startMinuteChoice;
    @FXML private ChoiceBox startAmPmChoice;
    @FXML private DatePicker endDatePicker;
    @FXML private ChoiceBox endHourChoice;
    @FXML private ChoiceBox endMinuteChoice;
    @FXML private ChoiceBox endAmPmChoice;
    @FXML private TextField locationField;
    @FXML private TextField typeField;
    @FXML private  TextField descriptionField;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    ObservableList<String> amPmList = FXCollections.observableArrayList("AM", "PM");
    ObservableList<String> minuteList = FXCollections.observableArrayList("00", "15", "30", "45");
    ObservableList<String> hourList = FXCollections.observableArrayList("12", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11");

    @FXML
    private ChoiceBox amPmChoiceBox;
    @FXML
    private ChoiceBox minuteBox;

    /*static void generateCustomersList() throws SQLException {
        ObservableList<Customers> allCustomersList = CustomersAccess.getCustomers();
        ObservableList<String> allCustomerNamesList = FXCollections.observableArrayList();
        allCustomersList.stream().map(Customers::getCustomerName).forEach(allCustomerNamesList::add);
    }*/

    @FXML
    void initialize() throws SQLException {
        //sets start hour times
        startHourChoice.setItems(hourList);
        startHourChoice.setValue("12");
        startMinuteChoice.setItems(minuteList);
        startMinuteChoice.setValue("00");
        startAmPmChoice.setItems(amPmList);
        startAmPmChoice.setValue("AM");
        //sets start hour times
        endHourChoice.setItems(hourList);
        endHourChoice.setValue("12");
        endMinuteChoice.setItems(minuteList);
        endMinuteChoice.setValue("00");
        endAmPmChoice.setItems(amPmList);
        endAmPmChoice.setValue("AM");


        //check to see date and time. If date is the same, will not allow user to select earlier hour

        ObservableList<Customers> allCustomersList = CustomersAccess.getCustomers();
        ObservableList<Appointments> allAppointmentsList = AppointmentsAccess.getAppointments();

        ObservableList<String> allCustomerNamesList = FXCollections.observableArrayList();

        int totalAppointments = allAppointmentsList.size();
        int intCurrApptID = totalAppointments + 1;
        String stringCurrApptID = String.valueOf(intCurrApptID);
        /*FilteredList<Customers> customerNamesList = new FilteredList<>(allCustomersList,
                i-> i.getCustomerName() ==);*/
        allCustomersList.stream().map(Customers::getCustomerName).forEach(allCustomerNamesList::add);
        appointmentIDLabel.setText(stringCurrApptID);
        customerNameField.setItems(allCustomerNamesList);

    }

    @FXML
    void addAppointmentSaveButton(ActionEvent event) throws IOException, SQLException {
        //get something to count total user IDs, then +1 to generate appointment ID

        String insertStatement = "INSERT INTO client_schedule.appointments (Appointment_ID, Title, Description, Location, " +
                "Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        JDBC.openConnection();
        JDBC.setPreparedStatement(JDBC.connection, insertStatement);
        PreparedStatement ps = JDBC.getPreparedStatement();

        int currApptID = Integer.parseInt(appointmentIDLabel.getText());
        String currApptTitle = appointmentTitleField.getText();
        String currApptLocation = locationField.getText();
        String currApptType = typeField.getTypeSelector();
        String currApptDescription = descriptionField.getText();
        //need to get date from 1 box, Hour, Minute and AM/PM. Then combine
        //start date time
        LocalDate apptStartDate = startDatePicker.getValue();
        String apptStartHour = startHourChoice.getValue().toString();
        String apptStartMinute = startMinuteChoice.getValue().toString();
        boolean apptStartPm = startAmPmChoice.getValue().toString().equals("PM");
        //end date time
        LocalDate apptEndDate = endDatePicker.getValue();
        String apptEndHour = endHourChoice.getValue().toString();
        String apptEndMinute = endMinuteChoice.getValue().toString();
        boolean apptEndPm = endAmPmChoice.getValue().toString().equals("PM");
        //Make this a Lambda?
        //do we need this T? or do we replace with " "
        String startDateTimeString = TimeLogicConverter.convertTime(apptStartDate, apptStartHour, apptStartMinute, apptStartPm);
        String endDateTimeString = TimeLogicConverter.convertTime(apptEndDate, apptEndHour, apptEndMinute, apptEndPm);
        System.out.println(startDateTimeString);
        System.out.println(endDateTimeString);
        //Now convert dateTimeStrings to format to enter into databse. Do we need UTC?



        ps.setInt(1, currApptID);
        ps.setString(2, currApptTitle);
        ps.setString(3, currApptDescription);
        ps.setString(4, currApptLocation);
        ps.setString(5, currApptType);
        //ps.setTimestamp(6, Timestamp.valueOf(startLocalDateTimeToAdd));
        //ps.setTimestamp(6, Timestamp.valueOf(converted to UTC start date));
        //ps.setTimestamp(7, Timestamp.valueOf(conerted to UTS end date));
        ps.setTimestamp(6, Timestamp.valueOf(startDateTimeString));
        ps.setTimestamp(7, Timestamp.valueOf(endDateTimeString));
        //need to verify this is correct
        ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(9, "admin");
        ps.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
        ps.setInt(11, 1);
        ps.setInt(12, Integer.parseInt(customerIDField.getText()));
        //ps.setInt(13, Integer.parseInt(contactAccess.findContactID(addAppointmentContact.getValue())));
        //ps.setInt(14, Integer.parseInt(contactAccess.findContactID(userIDLabel.getText())));
        ps.setInt(13, 1);
        ps.setInt(14, Integer.parseInt(contactField.getText()));

        //System.out.println("ps " + ps);
        ps.execute();


        JDBC.closeConnection();

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();

    }

    @FXML public void addAppointmentCancelButton (ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    @FXML
    void keyEnteredCustomerNameSearch (KeyEvent event) throws SQLException {

        String searchName = customerNameField.getValue().toString();
        System.out.println(searchName);

        //customerNameField.addEventHandler();

        /*
        ObservableList<Customers> allCustomersList = CustomersAccess.getCustomers();
        ObservableList<String> allCustomerNamesList = FXCollections.observableArrayList();
        allCustomersList.stream().map(Customers::getCustomerName).forEach(allCustomerNamesList::add);

        FilteredList<Customers> filteredCustomers = new FilteredList<>(allCustomersList, b-> true);


        ObservableList<String> searchMatchResults = FXCollections.observableArrayList();
        System.out.println(nameString);
        try {
            while (nameString.length() > 0) {
                //FilteredList<String> searchResultsList = allCustomerNamesList.filtered(String -> String.contains(nameString));
                //System.out.println(searchResultsList);
                //customerNameField.setItems(searchResultsList);
            }
        } catch (NumberFormatException e) {
            Alert noProductSearchBar = new Alert(Alert.AlertType.ERROR);
            noProductSearchBar.setTitle("Error Message");
            noProductSearchBar.setContentText("Product not found");
            noProductSearchBar.showAndWait();
        }
        */

    }

    /*
    @FXML
    private void doAutoComplete() {
        this.customerNameField.getEditor().focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                customerNameField.show();
            }
        });

        this.
    }
    */


}
