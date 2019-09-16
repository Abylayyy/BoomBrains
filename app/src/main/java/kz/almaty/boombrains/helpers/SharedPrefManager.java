package kz.almaty.boombrains.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static final String TAG = SharedPrefManager.class.getSimpleName();
    private static final String APP_SETTINGS = "APP_SETTINGS";
    private static final String SHULTE_RECORD = "SHULTE_RECORD";
    private static final String CHISLO_RECORD = "CHISLO_RECORD";
    private static final String GAME_LANGUAGE = "GAME_LANGUAGE";
    private static final String SOUND_ENABLED = "SOUND_ENABLED";
    private static final String FIND_RECORD = "FIND_RECORD";


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
}
