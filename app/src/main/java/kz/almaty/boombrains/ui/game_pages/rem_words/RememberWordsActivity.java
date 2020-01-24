package kz.almaty.boombrains.ui.game_pages.rem_words;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.files.RememberWordsEn;
import kz.almaty.boombrains.files.RememberWordsKz;
import kz.almaty.boombrains.files.RememberWordsRu;
import kz.almaty.boombrains.files.RememberWordsSpain;
import kz.almaty.boombrains.helpers.DialogHelperActivity;
import kz.almaty.boombrains.helpers.SharedPrefManager;
import kz.almaty.boombrains.helpers.SharedUpdate;
import kz.almaty.boombrains.ui.main_pages.FinishedActivity;

@SuppressLint("SetTextI18n")
public class RememberWordsActivity extends DialogHelperActivity implements SlovoAdapter.SlovoListener {

    SlovoAdapter adapter;
    List<String> numbersList;
    List<String> subList;
    List<String> correctsResults = new ArrayList<>();
    int position;

    @BindView(R.id.shulteRecord) TextView recordTxt;
    @BindView(R.id.shulteTime) TextView timeTxt;
    @BindView(R.id.nextNumShulte) TextView nextNum;
    @BindView(R.id.pauseBtn) ConstraintLayout pauseImg;
    @BindView(R.id.shulteRecycler) RecyclerView shulteRecycler;
    @BindView(R.id.remConst) ConstraintLayout background;
    @BindView(R.id.slovo_teksts) TextView slovo;
    @BindView(R.id.wordConst) ConstraintLayout wordLayout;

    @BindView(R.id.life1) ImageView life1;
    @BindView(R.id.life2) ImageView life2;
    @BindView(R.id.life3) ImageView life3;

    private int lifes = 3;

