package kz.almaty.boombrains.util.helpers.dialog_helper;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import es.dmoral.toasty.Toasty;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.ui.game_pages.start_page.AreYouReadyActivity;
import kz.almaty.boombrains.ui.main_pages.MainActivity;
import kz.almaty.boombrains.util.helpers.preference.SharedPrefManager;
import kz.almaty.boombrains.util.helpers.socket_helper.SocketManager;

public abstract class DialogHelperActivity extends SocketManager {

    TextView returnGame, restart, sound, exit, contentTxt;
    LinearLayout pauseBack;
    Dialog dialog, dialogLife;

    CardView cardCont, cardFinish;
    ImageView closeImg;
    ConstraintLayout closeConst;

    private static final String format = "%02d:%02d";
    CountDownTimerWithPause countDownTimer;
    InterstitialAd mInterstitialAd;

    public void setupLifeDialog(Context context, int color) {
        dialogLife = new Dialog(context, R.style.mytheme);
        dialogLife.setContentView(R.layout.ad_for_life);
        dialogLife.setCancelable(false);

        contentTxt = dialogLife.findViewById(R.id.contentTxt);
        cardCont = dialogLife.findViewById(R.id.cardCont);
        cardFinish = dialogLife.findViewById(R.id.cardFinish);
        closeImg = dialogLife.findViewById(R.id.closeImg);
        closeConst = dialogLife.findViewById(R.id.closeConst);

        contentTxt.setTextColor(getResources().getColor(color));
        cardCont.setCardBackgroundColor(getResources().getColor(color));
        cardFinish.setCardBackgroundColor(getResources().getColor(color));

        closeImg.setColorFilter(getResources().getColor(color));

        cardCont.setOnClickListener(v -> showGoogleAdd());

        cardFinish.setOnClickListener(v -> {
            dialogLife.dismiss();
            startNewActivity();
        });

        closeConst.setOnClickListener(v -> {
            dialogLife.dismiss();
            startNewActivity();
        });
    }

    public void showLifeDialog(Context context) {
        pauseTimer();
        if (SharedPrefManager.isNetworkOnline(context)) {
            dialogLife.show();
        } else {
            startNewActivity();
        }
    }

    public int result(int score) {
        if (score >= 1000) {
            return score / 1000;
        }
        return 0;
    }

    public void hideLifeDialog() {
        dialogLife.dismiss();
        resumeTimer();
    }

    public void setupDialog(Context context, int style, int drawable, int position, String name) {
        dialog = new Dialog(context, style);
        dialog.setContentView(R.layout.pause_layout);
        dialog.setCancelable(false);
        returnGame = dialog.findViewById(R.id.return_game);
        restart = dialog.findViewById(R.id.restart_game);
        sound = dialog.findViewById(R.id.sound_on);
        exit = dialog.findViewById(R.id.exit_to_main);
        pauseBack = dialog.findViewById(R.id.pauseBack);

        if (SharedPrefManager.isSoundEnabled(getApplication())) {
            sound.setText(getString(R.string.Sound));
        } else {
            sound.setText(getString(R.string.Mute));
        }

        sound.setOnClickListener(v -> {
            if (sound.getText().toString().equals(getString(R.string.Mute))) {
                sound.setText(getString(R.string.Sound));
                SharedPrefManager.setSoundEnabled(getApplication(), true);
            } else {
                sound.setText(getString(R.string.Mute));
                SharedPrefManager.setSoundEnabled(getApplication(), false);
            }
        });

        pauseBack.setBackgroundResource(drawable);

        returnGame.setOnClickListener(v -> hidePauseDialog());

        restart.setOnClickListener(v -> {
            finish();
            overridePendingTransition( 0, 0);
            startIntent(new Intent(getApplication(), AreYouReadyActivity.class), position, name);
            overridePendingTransition( 0, 0);
        });

        exit.setOnClickListener(v -> {
            startActivity(new Intent(context, MainActivity.class));
            finishAffinity();
        });
    }

    public void vibrate(int mill) {
        if (SharedPrefManager.isVibrateEnabled(getApplication())) {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(mill);
        }
    }

    public void startIntent(Intent intent, int position, String name) {
        intent.putExtra("position", position);
        if (!name.equals("")) {
            intent.putExtra("name", name);
        }
        startActivity(intent);
    }

    public void showPauseDialog() {
        dialog.show();
        pauseTimer();
    }

    public void hidePauseDialog() {
        dialog.dismiss();
        resumeTimer();
    }

    public void setAudio(int raw) {
        if (SharedPrefManager.isSoundEnabled(getApplication())) {
            MediaPlayer player = MediaPlayer.create(this, raw);
            player.setOnCompletionListener(mp -> {
                mp.reset();
                mp.release();
            });
            player.start();
        }
    }

    public void setCount() {
        SharedPrefManager.setPlayCount(getApplication(), SharedPrefManager.getPlayCount(getApplication()) + 1);
    }

    public void loadGoogleAd() {
        SharedPrefManager.setAddCount(getApplication(), SharedPrefManager.getAddCount(getApplication()) + 1);
    }

    public void startTimer(int millSec, TextView timeTxt) {
        countDownTimer = new CountDownTimerWithPause(millSec, 1000, true) {

            @SuppressLint("DefaultLocale")
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                timeTxt.setText(String.format(format, minutes, seconds));
            }

            @Override
            public void onFinish() {
                startNewActivity();
            }
        };
        countDownTimer.create();
    }

    public void dismissDialog() {
        dialog.dismiss();
        dialogLife.dismiss();
    }

    public void startNewActivity() {
    }

    public void cancel() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    public void showToast(int type, String s) {
        switch (type) {
            case 0: {
                Toasty.info(this, s, Toasty.LENGTH_SHORT).show();
                break;
            }
            case 1: {
                Toasty.success(this, s, Toasty.LENGTH_SHORT).show();
                break;
            }
            case 2: {
                Toasty.error(this, s, Toasty.LENGTH_SHORT).show();
                break;
            }
            case 3: {
                Toasty.warning(this, s, Toasty.LENGTH_SHORT).show();
                break;
            }
            default: {
                Toasty.normal(this, s, Toasty.LENGTH_SHORT).show();
                break;
            }
        }
    }

    public boolean isPaused() {
        return countDownTimer != null && countDownTimer.isPaused();
    }

    public boolean isRunning() {
        return countDownTimer != null && countDownTimer.isRunning();
    }

    public void pauseTimer() {
        if (countDownTimer != null && isRunning()) {
            countDownTimer.pause();
        }
    }

    public void resumeTimer() {
        if (countDownTimer != null && isPaused()) {
            countDownTimer.resume();
        }
    }

    public void loadAddForLife() {
        MobileAds.initialize(this, initializationStatus -> Log.d("INITIALIZATION::", "COMPLETED"));
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7342268862236285/2044423261");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
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
                hideLifeDialog();
                startWithLife();
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }

    public void startWithLife() {

    }

    private void showGoogleAdd() {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                showToast(4, getString(R.string.CheckConnection));
            }
            SharedPrefManager.setAddCount(getApplication(), 0);
    }
}
