package kz.almaty.boombrains.ui.game_pages.number_znaki;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import butterknife.BindView;
import butterknife.ButterKnife;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.util.helpers.dialog_helper.DialogHelperActivity;
import kz.almaty.boombrains.util.helpers.preference.SharedPrefManager;
import kz.almaty.boombrains.util.helpers.preference.SharedUpdate;
import kz.almaty.boombrains.ui.main_pages.FinishedActivity;

@SuppressLint("SetTextI18n")
public class NumberZnakiActivity extends DialogHelperActivity {

    @BindView(R.id.shulteRecord) TextView recordTxt;
    @BindView(R.id.slovo_teksts) TextView randomNumberTxt;
    @BindView(R.id.shulteTime) TextView timeTxt;
    @BindView(R.id.pauseBtn) ConstraintLayout pauseImg;
    @BindView(R.id.findNumContainer) ConstraintLayout container;
    @BindView(R.id.nextNumShulte2) TextView levelTxt;

    @BindView(R.id.button1) TextView first;
    @BindView(R.id.button2) TextView second;
    @BindView(R.id.button3) TextView third;
    @BindView(R.id.button4) TextView forth;

    @BindView(R.id.life1) ImageView life1;
    @BindView(R.id.life2) ImageView life2;
    @BindView(R.id.life3) ImageView life3;

    private int lifes = 3;

    private int position;
    private int score = 0, currentLevel = 1;
    private List<String> numbersList = new ArrayList<>();
    private List<String> randomList = new ArrayList<>();
    private String[] symbols;
    private TextView[] variants;
    private int errors = 0;

