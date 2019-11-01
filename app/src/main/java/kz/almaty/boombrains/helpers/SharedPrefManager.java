package kz.almaty.boombrains.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.lang.ref.WeakReference;

public class SharedPrefManager {

    private static final String APP_SETTINGS = "APP_SETTINGS";

    private static final String SHULTE_RECORD = "SHULTE_RECORD";
    private static final String CHISLO_RECORD = "CHISLO_RECORD";
    private static final String GAME_LANGUAGE = "GAME_LANGUAGE";
    private static final String SOUND_ENABLED = "SOUND_ENABLED";
    private static final String FIND_RECORD = "FIND_RECORD";
    private static final String NUM_ZNAKI_RECORD = "NUM_ZNAKI_RECORD";
    private static final String EQUATION_RECORD = "EQUATION_RECORD";
    private static final String SLOVO_RECORD = "SLOVO_RECORD";
    private static final String SHULTE_LETTER = "SHULTE_LETTER";
    private static final String SQUARE_RECORD = "SQUARE_RECORD";
    private static final String COLOR_RECORD = "COLOR_RECORD";
    private static final String FIGURE_RECORD = "FIGURE_RECORD";

    private static final String PLAY_COUNT = "PLAY_COUNT";
    private static final String SHOW_AGAIN = "SHOW_AGAIN";
    private static final String ADD_COUNT = "ADD_COUNT";
    private static final String FIRST_SHOWN = "FIRST_SHOWN";
    private static final String FIRST_USER = "FIRST_USER";
    private static final String VIBRATE = "VIBRATE";

