package com.naijaplanet.magosla.android.journalapp.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.naijaplanet.magosla.android.journalapp.models.User;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("SpellCheckingInspection")
public class Values {
    private static final String PREF_USER_KEY = "user_pref_key";
    private static final String PREF_USER_ID = "id";
    private static final String PREF_USER_EMAIL = "email";
    private static final String PREF_USER_NAME = "name";
    private static final String PREF_USER_PHOTO_URL = "photourl";
    // --Commented out by Inspection (30/06/2018 20:39):public static final String EXTRA_USER = "user";
    public static final String EXTRA_JOURNAL_KEY = "journal_key";
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
     * @param format the format for the time to return
     * @return a formatted date/time
     */
    public static String getFormattedDate(Long timestamp, @Nullable String format){
        if(format == null){format = Values.DATE_FORMAT;}
        final SimpleDateFormat dateFormatter = new SimpleDateFormat(format, Locale.getDefault());
        dateFormatter.setDateFormatSymbols(new DateFormatSymbols());
        return dateFormatter.format(timestamp);
    }

    /**
     *  Saves a user object to Shared preference
     * @param context the context
     * @param user the user
     */
    public static void saveUserInPreference(Context context, @NonNull User user){

        context.getSharedPreferences(PREF_USER_KEY, Context.MODE_PRIVATE)
                .edit()
                .putString(PREF_USER_ID, user.getId())
                .putString(PREF_USER_EMAIL, user.getEmail())
                .putString(PREF_USER_NAME, user.getName())
                .putString(PREF_USER_PHOTO_URL, user.getPhotoUrl())
                .apply();
    }

    /**
     * Clears the user data from the saved preference
     * @param context the application context
     */
    public static void clearUserInPreference(Context context){
        SharedPreferences pref = context.getSharedPreferences(PREF_USER_KEY, Context.MODE_PRIVATE);
        pref.edit().clear().apply();
    }

    /**
     * Gets the user from preference
     * @param context the application context
     * @return the user object
     */
    public static User getUserFromPreference(Context context){
        User user = null;
        SharedPreferences pref = context.getSharedPreferences(PREF_USER_KEY, Context.MODE_PRIVATE);
        if(pref!=null && !pref.getString(PREF_USER_ID, "").isEmpty()){
            user = new User();
            user.setId(pref.getString(PREF_USER_ID,""));
            user.setEmail(pref.getString(PREF_USER_EMAIL,""));
            user.setName(pref.getString(PREF_USER_NAME,""));
            user.setPhotoUrl(pref.getString(PREF_USER_PHOTO_URL,""));
        }
        return user;
    }

}