    private boolean lifeUsed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_znaki);
        ButterKnife.bind(this);
        position = getIntent().getIntExtra("position", 0);
        setupDialog(this, R.style.numZnakiTheme, R.drawable.pause_num_znaki, position, "");
        startTimer(15000, timeTxt);
        setCount();
        loadGoogleAd();

        setupLifeDialog(this, R.color.topNumZnaki);
        loadAddForLife();

        symbols = new String[] {"+", "-", "*", "/"};
        variants = new TextView[] {first, second, third, forth};
        pauseImg.setOnClickListener(v -> showPauseDialog());

        levelTxt.setText(getString(R.string.Level) + " " + currentLevel);

        setTextSizes(20);
        generate(2);
        container.getLayoutParams().height = (int) (height("y") / 3.5);
    }

    private int height(String type) {
        int res = 0;

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        if (type.equals("x")) {
            res = size.x;
        } else if (type.equals("y")) {
            res = size.y;
        }
        return res;
    }

    private int getRandom(int start, int end) {
        return new Random().nextInt(end - start) + start;
    }

    private void generate(int length) {

        String a = generateProblem(length);
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("rhino");
        Object result = new Object();

        try {
            result = engine.eval(a);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        int ans = 0;
        if (result != null) {
            ans = (int) Float.parseFloat(result.toString());
        }

        generateList(ans);
        String answer = String.valueOf(ans);
        randomNumberTxt.setText(a);

        for (int i = 0; i < variants.length; i++) {
            variants[i].setText(numbersList.get(i));
        }

        for (TextView view : variants) {
            view.setOnClickListener(v -> {
                if (view.getText().toString().equals(answer)) {
                    setBackgroundSuccess(view);
                } else {
                    setBackgroundError(view);
                }
            });
        }
    }

    private void setBackgroundSuccess(TextView view) {
        view.setBackgroundResource(R.drawable.find_success);
        view.setTextColor(Color.WHITE);
        new Handler().postDelayed(()-> countSuccess(view), 100);
    }

    private void countSuccess(TextView view) {
        setAudio(R.raw.click);
        currentLevel += 1;
        setScore(currentLevel);
        view.setBackgroundResource(R.drawable.find_item_back);
        view.setTextColor(Color.parseColor("#2CB0B2"));
        recordTxt.setText("" + score);
        levelTxt.setText(getString(R.string.Level) + " " + currentLevel);
        if (lifes > 0) {
            getLevels(currentLevel);
            startNewQuestion();
        }
    }

    private void setScore(int level) {
        if (level <= 5) { score += 100;}
        else if (level <= 10) {score += 200;}
        else if (level <= 15) {score += 300;}
        else if (level <= 20) {score += 400;}
        else if (level <= 25) {score += 500;}
        else if (level <= 30) {score += 600;}
        else if (level <= 35) {score += 700;}
        else if (level <= 40) {score += 800;}
        else {score += 900;}
    }

    private void setBackgroundError(TextView view) {
        view.setBackgroundResource(R.drawable.find_item_red);
        view.setTextColor(Color.WHITE);
        new Handler().postDelayed(()-> countError(view), 100);
    }

    private void countError(TextView view) {
        vibrate(100);
        setAudio(R.raw.wrong_clicked);
        errors += 1;
        if (lifes > 0) {
            lifes -= 1;
        }
        lifeRemained(lifes);
        if (lifes == 0) {
            if (!lifeUsed) {
                showLifeDialog(this);
            } else {
                startNewActivity();
            }
        }
        view.setBackgroundResource(R.drawable.find_item_back);
        view.setTextColor(Color.parseColor("#2CB0B2"));
        recordTxt.setText("" + score);
        if (lifes > 0) {
            getLevels(currentLevel);
            startNewQuestion();
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

    private void getLevels(int level) {
        int size, length;
        if (level <= 5) { size = 20; length = 2; }
        else if (level <= 10) {size = 21; length = 3; }
        else if (level <= 15) {size = 22; length = 4; }
        else if (level <= 20) {size = 23; length = 5; }
        else if (level <= 25) {size = 24; length = 6; }
        else if (level <= 30) {size = 25; length = 7; }
        else if (level <= 35) {size = 26; length = 8; }
        else if (level <= 40) {size = 27; length = 9; }
        else {size = 28; length = 10;}
        setTextSizes(size);
        generate(length);
    }

    private void setTextSizes(float size) {
        int width = height("x");
        randomNumberTxt.setTextSize(width / size);
    }

    private void generateList(int result) {

        numbersList.clear();
        randomList.clear();

        numbersList.add(String.valueOf(result));
        for (int i = 1; i < 5; i++) {
            int a = result + i;
            int b = result - i;
            randomList.add(String.valueOf(a));
            randomList.add(String.valueOf(b));
        }
        Collections.shuffle(randomList);

        for (int i = 1; i < 4; i++) {
            numbersList.add(randomList.get(i));
        }
        Collections.shuffle(numbersList);
    }

    private String generateProblem(Integer length) {
        StringBuilder result = new StringBuilder(String.valueOf(getRandom(1,10)));
        for (int i = 1; i < length; i++) {
            int random = getRandom(1,10);
            String symbol = String.valueOf(symbols[new Random().nextInt(symbols.length)]);
            if (symbol.equals("/")) {
                if (result.substring(result.length() - 1).equals(")")) {
                    result = new StringBuilder(result);
                } else {
                    result = new StringBuilder(result.substring(0, result.length() - 1));
                    result = new StringBuilder(result + "(" + (random * getRandom(2, 6)));
                    result = new StringBuilder(result + "" + symbol + random + ")");
                }
            } else {
                result = new StringBuilder(result + "" + symbol + random);
            }
        }
        return result.toString();
    }


    @Override
    public void onBackPressed() {
        if (!isPaused()) {
            showPauseDialog();
        }
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
        String oldScore = SharedPrefManager.getNumZnakiRecord(getApplication());

        SharedPrefManager.setCoin(getApplication(), SharedPrefManager.getCoin(getApplication()) + result(score));

        if (oldScore != null) {
            if (score > Integer.parseInt(oldScore)) {
                SharedPrefManager.setNumZnakiRecord(getApplication(), String.valueOf(score));
                SharedUpdate.setNumZnakiUpdate(getApplication(), String.valueOf(score));
                intent.putExtra("record", getString(R.string.CongratulationNewRecord));
            }
        } else {
            if (score > 0) {
                SharedPrefManager.setNumZnakiRecord(getApplication(), String.valueOf(score));
                SharedUpdate.setNumZnakiUpdate(getApplication(), String.valueOf(score));
                intent.putExtra("record", getString(R.string.CongratulationNewRecord));
            }
        }
        return intent;
    }

    @Override
    public void startNewActivity() {
        startActivity(myIntent());
        overridePendingTransition(0,0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissDialog();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!isPaused()) {
            showPauseDialog();
        }
    }
}
