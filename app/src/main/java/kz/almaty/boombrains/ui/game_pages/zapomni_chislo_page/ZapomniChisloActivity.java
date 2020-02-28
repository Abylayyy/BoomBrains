package kz.almaty.boombrains.ui.game_pages.zapomni_chislo_page;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import com.poovam.pinedittextfield.LinePinField;
import java.util.Random;
import butterknife.BindView;
import butterknife.ButterKnife;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.util.helpers.dialog_helper.DialogHelperActivity;
import kz.almaty.boombrains.util.helpers.preference.SharedPrefManager;
import kz.almaty.boombrains.util.helpers.preference.SharedUpdate;
import kz.almaty.boombrains.ui.main_pages.FinishedActivity;

@SuppressLint("SetTextI18n")
public class ZapomniChisloActivity extends DialogHelperActivity {

    private int position;
    @BindView(R.id.shulteRecord) TextView recordTxt;
    @BindView(R.id.shulteTime) TextView timeTxt;
    @BindView(R.id.nextNumShulte) TextView nextNum;
    @BindView(R.id.pauseBtn) ConstraintLayout pauseImg;
    @BindView(R.id.slovo_teksts) TextView slovo;
    @BindView(R.id.slovo_teksts_new) LinePinField slovo_new;
    @BindView(R.id.slovo_teksts_error) LinePinField slovo_error;
    @BindView(R.id.slovo_teksts_success) LinePinField slovo_success;
    @BindView(R.id.hintNum) TextView hint_num;
    @BindView(R.id.button1) TextView btn1;
    @BindView(R.id.button2) TextView btn2;
    @BindView(R.id.button3) TextView btn3;
    @BindView(R.id.button4) TextView btn4;
    @BindView(R.id.button5) TextView btn5;
    @BindView(R.id.button6) TextView btn6;
    @BindView(R.id.button7) TextView btn7;
    @BindView(R.id.button8) TextView btn8;
    @BindView(R.id.button9) TextView btn9;
    @BindView(R.id.button0) TextView btn0;
    @BindView(R.id.showNumBtn) RelativeLayout showBtn;
    @BindView(R.id.clearNumBtn) RelativeLayout clearBtn;
    @BindView(R.id.imageView2) ImageView showImg;
    @BindView(R.id.zapomniContainer) ConstraintLayout container;

    @BindView(R.id.life1) ImageView life1;
    @BindView(R.id.life2) ImageView life2;
    @BindView(R.id.life3) ImageView life3;

    @BindView(R.id.pauseImg) ImageView pauseIcon;
    @BindView(R.id.potionLayout)
    LinearLayout potionLayout;
    @BindView(R.id.meTxt) TextView myName;
    @BindView(R.id.opTxt) TextView opName;
    @BindView(R.id.meRecord) TextView meRecord;
    @BindView(R.id.opRecord) TextView opRecord;
    private String type = null;

    private int lifes = 3;

    private boolean visible = true;
    private int currentLevel = 1, score = 0, errors = 0;
    private String random;

