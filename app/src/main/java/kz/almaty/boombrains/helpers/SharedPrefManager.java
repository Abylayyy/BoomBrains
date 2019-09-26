package kz.almaty.boombrains.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

public class SharedPrefManager {

    private static final String TAG = SharedPrefManager.class.getSimpleName();
    private static final String APP_SETTINGS = "APP_SETTINGS";
    private static final String SHULTE_RECORD = "SHULTE_RECORD";
    private static final String CHISLO_RECORD = "CHISLO_RECORD";
    private static final String GAME_LANGUAGE = "GAME_LANGUAGE";
    private static final String SOUND_ENABLED = "SOUND_ENABLED";
    private static final String FIND_RECORD = "FIND_RECORD";
    private static final String NUM_ZNAKI_RECORD = "NUM_ZNAKI_RECORD";
    private static final String EQUATION_RECORD = "EQUATION_RECORD";
    private static final String PLAY_COUNT = "PLAY_COUNT";
    private static final String SHOW_AGAIN = "SHOW_AGAIN";
    private static final String ADD_COUNT = "ADD_COUNT";
    private static final String FIRST_SHOWN = "FIRST_SHOWN";
    private static final String DEVICE_ID = "DEVICE_ID";


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

    public static void setShulteRecord(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(SHULTE_RECORD, newValue);
        editor.apply();
    }

    public static String getShulteRecord(Context context) {
        return getSharedPreferences(context).getString(SHULTE_RECORD, null);
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

    public static void clearSettings(Context context) {
        setLanguage(context, null);
        setPlayCount(context, 0);
        setAddCount(context, 0);
        setNeverShowAgain(context, false);
        setIsFirstShown(context, false);
    }

    public static void clearResults(Context context) {
        setShulteRecord(context, "0");
        setChisoRecord(context, "0");
        setFindRecord(context, "0");
        setNumZnakiRecord(context, "0");
        setEquationRecord(context, "0");
        setSoundEnabled(context, false);
        setLanguage(context, null);
        setPlayCount(context, 0);
        setNeverShowAgain(context, false);
    }

    public static void setDeviceId(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(DEVICE_ID, newValue);
        editor.apply();
    }

    public static String getDeviceId(Context context) {
        String uniqueId = UUID.randomUUID().toString();
        setDeviceId(context, uniqueId);
        return getSharedPreferences(context).getString(DEVICE_ID, null);
    }

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
}
