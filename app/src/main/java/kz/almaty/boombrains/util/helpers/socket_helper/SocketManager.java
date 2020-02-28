package kz.almaty.boombrains.util.helpers.socket_helper;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import kz.almaty.boombrains.MyApplication;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.ui.game_pages.duel.DuelActivity;
import kz.almaty.boombrains.ui.game_pages.start_page.AreYouReadyActivity;
import kz.almaty.boombrains.ui.main_pages.MainActivity;
import kz.almaty.boombrains.util.helpers.preference.SharedPrefManager;
import kz.almaty.boombrains.util.helpers.preference.SharedUpdate;

public abstract class SocketManager extends AppCompatActivity {

    Socket mSocket;
    Dialog dialogAdd;
    private Button acceptBtn, rejectBtn;
    private TextView errorTxt;
    private ConstraintLayout closeAdd;
    private boolean challege;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (SharedPrefManager.isNetworkOnline(this)) {
            initSocket();
        }
    }

    public void initSocket() {
        MyApplication application = (MyApplication) getApplication();
        mSocket = application.getSocket();
    }

    /* Socket emitter listeners */

    private Emitter.Listener onForced = args -> runOnUiThread(() -> mSocket.close());

    private Emitter.Listener onGameEnded = args -> runOnUiThread(() -> {
        JSONObject object = (JSONObject) args[0];
        JSONObject recip = new JSONObject(), request = new JSONObject();

        try {
            recip = object.getJSONObject("recipient");
            request = object.getJSONObject("requester");
        } catch (JSONException e) {
            Log.e("SOCKET:", e.getMessage());
        }
        finish();
        Intent intent = new Intent(this, DuelActivity.class);
        putGameEndExtras(intent, object, request, recip);
        startActivity(intent);
    });

    private Emitter.Listener onGameStarted = args -> runOnUiThread(() -> {
        JSONObject object = (JSONObject) args[0];
        JSONObject recip = new JSONObject(), request = new JSONObject();

        try {
            recip = object.getJSONObject("recipient");
            request = object.getJSONObject("requester");
        } catch (JSONException e) {
            Log.e("SOCKET:", e.getMessage());
        }
        finish();
        Intent intent = new Intent(this, AreYouReadyActivity.class);
        putExtras(intent, object, request, recip);
        startActivity(intent);
    });

    private int getGamePosition(String name) {
        int intent = 0;
        switch (name) {
            case "shulteTable": intent = 0; break;
            case "rememberNumber": intent = 1; break;
            case "findNumber": intent = 2; break;
            case "calculation": intent = 3; break;
            case "equation": intent = 4; break;
            case "shulteLetters": intent = 5; break;
            case "rememberWords": intent = 6; break;
            case "memorySquare": intent = 7; break;
            case "coloredWords": intent = 8; break;
            case "coloredFigures": intent = 9; break;
        }
        return intent;
    }

    public void setReadyUpdate(boolean rec, boolean req) { }

    private Emitter.Listener onReadyUpdate = args -> runOnUiThread(() -> {
        JSONObject object = (JSONObject) args[0];
        setReadyUpdate(MyJson.recReady(object), MyJson.reqReady(object));
    });

    private Emitter.Listener onGamePreStart = args -> runOnUiThread(() -> {

        JSONObject object = (JSONObject) args[0];
        JSONObject recip = new JSONObject(), request = new JSONObject();

        try {
            recip = object.getJSONObject("recipient");
            request = object.getJSONObject("requester");
        } catch (JSONException e) {
            Log.e("SOCKET:", e.getMessage());
        }
        MainActivity activity = new MainActivity();
        if (!this.equals(activity)) {
            this.finish();
        }
        Intent intent = new Intent(this, DuelActivity.class);
        putExtras(intent, object, request, recip);
        startActivity(intent);
    });

    private Emitter.Listener onNotificationError = args -> runOnUiThread(() -> SharedUpdate.showToast(0, args[0].toString(), this));

    private Emitter.Listener onConnect = args -> runOnUiThread(() -> {
        SharedUpdate.showToast(1, "Connected!!!", this);
        mSocket.emit("user-init", SharedPrefManager.getUserName(this));
    });

    private Emitter.Listener onDisconnect = args -> runOnUiThread(() -> SharedUpdate.showToast(0, "Disconnected!!!", this));

    private Emitter.Listener onReconnect = args -> runOnUiThread(() -> SharedUpdate.showToast(1,"Reconnected!", this));

    private Emitter.Listener onConnectError = args -> runOnUiThread(() -> SharedUpdate.showToast(2, "Error with connection!", this));

    private Emitter.Listener onNotification = args -> runOnUiThread(() -> {
        JSONObject object = (JSONObject) args[0];
        showAcceptDialog(MyJson.reqName(object));
    });

    private Emitter.Listener onGameUpdate = args -> runOnUiThread(() -> {
        JSONObject object = (JSONObject) args[0];
        setRecordUpdates(MyJson.reqRecord(object), MyJson.recRecord(object));
    });

    public void setRecordUpdates(int reqRecord, int recRecord) { }

    /* Socket emit functions */

    public void challengeUser(String username, List<String> gameList) {
        JSONObject object = new JSONObject();
        JSONArray arr = new JSONArray(gameList);

        try {
            object.put("recipientUsername", username);
            object.put("gameNameList", arr);
            object.put("rounds", gameList.size());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("challenge-player", object);
        challege = true;
    }

    public void rejectAction(String username) {
        JSONObject object = new JSONObject();
        try {
            object.put("requesterUsername", username);
            object.put("error", "Sorry, I can't play right now");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSocket.emit("reject-player",object);
    }

    public boolean myChallenge() {
        return challege;
    }

    public void gameUpdate(int record) {
        mSocket.emit("game-update", record);
    }

    public void roundEnded() {
        mSocket.emit("game-round-end");
    }

    public void gameEnded() {
        mSocket.emit("game-end");
    }

    public void giveUp() { mSocket.emit("give-up"); }

    public void readyAction() {
        mSocket.emit("ready-set");
    }

    /* Socket and dialog connections */

    private void showAcceptDialog(String username) {
        errorTxt.setText(String.format("You are challenged to a duel by %s", username));

        acceptBtn.setOnClickListener(v -> {
            mSocket.emit("accept-challenge", username);
            dialogAdd.dismiss();
        });

        closeAdd.setOnClickListener(v -> {
            rejectAction(username);
            dialogAdd.dismiss();
        });

        rejectBtn.setOnClickListener(v -> {
            rejectAction(username);
            dialogAdd.dismiss();
        });

        dialogAdd.show();
    }

    private void putGameEndExtras(Intent intent, JSONObject object, JSONObject request, JSONObject recip) {
        putExtras(intent, object, request, recip);
        intent.putExtra("result", MyJson.gameResult(object));
        intent.putExtra("disconnected", MyJson.enemyError(object));
        intent.putExtra("gameFinished", "ok");
    }

    private void putExtras(Intent intent, JSONObject object, JSONObject request, JSONObject recip) {

        intent.putExtra("myName", MyJson.getName(request));
        intent.putExtra("myRecord", MyJson.getRecord(request));
        intent.putExtra("myReady", MyJson.getReady(request));
        intent.putExtra("myWin", MyJson.getWinCount(request));

        intent.putExtra("oName", MyJson.getName(recip));
        intent.putExtra("oRecord", MyJson.getRecord(recip));
        intent.putExtra("oReady", MyJson.getReady(recip));
        intent.putExtra("oWin", MyJson.getWinCount(recip));

        intent.putStringArrayListExtra("gameList", MyJson.getGameList(object));
        intent.putExtra("roomId", MyJson.getRoomId(object));
        intent.putExtra("currentGame", MyJson.getCurrent(object));
        intent.putExtra("rounds", MyJson.rounds(object));
        intent.putExtra("currentRound", MyJson.currentRound(object));
        intent.putExtra("type", "duel");
        intent.putExtra("position", getGamePosition(MyJson.getCurrent(object)));
    }

    public void destroyConnection() {
        mSocket.off(Socket.EVENT_RECONNECT, onReconnect);
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off("notification-challenge", onNotification);
        mSocket.off("notification-error", onNotificationError);
        mSocket.off("game-pre-start", onGamePreStart);
        mSocket.off("ready-update", onReadyUpdate);
        mSocket.off("game-update", onGameUpdate);
        mSocket.off("game-start", onGameStarted);
        mSocket.off("game-end", onGameEnded);
        mSocket.off("force-disconnect", onForced);
    }

    public void connectSocket() {
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_RECONNECT, onReconnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on("notification-challenge", onNotification);
        mSocket.on("notification-error", onNotificationError);
        mSocket.on("game-pre-start", onGamePreStart);
        mSocket.on("ready-update", onReadyUpdate);
        mSocket.on("game-update", onGameUpdate);
        mSocket.on("game-start", onGameStarted);
        mSocket.on("game-end", onGameEnded);
        mSocket.on("force-disconnect", onForced);
        mSocket.connect();
    }

    public void loadAcceptDialog(Context context) {
        dialogAdd = new Dialog(context, R.style.dialogTheme);
        dialogAdd.setContentView(R.layout.challenge_layout);
        acceptBtn = dialogAdd.findViewById(R.id.acceptChallenge);
        rejectBtn = dialogAdd.findViewById(R.id.rejectChallenge);
        errorTxt = dialogAdd.findViewById(R.id.errorTxt);
        closeAdd = dialogAdd.findViewById(R.id.closeConst);
        dialogAdd.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialogAdd != null) {
            dialogAdd.dismiss();
        }
        destroyConnection();
    }
}
