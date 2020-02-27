package kz.almaty.boombrains.ui.game_pages.duel;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.iambedant.text.OutlineTextView;
import com.mikhaellopez.circularimageview.CircularImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.util.helpers.preference.SharedPrefManager;
import kz.almaty.boombrains.util.helpers.preference.SharedUpdate;
import kz.almaty.boombrains.util.helpers.socket_helper.SocketManager;

@SuppressLint("SetTextI18n")
public class DuelActivity extends SocketManager {

    @BindView(R.id.duelTxt) OutlineTextView duelTxt;
    @BindView(R.id.girlImg) CircularImageView girlImg;
    @BindView(R.id.boyImg) CircularImageView boyImg;
    @BindView(R.id.duelBtn) Button duelBtn;
    @BindView(R.id.imageView22) ImageView backImg;

    // Main duel views
    @BindView(R.id.duelGame) TextView duelGame;
    @BindView(R.id.myName) TextView myNameTv;
    @BindView(R.id.myRecord) TextView myRecordTv;
    @BindView(R.id.myReady) TextView myReadyTv;
    @BindView(R.id.oName) TextView oNameTv;
    @BindView(R.id.oRecord) TextView oRecordTv;
    @BindView(R.id.oReady) TextView oReadyTv;

    String isFinished = null, result = null;
    boolean disconnected = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.duel_activity);
        ButterKnife.bind(this);

        isFinished = getIntent().getStringExtra("gameFinished");
        result = getIntent().getStringExtra("result");
        disconnected = getIntent().getBooleanExtra("disconnected", false);

        if (isFinished != null && result != null) {
            duelTxt.setText(result);
            duelBtn.setText("Finish");
            duelBtn.setOnClickListener(v -> {
                gameEnded();
                finish();
            });
        } else {
            duelBtn.setOnClickListener(v -> readyAction());
        }

        if (disconnected) {
            backImg.setImageResource(R.drawable.duel_disconnect);
        }

        Glide.with(this).load(R.drawable.boy_1)
                .into(boyImg);

        Glide.with(this).load(R.drawable.girl_1)
                .into(girlImg);

        connectSocket();
        setMyInfo();
    }

    private void setMyInfo() {
        String myName = getIntent().getStringExtra("myName");
        int myRecord = getIntent().getIntExtra("myRecord", 0);
        String oName = getIntent().getStringExtra("oName");
        int oRecord = getIntent().getIntExtra("oRecord", 0);

        if (myChallenge()) {
            myNameTv.setText(myName);
            myRecordTv.setText(myRecord + "");
            oNameTv.setText(oName);
            oRecordTv.setText(oRecord + "");
        } else {
            myNameTv.setText(oName);
            myRecordTv.setText(oRecord + "");
            oNameTv.setText(myName);
            oRecordTv.setText(myRecord + "");
        }
        duelGame.setText(getString(
                getGameName(
                    getIntent().getStringExtra("currentGame")
                )
            )
        );
    }

    private int getGameName(String game) {
        int name = R.string.AttentionSchulteTable;
        switch (game) {
            case "shulteTable": name = R.string.AttentionSchulteTable; break;
            case "rememberNumber": name = R.string.MemoryRemNum; break;
            case "findNumber": name = R.string.AttentionFigure; break;
            case "calculation": name = R.string.NumberZnaki; break;
            case "equation": name = R.string.Equation; break;
            case "shulteLetters": name = R.string.ShulteLetters; break;
            case "rememberWords": name = R.string.RememberWords; break;
            case "memorySquare": name = R.string.SquareMemory; break;
            case "coloredWords": name = R.string.Colors; break;
            case "coloredFigures": name = R.string.Figures; break;
        }
        return name;
    }

    @Override
    public void setReadyUpdate(boolean rec, boolean req) {
        setMyReady(req);
        setHisReady(rec);
        duelTxt.setText("Round " + getIntent().getIntExtra("currentRound", 1));
    }

    private void setMyReady(boolean req) {
        if (req) {
            myReadyTv.setText("Ready");
        } else {
            myReadyTv.setText("Not Ready");
        }
    }

    private void setHisReady(boolean rec) {
        if (rec) {
            oReadyTv.setText("Ready");
        } else {
            oReadyTv.setText("Not Ready");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
