package kz.almaty.boombrains.ui.game_pages.equation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import java.util.ArrayList;
import java.util.Arrays;
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
public class EquationActivity extends DialogHelperActivity {

    @BindView(R.id.shulteRecord) TextView recordTxt;
    @BindView(R.id.slovo_teksts) TextView randomNumberTxt;
    @BindView(R.id.shulteTime) TextView timeTxt;
    @BindView(R.id.pauseBtn) ConstraintLayout pauseImg;
    @BindView(R.id.findNumContainer) ConstraintLayout container;
    @BindView(R.id.nextNumShulte2) TextView levelTxt;
    @BindView(R.id.plusBtn) TextView plusBtn;
    @BindView(R.id.minusBtn) TextView minusBtn;
    @BindView(R.id.multBtn) TextView multBtn;
    @BindView(R.id.divBtn) TextView divBtn;

    @BindView(R.id.life1) ImageView life1;
    @BindView(R.id.life2) ImageView life2;
    @BindView(R.id.life3) ImageView life3;

    @BindView(R.id.pauseImg) ImageView pauseIcon;
    @BindView(R.id.potionLayout) LinearLayout potionLayout;
    @BindView(R.id.meTxt) TextView myName;
    @BindView(R.id.opTxt) TextView opName;
    @BindView(R.id.meRecord) TextView meRecord;
    @BindView(R.id.opRecord) TextView opRecord;
    private String type = null;

    private int lifes = 3;

    private int position, score = 0, currentLevel = 1, errors = 0;
    private ArrayList<String> symbols;
    private TextView[] variants;
    private boolean lifeUsed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equation);
        ButterKnife.bind(this);
        position = getIntent().getIntExtra("position", 0);
        setupDialog(this, R.style.equationTheme, R.drawable.pause_equation, position, "");
        startTimer(15000, timeTxt);
        setCount();
        loadGoogleAd();

        setTypeAndLayout();

        loadAddForLife();
        setupLifeDialog(this, R.color.topEquation);

        symbols = new ArrayList<>(Arrays.asList("+", "-", "*", "/"));
        variants = new TextView[] {plusBtn, minusBtn, multBtn, divBtn};

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
        String answer = String.valueOf(ans);
        generateRandomQuestion(a, length, answer);
    }

    private void generateRandomQuestion(String text, int length, String answer) {
        int count = 0, randomQuestion = getRandom(1, length);
        String symbol = null;
        String[] list = text.split("");
        for (int i = 0; i < list.length; i++) {
            if (symbols.contains(list[i])) {
                count += 1;
                if (count == randomQuestion) {
                    symbol = list[i];
                    list[i] = "?";
                }
            }
        }

        StringBuilder new_word = new StringBuilder();
        for (String s : list) {
            new_word.append(s);
        }

        if (!new_word.toString().contains("?")) {
                getLevels(currentLevel);
        } else {
            StringBuilder whole = new StringBuilder();
            String old_word = new_word.toString() + "=" + answer;

            for (int i = 0; i < old_word.length(); i++) {
                String part = String.valueOf(old_word.charAt(i));
                if (part.equals("?")) {
                    part = "&nbsp;<span style=\"background: #ffffff; color: #BE1856; font-size: 22px; \">" + "&nbsp;" + part + "&nbsp;" + "</span>&nbsp;";
                }
                whole.append(part);
            }

            randomNumberTxt.setText(Html.fromHtml(whole.toString()));

            for (TextView view : variants) {
                String finalSymbol = symbol;
                view.setOnClickListener(v -> {
                    String result = view.getText().toString();
                    if (result.equals("×")) {
                        result = "*";
                    } else if (result.equals("÷")) {
                        result = "/";
                    }

                    if (result.equals(finalSymbol)) {
                        setBackgroundSuccess(view);
                    } else {
                        setBackgroundError(view);
                    }
                });
            }
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
        view.setTextColor(Color.parseColor("#BE1856"));
        recordTxt.setText("" + score);
        levelTxt.setText(getString(R.string.Level) + " " + currentLevel);
        if (lifes > 0) {
            getLevels(currentLevel);
            startNewQuestion();
        }
        updateSocketScore(score);
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
        setAudio(R.raw.wrong_clicked);
        vibrate(100);
        errors += 1;
        if (lifes > 0) {
            lifes -= 1;
        }
        lifeRemained(lifes);
        if (lifes == 0) {
            if (!lifeUsed) {
                showLifeDialog(this, type);
            } else {
                startNewActivity();
            }
        }
        view.setBackgroundResource(R.drawable.find_item_back);
        view.setTextColor(Color.parseColor("#BE1856"));
        recordTxt.setText("" + score);
        if (lifes > 0) {
            getLevels(currentLevel);
            startNewQuestion();
        }
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

    private void lifeRemained(int i) {
        ImageView[] lifes = {life1, life2, life3};
        if (i >= 0) {
            lifes[i].setImageResource(R.drawable.life_border);
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
        if (level <= 5) { size = 21; length = 2; }
        else if (level <= 10) {size = 22; length = 3; }
        else if (level <= 15) {size = 23; length = 4; }
        else if (level <= 20) {size = 24; length = 5; }
        else if (level <= 25) {size = 25; length = 6; }
        else if (level <= 30) {size = 26; length = 7; }
        else if (level <= 35) {size = 27; length = 8; }
        else if (level <= 40) {size = 28; length = 9; }
        else {size = 29; length = 10;}
        setTextSizes(size);
        generate(length);
    }

    private void setTextSizes(float size) {
        int width = height("x");
        randomNumberTxt.setTextSize(width / size - 1);
    }

    private String generateProblem(Integer length) {
        StringBuilder result = new StringBuilder(String.valueOf(getRandom(2,10)));
        for (int i = 1; i < length; i++) {
            int random = getRandom(2,10);
            String symbol = String.valueOf(symbols.get(new Random().nextInt(symbols.size())));
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

    private Intent myIntent() {
        Intent intent = new Intent(getApplication(), FinishedActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("score", score);
        intent.putExtra("errors", errors);
        String oldScore = SharedPrefManager.getEquationRecord(getApplication());

        SharedPrefManager.setCoin(getApplication(), SharedPrefManager.getCoin(getApplication()) + result(score));

        if (oldScore != null) {
            if (score > Integer.parseInt(oldScore)) {
                SharedPrefManager.setEquationRecord(getApplication(), String.valueOf(score));
                SharedUpdate.setEquationUpdate(getApplication(), String.valueOf(score));
                intent.putExtra("record", getString(R.string.CongratulationNewRecord));
            }
        } else {
            if (score > 0) {
                SharedPrefManager.setEquationRecord(getApplication(), String.valueOf(score));
                SharedUpdate.setEquationUpdate(getApplication(), String.valueOf(score));
                intent.putExtra("record", getString(R.string.CongratulationNewRecord));
            }
        }
        return intent;
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
