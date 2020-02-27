package kz.almaty.boombrains.util.helpers.socket_helper;


import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class MyJson {

    static String getName(@NotNull JSONObject object) {
        String myName = "";
        try {
            myName = object.getString("username");
        } catch (JSONException e) {
            Log.e("SOCKET:", e.getMessage());
        }
        return myName;
    }

    static int getRecord(@NotNull JSONObject object) {
        int record = 0;
        try {
            record = object.getInt("record");
        } catch (JSONException e) {
            Log.e("SOCKET:", e.getMessage());
        }
        return record;
    }

    static boolean getReady(@NotNull JSONObject object) {
        boolean ready = false;
        try {
            ready = object.getBoolean("ready");
        } catch (JSONException e) {
            Log.e("SOCKET:", e.getMessage());
        }
        return ready;
    }

    static String getRoomId(@NotNull JSONObject object) {
        String id = "";
        try {
            id = object.getString("gameRoomId");
        } catch (JSONException e) {
            Log.e("SOCKET:", e.getMessage());
        }
        return id;
    }

    static String getCurrent(@NotNull JSONObject object) {
        String current = "";
        try {
            current = object.getString("gameNameCurrent");
        } catch (JSONException e) {
            Log.e("SOCKET:", e.getMessage());
        }
        return current;
    }

    static ArrayList<String> getGameList(@NotNull JSONObject object) {
        ArrayList<String> list = new ArrayList<>();
        try {
            JSONArray array = object.getJSONArray("gameNameList");
            for (int i = 0; i < array.length(); i++) {
                list.add(array.getString(i));
            }
        } catch (JSONException e) {
            Log.e("SOCKET:", e.getMessage());
        }

        return list;
    }

    static int rounds(@NotNull JSONObject object) {
        int round = 0;
        try {
            round = object.getInt("rounds");
        } catch (JSONException e) {
            Log.e("SOCKET:", e.getMessage());
        }
        return round;
    }

    static int currentRound(@NotNull JSONObject object) {
        int round = 0;
        try {
            round = object.getInt("currentRound");
        } catch (JSONException e) {
            Log.e("SOCKET:", e.getMessage());
        }
        return round;
    }

    public static String gameResult(@NotNull JSONObject object) {
        String current = "";
        try {
            current = object.getString("gameResult");
        } catch (JSONException e) {
            Log.e("SOCKET:", e.getMessage());
        }
        return current;
    }

    public static boolean enemyError(@NotNull JSONObject object) {
        boolean ready = false;
        try {
            ready = object.getBoolean("isEnemyDisconnected");
        } catch (JSONException e) {
            Log.e("SOCKET:", e.getMessage());
        }
        return ready;
    }

    static boolean reqReady(@NotNull JSONObject object) {
        boolean ready = false;
        try {
            ready = object.getBoolean("requesterReady");
        } catch (JSONException e) {
            Log.e("SOCKET:", e.getMessage());
        }
        return ready;
    }

    static boolean recReady(@NotNull JSONObject object) {
        boolean ready = false;
        try {
            ready = object.getBoolean("recipientReady");
        } catch (JSONException e) {
            Log.e("SOCKET:", e.getMessage());
        }
        return ready;
    }

    static int recRecord(@NotNull JSONObject object) {
        int round = 0;
        try {
            round = object.getInt("recipientRecord");
        } catch (JSONException e) {
            Log.e("SOCKET:", e.getMessage());
        }
        return round;
    }

    static int reqRecord(@NotNull JSONObject object) {
        int round = 0;
        try {
            round = object.getInt("requesterRecord");
        } catch (JSONException e) {
            Log.e("SOCKET:", e.getMessage());
        }
        return round;
    }

    static String reqName(@NotNull JSONObject object) {
        String current = "";
        try {
            current = object.getString("requesterUsername");
        } catch (JSONException e) {
            Log.e("SOCKET:", e.getMessage());
        }
        return current;
    }
}
