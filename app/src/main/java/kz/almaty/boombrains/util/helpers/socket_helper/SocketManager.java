package kz.almaty.boombrains.util.helpers.socket_helper;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import kz.almaty.boombrains.MyApplication;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.ui.game_pages.duel.DuelActivity;
import kz.almaty.boombrains.util.helpers.preference.SharedPrefManager;
import kz.almaty.boombrains.util.helpers.preference.SharedUpdate;

public abstract class SocketManager extends AppCompatActivity {

    Socket mSocket;
    Dialog dialogAdd, dialogChallenge;
    private Button acceptBtn, rejectBtn, challengeBtn, cancelBtn;
    private TextView errorTxt, errChallenge;
    private ConstraintLayout closeAdd, closeChallenge;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadChallengeDialog();
        loadAcceptDialog();
        initSocket();
    }

    public void initSocket() {
        MyApplication application = (MyApplication) getApplication();
        mSocket = application.getSocket();
    }

    private void loadAcceptDialog() {
        dialogAdd = new Dialog(this, R.style.dialogTheme);
        dialogAdd.setContentView(R.layout.challenge_layout);
        acceptBtn = dialogAdd.findViewById(R.id.acceptChallenge);
        rejectBtn = dialogAdd.findViewById(R.id.rejectChallenge);
        errorTxt = dialogAdd.findViewById(R.id.errorTxt);
        closeAdd = dialogAdd.findViewById(R.id.closeConst);
        dialogAdd.setCanceledOnTouchOutside(false);
    }

    private void loadChallengeDialog() {
        dialogChallenge = new Dialog(this, R.style.dialogTheme);
        dialogChallenge.setContentView(R.layout.challenge_layout);
        challengeBtn = dialogChallenge.findViewById(R.id.acceptChallenge);
        cancelBtn = dialogChallenge.findViewById(R.id.rejectChallenge);
        errChallenge = dialogChallenge.findViewById(R.id.errorTxt);
        closeChallenge = dialogChallenge.findViewById(R.id.closeConst);
        dialogChallenge.setCanceledOnTouchOutside(false);
        cancelBtn.setText("Cancel");
        challengeBtn.setText("Challenge");
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
        mSocket.on("game-start", onGameStarted);
        mSocket.connect();
    }

    private Emitter.Listener onGameStarted = args -> runOnUiThread(() -> SharedUpdate.showToast(1, "Game started", this));

    public void setReadyUpdate(boolean rec, boolean req) { }

    private Emitter.Listener onReadyUpdate = args -> runOnUiThread(() -> {
        JSONObject object = (JSONObject) args[0];
        boolean recipReady = false, reqReady = false;
        try {
            recipReady = object.getBoolean("recipientReady");
            reqReady = object.getBoolean("requesterReady");

        } catch (JSONException e) {
            Log.e("SOCKET::", e.getMessage());
        }
        setReadyUpdate(recipReady, reqReady);
    });

    private Emitter.Listener onGamePreStart = args -> runOnUiThread(() -> {

        JSONObject object = (JSONObject) args[0];
        JSONObject recip, request;

        String recipName = "", requestName = "", gameName = "";
        int recipeRecord = 0, requestRecord = 0;
        boolean recipReady = false, requestReady = false;

        try {
            gameName = object.getString("gameName");
            recip = object.getJSONObject("recipient");
            request = object.getJSONObject("requester");

            recipName = recip.getString("username");
            recipeRecord = recip.getInt("record");
            recipReady = recip.getBoolean("ready");

            requestName = request.getString("username");
            requestRecord = request.getInt("record");
            requestReady = request.getBoolean("ready");

        } catch (JSONException e) {
            Log.e("SOCKET:", e.getMessage());
        }

        Intent intent = new Intent(this, DuelActivity.class);

        intent.putExtra("myName", requestName);
        intent.putExtra("myRecord", requestRecord);
        intent.putExtra("myReady", requestReady);

        intent.putExtra("oName", recipName);
        intent.putExtra("oRecord", recipeRecord);
        intent.putExtra("oReady", recipReady);

        intent.putExtra("game", gameName);

        startActivity(intent);
    });

    private Emitter.Listener onNotificationError = args -> runOnUiThread(() -> SharedUpdate.showToast(0, args[0].toString(), this));

    private Emitter.Listener onConnect = args -> runOnUiThread(() -> {
        mSocket.emit("user-init", SharedPrefManager.getUserName(this));
        SharedUpdate.showToast(1, "Connected!!!", this);
    });

    private Emitter.Listener onDisconnect = args -> runOnUiThread(() -> SharedUpdate.showToast(0, "Disconnected!!!", this));

    private Emitter.Listener onReconnect = args -> runOnUiThread(() -> SharedUpdate.showToast(1,"Reconnected!", this));

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

    private void rejectAction(String username) {
        JSONObject object = new JSONObject();
        try {
            object.put("requesterUsername", username);
            object.put("error", "Sorry, I can't play right now");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSocket.emit("reject-player",object);
    }

    private Emitter.Listener onConnectError = args -> runOnUiThread(() -> SharedUpdate.showToast(2, "Error with connection!", this));

    private Emitter.Listener onNotification = args -> runOnUiThread(() -> {
        JSONObject object = (JSONObject) args[0];
        String username = "";
        try {
            username = object.getString("requesterUsername");
        } catch (JSONException e) {
            Log.e("SOCKET:", e.getMessage());
        }
        showAcceptDialog(username);
    });

    public void destroyConnection() {
        mSocket.disconnect();
        mSocket.off(Socket.EVENT_RECONNECT, onReconnect);
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off("notification-challenge", onNotification);
        mSocket.off("notification-error", onNotificationError);
        mSocket.off("game-pre-start", onGamePreStart);
        mSocket.off("ready-update", onReadyUpdate);
        mSocket.off("game-start", onGameStarted);
    }

    public void showChallengeDialog(String username, String gameName) {

        errChallenge.setText(String.format("Challenge %s to a duel?", username));

        challengeBtn.setOnClickListener(v -> {
            challengeUser("sanch", gameName);
            dialogChallenge.dismiss();
        });

        cancelBtn.setOnClickListener(v -> dialogChallenge.dismiss());
        closeChallenge.setOnClickListener(v -> dialogChallenge.dismiss());

        dialogChallenge.show();
    }

    public void challengeUser(String username, String gameName) {
        JSONObject object = new JSONObject();
        try {
            object.put("recipientUsername", username);
            object.put("gameName", gameName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSocket.emit("challenge-player", object);
    }

    public void readyAction(String username, boolean ready) {
        JSONObject object = new JSONObject();
        try {
            object.put("username", username);
            object.put("ready", ready);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSocket.emit("ready-set", object);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialogAdd.dismiss();
        dialogChallenge.dismiss();
    }
}
