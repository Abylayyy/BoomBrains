package kz.almaty.boombrains.ui.game_pages.duel;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.util.helpers.preference.SharedPrefManager;
import kz.almaty.boombrains.util.helpers.preference.SharedUpdate;
import kz.almaty.boombrains.util.helpers.socket_helper.SocketManager;

@SuppressLint("SetTextI18n")
public class DuelActivity extends SocketManager {

    @BindView(R.id.duelTxt) TextView duelTxt;
    @BindView(R.id.girlImg) CircularImageView girlImg;
    @BindView(R.id.boyImg) CircularImageView boyImg;
    @BindView(R.id.duelBtn) Button duelBtn;

    // Main duel views
    @BindView(R.id.duelGame) TextView duelGame;
    @BindView(R.id.myName) TextView myNameTv;
    @BindView(R.id.myRecord) TextView myRecordTv;
    @BindView(R.id.myReady) TextView myReadyTv;
    @BindView(R.id.oName) TextView oNameTv;
    @BindView(R.id.oRecord) TextView oRecordTv;
    @BindView(R.id.oReady) TextView oReadyTv;

    private String game = "";
    private boolean amIReady, isHeReady;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.duel_activity);
        ButterKnife.bind(this);

        Glide.with(this).load(R.drawable.boy_1)
                .into(boyImg);

        Glide.with(this).load(R.drawable.girl_1)
                .into(girlImg);

        setMyInfo();
        setHisInfo();

        duelBtn.setOnClickListener(v -> readyAction());
    }

    private void setMyInfo() {
        String myName = getIntent().getStringExtra("myName");
        int myRecord = getIntent().getIntExtra("myRecord", 0);
        amIReady = getIntent().getBooleanExtra("myReady", false);

        myNameTv.setText(myName);
        myRecordTv.setText(myRecord + "");

        if (amIReady) {
            myReadyTv.setText("Ready");
        } else {
            myReadyTv.setText("Not Ready");
        }
    }

    private void setHisInfo() {
        String oName = getIntent().getStringExtra("oName");
        int oRecord = getIntent().getIntExtra("oRecord", 0);
        isHeReady = getIntent().getBooleanExtra("oReady", false);

        oNameTv.setText(oName);
        oRecordTv.setText(oRecord + "");

        if (isHeReady) {
            oReadyTv.setText("Ready");
        } else {
            oReadyTv.setText("Not Ready");
        }
    }

    @Override
    public void setReadyUpdate(boolean rec, boolean req) {
        if (rec) {
            oReadyTv.setText("Ready");
        } else {
            oReadyTv.setText("Not Ready");
        }
        if (req) {
            myReadyTv.setText("Ready");
        } else {
            myReadyTv.setText("Not Ready");
        }
    }
}
