package DatabaseAccessObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import helper.JDBC;
import Model.Appointments;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.ResolverStyle;

import static helper.JDBC.connection;

public class AppointmentsAccess {

    public static ObservableList<Appointments> getAppointments() throws SQLException {
        ObservableList<Appointments> appointmentsObservableList = FXCollections.observableArrayList();
        JDBC.openConnection();
        String query_string = "SELECT * FROM client_schedule.appointments";

        PreparedStatement ps = connection.prepareStatement(query_string);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String appointmentTitle = rs.getString("Title");
            String appointmentDescription = rs.getString("Description");
            String appointmentLocation = rs.getString("Location");
            int appointmentContactID = rs.getInt("Contact_ID");
            String appointmentType = rs.getString("Type");
            LocalDateTime appointmentStart = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime appointmentEnd = rs.getTimestamp("End").toLocalDateTime();
            int appointmentCustomerID = rs.getInt("Customer_ID");
            int appointmentUserID = rs.getInt("User_ID");

            Appointments appointment = new Appointments(appointmentID, appointmentTitle, appointmentDescription,
                    appointmentLocation, appointmentContactID, appointmentType, appointmentStart, appointmentEnd,
                    appointmentCustomerID, appointmentUserID);

            appointmentsObservableList.add(appointment);

        } // end of while loop

        JDBC.closeConnection();
        return appointmentsObservableList;

    } // END of  ObservableList<Appointments> function

    public static int generateAppointmentID() throws SQLException {

        int appointmentID = 0;
        JDBC.openConnection();
        String query_string = "SELECT MAX(Appointment_ID) FROM client_schedule.appointments";
        PreparedStatement ps = connection.prepareStatement(query_string);
        ResultSet rs = ps.executeQuery();

        while(rs.next()) {
            int maxAppointmentID = rs.getInt("Max(Appointment_ID)");
            appointmentID = maxAppointmentID + 1;
        }

        JDBC.closeConnection();
        return appointmentID;
    }

    //should we add the delete appointment action here?
    public static void deleteAppointment(int appointmentID) throws SQLException {

        JDBC.openConnection();
        String query_string = "DELETE FROM client_schedule.appointments WHERE Appointment_ID = " + appointmentID;
        PreparedStatement ps = connection.prepareStatement(query_string);
        ps.executeUpdate();

        JDBC.closeConnection();
    }

    //delete this. just pass the pbject when you select the appointment
    public static ObservableList<Appointments> getAppointmentToModify(int givenCustomerID) throws SQLException {
        ObservableList<Appointments> appointmentInfoObservableList = FXCollections.observableArrayList();
        JDBC.openConnection();
        String query_string = "SELECT * FROM client_schedule.appointments WHERE Customer_ID = " + givenCustomerID;

        PreparedStatement ps = connection.prepareStatement(query_string);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String appointmentTitle = rs.getString("Title");
            String appointmentDescription = rs.getString("Description");
            String appointmentLocation = rs.getString("Location");
            int appointmentContactID = rs.getInt("Contact_ID");
            String appointmentType = rs.getString("Type");
            LocalDateTime appointmentStart = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime appointmentEnd = rs.getTimestamp("End").toLocalDateTime();
            int appointmentCustomerID = rs.getInt("Customer_ID");
            int appointmentUserID = rs.getInt("User_ID");

            Appointments appointment = new Appointments(appointmentID, appointmentTitle, appointmentDescription,
                    appointmentLocation, appointmentContactID, appointmentType, appointmentStart, appointmentEnd,
                    appointmentCustomerID, appointmentUserID);

            appointmentInfoObservableList.add(appointment);

        } // end of while loop

        JDBC.closeConnection();
        return appointmentInfoObservableList;

    } // END of  ObservableList<appointmentInfoObservableList> function
}
