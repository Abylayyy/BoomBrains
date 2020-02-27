package kz.almaty.boombrains.ui.game_pages.shulte_letter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.util.files.RememberWordsEn;
import kz.almaty.boombrains.util.files.RememberWordsKz;
import kz.almaty.boombrains.util.files.RememberWordsRu;
import kz.almaty.boombrains.util.helpers.dialog_helper.DialogHelperActivity;
import kz.almaty.boombrains.util.helpers.preference.SharedPrefManager;
import kz.almaty.boombrains.util.helpers.preference.SharedUpdate;
import kz.almaty.boombrains.util.helpers.list_helper.SpaceItemDecoration;
import kz.almaty.boombrains.ui.main_pages.FinishedActivity;

@SuppressLint("SetTextI18n")
public class ShulteLetterActivity extends DialogHelperActivity implements LetterAdapter.LetterListener {

    LetterAdapter adapter;
    List<String> numbersList;
    String number;
    int position;

    @BindView(R.id.shulteRecord) TextView recordTxt;
    @BindView(R.id.shulteTime) TextView timeTxt;
    @BindView(R.id.nextNumShulte) TextView nextNum;
    @BindView(R.id.constraintLayout2) ConstraintLayout navBarLayout;
    @BindView(R.id.pauseBtn) ConstraintLayout pauseImg;
    @BindView(R.id.shulteRecycler) RecyclerView shulteRecycler;

    @BindView(R.id.life1) ImageView life1;
    @BindView(R.id.life2) ImageView life2;
    @BindView(R.id.life3) ImageView life3;

    private int lifes = 3;
    private boolean lifeUsed = false;

