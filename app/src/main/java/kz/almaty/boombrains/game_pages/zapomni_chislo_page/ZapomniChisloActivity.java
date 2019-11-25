package kz.almaty.boombrains.game_pages.zapomni_chislo_page;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.poovam.pinedittextfield.LinePinField;
import java.util.Random;
import butterknife.BindView;
import butterknife.ButterKnife;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.helpers.DialogHelperActivity;
import kz.almaty.boombrains.helpers.SharedPrefManager;
import kz.almaty.boombrains.helpers.SharedUpdate;
import kz.almaty.boombrains.main_pages.FinishedActivity;

@SuppressLint("SetTextI18n")
public class ZapomniChisloActivity extends DialogHelperActivity {

    int position;
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

    View[] numbers;
    private boolean visible = true;
    int currentLevel = 1, score = 0, errors = 0;
    int random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zapomni_chislo);

        ButterKnife.bind(this);

        position = getIntent().getIntExtra("position", 0);
        numbers = new View[] {btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0};

        setupDialog(this, R.style.chisloTheme, R.drawable.pause_zapomni, position, "");
        startTimer(60000, timeTxt);
        setCount();
        loadGoogleAd();

        pauseImg.setOnClickListener(v -> showPauseDialog());

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
        if (word.equals(correct)) {
            currentLevel += 1;
            score += 100;
            nextNum.setText(getString(R.string.Level) + " " + currentLevel);
            recordTxt.setText("" + score);
            showSuccess(word);
            setAudio(R.raw.level_complete);
        } else {
            if (currentLevel > 1) {
                currentLevel -= 1;
            }
            if (score > 0) {
                score -= 50;
            }
            errors += 1;
            nextNum.setText(getString(R.string.Level) + " " + currentLevel);
            recordTxt.setText("" + score);
            showError(word);
            setAudio(R.raw.wrong_clicked);
            vibrate(100);
        }

        new Handler().postDelayed(()-> getLevels(currentLevel), 500);
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
        switch(level) {
            case 1: case 2: case 3: {
                random = getRandom(100, 1000);
                setFields(3);
                showAndHide();
                break;
            }
            case 4: case 5: case 6: {
                random = getRandom(1000, 10000);
                setFields(4);
                showAndHide();
                break;
            }
            case 7: case 8: case 9: {
                random = getRandom(10000, 100000);
                setFields(5);
                showAndHide();
                break;
            }
            case 10: case 11: case 12: {
                random = getRandom(100000, 1000000);
                setFields(6);
                showAndHide();
                break;
            }
            case 13: case 14: case 15: {
                random = getRandom(1000000, 10000000);
                setFields(7);
                showAndHide();
                break;
            }
            case 16: case 17: case 18: {
                random = getRandom(10000000, 100000000);
                setFields(8);
                showAndHide();
                break;
            }
            default: {
                random = getRandom(100000000, 1000000000);
                setFields(9);
                showAndHide();
                break;
            }
        }
    }

    private void setFields(int num) {
        slovo_new.setNumberOfFields(num);
        slovo_error.setNumberOfFields(num);
        slovo_success.setNumberOfFields(num);
    }

    private void showAndHide() {
        pauseTimer();
        slovo.setVisibility(View.VISIBLE);
        slovo.setText(String.valueOf(random));
        hideAllViews();
        showBtn.setEnabled(false);
        pauseImg.setEnabled(false);

        new Handler().postDelayed(()-> {
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

    private int getRandom(int start, int end) {
        return new Random().nextInt(end - start) + start;
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

    @Override
    public void startNewActivity() {
        Intent intent = new Intent(getApplication(), FinishedActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("score", score);
        intent.putExtra("errors", errors);

        String oldScore = SharedPrefManager.getChisloRecord(getApplication());
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
        startActivity(intent);
        overridePendingTransition(0,0);
    }
}
