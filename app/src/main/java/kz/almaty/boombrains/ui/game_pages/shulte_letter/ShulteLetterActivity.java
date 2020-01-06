package kz.almaty.boombrains.ui.game_pages.shulte_letter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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
import kz.almaty.boombrains.files.RememberWordsEn;
import kz.almaty.boombrains.files.RememberWordsKz;
import kz.almaty.boombrains.files.RememberWordsRu;
import kz.almaty.boombrains.helpers.DialogHelperActivity;
import kz.almaty.boombrains.helpers.SharedPrefManager;
import kz.almaty.boombrains.helpers.SharedUpdate;
import kz.almaty.boombrains.helpers.SpaceItemDecoration;
import kz.almaty.boombrains.ui.main_pages.FinishedActivity;

@SuppressLint("SetTextI18n")
public class ShulteLetterActivity extends DialogHelperActivity implements LetterAdapter.LetterListener {

    private static final int LEVEL6 = 66;
    private static final int LEVEL7 = 77;
    private static final int LEVEL8 = 88;
    public static final int LEVEL1 = 11;
    public static final int LEVEL2 = 22;
    public static final int LEVEL3 = 33;
    public static final int LEVEL4 = 44;
    public static final int LEVEL5 = 55;

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

    private View view;
    public int index = 0;
    public int score = 0;
    int currentLevel = LEVEL1;
    private int errors;
    private String name;
    private List<String> letters;
    String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shulte_letter);
        ButterKnife.bind(this);
        position = getIntent().getIntExtra("position", 0);
        lang = SharedUpdate.getLanguage(getApplication());

        if (lang.equals("kk") || lang.equals("ru")) {
            startTimer(120000, timeTxt);
        } else {
            startTimer(100000, timeTxt);
        }

        setCount();
        loadGoogleAd();

        numbersList = new ArrayList<>();
        setupDialog(this, R.style.shulteLetterTheme, R.drawable.pause_letter, position, "");

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
        Collections.shuffle(numbersList);
        adapter = new LetterAdapter(numbersList, this, this);
        shulteRecycler.setAdapter(adapter);
        shulteRecycler.setLayoutManager(new GridLayoutManager(this, 4));
        shulteRecycler.addItemDecoration(new SpaceItemDecoration(0));
        shulteRecycler.setItemAnimator(new DefaultItemAnimator());
        adapter.notifyDataSetChanged();
    }

    private void setupList(int size, int span) {
        numbersList.clear();
        for (int i = 0; i < size; i++) {
            numbersList.add(letters.get(i));
        }
        Collections.shuffle(numbersList);
        adapter = new LetterAdapter(numbersList, this, this);
        GridLayoutManager manager = new GridLayoutManager(this, span);
        shulteRecycler.setLayoutManager(manager);
        shulteRecycler.addItemDecoration(new SpaceItemDecoration(0));
        shulteRecycler.setItemAnimator(new DefaultItemAnimator());
        shulteRecycler.setAdapter(adapter);
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
                index = 0;
                nextNum.setText(getString(R.string.NextLetter) + " " + letters.get(index));
                setGameLevels(getLevel(numbersList.size()));
            }
        }
        else {
            errors += 1;
            if (score > 0) {
                score -= 50;
            }
            recordTxt.setText(score + "");
            setBackgroundError(view);
            setAudio(R.raw.wrong_clicked);
            vibrate(100);
        }
    }

    @Override
    public void setSize(View view, TextView textView) {
        switch (lang) {
            case "en": case "es": {
                switch (currentLevel) {
                    case LEVEL1: { setSizes(view, textView, 4, 4); break; }
                    default: { setSizes(view, textView, 5, 5); break; }
                }
                break;
            }
            case "ru": {
                switch (currentLevel) {
                    case LEVEL1: { setSizes(view, textView, 4, 4); break; }
                    case LEVEL2: { setSizes(view, textView, 5, 5); break; }
                    default: { setSizes(view, textView, 5,6); break; }
                }
                break;
            }
            case "kk": {
                switch (currentLevel) {
                    case LEVEL1: { setSizes(view, textView, 4, 4); break; }
                    case LEVEL2: { setSizes(view, textView, 5, 5); break; }
                    case LEVEL3: { setSizes(view, textView, 6,6); break; }
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

    public int getLevel(int size) {
        int level = 0;
        switch (lang) {
            case "en": case "es": {
                switch (size) {
                    case 16: level = LEVEL2; break;
                    default: level = LEVEL3; break;
                }
                break;
            }
            case "ru": {
                switch (size) {
                    case 16: level = LEVEL2; break;
                    case 25: level = LEVEL3; break;
                    default: level = LEVEL4; break;
                }
                break;
            }
            case "kk": {
                switch (size) {
                    case 16: level = LEVEL2; break;
                    case 25: level = LEVEL3; break;
                    case 36: level = LEVEL4; break;
                    default: level = LEVEL5; break;
                }
                break;
            }
        }
        return level;
    }

    public void setGameLevels(int level) {
        switch (lang) {
            case "en": case "es": {
                switch (level) {
                    case LEVEL1: { setRecycler(); currentLevel = LEVEL1; break; }
                    default: { currentLevel = LEVEL2; setupList(25, 5); setAudio(R.raw.level_complete); break; }
                }
                break;
            }
            case "kk": {
                switch (level) {
                    case LEVEL1: { setRecycler(); currentLevel = LEVEL1;break; }
                    case LEVEL2: { currentLevel = LEVEL2; setupList(25, 5); setAudio(R.raw.level_complete);break; }
                    case LEVEL3: { currentLevel = LEVEL3; setupList(36, 6); setAudio(R.raw.level_complete);break; }
                    default: { currentLevel = LEVEL4; setupList(42, 6); setAudio(R.raw.level_complete); break; }
                }
                break;
            }
            case "ru": {
                switch (level) {
                    case LEVEL1: { setRecycler(); currentLevel = LEVEL1; break; }
                    case LEVEL2: { currentLevel = LEVEL2; setupList(25, 5); setAudio(R.raw.level_complete); break; }
                    default: { currentLevel = LEVEL3; setupList(30, 5); setAudio(R.raw.level_complete);break; }
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

    @Override
    public void startNewActivity() {
        Intent intent = new Intent(getApplication(), FinishedActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("score", score);
        intent.putExtra("errors", errors);

        String oldScore = SharedPrefManager.getShulteLetterRecord(getApplication());
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
        startActivity(intent);
        overridePendingTransition(0,0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissDialog();
    }
}
