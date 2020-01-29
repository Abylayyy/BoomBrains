package kz.almaty.boombrains.ui.game_pages.shulte_page;

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
import kz.almaty.boombrains.util.helpers.DialogHelperActivity;
import kz.almaty.boombrains.util.helpers.SharedPrefManager;
import kz.almaty.boombrains.util.helpers.SharedUpdate;
import kz.almaty.boombrains.util.helpers.SpaceItemDecoration;
import kz.almaty.boombrains.ui.main_pages.FinishedActivity;

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
    @BindView(R.id.mainShulteConst) ConstraintLayout layout;

    @BindView(R.id.life1) ImageView life1;
    @BindView(R.id.life2) ImageView life2;
    @BindView(R.id.life3) ImageView life3;

    private int lifes = 3;

    private View view;
    private int index = 1;
    private int score = 0;
    int currentLevel = 1;
    private String name;
    private int errors = 0;
    private boolean lifeUsed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shulte);
        ButterKnife.bind(this);
        position = getIntent().getIntExtra("position", 0);
        startTimer(60000, timeTxt);
        setCount();
        loadGoogleAd();

        name = getIntent().getStringExtra("name");
        numbersList = new ArrayList<>();
        setupDialog(this, R.style.shulteTheme, R.drawable.pause_shulte, position, name);

        setupLifeDialog(this, R.color.topShulte);
        loadAddForLife();

        setRecycler();

        pauseImg.setOnClickListener(v -> showPauseDialog());
        nextNum.setText(getString(R.string.SchulteNextNumber) + " " + index);
    }

    private void setRecycler() {
        for (int i = 1; i <= 16; i++) {
            numbersList.add(String.valueOf(i));
        }
        setRecyclerDefaults(4);
    }

    private void setupList(int size) {
        numbersList.clear();

        int res = size * size;

        for (int i = 1; i <= res; i++) {
            numbersList.add(String.valueOf(i));
        }
        setRecyclerDefaults(size);
    }

    @Override
    public void onBackPressed() {
        if (!isPaused()) {
            showPauseDialog();
        }
    }

    private void setRecyclerDefaults(int size) {
        Collections.shuffle(numbersList);
        adapter = new ShulteAdapter(numbersList, this, this);
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
    public void updateNumbers(TextView text) {
        if (number.equals(String.valueOf(index))) {
            setSuccess(text, name);
        }
        else {
            setError();
        }
    }

    private void setError() {
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

    private void setSuccess(TextView text, String name) {
        if (name.equals(getString(R.string.Easy))) { setEasyLevel(text); }
        else if (name.equals(getString(R.string.Medium))) { success(); }
        else if (name.equals(getString(R.string.Hard))) { success();setHardLevels(currentLevel); }
    }


    private void setEasyLevel(TextView text) {
        text.setVisibility(View.INVISIBLE);
        success();
    }

    private void success() {
        setAudio(R.raw.click);
        setBackgroundSuccess(view);
        score += 100;
        index += 1;
        recordTxt.setText(score + "");
        nextNum.setText(getString(R.string.SchulteNextNumber) + " " + index);
        if (number.equals(String.valueOf(numbersList.size()))) {
            currentLevel += 1;
            index = 1;
            nextNum.setText(getString(R.string.SchulteNextNumber) + " " + index);
            setGameLevels(currentLevel);
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
    public void setSize(View view, TextView text) {
        setSizes(view, text,3 + currentLevel);
    }

    private void setSizes(View view, TextView textView, int i) {
        int width = shulteRecycler.getWidth();
        int height = shulteRecycler.getHeight();
        int new_width = 0, new_height = 0;

        switch (i) {
            case 4: case 5: case 6: { new_width = width / i - 4; new_height = height / i - 5; break;}
            case 7: case 8:{new_width = width / i - 3; new_height = height / i - 4; break;}
            case 9: case 10: {new_width = width / i - 2; new_height = height / i - 3; break;}
        }
        view.getLayoutParams().width = new_width;
        view.getLayoutParams().height = new_height;
        sozSizes(textView, width, i);
    }

    private void sozSizes(TextView textView, int width, int i) {
        float size = 0f;
        switch (i) {
            case 4: { size = (float)((width / i) / 6.6);break; }
            case 5: { size = (float)((width / i) / 6.2);break; }
            case 6: { size = (float)((width / i) / 5.8);break; }
            case 7: { size = (float)((width / i) / 5.4);break; }
            case 8: { size = (float)((width / i) / 5.0);break; }
            case 9: { size = (float)((width / i) / 4.7);break; }
            case 10: { size = (float)((width / i) / 4.4);break;}
        }
        textView.setTextSize(size);
    }

    public void setGameLevels(int level) {
        if (level == 1) {
            setRecycler();
        } else {
            setupList(level + 3);
            setAudio(R.raw.level_complete);
        }
    }

    public void setHardLevels(int level) {
        setListWithHandler(level + 3);
    }

    private void setListWithHandler(int i) {
        new Handler().postDelayed(()-> setupList(i), 100);
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
        intent.putExtra("name", name);
        String oldScore = SharedPrefManager.getShulteRecord(getApplication());
        SharedPrefManager.setCoin(getApplication(), SharedPrefManager.getCoin(getApplication()) + result(score));
        if (oldScore != null) {
            if (score > Integer.parseInt(oldScore)) {
                SharedPrefManager.setShulteRecord(getApplication(), String.valueOf(score));
                SharedUpdate.setShulteUpdate(getApplication(), String.valueOf(score));
                intent.putExtra("record", getString(R.string.CongratulationNewRecord));
            }
        } else {
            if (score > 0) {
                SharedPrefManager.setShulteRecord(getApplication(), String.valueOf(score));
                SharedUpdate.setShulteUpdate(getApplication(), String.valueOf(score));
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