    private int currentLevel = 1;
    private String random;
    private int score = 0;
    private TextView text;
    private int errors = 0;
    private boolean watched = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remember_words);
        ButterKnife.bind(this);
        position = getIntent().getIntExtra("position", 0);

        setupDialog(this, R.style.slovoTheme, R.drawable.pause_rem_word, position, "");
        startTimer(60000, timeTxt);
        setCount();
        loadGoogleAd();

        String lang = SharedUpdate.getLanguage(this);
        numbersList = new ArrayList<>();
        subList = new ArrayList<>();
        setupList(lang);

        wordLayout.getLayoutParams().height = height() / 3;

        pauseImg.setOnClickListener(v -> showPauseDialog());
        setRecyclerItemFirst();
        nextNum.setText(getString(R.string.Level) + " " + currentLevel);
    }

    private int height() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    private void setupList(String lang) {
        switch (lang) {
            case "en": {
                numbersList = RememberWordsEn.wordsEnList;
                break;
            }
            case "es": {
                numbersList = RememberWordsSpain.wordsESList;
                break;
            }
            case "ru": {
                numbersList = RememberWordsRu.wordsRuList;
                break;
            }
            case "kk": {
                numbersList = RememberWordsKz.wordsKzList;
                break;
            }
        }
    }

    private List<String> getSuffledList(int size) {
        Collections.shuffle(numbersList);
        return numbersList.subList(0, size);
    }

    private void setRecyclerItem(int size) {
        subList.clear();
        subList = getSuffledList(size);
        adapter = new SlovoAdapter(subList, this, this);
        shulteRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        shulteRecycler.setItemAnimator(new DefaultItemAnimator());
        shulteRecycler.setAdapter(adapter);
    }

    private void setRecyclerItemFirst() {
        subList = getSuffledList(8);
        adapter = new SlovoAdapter(subList, this, this);
        shulteRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        shulteRecycler.setItemAnimator(new DefaultItemAnimator());
        shulteRecycler.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (!isPaused()) {
            showPauseDialog();
        }
    }

    private String getRandom(int size) {
        StringBuilder soz = new StringBuilder();
        List<String> randomList = getRandomElement(subList, subList.size());
        List<String> words = null;
        if (randomList.size() >= size) {
            words = randomList.subList(0, size);
        }
        if (words != null) {
            for (String word : words) {
                soz.append(" ").append(word);
            }
        }
        return soz.toString();
    }

    public List<String> getRandomElement(List<String> list, int totalItems) {
        Random rand = new Random();
        List<String> newList = new ArrayList<>();
        for (int i = 0; i < totalItems; i++) {
            int randomIndex = rand.nextInt(list.size());
            if (!newList.contains(list.get(randomIndex))) {
                newList.add(list.get(randomIndex));
            }
        }
        return newList;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissDialog();
    }

    @Override
    public void setSlovo(LinearLayout view, TextView text) {
        setBackgroundTypes(view, text);
    }

    private void getRandomByLevel(int level) {
        switch (level) {
            case 1: case 2: case 3:
                random = getRandom(1);
                break;
            case 4: case 5: case 6:
                random = getRandom(2);
                break;
            case 7: case 8: case 9:
                random = getRandom(3);
                break;
            case 10: case 11: case 12: case 13: case 14: case 15:
                random = getRandom(4);
                break;
            case 16: case 17: case 18: case 19: case 20: case 21:
                random = getRandom(5);
                break;
            case 22: case 23: case 24: case 25: case 26: case 27:
                random = getRandom(6);
                break;
            default:
                random = getRandom(7);
                break;
        }
    }

    public void setBackgroundTypes(View view, TextView text) {

        String[] array = slovo.getText().toString().trim().split(" ");
        List<String> results = Arrays.asList(array);
        String soz = text.getText().toString().trim();

        if (results.contains(soz)) {
            view.setBackgroundResource(R.drawable.card_background_success);
            text.setTextColor(Color.WHITE);
            correctsResults.add(soz);
            if (results.size() == correctsResults.size()) {
                correctResult();
                correctsResults.clear();
            }
            score += 1;
            recordTxt.setText(score + "");
        } else {
            view.setBackgroundResource(R.drawable.card_background_error);
            text.setTextColor(Color.WHITE);
            errorResult();
            correctsResults.clear();
        }
        Log.d("CORRECT::", results.toString());
        Log.d("SELECTED::", correctsResults.toString());
    }

    private void correctResult() {
        new Handler().postDelayed(() -> {
            correctsResults.clear();
            currentLevel += 1;
            setAudio(R.raw.level_complete);
            setRecyclerSizes(currentLevel);
            nextNum.setText(getString(R.string.Level) + " " + currentLevel);
        }, 200);
    }

    private void errorResult() {
        new Handler().postDelayed(()-> {
            errors += 1;
            if (lifes > 0) {
                lifes -= 1;
            }
            lifeRemained(lifes);
            if (lifes == 0) {
                gameFinished();
            }
            vibrate(100);
            setRecyclerSizes(currentLevel);
            nextNum.setText(getString(R.string.Level) + " " + currentLevel);
            recordTxt.setText(score + "");
        }, 200);
    }

    @Override
    public void setSize(View view) {
        switch (currentLevel) {
            case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: {
                showWord();
                setSizes(view, 4);
                break;
            }
            case 10: case 11: case 12: case 13: case 14: case 15: {
                showWord();
                setSizes(view, 5);
                break;
            }
            case 16: case 17: case 18: case 19: case 20: case 21: {
                showWord();
                setSizes(view, 6);
                break;
            }
            case 22: case 23: case 24: case 25: case 26: case 27: {
                showWord();
                setSizes(view, 7);
                break;
            }
            default: {
                showWord();
                setSizes(view, 8);
                break;
            }
        }
        getRandomByLevel(currentLevel);
    }

    public void setRecyclerSizes(int level) {
        switch (level) {
            case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: {
                setRecyclerItemFirst();
                break;
            }
            case 10: case 11: case 12: case 13: case 14: case 15: {
                setRecyclerItem(10);
                break;
            }
            case 16: case 17: case 18: case 19: case 20: case 21: {
                setRecyclerItem(12);
                break;
            }
            case 22: case 23: case 24: case 25: case 26: case 27: {
                setRecyclerItem(14);
                break;
            }
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
        String oldScore = SharedPrefManager.getSlovoRecord(getApplication());
        if (oldScore != null) {
            if (score > Integer.parseInt(oldScore)) {
                SharedPrefManager.setSlovoRecord(getApplication(), String.valueOf(score));
                SharedUpdate.setSlovoUpdate(getApplication(), String.valueOf(score));
                intent.putExtra("record", getString(R.string.CongratulationNewRecord));
            }
        } else {
            if (score > 0) {
                SharedPrefManager.setSlovoRecord(getApplication(), String.valueOf(score));
                SharedUpdate.setSlovoUpdate(getApplication(), String.valueOf(score));
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
    public void onStop() {
        super.onStop();
        if (!isPaused()) {
            showPauseDialog();
        }
    }

    private void showWord() {
        pauseImg.setEnabled(false);
        pauseTimer();
        slovo.setVisibility(View.VISIBLE);
        background.setVisibility(View.INVISIBLE);
        slovo.setText(random);
        new Handler().postDelayed(()-> {
            resumeTimer();
            pauseImg.setEnabled(true);
            slovo.setVisibility(View.INVISIBLE);
            background.setVisibility(View.VISIBLE);
        }, 2000);
    }

    private void setSizes(View view, int x) {
        int width = shulteRecycler.getWidth();
        int height = shulteRecycler.getHeight();
        view.getLayoutParams().width = width / 2 - 12;
        view.getLayoutParams().height = height / x - 10;
    }
}