    private boolean lifeUsed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zapomni_chislo);
        ButterKnife.bind(this);

        position = getIntent().getIntExtra("position", 0);
        View[] numbers = new View[]{btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0};

        setupDialog(this, R.style.chisloTheme, R.drawable.pause_zapomni, position, "");
        startTimer(15000, timeTxt);
        setCount();
        loadGoogleAd();

        setTypeAndLayout();

        setupLifeDialog(this, R.color.topSlovo);
        loadAddForLife();

        for (View view : numbers) {
            view.setOnClickListener(v -> {
                if (slovo_new.getVisibility() == View.VISIBLE) {
                    slovo_new.append(((TextView) view).getText());
                    setAudio(R.raw.click);
                }
            });
        }

        clearBtn.setOnClickListener(v -> {
            if (slovo_new.getVisibility() == View.VISIBLE) {
                String text = slovo_new.getText().toString();
                int length = text.length();
                if (length > 0) {
                    slovo_new.setText(text.substring(0, text.length() - 1));
                }
            }
        });

        showBtn.setOnClickListener(v -> {
            if (visible) {
                hint_num.setVisibility(View.VISIBLE);
                hint_num.setText(slovo.getText());
                visible = false;
                showImg.setColorFilter(Color.parseColor("#666666"));
                setAudio(R.raw.click);
            }
        });

        getLevels(currentLevel);

        slovo_new.setOnTextCompleteListener(s -> {
            showBackgrounds();
            return true;
        });

        nextNum.setText(getString(R.string.Level) + " " + currentLevel);

        container.getLayoutParams().height = (int) (height() / 4.5);
    }

    private int height() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    private void showBackgrounds() {
        String correct = slovo.getText().toString();
        String word = slovo_new.getText().toString();
        int len = word.length();

        if (word.equals(correct)) {
            currentLevel += 1;
            score += len * 100;
            successClicked(word);
        } else {
            errors += 1;
            if (lifes > 0) {
                lifes -= 1;
            }
            lifeRemained(lifes);
            errorClicked(word);

            if (lifes == 0) {
                if (!lifeUsed) {
                    showLifeDialog(this, type);
                } else {
                    startNewActivity();
                }
            }
        }

        new Handler().postDelayed(()-> {
            if (lifes > 0) {
                startNewQuestion();
                getLevels(currentLevel);
            }
        }, 500);
    }

    private void errorClicked(String word) {
        nextNum.setText(getString(R.string.Level) + " " + currentLevel);
        recordTxt.setText("" + score);
        showError(word);
        setAudio(R.raw.wrong_clicked);
        vibrate(100);
    }

    private void successClicked(String word) {
        nextNum.setText(getString(R.string.Level) + " " + currentLevel);
        recordTxt.setText("" + score);
        updateSocketScore(score);
        showSuccess(word);
        setAudio(R.raw.level_complete);
    }

    private void setTypeAndLayout() {
        type = getIntent().getStringExtra("type");
        setupLeaveDialog(this);

        if (type != null) {
            if (SharedPrefManager.isUserLoggedIn(this) && SharedPrefManager.isNetworkOnline(this)) {
                connectSocket();
                loadAcceptDialog(this);
            }
            pauseIcon.setImageResource(R.drawable.exit_icon);
            potionLayout.setVisibility(View.GONE);
            myName.setText(getIntent().getStringExtra("myName"));
            opName.setText(getIntent().getStringExtra("oName"));

            pauseImg.setOnClickListener(v -> showLeaveDialog());

        } else {
            potionLayout.setVisibility(View.VISIBLE);
            pauseImg.setOnClickListener(v -> showPauseDialog());
        }
    }

    private void updateSocketScore(int score) {
        if (type != null) {
            gameUpdate(score);
        }
    }

    @Override
    public void setRecordUpdates(int reqRecord, int recRecord) {
        meRecord.setText("" + reqRecord);
        opRecord.setText("" + recRecord);
    }

    @Override
    public void startNewActivity() {
        if (type != null) {
            roundEnded();
        } else {
            startActivity(myIntent());
            overridePendingTransition(0, 0);
        }
    }

    private void startNewQuestion() {
        cancel();
        startTimer(15000, timeTxt);
    }

    @Override
    public void startWithLife() {
        lifeUsed = true;
        startNewQuestion();
        lifes = 1;
        life1.setImageResource(R.drawable.life_full);
        getLevels(currentLevel);
    }

    private void showSuccess(String slovo) {
        slovo_success.setVisibility(View.VISIBLE);
        slovo_new.setVisibility(View.INVISIBLE);
        slovo_error.setVisibility(View.INVISIBLE);
        slovo_success.setText(slovo);
    }

    private void showError(String slovo) {
        slovo_error.setVisibility(View.VISIBLE);
        slovo_new.setVisibility(View.INVISIBLE);
        slovo_success.setVisibility(View.INVISIBLE);
        slovo_error.setText(slovo);
    }

    private void getLevels(int level) {
        if (level == 1) {
            random = getRandom(level + 2);
            setFields(level + 2);
        } else {
            if (level % 2 == 1){
                random = getRandom((level + 1) / 2 + 2);
                setFields((level + 1) / 2 + 2);
            } else {
                random = getRandom((level / 2) + 2);
                setFields((level / 2) + 2);
            }
        }
        showAndHide();
    }

    private void setFields(int num) {
        slovo_new.setNumberOfFields(num);
        slovo_error.setNumberOfFields(num);
        slovo_success.setNumberOfFields(num);
    }

    private void showAndHide() {
        pauseTimer();
        slovo.setVisibility(View.VISIBLE);
        slovo.setText(random);
        hideAllViews();
        showBtn.setEnabled(false);
        pauseImg.setEnabled(false);

        new Handler().postDelayed(() -> {
            resumeTimer();
            showBtn.setEnabled(true);
            slovo.setVisibility(View.INVISIBLE);

            slovo_new.setVisibility(View.VISIBLE);
            slovo_new.setText("");
            slovo_new.setBackgroundResource(R.drawable.trans_color);

            pauseImg.setEnabled(true);
        }, 2000);
    }

    private void hideAllViews() {
        slovo_error.setVisibility(View.INVISIBLE);
        slovo_success.setVisibility(View.INVISIBLE);
        hint_num.setVisibility(View.INVISIBLE);
        slovo_new.setVisibility(View.INVISIBLE);
    }

    private String getRandom(int n) {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= n; i++) {
            builder.append(new Random().nextInt(10 - 1) + 1);
        }
        return builder.toString();
    }

    @Override
    public void onBackPressed() {
        if (!isPaused()) {
            showPauseDialog();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!isPaused()) {
            showPauseDialog();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissDialog();
    }

    private void lifeRemained(int i) {
        ImageView[] lifes = {life1, life2, life3};
        if (i >= 0) {
            lifes[i].setImageResource(R.drawable.life_border);
        }
    }

    private Intent myIntent() {
        Intent intent = new Intent(getApplication(), FinishedActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("score", score);
        intent.putExtra("errors", errors);
        String oldScore = SharedPrefManager.getChisloRecord(getApplication());
        SharedPrefManager.setCoin(getApplication(), SharedPrefManager.getCoin(getApplication()) + result(score));
        if (oldScore != null) {
            if (score > Integer.parseInt(oldScore)) {
                SharedPrefManager.setChisoRecord(getApplication(), String.valueOf(score));
                SharedUpdate.setChisloUpdate(getApplication(), String.valueOf(score));
                intent.putExtra("record", getString(R.string.CongratulationNewRecord));
            }
        } else {
            if (score > 0) {
                SharedPrefManager.setChisoRecord(getApplication(), String.valueOf(score));
                SharedUpdate.setChisloUpdate(getApplication(), String.valueOf(score));
                intent.putExtra("record", getString(R.string.CongratulationNewRecord));
            }
        }
        return intent;
    }
}
