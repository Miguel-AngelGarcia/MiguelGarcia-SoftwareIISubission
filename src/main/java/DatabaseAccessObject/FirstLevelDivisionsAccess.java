package DatabaseAccessObject;

import Model.FirstLevelDivisions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import helper.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static helper.JDBC.connection;

public class FirstLevelDivisionsAccess {

    public static ObservableList<FirstLevelDivisions> getFirstLevelDivisions() throws SQLException{

        ObservableList<FirstLevelDivisions> firstLevelDivisionsObservableList = FXCollections.observableArrayList();
        JDBC.openConnection();
        String query_string = "SELECT * FROM client_schedule.first_level_divisions";

        PreparedStatement ps = connection.prepareStatement(query_string);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int divisionID = rs.getInt("Division_ID");
            String division = rs.getString("Division");
            int countryID = rs.getInt("Country_ID");

            FirstLevelDivisions firstLevelDivisions = new FirstLevelDivisions(divisionID, division, countryID);

            firstLevelDivisionsObservableList.add(firstLevelDivisions);

        }//end of while loop

        JDBC.closeConnection();
        return firstLevelDivisionsObservableList;

    }//end of ObservableList<FirstLevelDivisions> getFirstLevelDivisions()

    public static int getDivisionIDfromDivisionName(String divisionName) throws SQLException {

        int divisionID = 0;
        JDBC.openConnection();
        String query_string = "SELECT Division_ID FROM client_schedule.first_level_divisions " +
                "WHERE Division = '" + divisionName + "' LIMIT 1";
        PreparedStatement ps = connection.prepareStatement(query_string);
        ResultSet rs = ps.executeQuery();

        while(rs.next()) {

            divisionID = rs.getInt("Division_ID");

        } // end of while loop

        JDBC.closeConnection();
        return divisionID;
    } // end of getDivisionIDfromDivisionName


}
