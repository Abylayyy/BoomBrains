package kz.almaty.boombrains.helpers;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ankushgrover.hourglass.Hourglass;

import kz.almaty.boombrains.R;
import kz.almaty.boombrains.main_pages.MainActivity;

public abstract class DialogHelperActivity extends AppCompatActivity {

    TextView returnGame, restart, sound, exit;
    LinearLayout pauseBack;
    Dialog dialog, dialog2;
    private static final String format = "%02d:%02d";
    Hourglass countDownTimer;
    private boolean finished = false;
    private boolean questions_completed = false;
    private int i = 0;

    public void setupDialog(Context context, int style, int drawable) {
        dialog = new Dialog(context, style);
        dialog.setContentView(R.layout.pause_layout);
        dialog.setCancelable(false);
        returnGame = dialog.findViewById(R.id.return_game);
        restart = dialog.findViewById(R.id.restart_game);
        sound = dialog.findViewById(R.id.sound_on);
        exit = dialog.findViewById(R.id.exit_to_main);
        pauseBack = dialog.findViewById(R.id.pauseBack);

        if (SharedPrefManager.isSoundEnabled(getApplication())) {
            sound.setText(getString(R.string.Mute));
        } else {
            sound.setText(getString(R.string.Sound));
        }

        sound.setOnClickListener(v -> {
            if (sound.getText().toString().equals(getString(R.string.Sound))) {
                sound.setText(getString(R.string.Mute));
                SharedPrefManager.setSoundEnabled(getApplication(), true);
            } else {
                sound.setText(getString(R.string.Sound));
                SharedPrefManager.setSoundEnabled(getApplication(), false);
            }
        });

        pauseBack.setBackgroundResource(drawable);

        returnGame.setOnClickListener(v -> hidePauseDialog());

        restart.setOnClickListener(v -> {
            finish();
            overridePendingTransition( 0, 0);
            startActivity(getIntent());
            overridePendingTransition( 0, 0);
        });

        exit.setOnClickListener(v -> {
            startActivity(new Intent(context, MainActivity.class));
            finishAffinity();
        });
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

    public void setProgressBar(int i, ProgressBar bar, int millSec) {
        bar.setProgress(i * 100 / (millSec / 1000));
    }

    public void startTimer(int millSec, TextView timeTxt) {
        finished = false;
        countDownTimer = new Hourglass(millSec, 1000) {

            @SuppressLint("DefaultLocale")
            @Override
            public void onTimerTick(long timeRemaining) {
                int seconds = (int) (timeRemaining / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                timeTxt.setText(String.format(format, minutes, seconds));
            }

            @Override
            public void onTimerFinish() {
                finished = true;
                if (!questions_completed) {
                    startNewActivity();
                }
            }
        };
        countDownTimer.startTimer();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }

    public void startNewActivity() {

    }

    public boolean isPaused() {
        return countDownTimer.isPaused();
    }

    public void pauseTimer() {
        countDownTimer.pauseTimer();
    }

    public void resumeTimer() {
        countDownTimer.resumeTimer();
    }
}
