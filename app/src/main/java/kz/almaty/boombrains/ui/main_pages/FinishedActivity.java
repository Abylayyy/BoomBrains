package kz.almaty.boombrains.ui.main_pages;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.ui.game_pages.start_page.AreYouReadyActivity;
import kz.almaty.boombrains.helpers.SharedPrefManager;
import kz.almaty.boombrains.helpers.SharedUpdate;
import kz.almaty.boombrains.viewmodel.records.new_record_model.NewRecordView;
import kz.almaty.boombrains.viewmodel.records.new_record_model.NewRecordViewModel;
import kz.almaty.boombrains.viewmodel.records.send_minutes.SendSecondView;
import kz.almaty.boombrains.viewmodel.records.send_minutes.SendSecondViewModel;

@SuppressLint("SetTextI18n")
public class FinishedActivity extends AppCompatActivity implements NewRecordView, SendSecondView {

    @BindView(R.id.successImg) ImageView successImg;
    @BindView(R.id.successMessTxt) TextView successMessage;
    @BindView(R.id.errorsTxt) TextView errors;
    @BindView(R.id.yourScoreTxt) TextView scoreTxt;
    @BindView(R.id.recordTxt) TextView recordTxt;
    @BindView(R.id.zanovoBtn) TextView zanovoBtn;
    @BindView(R.id.quitBtn) TextView quitBtn;
    @BindView(R.id.parentConst) ConstraintLayout parent;
    @BindView(R.id.view3) View view1;
    @BindView(R.id.view4) View view2;

