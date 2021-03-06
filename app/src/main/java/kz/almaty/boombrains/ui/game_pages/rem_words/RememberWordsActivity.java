package kz.almaty.boombrains.ui.game_pages.rem_words;

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
import kz.almaty.boombrains.util.files.RememberWordsEn;
import kz.almaty.boombrains.util.files.RememberWordsKz;
import kz.almaty.boombrains.util.files.RememberWordsRu;
import kz.almaty.boombrains.util.files.RememberWordsSpain;
import kz.almaty.boombrains.util.helpers.dialog_helper.DialogHelperActivity;
import kz.almaty.boombrains.util.helpers.preference.SharedPrefManager;
import kz.almaty.boombrains.util.helpers.preference.SharedUpdate;
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
    @BindView(R.id.remCont) ConstraintLayout background;
    @BindView(R.id.slovo_teksts) TextView slovo;
    @BindView(R.id.wordConst) ConstraintLayout wordLayout;

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

    private int currentLevel = 1, score = 0, errors = 0;
    private String random;
    private boolean lifeUsed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remember_words);
        ButterKnife.bind(this);
        position = getIntent().getIntExtra("position", 0);

        setupDialog(this, R.style.slovoTheme, R.drawable.pause_rem_word, position, "");
        startTimer(15000, timeTxt);
        setCount();
        loadGoogleAd();

        setTypeAndLayout();

        setupLifeDialog(this, R.color.topRemWords);
        loadAddForLife();

        String lang = SharedUpdate.getLanguage(this);
        numbersList = new ArrayList<>();
        subList = new ArrayList<>();
        setupList(lang);

        wordLayout.getLayoutParams().height = (int) (height() / 3.8);

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
            case "en": { numbersList = RememberWordsEn.wordsEnList; break; }
            case "es": { numbersList = RememberWordsSpain.wordsESList; break; }
            case "ru": { numbersList = RememberWordsRu.wordsRuList; break; }
            case "kk": { numbersList = RememberWordsKz.wordsKzList; break; }
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
        if (level <= 3) { random = getRandom(1); }
        else if (level <= 6) { random = getRandom(2); }
        else if (level <= 9) { random = getRandom(3); }
        else if (level <= 15) { random = getRandom(4); }
        else if (level <= 21) { random = getRandom(5); }
        else if (level <= 27) { random = getRandom(6); }
        else { random = getRandom(7); }
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
            score += 100;
            recordTxt.setText(score + "");
            updateSocketScore(score);
        } else {
            view.setBackgroundResource(R.drawable.card_background_error);
            text.setTextColor(Color.WHITE);
            errorResult();
            correctsResults.clear();
        }
    }

    private void correctResult() {
        new Handler().postDelayed(() -> {
            correctsResults.clear();
            currentLevel += 1;
            setAudio(R.raw.level_complete);
            if (lifes > 0) {
                startNewQuestion();
                setRecyclerSizes(currentLevel);
            }
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
                if (!lifeUsed) {
                    showLifeDialog(this, type);
                } else {
                    startNewActivity();
                }
            }
            errorClicked();
        }, 200);
    }

    private void errorClicked() {
        setAudio(R.raw.wrong_clicked);
        vibrate(100);
        if (lifes > 0) {
            startNewQuestion();
            setRecyclerSizes(currentLevel);
        }
        nextNum.setText(getString(R.string.Level) + " " + currentLevel);
        recordTxt.setText(score + "");
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
        setRecyclerSizes(currentLevel);
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

    @Override
    public void setSize(View view) {
        if (currentLevel <= 9) { setSizes(view, 4); }
        else if (currentLevel <= 15) { setSizes(view, 5); }
        else if (currentLevel <= 21) { setSizes(view, 6); }
        else if (currentLevel <= 27) { setSizes(view, 7); }
        else { setSizes(view, 8); }
        showWord();
        getRandomByLevel(currentLevel);
    }

    public void setRecyclerSizes(int level) {
        if (level <= 9) {setRecyclerItemFirst();}
        else if (level <= 15) {setRecyclerItem(10);}
        else if (level <= 21) {setRecyclerItem(11);}
        else if (level <= 27) {setRecyclerItem(12);}
        else {setRecyclerItem(13);}
    }

    private void lifeRemained(int i) {
        ImageView[] lifes = {life1, life2, life3};
        if (i >= 0) {
            lifes[i].setImageResource(R.drawable.life_border);
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

    private Intent myIntent() {
        Intent intent = new Intent(getApplication(), FinishedActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("score", score);
        intent.putExtra("errors", errors);
        String oldScore = SharedPrefManager.getSlovoRecord(getApplication());
        SharedPrefManager.setCoin(getApplication(), SharedPrefManager.getCoin(getApplication()) + result(score));
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
    public void onStop() {
        super.onStop();
        if (!isPaused()) {
            showPauseDialog();
        }
    }
}
