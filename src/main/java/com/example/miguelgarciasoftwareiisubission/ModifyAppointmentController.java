package com.example.miguelgarciasoftwareiisubission;
import DatabaseAccessObject.AppointmentsAccess;
import DatabaseAccessObject.ContactsAccess;
import DatabaseAccessObject.CustomersAccess;
import DatabaseAccessObject.UsersAccess;
import Model.Appointments;
import Model.Contacts;
import Model.Customers;
import helper.JDBC;
import helper.TimeLogicConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/*
get current time, round to nearest hour
Then autofill?
*/

public class ModifyAppointmentController implements Initializable {

    @FXML private Label appointmentIDLabel;
    @FXML private Label userIDLabel;
    @FXML private TextField appointmentTitleField;
    //make a combo box, should only select current customers, not create new ones
    //@FXML private TextField customerNameField;
    @FXML private ComboBox customerNameField;
    @FXML private TextField customerIDField;
    //@FXML private ComboBox customerIDField;
    @FXML private ComboBox contactField;
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
    @FXML private TextField descriptionField;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    ObservableList<String> amPmList = FXCollections.observableArrayList("AM", "PM");
    ObservableList<String> minuteList = FXCollections.observableArrayList("00", "15", "30", "45");
    ObservableList<String> hourList = FXCollections.observableArrayList("12", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11");

    @FXML
    private ChoiceBox amPmChoiceBox;
    @FXML
    private ChoiceBox minuteBox;

    private int contactNameID;

    /*static void generateCustomersList() throws SQLException {
        ObservableList<Customers> allCustomersList = CustomersAccess.getCustomers();
        ObservableList<String> allCustomerNamesList = FXCollections.observableArrayList();
        allCustomersList.stream().map(Customers::getCustomerName).forEach(allCustomerNamesList::add);
    }*/
    public void getAppointmentInfo(Appointments selectedAppointment) throws SQLException, IOException {
        appointmentIDLabel.setText(String.valueOf(selectedAppointment.getAppointmentID()));
        userIDLabel.setText(String.valueOf(selectedAppointment.getAppointmentUserID()));
        appointmentTitleField.setText(selectedAppointment.getAppointmentTitle());
        //first set CustomerID and then get Name from DB, same for Contact
        customerIDField.setText(String.valueOf(selectedAppointment.getAppointmentCustomerID()));
        locationField.setText(selectedAppointment.getAppointmentLocation());
        typeField.setText(selectedAppointment.getAppointmentType());
        descriptionField.setText(selectedAppointment.getAppointmentDescription());

        //get time from DB, convert to local time, then split into Hour, Minute, AM/PM
        LocalDateTime apptStartTime = selectedAppointment.getAppointmentStart();
        LocalDateTime apptEndTime = selectedAppointment.getAppointmentEnd();

        //sets start time from appointment
        String[] appStartDateTime = TimeLogicConverter.deconvertTime(apptStartTime);
        boolean startPmFlag = Integer.parseInt(appStartDateTime[1]) > 12;

        startDatePicker.setValue(LocalDate.parse(appStartDateTime[0]));
        startHourChoice.setValue(appStartDateTime[1]);
        startMinuteChoice.setValue(appStartDateTime[2]);
        if (startPmFlag) {
            startAmPmChoice.setValue("PM");
            int tempHour = Integer.parseInt(appStartDateTime[1]) - 12;
            startHourChoice.setValue(String.valueOf(tempHour));
        }

        //sets end time from appointment
        String[] endStartDateTime = TimeLogicConverter.deconvertTime(apptEndTime);
        boolean endPmFlag = Integer.parseInt(endStartDateTime[1]) > 12;

        endDatePicker.setValue(LocalDate.parse(endStartDateTime[0]));
        endHourChoice.setValue(endStartDateTime[1]);
        endMinuteChoice.setValue(endStartDateTime[2]);
        if (endPmFlag) {
            endAmPmChoice.setValue("PM");
            int tempHour = Integer.parseInt(endStartDateTime[1]) - 12;
            endHourChoice.setValue(String.valueOf(tempHour));
        }

       customerAppointmentGetCustomerName(selectedAppointment.getAppointmentCustomerID());
       contactAppointmentGetContactName(selectedAppointment.getAppointmentContactID());


    }

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)  {
        //sets start hour times
        startHourChoice.setItems(hourList);
        startMinuteChoice.setItems(minuteList);
        startAmPmChoice.setItems(amPmList);
        startAmPmChoice.setValue("AM");
        //sets start hour times
        endHourChoice.setItems(hourList);
        endMinuteChoice.setItems(minuteList);
        endAmPmChoice.setItems(amPmList);
        endAmPmChoice.setValue("AM");

        int currUserID = LoginScreenController.getCurrUserID();
        userIDLabel.setText(String.valueOf(currUserID));


        //check to see date and time. If date is the same, will not allow user to select earlier hour

        ObservableList<Customers> allCustomersList = null;
        try {
            allCustomersList = CustomersAccess.getCustomers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            ObservableList<Appointments> allAppointmentsList = AppointmentsAccess.getAppointments();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ObservableList<Contacts> allContactsList = null;
        try {
            allContactsList = ContactsAccess.getContacts();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<String> allCustomerNamesList = FXCollections.observableArrayList();
        ObservableList<String> allContactNamesList = FXCollections.observableArrayList();

        int intCurrApptID = 0;
        try {
            intCurrApptID = AppointmentsAccess.generateAppointmentID();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String stringCurrApptID = String.valueOf(intCurrApptID);
        /*FilteredList<Customers> customerNamesList = new FilteredList<>(allCustomersList,
                i-> i.getCustomerName() ==);*/
        allCustomersList.stream().map(Customers::getCustomerName).forEach(allCustomerNamesList::add);
        allContactsList.stream().map(Contacts::getContactName).forEach(allContactNamesList::add);

        appointmentIDLabel.setText(stringCurrApptID);
        customerNameField.setItems(allCustomerNamesList);
        contactField.setItems(allContactNamesList);

    }


    @FXML
    void customerSelectedGetCustomerID(ActionEvent event) throws IOException, SQLException {
        String customerName = customerNameField.getValue().toString();
        int customerID = CustomersAccess.getSelectedCustomerID(customerName);
        customerIDField.setText(String.valueOf(customerID));

    }

    @FXML
    void contactSelectedGetContactsID(ActionEvent event) throws IOException, SQLException {
        String contactName = contactField.getValue().toString();
        int contactID = ContactsAccess.getSelectedContactID(contactName);
        //System.out.println(contactID);
        contactNameID = contactID;

    }


    @FXML
    void customerAppointmentGetCustomerName(int customerID) throws IOException, SQLException {
        String customerName = CustomersAccess.getSelectedCustomerName(customerID);
        customerNameField.setValue(customerName);

    }

    @FXML
    void contactAppointmentGetContactName(int contactID) throws IOException, SQLException {
        contactNameID = contactID;
        String contactName = ContactsAccess.getSelectedContactName(contactID);
        contactField.setValue(contactName);

    }



    @FXML
    void modifyAppointmentSaveButton(ActionEvent event) throws IOException, SQLException {
        //get something to count total user IDs, then +1 to generate appointment ID

        String updateStatement = "UPDATE client_schedule.appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, " +
        "Create_Date = ?, Created_By = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ?  WHERE APPOINTMENT_ID = ?";

        JDBC.openConnection();
        JDBC.setPreparedStatement(JDBC.connection, updateStatement);
        PreparedStatement ps = JDBC.getPreparedStatement();

        int currApptID = Integer.parseInt(appointmentIDLabel.getText());
        String currApptTitle = appointmentTitleField.getText();
        String currApptLocation = locationField.getText();
        String currApptType = typeField.getText();
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
        //System.out.println(startDateTimeString);
        //System.out.println(endDateTimeString);
        //Now convert dateTimeStrings to format to enter into database. Do we need UTC?
        String startDateTimeStringUTC = TimeLogicConverter.convertDateTimeToUTC(startDateTimeString);
        String endDateTimeStringUTC = TimeLogicConverter.convertDateTimeToUTC(endDateTimeString);

        ps.setString(1, currApptTitle);
        ps.setString(2, currApptDescription);
        ps.setString(3, currApptLocation);
        ps.setString(4, currApptType);
        //ps.setTimestamp(6, Timestamp.valueOf(startLocalDateTimeToAdd));
        //ps.setTimestamp(6, Timestamp.valueOf(converted to UTC start date));
        //ps.setTimestamp(7, Timestamp.valueOf(conerted to UTS end date));
        ps.setTimestamp(5, Timestamp.valueOf(startDateTimeStringUTC));
        ps.setTimestamp(6, Timestamp.valueOf(endDateTimeStringUTC));
        //need to verify this is correct
        ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(8, "admin");
        ps.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
        ps.setInt(10, 1);
        ps.setInt(11, Integer.parseInt(customerIDField.getText()));
        //ps.setInt(13, Integer.parseInt(contactAccess.findContactID(addAppointmentContact.getValue())));
        //ps.setInt(14, Integer.parseInt(contactAccess.findContactID(userIDLabel.getText())));
        ps.setInt(12, Integer.parseInt(userIDLabel.getText()));
        ps.setInt(13, contactNameID);
        ps.setInt(14, currApptID);

        //System.out.println("ps " + ps);
        ps.execute();


        JDBC.closeConnection();

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();

    }

    @FXML public void modifyAppointmentCancelButton (ActionEvent event) throws IOException {
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
