package DatabaseAccessObject;

import Model.Customers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import helper.JDBC;
import Model.Appointments;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static helper.JDBC.connection;

public class CustomersAccess {

    public static ObservableList<Customers> getCustomers(/*Connection connection*/) throws SQLException {

        ObservableList<Customers> customersObservableList = FXCollections.observableArrayList();
        JDBC.openConnection();

        String query_string = "SELECT * FROM client_schedule.customers";

        PreparedStatement ps = connection.prepareStatement(query_string);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {

            int customerID = rs.getInt("Customer_ID");
            String customerName = rs.getString("Customer_Name");
            String customerAddress = rs.getString("Address");
            String customerPostalCode = rs.getString("Postal_Code");
            String customerPhone = rs.getString("Phone");
            int customerDivisionID = rs.getInt("Division_ID");

            Customers customer = new Customers(customerID, customerName, customerAddress, customerPostalCode,
                    customerPhone, customerDivisionID);

            customersObservableList.add(customer);

        } // end of while loop

        JDBC.closeConnection();
        return  customersObservableList;
    }   // END of  ObservableList<Customers> function

    //should we add the delete appointment action here?
    public static int getSelectedCustomerID (String customerName) throws SQLException{
        int customerID = 0;
        
        JDBC.openConnection();
        String query_string = "SELECT Customer_ID FROM client_schedule.customers WHERE Customer_Name = '" + customerName + "' LIMIT 1";
        PreparedStatement ps = connection.prepareStatement(query_string);
        ResultSet rs = ps.executeQuery();
        
        while(rs.next()) {
            customerID = rs.getInt("Customer_ID");
        }

        return customerID;
    }

}
