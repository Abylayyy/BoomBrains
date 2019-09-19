package kz.almaty.boombrains.game_pages.equation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.Display;
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
import kz.almaty.boombrains.helpers.DialogHelperActivity;
import kz.almaty.boombrains.helpers.SharedPrefManager;
import kz.almaty.boombrains.main_pages.FinishedActivity;

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

    private int position;
    private int score = 0, errors = 0, currentLevel = 1;
    private ArrayList<String> symbols;
    private TextView[] variants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equation);
        ButterKnife.bind(this);
        setupDialog(this, R.style.equationTheme, R.drawable.pause_equation);
        startTimer(60000, timeTxt);

        symbols = new ArrayList<>(Arrays.asList("+", "-", "*", "/"));
        variants = new TextView[] {plusBtn, minusBtn, multBtn, divBtn};

        position = getIntent().getIntExtra("position", 0);
        pauseImg.setOnClickListener(v -> showPauseDialog());

        levelTxt.setText(getString(R.string.Level) + " " + currentLevel);

        getLevels(currentLevel);
        container.getLayoutParams().height = (int) (height("y") / 2.8);
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
        Object result = null;
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
        String symbol = "";
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

        StringBuilder whole = new StringBuilder();
        String old_word = new_word.toString() + "=" + answer;

        for (int i = 0; i < old_word.length(); i++) {
            String part = String.valueOf(old_word.charAt(i));
            if (part.equals("?")) {
                part = "&nbsp;<span style=\"background: #ffffff; color: #FE1E45; font-size: 22px; \">" + "&nbsp;" + part + "&nbsp;" + "</span>&nbsp;";
            }
            whole.append(part);
        }

        randomNumberTxt.setText(Html.fromHtml(whole.toString()));

        for (TextView view : variants) {
            String finalSymbol = symbol;
            view.setOnClickListener(v -> {
                if (view.getText().toString().equals(finalSymbol)) {
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
        new Handler().postDelayed(()-> countSuccess(view), 200);
    }

    private void countSuccess(TextView view) {
        setAudio(R.raw.click);
        currentLevel += 1;
        score += 100;
        view.setBackgroundResource(R.drawable.find_item_back);
        view.setTextColor(Color.parseColor("#FE1E45"));
        recordTxt.setText("" + score);
        levelTxt.setText(getString(R.string.Level) + " " + currentLevel);
        getLevels(currentLevel);
    }

    private void setBackgroundError(TextView view) {
        view.setBackgroundResource(R.drawable.find_item_red);
        view.setTextColor(Color.WHITE);
        new Handler().postDelayed(()-> countError(view), 200);
    }

    private void countError(TextView view) {
        setAudio(R.raw.wrong_clicked);
        errors += 1;
        if (score > 0) {
            score -= 50;
        }
        view.setBackgroundResource(R.drawable.find_item_back);
        view.setTextColor(Color.parseColor("#FE1E45"));
        recordTxt.setText("" + score);
        getLevels(currentLevel);
    }

    private void getLevels(int level) {
        switch(level) {
            case 1: case 2: case 3: case 4: case 5: {
                setTextSizes(20);
                generate(2);
                break;
            }
            case 6: case 7: case 8: case 9: case 10: {
                setTextSizes(21);
                generate(3);
                break;
            }
            case 11: case 12: case 13: case 14: case 15: {
                setTextSizes(22);
                generate(4);
                break;
            }
            case 16: case 17: case 18: case 19: case 20: {
                setTextSizes(23);
                generate(5);
                break;
            }
            case 21: case 22: case 23: case 24: case 25: {
                setTextSizes(24);
                generate(6);
                break;
            }
            case 26: case 27: case 28: case 29: case 30: {
                setTextSizes(25);
                generate(7);
                break;
            }
            case 31: case 32: case 33: case 34: case 35: {
                setTextSizes(26);
                generate(8);
                break;
            }
            default: {
                setTextSizes(27);
                generate(9);
                break;
            }
        }
    }

    private void setTextSizes(float size) {
        int width = height("x");
        randomNumberTxt.setTextSize(width / size);
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

    @Override
    public void startNewActivity() {
        Intent intent = new Intent(getApplication(), FinishedActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("equationScore", score);
        intent.putExtra("equationErrors", errors);

        String oldScore = SharedPrefManager.getEquationRecord(getApplication());
        if (oldScore != null) {
            if (score > Integer.parseInt(oldScore)) {
                SharedPrefManager.setEquationRecord(getApplication(), String.valueOf(score));
                intent.putExtra("equationRecord", getString(R.string.CongratulationNewRecord));
            }
        } else {
            if (score > 0) {
                SharedPrefManager.setEquationRecord(getApplication(), String.valueOf(score));
                intent.putExtra("equationRecord", getString(R.string.CongratulationNewRecord));
            }
        }
        startActivity(intent);
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
