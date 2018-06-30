package com.naijaplanet.magosla.android.journalapp.utilities;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Values {
    public static final String USER_INTENT_DATA_KEY = "user";
    public static final String INTENT_JOURNAL_ID_KEY = "journal_key";
    public static final String JOURNAL_ID_NONE = "none";
    public static final int REQUEST_CODE_SIGN_IN = 1;
    public static final String JOURNAL_DATABASE_PATH="journals";// Constant for date format
    public static final String DATE_FORMAT = "d MMM yyyy";
    @SuppressWarnings("unused")
    public static final String DATE_TIME_FORMAT = "d MMM yyyy, h:mm aaa";
    @SuppressWarnings("unused")
    public static final String TIME_FORMAT = "h:mm aaa";

    /**
     * Returns the number of milliseconds that have elapsed since January 1, 1970.
     * @return the current time stamp
     */
    public static Long getCurrentTime(){
        return new Date().getTime();
    }

    /**
     * Returns a formatted data from provided timestamp
     * @param timestamp the timestamp
     * @return a formatted date/time
     */
    public static String getFormattedDate(Long timestamp){
        final SimpleDateFormat dateFormater = new SimpleDateFormat(Values.DATE_FORMAT, Locale.getDefault());
        dateFormater.setDateFormatSymbols(new DateFormatSymbols());
        return dateFormater.format(timestamp);
    }

}
