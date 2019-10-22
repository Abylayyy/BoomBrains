package kz.almaty.boombrains.game_pages.find_number;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
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
import kz.almaty.boombrains.helpers.DialogHelperActivity;
import kz.almaty.boombrains.helpers.SharedPrefManager;
import kz.almaty.boombrains.main_pages.FinishedActivity;
import static android.view.animation.Animation.INFINITE;
import static android.view.animation.Animation.RELATIVE_TO_SELF;
import static android.view.animation.Animation.REVERSE;

@SuppressLint("SetTextI18n")
public class FindNumberActivity extends DialogHelperActivity implements FindNumberAdapter.FindListener {

    @BindView(R.id.shulteRecord) TextView recordTxt;
    @BindView(R.id.slovo_teksts) TextView randomNumberTxt;
    @BindView(R.id.shulteTime) TextView timeTxt;
    @BindView(R.id.pauseBtn) ConstraintLayout pauseImg;
    @BindView(R.id.findRecycler) RecyclerView findRecycler;
    @BindView(R.id.findNumContainer) ConstraintLayout container;

    int position;
    List<Integer> numbersList = new ArrayList<>();
    FindNumberAdapter adapter;
    int currentLevel = 1;
    int score = 0;
    private int errors = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_number);
        ButterKnife.bind(this);
        position = getIntent().getIntExtra("position", 0);
        setupDialog(this, R.style.findTheme, R.drawable.pause_find, position, "");
        startTimer(60000, timeTxt);
        setCount();
        loadGoogleAd();
        pauseImg.setOnClickListener(v -> showPauseDialog());
        setupRecycler();

        container.getLayoutParams().height = height() / 4;
    }

    private int height() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    private int getRandom() {
        return new Random().nextInt(1000 - 100) + 100;
    }

    private void setupRecycler() {
        numbersList.clear();
        for (int i = 1; i <= 4; i++) {
            numbersList.add(getRandom());
        }
        Collections.shuffle(numbersList);
        adapter = new FindNumberAdapter(numbersList, this, this);
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        findRecycler.setLayoutManager(manager);
        findRecycler.setItemAnimator(new DefaultItemAnimator());
        findRecycler.setAdapter(adapter);
        randomNumberTxt.setText(String.valueOf(numbersList.get(new Random().nextInt(numbersList.size()))));
    }

    private void setupBySize(int size) {
        numbersList.clear();
        for (int i = 1; i <= size; i++) {
            numbersList.add(getRandom());
        }
        Collections.shuffle(numbersList);
        adapter = new FindNumberAdapter(numbersList, this, this);
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        findRecycler.setLayoutManager(manager);
        findRecycler.setItemAnimator(new DefaultItemAnimator());
        findRecycler.setAdapter(adapter);
        randomNumberTxt.setText(String.valueOf(numbersList.get(new Random().nextInt(numbersList.size()))));
    }

    private void setSizes(ConstraintLayout view) {
        int width = findRecycler.getWidth();
        int height = findRecycler.getHeight();
        view.getLayoutParams().width = width / 4 - 10 ;
        view.getLayoutParams().height = height / 5 - 13;
    }

    private void setRecyclerByLevel(int currentLevel) {
        switch (currentLevel) {
            case 1: case 2: case 3: case 4: case 5: case 6: case 7: {
                setupBySize(4);
                break;
            }
            case 8: case 9: case 10: case 11: case 12: case 13: case 14: {
                setupBySize(12);
                break;
            }
            default: {
                setupBySize(20);
                break;
            }
        }
    }

    private void setAnimationByLevel(ConstraintLayout layout, TextView text) {
        switch (currentLevel) {
            case 1: case 8: case 15: case 22: case 29: case 36: case 43: case 50: case 57: case 64: case 71: case 78:{
                zoomInZoomOut(text);
                break;
            }
            case 2: case 9: case 16: case 23: case 30: case 37: case 44: case 51: case 58: case 65: case 72: case 79:{
                setRandomColor(text, layout);
                break;
            }
            case 3: case 10: case 17: case 24: case 31: case 38: case 45: case 52: case 59: case 66: case 73: case 80:{
                setRotations90(text);
                break;
            }
            case 4: case 11: case 18: case 25: case 32: case 39: case 46: case 53: case 60: case 67: case 74: case 81:{
                setRotations180(text);
                break;
            }
            case 5: case 12: case 19: case 26: case 33: case 40: case 47: case 54: case 61: case 68: case 75: case 82:{
                setRotate(text);
                break;
            }
            case 6: case 13: case 20: case 27: case 34: case 41: case 48: case 55: case 62: case 69: case 76: case 83:{
                setAllRandomRotations(text);
                break;
            }
            case 7: case 14: case 21: case 28: case 35: case 42: case 49: case 56: case 63: case 70: case 77: case 84:{
                setAllRandomRotationColors(text, layout);
                break;
            }

        }
    }

    private void zoomInZoomOut(TextView textView) {
        Animation anim = new ScaleAnimation(1f, 0.5f, 1f, 0.5f,
                RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(true);
        anim.setDuration(500);
        anim.setRepeatCount(INFINITE);
        anim.setRepeatMode(REVERSE);
        textView.startAnimation(anim);
    }

    private void setRandomColor(TextView textView, ConstraintLayout layout) {
        List<Integer> list = new ArrayList<>(Arrays.asList(R.drawable.find_item_black, R.drawable.find_item_red));
        layout.setBackgroundResource(list.get(new Random().nextInt(list.size())));
        textView.setTextColor(Color.WHITE);
    }

    private void setRotate(TextView textView) {
        RotateAnimation animation = new RotateAnimation(-45f, 45f,
                RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(300);
        animation.setRepeatCount(INFINITE);
        animation.setFillAfter(true);
        animation.setRepeatMode(REVERSE);
        textView.startAnimation(animation);
    }

    private void setRotations90(TextView textView) {
        List<Float> rotations = new ArrayList<>(Arrays.asList(-90f, 90f));
        textView.setRotation(rotations.get(new Random().nextInt(rotations.size())));
    }

    private void setAllRandomRotations(TextView textView) {
        List<Float> rotations = new ArrayList<>(Arrays.asList(-90f, 90f, 180f, 0f));
        textView.setRotation(rotations.get(new Random().nextInt(rotations.size())));
    }

    private void setAllRandomRotationColors(TextView textView, ConstraintLayout layout) {
        List<Float> rotations = new ArrayList<>(Arrays.asList(-90f, 90f, 180f, 0f));
        textView.setRotation(rotations.get(new Random().nextInt(rotations.size())));

        List<Integer> list = new ArrayList<>(Arrays.asList(R.drawable.find_item_black, R.drawable.find_item_red));
        layout.setBackgroundResource(list.get(new Random().nextInt(list.size())));
        textView.setTextColor(Color.WHITE);
    }

    private void setRotations180(TextView textView) {
        textView.setRotation(180f);
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
        intent.putExtra("findScore", score);
        intent.putExtra("findErrors", errors);

        String oldScore = SharedPrefManager.getFindRecord(getApplication());
        if (oldScore != null) {
            if (score > Integer.parseInt(oldScore)) {
                SharedPrefManager.setFindRecord(getApplication(), String.valueOf(score));
                intent.putExtra("findRecord", getString(R.string.CongratulationNewRecord));
            }
        } else {
            if (score > 0) {
                SharedPrefManager.setFindRecord(getApplication(), String.valueOf(score));
                intent.putExtra("findRecord", getString(R.string.CongratulationNewRecord));
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

    @Override
    public void getItem(ConstraintLayout layout, TextView text) {
        setSizes(layout);
        setAnimationByLevel(layout, text);
    }

    @Override
    public void onItemSelected(ConstraintLayout layout, TextView numTxt, String value) {
        setBackByName(layout, numTxt, value);
    }

    private void setBackByName(ConstraintLayout layout, TextView numTxt, String value) {
        if (value.equals(randomNumberTxt.getText().toString())) {

            randomNumberTxt.setTextColor(getResources().getColor(R.color.successFind));
            numTxt.setTextColor(Color.WHITE);
            layout.setBackgroundResource(R.drawable.find_success);

            new Handler().postDelayed(()-> {
                setAudio(R.raw.click);
                randomNumberTxt.setTextColor(Color.WHITE);
                score += 100;
                currentLevel += 1;
                recordTxt.setText("" + score);
                setRecyclerByLevel(currentLevel);
            },100);
        } else {

            randomNumberTxt.setTextColor(getResources().getColor(R.color.errorColor));
            numTxt.setTextColor(Color.WHITE);
            layout.setBackgroundResource(R.drawable.find_error);

            new Handler().postDelayed(()-> {
                setAudio(R.raw.wrong_clicked);
                vibrate(100);
                randomNumberTxt.setTextColor(Color.WHITE);
                if (score > 0) {
                    score -= 50;
                }
                errors += 1;
                currentLevel += 1;
                setRecyclerByLevel(currentLevel);
                recordTxt.setText("" + score);
            },100);
        }
    }
}
