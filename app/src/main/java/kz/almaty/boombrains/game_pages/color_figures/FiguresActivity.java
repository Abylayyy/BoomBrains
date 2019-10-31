package kz.almaty.boombrains.game_pages.color_figures;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
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
import kz.almaty.boombrains.game_pages.shulte_page.ShulteAdapter;
import kz.almaty.boombrains.game_pages.square_memory.SquareAdapter;
import kz.almaty.boombrains.helpers.DialogHelperActivity;
import kz.almaty.boombrains.helpers.SharedPrefManager;
import kz.almaty.boombrains.helpers.SpaceItemDecoration;
import kz.almaty.boombrains.main_pages.FinishedActivity;
import kz.almaty.boombrains.models.game_models.FigureModel;
import kz.almaty.boombrains.models.game_models.SquareModel;

@SuppressLint("SetTextI18n")
public class FiguresActivity extends DialogHelperActivity implements FigureAdapter.FigureListener {

    @BindView(R.id.shulteRecord) TextView recordTxt;
    @BindView(R.id.shulteTime) TextView timeTxt;
    @BindView(R.id.nextNumShulte) TextView nextNum;
    @BindView(R.id.pauseBtn) ConstraintLayout pauseImg;
    @BindView(R.id.shulteRecycler) RecyclerView shulteRecycler;
    @BindView(R.id.mainShulteConst) ConstraintLayout layout;

    private int position;
    private int score = 0;
    private int errors = 0;
    private int count = 0;
    boolean isEnabled = false;

    FigureAdapter adapter;
    List<FigureModel> numbersList;
    int currentLevel = 1;
    List<Integer> randomcolors;
    List<Integer> randomFigures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_figures);
        ButterKnife.bind(this);

        position = getIntent().getIntExtra("position", 0);
        setupDialog(this, R.style.figureTheme, R.drawable.pause_figure, position, "");
        startTimer(20000, timeTxt);
        setCount();
        loadGoogleAd();

        randomcolors = new ArrayList<>(Arrays.asList(R.color.orangeFigure, R.color.blueFigure, R.color.greenFigure, R.color.redFigure));
        randomFigures = new ArrayList<>(Arrays.asList(R.drawable.rectangle, R.drawable.circle_shape, R.drawable.triangle, R.drawable.romb_shape));
        numbersList = new ArrayList<>();

        pauseImg.setOnClickListener(v -> showPauseDialog());
        nextNum.setText(getString(R.string.Level) + " " + currentLevel);

        setRecycler();
    }

    private void setRecycler() {
        for (int i = 0; i < 60; i++) {
            numbersList.add(new FigureModel(
                    randomcolors.get(new Random().nextInt(randomcolors.size())),
                    randomFigures.get(new Random().nextInt(randomFigures.size()))
            ));
        }

        adapter = new FigureAdapter(numbersList, this, this);
        shulteRecycler.setAdapter(adapter);
        shulteRecycler.setLayoutManager(new GridLayoutManager(this, 6));
        shulteRecycler.addItemDecoration(new SpaceItemDecoration(0));
        shulteRecycler.setItemAnimator(new DefaultItemAnimator());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateNumbers(View view, int tint) {
        Log.d("REDS::", String.valueOf(adapter.getCountRed()));
        Log.d("BLUE::", String.valueOf(adapter.getCountBlue()));
        Log.d("GREEN::", String.valueOf(adapter.getCountGreen()));
        Log.d("ORANGE::", String.valueOf(adapter.getCountOrange()));
    }

    @Override
    public void setSize(View view) {
        setSizes(view);
    }

    private void setSizes(View view) {
        int width = shulteRecycler.getWidth();
        int height = shulteRecycler.getHeight();
        view.getLayoutParams().width = width / 6;
        view.getLayoutParams().height = height / 10;
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
        String oldScore = SharedPrefManager.getColorRecord(getApplication());
        if (oldScore != null) {
            if (score > Integer.parseInt(oldScore)) {
                SharedPrefManager.setColorRecord(getApplication(), String.valueOf(score));
                intent.putExtra("record", getString(R.string.CongratulationNewRecord));
            }
        } else {
            if (score > 0) {
                SharedPrefManager.setColorRecord(getApplication(), String.valueOf(score));
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
