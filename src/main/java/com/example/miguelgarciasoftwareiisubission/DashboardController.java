package com.example.miguelgarciasoftwareiisubission;

import DatabaseAccessObject.CustomersAccess;
import DatabaseAccessObject.DivisionsCountriesAccess;
import Model.Appointments;
import Model.Customers;
import Model.DivisionsCountries;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.Optional;

import DatabaseAccessObject.AppointmentsAccess;
import javafx.stage.Stage;

public class DashboardController {
    //Appointments Table
    @FXML
    private TableView<Appointments> dashboardAppointmentTable;
    @FXML
    private TableColumn<Appointments, Integer> apptIdcolumn;
    @FXML
    private TableColumn<Appointments, String> apptTitleColumn;
    @FXML
    private TableColumn<Appointments, String> apptDescColumn;
    @FXML
    private TableColumn<Appointments, String> appLocationColumn;
    @FXML
    private TableColumn<Appointments, String> apptContactColumn;
    @FXML
    private TableColumn<Appointments, String> apptTypeColumn;
    @FXML
    private TableColumn<Appointments, String> apptStartColumn;
    @FXML
    private TableColumn<Appointments, String> appEndColumn;
    @FXML
    private TableColumn<Appointments, Integer> apptCustId;
    @FXML
    private TableColumn<Appointments, Integer> apptUserID;

    //Custemrs Table
    @FXML
    private TableView<Customers> dashboardCustomerTable;
    @FXML
    private TableColumn<Customers, Integer> customerIdcolumn;
    @FXML
    private TableColumn<Customers, String> customerNameColumn;
    @FXML
    private TableColumn<Customers, String> customerAddress;
    @FXML
    private TableColumn<Customers, String> customerPostalCode;
    @FXML
    private TableColumn<Customers, Integer> customerStateProvince;
    @FXML
    private TableColumn<Customers, String> customerCountry;


    @FXML
    private Button addAppointmentButton;
    @FXML
    private Button modifyAppointmentButton;
    @FXML
    private Button deleteAppointmentButton;
    @FXML
    private Button addCustomerButton;
    @FXML
    private Button modifyCustomerButton;
    @FXML
    private Button deleteCustomerButton;

    @FXML
    private TabPane tabPane;
    @FXML
    private Tab appointmentTab;
    @FXML
    private Button signOutButton;

    private Stage stage;
    private int currentDivisionID;

    //@FXML
    //private TableView<Customers> dashboardCustomerTable;


    String query_string = "SELECT * FROM client_schedule.appointments";

    @FXML
    public void initialize() throws SQLException {
        try {
            //add contents to Appointment Table
            ObservableList<Appointments> allAppointmentsList = AppointmentsAccess.getAppointments();

            apptIdcolumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
            apptTitleColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
            apptDescColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
            appLocationColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
            apptContactColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentContactID"));
            apptTypeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
            apptStartColumn.setCellValueFactory((new PropertyValueFactory<>("appointmentStart")));
            appEndColumn.setCellValueFactory((new PropertyValueFactory<>("appointmentEnd")));
            apptCustId.setCellValueFactory((new PropertyValueFactory<>("appointmentCustomerID")));
            apptUserID.setCellValueFactory((new PropertyValueFactory<>("appointmentUserID")));

            dashboardAppointmentTable.setItems(allAppointmentsList);

            //Adds contents to Customer Table
            ObservableList<Customers> allCustomersList = CustomersAccess.getCustomers();

            customerIdcolumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            customerAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
            customerPostalCode.setCellValueFactory(new PropertyValueFactory<>("customerPostalCode"));
            //will impliment code to get State/Province and Country from divisionID
            customerStateProvince.setCellValueFactory(new PropertyValueFactory<>("customerDivision"));
            customerCountry.setCellValueFactory(new PropertyValueFactory<>("customerCountry"));

            dashboardCustomerTable.setItems(allCustomersList);



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void addAppointmentButton(ActionEvent event) throws Exception {
        Parent addAppointments = FXMLLoader.load(getClass().getResource("AddAppointment.fxml"));
        Scene scene = new Scene(addAppointments);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    void modifyAppointmentButton(ActionEvent event) throws Exception {
        try {
            Appointments selectedAppointment = dashboardAppointmentTable.getSelectionModel().getSelectedItem();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ModifyAppointment.fxml"));
            loader.load();

            //get controller
            ModifyAppointmentController modifyAppointmentController = loader.getController();
            modifyAppointmentController.getAppointmentInfo(selectedAppointment);
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();

        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Select a product first");
            alert.show();
        }
    }

    @FXML
    void signOutButtonClick(ActionEvent event) throws Exception {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    @FXML
    void deleteAppointmentButton(ActionEvent event) throws SQLException {
        int selectedAppointmentID = dashboardAppointmentTable.getSelectionModel().getSelectedItem().getAppointmentID();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Do you want to delete selected appointment?");
        Optional<ButtonType> result = alert.showAndWait();

        String result_string = String.valueOf(selectedAppointmentID);
        //checks if a part is selected, user can click delete when in part_search_box
        if (result_string == "null" && result.get() == ButtonType.OK ) {
            Alert noPartSearchBar = new Alert(Alert.AlertType.ERROR);
            noPartSearchBar.setTitle("Error Message");
            noPartSearchBar.setContentText("No Appointment Selected");
            noPartSearchBar.showAndWait();
        }

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
            alertConfirm.setTitle("Confirmation");
            alertConfirm.setContentText("Appointment ID: " + selectedAppointmentID + " has been deleted");

            AppointmentsAccess.deleteAppointment(selectedAppointmentID);
            ObservableList<Appointments> allAppointmentsList = AppointmentsAccess.getAppointments();
            dashboardAppointmentTable.setItems(allAppointmentsList);
        }

    }

    @FXML
    void addCustomerButton(ActionEvent event) throws Exception {
        Parent addCustomers = FXMLLoader.load(getClass().getResource("AddCustomer.fxml"));
        Scene scene = new Scene(addCustomers);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

}
