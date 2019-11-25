package kz.almaty.boombrains.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class SharedUpdate {
    private static final String SHULTE_UPDATE = "SHULTE_UPDATE";
    private static final String CHISLO_UPDATE = "CHISLO_UPDATE";
    private static final String FIND_UPDATE = "FIND_UPDATE";
    private static final String NUM_ZNAKI_UPDATE = "NUM_ZNAKI_UPDATE";
    private static final String EQUATION_UPDATE = "EQUATION_UPDATE";
    private static final String SLOVO_UPDATE = "SLOVO_UPDATE";
    private static final String LETTER_UPDATE = "LETTER_UPDATE";
    private static final String SQUARE_UPDATE = "SQUARE_UPDATE";
    private static final String COLOR_UPDATE = "COLOR_UPDATE";
    private static final String FIGURE_UPDATE = "FIGURE_UPDATE";
    private static final String UPDATE_RECORDS = "UPDATE_RECORDS";
    private static final String LANG_GAME = "LANG_GAME";
    private static final String RECORD_EXIST = "RECORD_EXIST";
    private static final String GAME_LANGUAGE = "GAME_LANGUAGE";
    private static final String COUNTRY_LANG = "COUNTRY_LANG";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(UPDATE_RECORDS, Context.MODE_PRIVATE);
    }

    private static SharedPreferences getLangPref(Context context) {
        return context.getSharedPreferences(LANG_GAME, Context.MODE_PRIVATE);
    }

    public static void setLanguage(Context context, String newValue) {
        final SharedPreferences.Editor editor = getLangPref(context).edit();
        editor.putString(GAME_LANGUAGE, newValue);
        editor.apply();
    }

    public static String getLanguage(Context context) {
        return getLangPref(context).getString(GAME_LANGUAGE, null);
    }

    public static void setCountry(Context context, String newValue) {
        final SharedPreferences.Editor editor = getLangPref(context).edit();
        editor.putString(COUNTRY_LANG, newValue);
        editor.apply();
    }

    public static String getCountry(Context context) {
        return getLangPref(context).getString(COUNTRY_LANG, null);
    }

    public static boolean isRecordExist(Context context) {
        return getSharedPreferences(context).getBoolean(RECORD_EXIST, false);
    }

    public static void setRecordExist(Context context, boolean newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(RECORD_EXIST, newValue);
        editor.apply();
    }

    public static void setShulteUpdate(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(SHULTE_UPDATE, newValue);
        editor.apply();
    }

    public static String getShulteUpdate(Context context) {
        return getSharedPreferences(context).getString(SHULTE_UPDATE, null);
    }

    public static String getChisloUpdate(Context context) {
        return getSharedPreferences(context).getString(CHISLO_UPDATE, null);
    }

    public static void setChisloUpdate(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(CHISLO_UPDATE, newValue);
        editor.apply();
    }

    public static String getFindUpdate(Context context) {
        return getSharedPreferences(context).getString(FIND_UPDATE, null);
    }

    public static void setFindUpdate(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(FIND_UPDATE, newValue);
        editor.apply();
    }

    public static String getNumZnakiUpdate(Context context) {
        return getSharedPreferences(context).getString(NUM_ZNAKI_UPDATE, null);
    }

    public static void setNumZnakiUpdate(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(NUM_ZNAKI_UPDATE, newValue);
        editor.apply();
    }

    public static String getEquationUpdate(Context context) {
        return getSharedPreferences(context).getString(EQUATION_UPDATE, null);
    }

    public static void setEquationUpdate(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(EQUATION_UPDATE, newValue);
        editor.apply();
    }

    public static String getSlovoUpdate(Context context) {
        return getSharedPreferences(context).getString(SLOVO_UPDATE, null);
    }

    public static void setSlovoUpdate(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(SLOVO_UPDATE, newValue);
        editor.apply();
    }

    public static String getLetterUpdate(Context context) {
        return getSharedPreferences(context).getString(LETTER_UPDATE, null);
    }

    public static void setLetterUpdate(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(LETTER_UPDATE, newValue);
        editor.apply();
    }

    public static String getSquareUpdate(Context context) {
        return getSharedPreferences(context).getString(SQUARE_UPDATE, null);
    }

    public static void setSquareUpdate(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(SQUARE_UPDATE, newValue);
        editor.apply();
    }

    public static String getColorUpdate(Context context) {
        return getSharedPreferences(context).getString(COLOR_UPDATE, null);
    }

    public static void setColorUpdate(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(COLOR_UPDATE, newValue);
        editor.apply();
    }

    public static String getFigureUpdate(Context context) {
        return getSharedPreferences(context).getString(FIGURE_UPDATE, null);
    }

    public static void setFigureUpdate(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(FIGURE_UPDATE, newValue);
        editor.apply();
    }

    public static void clear(Context context) {
        getSharedPreferences(context).edit().clear().apply();
    }

    public static int record(int position, Context context) {
        int record = 0;
        switch (position) {
            case 1: { if (getShulteUpdate(context) != null) {record = Integer.parseInt(getShulteUpdate(context));}break; }
            case 2: { if (getChisloUpdate(context) != null) {record = Integer.parseInt(getChisloUpdate(context));}break; }
            case 3: { if (getFindUpdate(context) != null) {record = Integer.parseInt(getFindUpdate(context));}break; }
            case 4: { if (getNumZnakiUpdate(context) != null) {record = Integer.parseInt(getNumZnakiUpdate(context));}break; }
            case 5: { if (getEquationUpdate(context) != null) {record = Integer.parseInt(getEquationUpdate(context));}break; }
            case 6: { if (getSlovoUpdate(context) != null) {record = Integer.parseInt(getSlovoUpdate(context));}break; }
            case 7: { if (getLetterUpdate(context) != null) {record = Integer.parseInt(getLetterUpdate(context));}break; }
            case 8: { if (getSquareUpdate(context) != null) {record = Integer.parseInt(getSquareUpdate(context));}break; }
            case 9: { if (getColorUpdate(context) != null) {record = Integer.parseInt(getColorUpdate(context));}break; }
            case 10: {if (getFigureUpdate(context) != null) {record = Integer.parseInt(getFigureUpdate(context));}break; }
        }
        return record;
    }

    public static void showToast(int type, String s, Context context) {
        switch (type) {
            case 0: {
                Toasty.info(context, s, Toasty.LENGTH_SHORT).show();
                break;
            }
            case 1: {
                Toasty.success(context, s, Toasty.LENGTH_SHORT).show();
                break;
            }
            case 2: {
                Toasty.error(context, s, Toasty.LENGTH_SHORT).show();
                break;
            }
            case 3: {
                Toasty.warning(context, s, Toasty.LENGTH_SHORT).show();
                break;
            }
            default: {
                Toasty.normal(context, s, Toasty.LENGTH_SHORT).show();
                break;
            }
        }
    }
}
