package kz.almaty.boombrains.game_pages.square_memory;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import butterknife.BindView;
import butterknife.ButterKnife;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.helpers.DialogHelperActivity;
import kz.almaty.boombrains.helpers.SharedPrefManager;
import kz.almaty.boombrains.main_pages.FinishedActivity;
import kz.almaty.boombrains.models.game_models.SquareModel;

@SuppressLint("SetTextI18n")
public class SquareMemory extends DialogHelperActivity implements SquareAdapter.SquareListener {

    @BindView(R.id.containerSquare) ConstraintLayout container;
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

    SquareAdapter adapter;
    List<SquareModel> numbersList;
    int currentLevel = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square_memory);
        ButterKnife.bind(this);
        position = getIntent().getIntExtra("position", 0);
        setupDialog(this, R.style.squareTheme, R.drawable.pause_square, position, "");
        startTimer(90000, timeTxt);
        setCount();
        loadGoogleAd();

        pauseImg.setOnClickListener(v -> showPauseDialog());
        container.getLayoutParams().height = width();

        numbersList = new ArrayList<>();
        nextNum.setText(getString(R.string.Level) + " " + currentLevel);
        setRecycler(1);
    }

    private void setRecycler(int selected) {
        numbersList.clear();
        for (int i = 1; i <= 36; i++) {
            numbersList.add(new SquareModel(String.valueOf(i)));
        }

        for (int i = 0; i < selected; i++) {
            numbersList.get(new Random().nextInt(numbersList.size())).setSelected(true);
        }

        adapter = new SquareAdapter(numbersList, this, this);
        shulteRecycler.setAdapter(adapter);
        shulteRecycler.setLayoutManager(new GridLayoutManager(this, 6));
        shulteRecycler.setItemAnimator(new DefaultItemAnimator());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateNumbers(View view, int position) {
        if (!isPaused()) {
            view.setEnabled(false);
            List<Integer> selectedList = adapter.selectedItems();
            if (selectedList.contains(position)) {
                count += 1;
                setSuccessColor(view);

                new Handler().postDelayed(this::setCorrectResults,400);

                new Handler().postDelayed(()-> {
                    if (count == selectedList.size()) {
                        currentLevel += 1;
                        nextNum.setText(getString(R.string.Level) + " " + currentLevel);
                        count = 0; setGameLevels();
                    }
                },550);
            } else {
                setErrorColor(view);
                new Handler().postDelayed(()-> {
                    count = 0; setErrors(); setGameLevels();
                }, 550);
            }
        }
    }

    private void setCorrectResults() {
        setAudio(R.raw.click);
        score += 100;
        recordTxt.setText("" + score);
    }

    private void setErrors() {
        setAudio(R.raw.wrong_clicked);
        vibrate(100);
        if (score > 0) {
            score -= 100;
        }
        errors += 1;
        recordTxt.setText("" + score);
    }


    private void setGameLevels() {
        switch (currentLevel) {
            case 1: { setRecycler(1);break; } case 2: {setRecycler(2);break;}
            case 3: { setRecycler(3);break; } case 4: {setRecycler(4);break;}
            case 5: { setRecycler(5);break; } case 6: {setRecycler(6);break;}
            case 7: { setRecycler(7);break; } case 8: {setRecycler(8);break;}
            case 9: { setRecycler(9);break; } case 10: {setRecycler(10);break;}
            case 11: { setRecycler(11);break; } case 12: {setRecycler(12);break;}
            case 13: { setRecycler(13);break; } case 14: {setRecycler(14);break;}
            case 15: { setRecycler(15);break; } case 16: {setRecycler(16);break;}
            case 17: { setRecycler(17);break; } case 18: {setRecycler(18);break;}
            case 19: { setRecycler(19);break; } default: {setRecycler(20);break;}
        }
    }

    private void setErrorColor(View view) {
        flipView(view);
        new Handler().postDelayed(()-> view.setBackgroundResource(R.drawable.square_error), 400);
    }

    private void setSuccessColor(View view) {
        flipView(view);
        new Handler().postDelayed(()-> view.setBackgroundResource(R.drawable.square_success), 400);
    }

    @Override
    public void setSize(View view) {
        setSizes(view);
    }

    @Override
    public void setSelected(View view) {
        showAndHide(view);
    }

    private void showAndHide(View view) {
        pauseTimer();
        flipView(view);
        new Handler().postDelayed(()-> view.setBackgroundResource(R.drawable.square_selected),400);
        new Handler().postDelayed(()-> flipReverse(view),2500);
        new Handler().postDelayed(()-> view.setBackgroundResource(R.drawable.back_shulte_item), 2900);
        new Handler().postDelayed(this::resumeTimer, 3000);
    }

    private void flipView(View view) {
        ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.flipping);
        anim.setTarget(view);
        anim.setDuration(500);
        anim.start();
    }

    private void flipReverse(View view) {
        ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.flipping_reverse);
        anim.setTarget(view);
        anim.setDuration(500);
        anim.start();
    }

    private void setSizes(View view) {
        int width = shulteRecycler.getWidth();
        int height = shulteRecycler.getHeight();
        view.getLayoutParams().width = width / 6 - 3;
        view.getLayoutParams().height = height / 6 - 4;
    }

    private int width() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
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
        String oldScore = SharedPrefManager.getSquareRecord(getApplication());
        if (oldScore != null) {
            if (score > Integer.parseInt(oldScore)) {
                SharedPrefManager.setSquareRecord(getApplication(), String.valueOf(score));
                intent.putExtra("record", getString(R.string.CongratulationNewRecord));
            }
        } else {
            if (score > 0) {
                SharedPrefManager.setSquareRecord(getApplication(), String.valueOf(score));
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