    private View view;
    private int index = 0;
    private int score = 0;
    private int currentLevel = 1;
    private List<String> letters;
    String lang;
    private int errors = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shulte_letter);
        ButterKnife.bind(this);
        position = getIntent().getIntExtra("position", 0);
        lang = SharedUpdate.getLanguage(getApplication());

        if (SharedPrefManager.isUserLoggedIn(this) && SharedPrefManager.isNetworkOnline(this)) {
            connectSocket();
        }

        startTimer(60000, timeTxt);

        setCount();
        loadGoogleAd();

        numbersList = new ArrayList<>();
        setupDialog(this, R.style.shulteLetterTheme, R.drawable.pause_letter, position, "");

        setupLifeDialog(this, R.color.topShulteLetter);
        loadAddForLife();

        setListByLang(lang);

        setRecycler();

        pauseImg.setOnClickListener(v -> showPauseDialog());
        nextNum.setText(getString(R.string.NextLetter) + " " + letters.get(index));
    }

    private void setListByLang(String lang) {
        switch (lang) {
            case "en": {
                letters = RememberWordsEn.letterENList;
                break;
            }
            case "ru": {
                letters = RememberWordsRu.letterRUList;
                break;
            }
            case "kk": {
                letters = RememberWordsKz.letterKZList;
                break;
            }
            case "es": {
                letters = RememberWordsEn.letterESList;
                break;
            }
        }
    }

    private void setRecycler() {
        for (int i = 0; i < 16; i++) {
            numbersList.add(letters.get(i));
        }
        setRecyclerDefaults(4);
    }

    private void setupList(int size, int span) {
        numbersList.clear();
        for (int i = 0; i < size; i++) {
            numbersList.add(letters.get(i));
        }
        setRecyclerDefaults(span);
    }

    private void setRecyclerDefaults(int size) {
        Collections.shuffle(numbersList);
        adapter = new LetterAdapter(numbersList, this, this);
        shulteRecycler.setAdapter(adapter);
        shulteRecycler.setLayoutManager(new GridLayoutManager(this, size));
        shulteRecycler.addItemDecoration(new SpaceItemDecoration(0));
        shulteRecycler.setItemAnimator(new DefaultItemAnimator());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setNumber(String value, View view) {
        this.number = value;
        this.view = view;
    }

    @Override
    public void updateNumbers(TextView textView) {
        if (number.equals(String.valueOf(letters.get(index)))) {
            setAudio(R.raw.click);
            setBackgroundSuccess(view);
            score += 100;
            index += 1;
            recordTxt.setText(score + "");
            nextNum.setText(getString(R.string.NextLetter) + " " + letters.get(index));
            if (number.equals(String.valueOf(letters.get(numbersList.size() - 1)))) {
                currentLevel += 1;
                index = 0;
                nextNum.setText(getString(R.string.NextLetter) + " " + letters.get(index));
                setGameLevels(currentLevel);
            }
        }
        else {
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
            recordTxt.setText(score + "");
            setBackgroundError(view);
            setAudio(R.raw.wrong_clicked);
            vibrate(100);
        }
    }

    @Override
    public void startWithLife() {
        lifeUsed = true;
        lifes = 1;
        life1.setImageResource(R.drawable.life_full);
        resumeTimer();
    }

    @Override
    public void setSize(View view, TextView textView) {
        switch (lang) {
            case "en": case "es": {
                if (currentLevel == 1) {setSizes(view, textView, 4, 4); }
                else { setSizes(view, textView, 5, 5); }
                break;
            }
            case "ru": {
                switch (currentLevel) {
                    case 1: { setSizes(view, textView, 4, 4); break; }
                    case 2: { setSizes(view, textView, 5, 5); break; }
                    default: { setSizes(view, textView, 5,6); break; }
                }
                break;
            }
            case "kk": {
                switch (currentLevel) {
                    case 1: { setSizes(view, textView, 4, 4); break; }
                    case 2: { setSizes(view, textView, 5, 5); break; }
                    case 3: { setSizes(view, textView, 6,6); break; }
                    default: { setSizes(view, textView, 6,7); break; }
                }
                break;
            }
        }
    }

    private void setSizes(View view, TextView textView, int i, int j) {
        int width = shulteRecycler.getWidth();
        int height = shulteRecycler.getHeight();
        int new_width = 0, new_height = 0;

        switch (i) {
            case 4: case 5: case 6: { new_width = width / i - 4; new_height = height / j - 5; break;}
            case 7: case 8: {new_width = width / i - 3; new_height = height / j - 4; break;}
            case 9: case 10: {new_width = width / i - 2; new_height = height / j - 3; break;}
        }
        view.getLayoutParams().width = new_width;
        view.getLayoutParams().height = new_height;

        setSozSize(textView, width, i);
    }

    private void setSozSize(TextView textView, int width, int i) {
        float size = 0f;
        switch (i) {
            case 4: { size = (float)((width / i) / 6.6); break; }
            case 5: { size = (float)((width / i) / 6.2); break; }
            case 6: { size = (float)((width / i) / 5.8); break; }
            case 7: { size = (float)((width / i) / 5.4); break; }
            case 8: { size = (float)((width / i) / 5.0); break; }
            case 9: { size = (float)((width / i) / 4.7); break; }
            case 10: { size = (float)((width / i) / 4.4);break; }
        }
        textView.setTextSize(size);
    }

    public void setGameLevels(int level) {
        switch (lang) {
            case "en": case "es": {
                if (level == 1) { setRecycler(); }
                else {
                    setupList(25, 5);
                    setAudio(R.raw.level_complete);
                }
                break;
            }
            case "kk": {
                switch (level) {
                    case 1: { setRecycler(); break; }
                    case 2: { setupList(25, 5); setAudio(R.raw.level_complete);break; }
                    case 3: { setupList(36, 6); setAudio(R.raw.level_complete);break; }
                    default: { setupList(42, 6); setAudio(R.raw.level_complete); break; }
                }
                break;
            }
            case "ru": {
                switch (level) {
                    case 1: { setRecycler(); break; }
                    case 2: { setupList(25, 5); setAudio(R.raw.level_complete); break; }
                    default: { setupList(30, 5); setAudio(R.raw.level_complete);break; }
                }
                break;
            }
        }
    }

    public void setBackgroundError(View view) {
        view.setBackgroundResource(R.drawable.shulte_error);
        new Handler().postDelayed(()-> view.setBackgroundResource(R.drawable.back_shulte_item), 100);
    }

    public void setBackgroundSuccess(View view) {
        view.setBackgroundResource(R.drawable.shulte_success);
        new Handler().postDelayed(()-> view.setBackgroundResource(R.drawable.back_shulte_item), 100);
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
        String oldScore = SharedPrefManager.getShulteLetterRecord(getApplication());
        SharedPrefManager.setCoin(getApplication(), SharedPrefManager.getCoin(getApplication()) + result(score));
        if (oldScore != null) {
            if (score > Integer.parseInt(oldScore)) {
                SharedPrefManager.setShulteLetterRecord(getApplication(), String.valueOf(score));
                SharedUpdate.setLetterUpdate(getApplication(), String.valueOf(score));
                intent.putExtra("record", getString(R.string.CongratulationNewRecord));
            }
        } else {
            if (score > 0) {
                SharedPrefManager.setShulteLetterRecord(getApplication(), String.valueOf(score));
                SharedUpdate.setLetterUpdate(getApplication(), String.valueOf(score));
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
}
