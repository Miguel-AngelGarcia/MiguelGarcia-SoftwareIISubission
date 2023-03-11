package helper;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeLogicConverter {

    /**
     * Method to get time information from Combo boxes and combine them into a single DateTime string
     * @param datePicked
     * @param hour
     * @param minute
     * @param pmFlag
     * @return
     */
    public static String convertTime(LocalDate datePicked, String hour, String minute, boolean pmFlag) {
        if (pmFlag) {
            int tempHour= Integer.parseInt(hour) + 12;
            hour = String.valueOf(tempHour);
        }

        if (hour.equals("12") || hour.equals("24")) {
            int tempHour= Integer.parseInt(hour) - 12;
            hour = String.valueOf(tempHour);
        }
        hour = String.format("%02d", Integer.parseInt(hour));

        String dateTimeString = datePicked + " " + hour + ":" + minute + ":00";
        return dateTimeString;
    }

    public static String[] deconvertTime(LocalDateTime givenDateTime) {
        String givenDateFromModify = givenDateTime.toString();
        String datePicked = givenDateFromModify.substring(0, 10);
        String hour = givenDateFromModify.substring(11, 13);
        String minute = givenDateFromModify.substring(14, 16);
        //System.out.println(datePicked + " " + hour + " " + minute);
        //format is yyyy-mm-dd HH:MM:SS. do if time > 12, fill AM_PM as PM on the Modify controller. Done need to worry about 12 or 0
        String[] dateTimeArray = {datePicked, hour, minute };
        return dateTimeArray;
    }

    /**
     * Method to convert dateTimeString into UTC to put into the database
     * @param dateTime
     * @return
     */
    public static String convertDateTimeToUTC(String dateTime) {
        Timestamp currentTimeStamp = Timestamp.valueOf(String.valueOf(dateTime));
        LocalDateTime locateDateAndTime = currentTimeStamp.toLocalDateTime();
        ZonedDateTime zoneDateTime = locateDateAndTime.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
        ZonedDateTime utcDateTime = zoneDateTime.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime localOffset = utcDateTime.toLocalDateTime();
        String utcReturn = localOffset.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return utcReturn;
    }




}
