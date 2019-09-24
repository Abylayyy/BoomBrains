package kz.almaty.boombrains.game_pages.shulte_page;

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
import kz.almaty.boombrains.helpers.DialogHelperActivity;
import kz.almaty.boombrains.helpers.SharedPrefManager;
import kz.almaty.boombrains.helpers.SpaceItemDecoration;
import kz.almaty.boombrains.main_pages.FinishedActivity;

@SuppressLint("SetTextI18n")
public class ShulteActivity extends DialogHelperActivity implements ShulteAdapter.ShulteListener {

    ShulteAdapter adapter;
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
    public int index = 1;
    public int score = 0;
    public static final int LEVEL1 = 11;
    public static final int LEVEL2 = 22;
    public static final int LEVEL3 = 33;
    public static final int LEVEL4 = 44;
    public static final int LEVEL5 = 55;
    int currentLevel = LEVEL1;
    private int errors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shulte);
        ButterKnife.bind(this);
        setupDialog(this, R.style.shulteTheme, R.drawable.pause_shulte);
        startTimer(120000, timeTxt);
        setCount();

        numbersList = new ArrayList<>();
        setRecycler();

        position = getIntent().getIntExtra("position", 0);

        pauseImg.setOnClickListener(v -> showPauseDialog());

        nextNum.setText(getString(R.string.SchulteNextNumber) + " " + index);
    }

    private void setRecycler() {
        for (int i = 1; i <= 16; i++) {
            numbersList.add(String.valueOf(i));
        }
        Collections.shuffle(numbersList);
        adapter = new ShulteAdapter(numbersList, this, this);
        shulteRecycler.setAdapter(adapter);
        shulteRecycler.setLayoutManager(new GridLayoutManager(this, 4));
        shulteRecycler.addItemDecoration(new SpaceItemDecoration(0));
        shulteRecycler.setItemAnimator(new DefaultItemAnimator());
        adapter.notifyDataSetChanged();
    }

    private void setupList(int size, int span) {
        numbersList.clear();
        for (int i = 1; i <= size; i++) {
            numbersList.add(String.valueOf(i));
        }
        Collections.shuffle(numbersList);
        adapter = new ShulteAdapter(numbersList, this, this);
        GridLayoutManager manager = new GridLayoutManager(this, span);
        shulteRecycler.setLayoutManager(manager);
        shulteRecycler.addItemDecoration(new SpaceItemDecoration(0));
        shulteRecycler.setItemAnimator(new DefaultItemAnimator());
        shulteRecycler.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (!isPaused()) {
            showPauseDialog();
        }
    }

    @Override
    public void setNumber(String value, View view) {
        this.number = value;
        this.view = view;
    }

    @Override
    public void updateNumbers() {
        if (number.equals(String.valueOf(index))) {
            setAudio(R.raw.click);
            setBackgroundSuccess(view);
            score += 100;
            index += 1;
            recordTxt.setText(score + "");
            nextNum.setText(getString(R.string.SchulteNextNumber) + " " + index);
            if (number.equals(String.valueOf(numbersList.size()))) {
                index = 1;
                nextNum.setText(getString(R.string.SchulteNextNumber) + " " + index);
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
        }
    }

    @Override
    public void setSize(View view, TextView text) {
        switch (currentLevel) {
            case LEVEL1: {
                setSizes(view, text,4);
                break;
            }
            case LEVEL2: {
                setSizes(view, text,5);
                break;
            }
            case LEVEL3: {
                setSizes(view, text,6);
                break;
            }
            case LEVEL4: {
                setSizes(view, text,7);
                break;
            }
            case LEVEL5: {
                setSizes(view, text,8);
                break;
            }
            default: {
                setSizes(view, text, 9);
                break;
            }
        }
    }

    private void setSizes(View view, TextView textView, int i) {
        int width = shulteRecycler.getWidth();
        int height = shulteRecycler.getHeight();
        view.getLayoutParams().width = width / i;
        view.getLayoutParams().height = height / i;

        float size = 0f;

        switch (i) {
            case 4: {
                size = (float)((width / i) / 6.6);
                break;
            }
            case 5: {
                size = (float)((width / i) / 6.2);
                break;
            }
            case 6: {
                size = (float)((width / i) / 5.8);
                break;
            }
            case 7: {
                size = (float)((width / i) / 5.4);
                break;
            }
            case 8: {
                size = (float)((width / i) / 5.0);
                break;
            }
        }
        textView.setTextSize(size);
    }


    public int getLevel(int size) {
        int level = 0;
        switch (size) {
            case 16:
                level = LEVEL2;
                break;
            case 25:
                level = LEVEL3;
                break;
            case 36:
                level = LEVEL4;
                break;
            case 49:
                level = LEVEL5;
                break;
        }
        return level;
    }

    public void setGameLevels(int level) {
        switch (level) {
            case LEVEL1: {
                setRecycler();
                currentLevel = LEVEL1;
                break;
            }
            case LEVEL2: {
                currentLevel = LEVEL2;
                setupList(25, 5);
                setAudio(R.raw.level_complete);
                break;
            }
            case LEVEL3: {
                currentLevel = LEVEL3;
                setupList(36, 6);
                setAudio(R.raw.level_complete);
                break;
            }
            case LEVEL4: {
                currentLevel = LEVEL4;
                setupList(49, 7);
                setAudio(R.raw.level_complete);
                break;
            }
            case LEVEL5: {
                currentLevel = LEVEL5;
                setupList(56, 8);
                setAudio(R.raw.level_complete);
                break;
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!isPaused()) {
            showPauseDialog();
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
    public void startNewActivity() {
        Intent intent = new Intent(getApplication(), FinishedActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("shulteScore", score);
        intent.putExtra("shulteErrors", errors);

        String oldScore = SharedPrefManager.getShulteRecord(getApplication());
        if (oldScore != null) {
            if (score > Integer.parseInt(oldScore)) {
                SharedPrefManager.setShulteRecord(getApplication(), String.valueOf(score));
                intent.putExtra("shulteRecord", getString(R.string.CongratulationNewRecord));
            }
        } else {
            if (score > 0) {
                SharedPrefManager.setShulteRecord(getApplication(), String.valueOf(score));
                intent.putExtra("shulteRecord", getString(R.string.CongratulationNewRecord));
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
