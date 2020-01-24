package kz.almaty.boombrains.ui.game_pages.number_znaki;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import kz.almaty.boombrains.helpers.DialogHelperActivity;
import kz.almaty.boombrains.helpers.SharedPrefManager;
import kz.almaty.boombrains.helpers.SharedUpdate;
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
    private boolean watched = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_znaki);
        ButterKnife.bind(this);
        position = getIntent().getIntExtra("position", 0);
        setupDialog(this, R.style.numZnakiTheme, R.drawable.pause_num_znaki, position, "");
        startTimer(60000, timeTxt);
        setCount();
        loadGoogleAd();

        symbols = new String[] {"+", "-", "*", "/"};
        variants = new TextView[] {first, second, third, forth};
        pauseImg.setOnClickListener(v -> showPauseDialog());

        levelTxt.setText(getString(R.string.Level) + " " + currentLevel);

        setTextSizes(20);
        generate(2);
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
        score += 1;
        view.setBackgroundResource(R.drawable.find_item_back);
        view.setTextColor(Color.parseColor("#2CB0B2"));
        recordTxt.setText("" + score);
        levelTxt.setText(getString(R.string.Level) + " " + currentLevel);
        getLevels(currentLevel);
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
            gameFinished();
        }
        view.setBackgroundResource(R.drawable.find_item_back);
        view.setTextColor(Color.parseColor("#2CB0B2"));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                lifes = data.getIntExtra("result", 0);
                watched = data.getBooleanExtra("watched", false);
                life1.setImageResource(R.drawable.life_full);
                showPauseDialog();
            }
        }
    }

    @Override
    public void gameFinished() {
        pauseTimer();
        startActivityForResult(intentErrorInfo(), 1);
        overridePendingTransition(0,0);
    }

    private Intent intentErrorInfo() {
        Intent intent = myIntent();
        intent.putExtra("lifeEnd", watched);
        return intent;
    }

    private Intent intentFinishInfo() {
        Intent intent = myIntent();
        intent.putExtra("lifeEnd", false);
        return intent;
    }

    private Intent myIntent() {
        Intent intent = new Intent(getApplication(), FinishedActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("score", score);
        intent.putExtra("errors", errors);
        String oldScore = SharedPrefManager.getNumZnakiRecord(getApplication());
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
        startActivity(intentFinishInfo());
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
