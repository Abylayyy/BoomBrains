package kz.almaty.boombrains.game_pages.number_znaki;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.helpers.DialogHelperActivity;
import kz.almaty.boombrains.helpers.SharedPrefManager;
import kz.almaty.boombrains.main_pages.FinishedActivity;

public class NumberZnakiActivity extends DialogHelperActivity implements NumberZnakiAdapter.ZnakiListener {

    @BindView(R.id.shulteRecord) TextView recordTxt;
    @BindView(R.id.slovo_teksts) TextView randomNumberTxt;
    @BindView(R.id.shulteTime) TextView timeTxt;
    @BindView(R.id.pauseBtn) ConstraintLayout pauseImg;
    @BindView(R.id.numZnakiRecycler) RecyclerView findRecycler;
    @BindView(R.id.findNumContainer) ConstraintLayout container;
    @BindView(R.id.nextNumShulte2) TextView levelTxt;
    private int position;
    private int score = 0, errors = 0;
    NumberZnakiAdapter adapter;
    private List<Integer> numbersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_znaki);
        ButterKnife.bind(this);
        setupDialog(this, R.style.numZnakiTheme, R.drawable.pause_num_znaki);
        startTimer(10000, timeTxt);

        position = getIntent().getIntExtra("position", 0);
        pauseImg.setOnClickListener(v -> showPauseDialog());
        setupRecycler();

        container.getLayoutParams().height = height() / 3;
    }

    private int height() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    private void setupRecycler() {
        numbersList.clear();
        for (int i = 1; i <= 4; i++) {
            numbersList.add(getRandom());
        }
        Collections.shuffle(numbersList);
        adapter = new NumberZnakiAdapter(numbersList, this, this);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        findRecycler.setLayoutManager(manager);
        findRecycler.setItemAnimator(new DefaultItemAnimator());
        findRecycler.setAdapter(adapter);
        randomNumberTxt.setText(String.valueOf(numbersList.get(new Random().nextInt(numbersList.size()))));
    }

    private Integer getRandom() {
        return new Random().nextInt(10);
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
        intent.putExtra("znakiScore", score);
        intent.putExtra("znakiErrors", errors);

        String oldScore = SharedPrefManager.getNumZnakiRecord(getApplication());
        if (oldScore != null) {
            if (score > Integer.parseInt(oldScore)) {
                SharedPrefManager.setNumZnakiRecord(getApplication(), String.valueOf(score));
                intent.putExtra("znakiRecord", getString(R.string.CongratulationNewRecord));
            }
        } else {
            if (score > 0) {
                SharedPrefManager.setNumZnakiRecord(getApplication(), String.valueOf(score));
                intent.putExtra("znakiRecord", getString(R.string.CongratulationNewRecord));
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

    private void setSizes(ConstraintLayout view) {
        int width = findRecycler.getWidth();
        int height = findRecycler.getHeight();
        view.getLayoutParams().width = width / 2 - 32 ;
        view.getLayoutParams().height = height / 2 - 32;
    }

    @Override
    public void getItem(ConstraintLayout layout, TextView text) {
        setSizes(layout);
    }

    @Override
    public void onItemSelected(ConstraintLayout layout, TextView textView, String value) {

    }
}