    String lang;
    int position, score, error;
    private InterstitialAd mInterstitialAd;
    private String name;
    NewRecordViewModel newRecordViewModel;
    SendSecondViewModel sendSecondViewModel;
    String second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished);
        ButterKnife.bind(this);

        loadGoogleAd();

        newRecordViewModel = ViewModelProviders.of(this).get(NewRecordViewModel.class);
        sendSecondViewModel = ViewModelProviders.of(this).get(SendSecondViewModel.class);

        position = getIntent().getIntExtra("position", 0);
        name = getIntent().getStringExtra("name");
        lang = SharedUpdate.getLanguage(getApplication());

        setBtnColor(position);
        setBackgrounds(position);

        zanovoBtn.setOnClickListener(v -> startActivities(position));

        quitBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finishAffinity();
            overridePendingTransition(0,0);
        });

        score = getIntent().getIntExtra("score", 0);
        error = getIntent().getIntExtra("errors", 0);
        String message = getIntent().getStringExtra("record");

        if (SharedPrefManager.isNetworkOnline(this) && SharedPrefManager.isUserLoggedIn(this)) {
            newRecordViewModel.setNewRecord(getGameName(position), score, this, this);
        }

        if (SharedPrefManager.isNetworkOnline(this) && SharedPrefManager.isUserLoggedIn(this)) {
            sendSecondViewModel.sendStatistics(second, this, this);
        }

        if (message != null) {
            successMessage.setText(message);
            setMeasures(successImg, R.drawable.zapomni_new_record);
            setAudio(R.raw.new_record);
        } else {
            successMessage.setText(getString(R.string.YesNoTimeOutAnswer));
            setMeasures(successImg, R.drawable.zapomni_timed_out);
            setAudio(R.raw.game_over);
        }
        scoreTxt.setText(getString(R.string.YourScore) + " " + score);
        errors.setText(getString(R.string.Mistakes) + " " + error);
    }

    private String getGameName(int position) {
        String name = "";
        switch (position) {
            case 0: name = "shulteTable"; break;
            case 1: name = "rememberNumber"; break;
            case 2: name = "findNumber"; break;
            case 3: name = "calculation"; break;
            case 4: name = "equation"; break;
            case 5: name = "shulteLetters"; break;
            case 6: name = "rememberWords"; break;
            case 7: name = "memorySquare"; break;
            case 8: name = "coloredWords"; break;
            case 9: name = "coloredFigures"; break;
        }
        return name;
    }

    private void loadGoogleAd() {
        MobileAds.initialize(this, initializationStatus -> Log.d("INITIALIZATION::", "COMPLETED"));
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7342268862236285/2044423261");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                showGoogleAdd();
                Log.d("LOADING::", "LOADED");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.d("LOADING::", "FAILED");
            }
            @Override
            public void onAdOpened() { }
            @Override
            public void onAdClicked() { }
            @Override
            public void onAdLeftApplication() { }
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }

    private void showGoogleAdd() {
        if (SharedPrefManager.getAddCount(getApplication()) >= 3) {
            new Handler().postDelayed(() -> {
                mInterstitialAd.show();
                SharedPrefManager.setAddCount(getApplication(), 0);
            }, 30);
        }
    }

    private void startActivities(int position) {
        switch (position) {
            case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: {
                finishAndStart(new Intent(this, AreYouReadyActivity.class), position);
                break;
            }
        }
    }

    private void finishAndStart(Intent intent, int position) {
        intent.putExtra("position", position);
        if (name != null) {
            intent.putExtra("name", name);
        }
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private void setBackgrounds(int position) {
        switch (position) {
            case 0: { setShulteBackgrounds();break; }
            case 1: { setZapomniChisloBackgrounds();break; }
            case 2: { setFindNumBackgrounds();break; }
            case 3: { setNumZnakiBackgrounds();break; }
            case 4: { setEquationBackgrounds();break; }
            case 5: { setShulteLetterBackgrounds();break; }
            case 6: { setRemWordsBackgrounds();break; }
            case 7: { setSquareBackgrounds();break; }
            case 8: { setColorBackgrounds();break; }
            case 9: { setFigureBackgrounds();break; }
        }
    }

    private void setFigureBackgrounds() {
        parent.setBackgroundResource(R.drawable.shape_blur);
        setRecords(SharedPrefManager.getFigureRecord(getApplication()));
    }

    private void setColorBackgrounds() {
        parent.setBackgroundResource(R.drawable.color_blur);
        setRecords(SharedPrefManager.getColorRecord(getApplication()));
    }

    private void setAudio(int raw) {
        if (SharedPrefManager.isSoundEnabled(getApplication())) {
            MediaPlayer player = MediaPlayer.create(this, raw);
            player.setOnCompletionListener(mp -> {
                mp.reset();
                mp.release();
            });
            player.start();
        }
    }

    private void setEquationBackgrounds() {
        parent.setBackgroundResource(R.drawable.equation_blur);
        setRecords(SharedPrefManager.getEquationRecord(getApplication()));
    }

    private void setSquareBackgrounds() {
        parent.setBackgroundResource(R.drawable.square_blur);
        setRecords(SharedPrefManager.getSquareRecord(getApplication()));
    }

    private void setFindNumBackgrounds() {
        parent.setBackgroundResource(R.drawable.find_blur);
        setRecords(SharedPrefManager.getFindRecord(getApplication()));
    }

    private void setZapomniChisloBackgrounds() {
        parent.setBackgroundResource(R.drawable.zapomni_chislo_blur);
        setRecords(SharedPrefManager.getChisloRecord(getApplication()));
    }

    private void setShulteBackgrounds() {
        setRecords(SharedPrefManager.getShulteRecord(getApplication()));
        parent.setBackgroundResource(R.drawable.shulte_blur);
    }

    private void setNumZnakiBackgrounds() {
        setRecords(SharedPrefManager.getNumZnakiRecord(getApplication()));
        parent.setBackgroundResource(R.drawable.number_znaki_blur);
    }

    private void setShulteLetterBackgrounds() {
        setRecords(SharedPrefManager.getShulteLetterRecord(getApplication()));
        parent.setBackgroundResource(R.drawable.shulte_letter_blur);
    }

    private void setRemWordsBackgrounds() {
        setRecords(SharedPrefManager.getSlovoRecord(getApplication()));
        parent.setBackgroundResource(R.drawable.rem_words_blur);
    }

    private void setBtnColor(int position) {
        switch (position) {
            case 0: setColors(R.color.vnimanieColor);second = "120";break;
            case 1: setColors(R.color.pamiatColor);second = "60";break;
            case 2: setColors(R.color.findColor);second = "60";break;
            case 3: setColors(R.color.numZnakiColor);second = "60";break;
            case 4: setColors(R.color.equationColor);second = "60";break;
            case 5: setColors(R.color.shulteLetterColor);setByLang();break;
            case 6: setColors(R.color.remWordsColor);second = "60";break;
            case 7: setColors(R.color.topSquare);second = "60";break;
            case 8: setColors(R.color.topColor);second = "60";break;
            case 9: setColors(R.color.topShape);second = "60";break;
        }
    }

    private void setByLang() {
        if (lang != null) {
            switch (lang) {
                case "en": case "es": second = "100"; break;
                case "kk": case "ru": second = "120"; break;
            }
        } else {
            second = "120";
        }
    }

    @SuppressLint("SetTextI18n")
    private void setRecords(String record) {
        if (record != null) {
            recordTxt.setText(getString(R.string.Record) + " " + record);
        } else {
            recordTxt.setText(getString(R.string.Record) + " 0");
        }
    }

    private void setColors(int color) {
        zanovoBtn.setTextColor(getResources().getColor(color));
        quitBtn.setTextColor(getResources().getColor(color));
    }

    private void setMeasures(ImageView image, int resource) {
        image.setImageResource(resource);
        Drawable drawable = getResources().getDrawable(resource);
        int width = drawable.getIntrinsicWidth() / 2;
        int height = drawable.getIntrinsicHeight() / 2;
        image.getLayoutParams().width = width;
        image.getLayoutParams().height = height;
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        startActivity(new Intent(getApplication(), MainActivity.class));
        overridePendingTransition(0,0);
    }

    @Override
    public void success() {
        Log.d("Success::", "YEEEES!");
    }

    @Override
    public void error(String message) {
        Log.d("ERROR::", message);
    }

    @Override
    public void successSecond() {
        Log.d("Success::", "YEEEES!");
    }

    @Override
    public void errorSecond() {
        Log.d("ERROR::", "NOOOO!");
    }
}