    private static final String IS_USER_LOGGED_IN = "IS_USER_LOGGED_IN";
    private static final String USER_AUTH_TOKEN_KEY = "USER_AUTH_TOKEN_KEY";
    private static final String USER_EMAIL = "USER_EMAIL";
    private static final String USER_NAME = "USER_NAME";
    private static final String USER_PASS = "USER_PASS";


    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
    }

    public static void setLanguage(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(GAME_LANGUAGE, newValue);
        editor.apply();
    }

    public static String getLanguage(Context context) {
        return getSharedPreferences(context).getString(GAME_LANGUAGE, null);
    }

    public static boolean isSoundEnabled(Context context) {
        return getSharedPreferences(context).getBoolean(SOUND_ENABLED, false);
    }

    public static void setSoundEnabled(Context context, boolean newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(SOUND_ENABLED, newValue);
        editor.apply();
    }

    public static boolean isUserLoggedIn(Context context) {
        return getSharedPreferences(context).getBoolean(IS_USER_LOGGED_IN, false);
    }

    public static void setIsUserLoggedIn(Context context, boolean newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(IS_USER_LOGGED_IN, newValue);
        editor.apply();
    }

    public static boolean isVibrateEnabled(Context context) {
        return getSharedPreferences(context).getBoolean(VIBRATE, false);
    }

    public static void setVibrateEnabled(Context context, boolean newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(VIBRATE, newValue);
        editor.apply();
    }

    public static void setUserAuthTokenKey(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(USER_AUTH_TOKEN_KEY, newValue);
        editor.apply();
    }

    public static String getUserAuthTokenKey(Context context) {
        return getSharedPreferences(context).getString(USER_AUTH_TOKEN_KEY, null);
    }

    public static void setUserName(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(USER_NAME, newValue);
        editor.apply();
    }

    public static String getUserName(Context context) {
        return getSharedPreferences(context).getString(USER_NAME, null);
    }

    public static void setUserEmail(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(USER_EMAIL, newValue);
        editor.apply();
    }

    public static String getUserEmail(Context context) {
        return getSharedPreferences(context).getString(USER_EMAIL, null);
    }

    public static void setUserPass(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(USER_PASS, newValue);
        editor.apply();
    }

    public static String getUserPass(Context context) {
        return getSharedPreferences(context).getString(USER_PASS, null);
    }

    public static void setShulteRecord(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(SHULTE_RECORD, newValue);
        editor.apply();
    }

    public static String getShulteRecord(Context context) {
        return getSharedPreferences(context).getString(SHULTE_RECORD, null);
    }

    public static void setSquareRecord(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(SQUARE_RECORD, newValue);
        editor.apply();
    }

    public static String getSquareRecord(Context context) {
        return getSharedPreferences(context).getString(SQUARE_RECORD, null);
    }

    public static void setShulteLetterRecord(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(SHULTE_LETTER, newValue);
        editor.apply();
    }

    public static String getShulteLetterRecord(Context context) {
        return getSharedPreferences(context).getString(SHULTE_LETTER, null);
    }

    public static void setFindRecord(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(FIND_RECORD, newValue);
        editor.apply();
    }

    public static String getFindRecord(Context context) {
        return getSharedPreferences(context).getString(FIND_RECORD, null);
    }

    public static void setChisoRecord(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(CHISLO_RECORD, newValue);
        editor.apply();
    }

    public static String getChisloRecord(Context context) {
        return getSharedPreferences(context).getString(CHISLO_RECORD, null);
    }

    public static void setNumZnakiRecord(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(NUM_ZNAKI_RECORD, newValue);
        editor.apply();
    }

    public static String getSlovoRecord(Context context) {
        return getSharedPreferences(context).getString(SLOVO_RECORD, null);
    }

    public static void setSlovoRecord(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(SLOVO_RECORD, newValue);
        editor.apply();
    }

    public static String getNumZnakiRecord(Context context) {
        return getSharedPreferences(context).getString(NUM_ZNAKI_RECORD, null);
    }

    public static void setEquationRecord(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(EQUATION_RECORD, newValue);
        editor.apply();
    }

    public static String getEquationRecord(Context context) {
        return getSharedPreferences(context).getString(EQUATION_RECORD, null);
    }

    public static void setColorRecord(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(COLOR_RECORD, newValue);
        editor.apply();
    }

    public static String getColorRecord(Context context) {
        return getSharedPreferences(context).getString(COLOR_RECORD, null);
    }

    public static void setFigureRecord(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(FIGURE_RECORD, newValue);
        editor.apply();
    }

    public static String getFigureRecord(Context context) {
        return getSharedPreferences(context).getString(FIGURE_RECORD, null);
    }


    /* Google ad methods */
    public static void setPlayCount(Context context, int newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(PLAY_COUNT, newValue);
        editor.apply();
    }

    public static int getPlayCount(Context context) {
        return getSharedPreferences(context).getInt(PLAY_COUNT, 0);
    }

    public static void setAddCount(Context context, int newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(ADD_COUNT, newValue);
        editor.apply();
    }

    public static int getAddCount(Context context) {
        return getSharedPreferences(context).getInt(ADD_COUNT, 0);
    }

    /* Clear methods */
    public static void clearSettings(Context context) {
        setLanguage(context, null);
        setPlayCount(context, 0);
        setAddCount(context, 0);
        setNeverShowAgain(context, false);
        setIsFirstShown(context, false);
        setVibrateEnabled(context, true);
    }

    public static void clear(Context context) {
        setSquareRecord(context, "0");
    }

    public static void clearUserData(Context context) {
        setUserEmail(context, null);
        setUserName(context, null);
        setUserAuthTokenKey(context, null);
        setIsUserLoggedIn(context, false);
        setUserPass(context, null);
        clearSettings(context);
    }

    /*public static void setDeviceId(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(DEVICE_ID, newValue);
        editor.apply();
    }

    public static String getDeviceId(Context context) {
        String uniqueId = UUID.randomUUID().toString();
        setDeviceId(context, uniqueId);
        return getSharedPreferences(context).getString(DEVICE_ID, null);
    }*/

    public static boolean getNeverShowAgain(Context context) {
        return getSharedPreferences(context).getBoolean(SHOW_AGAIN, false);
    }

    public static void setNeverShowAgain(Context context, boolean newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(SHOW_AGAIN, newValue);
        editor.apply();
    }

    public static boolean getIsFirstShown(Context context) {
        return getSharedPreferences(context).getBoolean(FIRST_SHOWN, false);
    }

    public static void setIsFirstShown(Context context, boolean newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(FIRST_SHOWN, newValue);
        editor.apply();
    }

    public static boolean getIsFirstUser(Context context) {
        return getSharedPreferences(context).getBoolean(FIRST_USER, false);
    }

    public static void setIsFirstUser(Context context, boolean newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(FIRST_USER, newValue);
        editor.apply();
    }

    public static boolean isNetworkOnline(Context context) {
        boolean status=false;
        WeakReference data = null;
        try{
            ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            data = new WeakReference<>(cm);
            if(cm!=null) {
                NetworkInfo netInfo = cm.getNetworkInfo(0);
                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                    status = true;
                } else {
                    netInfo = cm.getNetworkInfo(1);
                    if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
                        status = true;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        finally {
            if(data!=null)
                data.clear();
        }
        return status;
    }
}
