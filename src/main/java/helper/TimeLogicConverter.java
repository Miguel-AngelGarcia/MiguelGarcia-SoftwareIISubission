package helper;

import java.time.LocalDate;

public class TimeLogicConverter {
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
}
