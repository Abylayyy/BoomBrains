package kz.almaty.boombrains.helpers;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.game_pages.start_page.AreYouReadyActivity;
import kz.almaty.boombrains.main_pages.MainActivity;

public abstract class DialogHelperActivity extends AppCompatActivity {

    TextView returnGame, restart, sound, exit;
    LinearLayout pauseBack;
    Dialog dialog;
    private static final String format = "%02d:%02d";
    CountDownTimerWithPause countDownTimer;

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
    }

    public void startNewActivity() {

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
        return countDownTimer.isPaused();
    }

    public boolean isRunning() {
        return countDownTimer.isRunning();
    }

    public void pauseTimer() {
        countDownTimer.pause();
    }

    public void resumeTimer() {
        countDownTimer.resume();
    }
}
