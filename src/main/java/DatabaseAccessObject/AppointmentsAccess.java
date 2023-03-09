package DatabaseAccessObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import helper.JDBC;
import Model.Appointments;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

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
            System.out.println(appointmentLocation);

        } // end of while loop

        JDBC.closeConnection();
        return appointmentsObservableList;

    } // END of  ObservableList<Appointments> function
}
