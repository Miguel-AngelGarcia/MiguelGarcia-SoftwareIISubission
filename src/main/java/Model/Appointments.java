package Model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static helper.JDBC.connection;

public class Appointments {

    private int appointmentID;

    private String appointmentTitle;

    private String appointmentDescription;

    private String appointmentLocation;

    private LocalDateTime appointmentStart;

    private LocalDateTime appointmentEnd;

    private LocalDateTime lastUpdate;

    private String createdBy;

    private int appointmentCustomerID;

    private int appointmentUserID;

    private int appointmentContactID;

    private String appointmentType;


    public Appointments(int appointmentID, String appointmentTitle, String appointmentDescription,
                        String appointmentLocation, int appointmentContactID, String appointmentType,
                        LocalDateTime appointmentStart, LocalDateTime appointmentEnd, int appointmentCustomerID,
                        int appointmentUserID) {
        this.appointmentID = appointmentID;
        this.appointmentTitle = appointmentTitle;
        this.appointmentDescription = appointmentDescription;
        this.appointmentLocation = appointmentLocation;
        this.appointmentContactID = appointmentContactID;
        this.appointmentType = appointmentType;
        this.appointmentStart = appointmentStart;
        this.appointmentEnd = appointmentEnd;
        //this.lastUpdate = lastUpdate;
        //this.createdBy = createdBy;
        this.appointmentCustomerID = appointmentCustomerID;
        this.appointmentUserID = appointmentUserID;


        }


    //Remember to make getters. we got 'non existing' errors because we did not have them before.


    public int getAppointmentID() {
        return appointmentID;
    }

    public String getAppointmentTitle() {
        return appointmentTitle;
    }

    public String getAppointmentDescription() {
        return appointmentDescription;
    }

    public String getAppointmentLocation() {
        return appointmentDescription;
    }

    public int getAppointmentContactID() {
        return appointmentContactID;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public LocalDateTime getAppointmentStart() {
        return appointmentStart;
    }

    public LocalDateTime getAppointmentEnd() {
        return appointmentEnd;
    }

    public int getAppointmentCustomerID() {
        return appointmentCustomerID;
    }

    public int getAppointmentUserID(){
        return appointmentUserID;
    }
}
