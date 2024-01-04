package com.example.todolist;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTime {

    private final static long HOUR = 1000L*60L*60L;
    private final static Calendar calendar = Calendar.getInstance();
    private final static long DAY = HOUR*24;
    private final static long WEEK = DAY*7;
    private final static long MONTH = 30*DAY;
    private final static long YEAR = 365*DAY;


    public final static  String[] HOW_MANY_TIME = new String[]
            {"Overdue", "< Hour", "Today", "Tomorrow", "< Week",
                    "< Month", "< Year", "More than year"};



    private static SimpleDateFormat simpleDateFormat
            = new SimpleDateFormat("EEEE, HH:mm, yyyy MMMM dd", Locale.ENGLISH);


    public static String fromDateToString(Date date) {
        return simpleDateFormat.format(date);
    }
    public static Date fromStringToDate(String stringDate) {
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(stringDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }

    public static boolean checkIsTomorrow(Date date) {
        Date currentDate = new Date();
        long offset = (long)calendar.get(Calendar.ZONE_OFFSET);
        long dateTime = date.getTime();
        long currentDateTime = currentDate.getTime();
        long nextDay = currentDateTime + DAY - currentDateTime % DAY - offset;
        return dateTime - currentDateTime > 0 && dateTime < nextDay;
    }
    public static int calculateLeftTime(Date taskDate) {
        Date currentDate = new Date();
        long currentDateTime, taskDateTime;
        long offset = (long)calendar.get(Calendar.ZONE_OFFSET);

        currentDateTime = currentDate.getTime();
        taskDateTime = taskDate.getTime();

        long nextDay = currentDateTime + DAY - currentDateTime % DAY - offset;
        long difTime = taskDateTime - currentDateTime;
        if (difTime < 0) {
            return 0;
        }
        else if (difTime < HOUR) {
            return 1;
        }
        else if (checkIsTomorrow(new Date(taskDateTime))) {
            return 2;
        }
        else if (taskDateTime < nextDay + DAY ) {
           return 3;
        }
        else if (taskDateTime < nextDay + WEEK){
            return 4;
        }
        else if (taskDateTime < nextDay + MONTH){
            return 5;
        }
        else if (taskDateTime < nextDay + YEAR){
            return 6;
        }
        else {
            return 7;
        }
    }

    public static String fromLongToString (Long l) {
        return simpleDateFormat.format(l);
    }
}
