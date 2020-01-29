package kz.almaty.boombrains.ui.game_pages.color_figures;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import kz.almaty.boombrains.util.helpers.DialogHelperActivity;
import kz.almaty.boombrains.util.helpers.SharedPrefManager;
import kz.almaty.boombrains.util.helpers.SharedUpdate;
import kz.almaty.boombrains.util.helpers.SpaceItemDecoration;
import kz.almaty.boombrains.ui.main_pages.FinishedActivity;
import kz.almaty.boombrains.data.models.game_models.FigureModel;

@SuppressLint("SetTextI18n")
public class FiguresActivity extends DialogHelperActivity implements FigureAdapter.FigureListener {

    @BindView(R.id.shulteRecord) TextView recordTxt;
    @BindView(R.id.shulteTime) TextView timeTxt;
    @BindView(R.id.nextNumShulte) TextView nextNum;
    @BindView(R.id.pauseBtn) ConstraintLayout pauseImg;
    @BindView(R.id.shulteRecycler) RecyclerView shulteRecycler;
    @BindView(R.id.mainShulteConst) ConstraintLayout layout;

    @BindView(R.id.life1) ImageView life1;
    @BindView(R.id.life2) ImageView life2;
    @BindView(R.id.life3) ImageView life3;

    private int lifes = 3;
    private boolean lifeUsed = false;

    private int position;
    private int score = 0;
    private int count = 0;
    boolean isEnabled = false;
    private int[][] levelArray;
    private List<Integer> thisLevelFigures = new ArrayList<>();
    private List<Integer> thisLevelColors = new ArrayList<>();
    private int maxColorIndex = 0;

    FigureAdapter adapter;
    List<FigureModel> numbersList;
    int currentLevel = 0;
    List<Integer> randomcolors;
    List<Integer> randomFigures;
    int transparent;
    private int errors = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_figures);
        ButterKnife.bind(this);

        position = getIntent().getIntExtra("position", 0);
        setupDialog(this, R.style.figureTheme, R.drawable.pause_figure, position, "");
        startTimer(60000, timeTxt);
        setCount();
        loadGoogleAd();

        setupLifeDialog(this, R.color.topShape);
        loadAddForLife();

        transparent = android.R.color.transparent;
        numbersList = new ArrayList<>();

        levelArray = new int[][]{
                // Easy levels
                {45, 21}, {42, 24}, {39, 27}, {36, 30}, {34, 32},
                // Medium levels
                {34, 22, 10}, {31, 22, 13}, {28, 22, 16}, {25, 22, 19}, {23, 22, 21},
                // Hard levels
                {28, 22, 11, 5}, {25, 21, 12, 8}, {22, 19, 14, 11}, {19, 18, 15, 14}, {18, 17, 16, 15}
        };

        randomcolors = new ArrayList<>(Arrays.asList(R.color.orangeFigure, R.color.blueFigure, R.color.greenFigure, R.color.redFigure));
        randomFigures = new ArrayList<>(Arrays.asList(R.drawable.rectangle, R.drawable.circle_shape, R.drawable.triangle, R.drawable.romb_shape));

        pauseImg.setOnClickListener(v -> showPauseDialog());
        nextNum.setText(getString(R.string.Level) + " " + (currentLevel + 1));

        setRecycler();
    }

    private void setRecycler() {
        clearElements();
        calcLevelFigures();
        startNewLevel();
    }

    private void clearElements() {
        maxColorIndex = 0;
        thisLevelColors.clear();
        thisLevelFigures.clear();
        numbersList.clear();
    }

    private void startNewLevel() {
        nextNum.setText(getString(R.string.Level) + " " + (currentLevel + 1));
        adapter = new FigureAdapter(numbersList, this, this);
        shulteRecycler.setAdapter(adapter);
        shulteRecycler.setLayoutManager(new GridLayoutManager(this, 6));
        shulteRecycler.addItemDecoration(new SpaceItemDecoration(0));
        shulteRecycler.setItemAnimator(new DefaultItemAnimator());
        adapter.notifyDataSetChanged();
    }

    private void makeInvisible(int color) {
        for (FigureModel model : numbersList) {
            if (model.getTint() == color) {
                model.setSelected(true);
            }
        }
        if (maxColorIndex == thisLevelColors.size()) {
            currentLevel += 1;
            setRecycler();
        } else {
            startNewLevel();
        }
    }

    private void calcLevelFigures(){

        int level = currentLevel;

        if (level > levelArray.length - 1) {
            level = levelArray.length - 1;
        }

        for (int i = 0; i < levelArray[level].length; i++) {
            thisLevelColors.add(randomcolors.get(i));
            thisLevelFigures.add(randomFigures.get(i));
        }

        Collections.shuffle(thisLevelColors);
        Collections.shuffle(thisLevelFigures);

        for (int i = 0; i < levelArray[level].length; i++) {
            for (int j = 0; j < levelArray[level][i]; j++) {
                FigureModel model = new FigureModel();
                model.setResource(thisLevelFigures.get(new Random().nextInt(thisLevelFigures.size())));
                model.setTint(thisLevelColors.get(i));
                numbersList.add(model);
            }
        }

        Collections.shuffle(numbersList);
    }

    @Override
    public void updateNumbers(View view, int tint) {
        if (tint == thisLevelColors.get(maxColorIndex)) {
            setAudio(R.raw.click);
            maxColorIndex += 1;
            score += 100;
            recordTxt.setText(""+score);
            makeInvisible(tint);
        } else {
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
            recordTxt.setText("" + score);
        }
    }

    private void lifeRemained(int i) {
        ImageView[] lifes = {life1, life2, life3};
        if (i >= 0) {
            lifes[i].setImageResource(R.drawable.life_border);
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
    public void setSize(View view) {
        setSizes(view);
    }

    private void setSizes(View view) {
        int width = shulteRecycler.getWidth();
        int height = shulteRecycler.getHeight();
        view.getLayoutParams().width = width / 6;
        view.getLayoutParams().height = height / 11;
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

    private Intent myIntent() {
        Intent intent = new Intent(getApplication(), FinishedActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("score", score);
        intent.putExtra("errors", errors);
        String oldScore = SharedPrefManager.getFigureRecord(getApplication());

        SharedPrefManager.setCoin(getApplication(), SharedPrefManager.getCoin(getApplication()) + result(score));

        if (oldScore != null) {
            if (score > Integer.parseInt(oldScore)) {
                SharedPrefManager.setFigureRecord(getApplication(), String.valueOf(score));
                SharedUpdate.setFigureUpdate(getApplication(), String.valueOf(score));
                intent.putExtra("record", getString(R.string.CongratulationNewRecord));
            }
        } else {
            if (score > 0) {
                SharedPrefManager.setFigureRecord(getApplication(), String.valueOf(score));
                SharedUpdate.setFigureUpdate(getApplication(), String.valueOf(score));
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
