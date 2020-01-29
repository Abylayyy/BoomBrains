package kz.almaty.boombrains.ui.game_pages.square_memory;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
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
import kz.almaty.boombrains.util.helpers.DialogHelperActivity;
import kz.almaty.boombrains.util.helpers.SharedPrefManager;
import kz.almaty.boombrains.util.helpers.SharedUpdate;
import kz.almaty.boombrains.ui.main_pages.FinishedActivity;
import kz.almaty.boombrains.data.models.game_models.SquareModel;

@SuppressLint("SetTextI18n")
public class SquareMemory extends DialogHelperActivity implements SquareAdapter.SquareListener {

    @BindView(R.id.containerSquare) ConstraintLayout container;
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

    private int position;
    private int score = 0;
    private int count = 0;
    private int errors = 0;

    private SquareAdapter adapter;
    private List<SquareModel> numbersList;
    private int currentLevel = 1;
    private boolean lifeUsed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square_memory);
        ButterKnife.bind(this);
        position = getIntent().getIntExtra("position", 0);
        setupDialog(this, R.style.squareTheme, R.drawable.pause_square, position, "");
        setupLifeDialog(this, R.color.topSquare);
        startTimer(15000, timeTxt);
        setCount();
        loadGoogleAd();
        loadAddForLife();

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
                        count = 0;
                        if (lifes > 0) {
                            setGameLevels();
                            startNewQuestion();
                        }
                    }
                },600);
            } else {
                setErrorColor(view);
                new Handler().postDelayed(()-> {
                    count = 0; setErrors();
                    if (lifes > 0) {
                        setGameLevels();
                        startNewQuestion();
                    }
                }, 600);
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

    private void lifeRemained(int i) {
        ImageView[] lifes = {life1, life2, life3};
        if (i >= 0) {
            lifes[i].setImageResource(R.drawable.life_border);
        }
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
        setGameLevels();
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
        new Handler().postDelayed(()-> view.setBackgroundResource(R.drawable.square_error), 200);
    }

    private void setSuccessColor(View view) {
        flipView(view);
        new Handler().postDelayed(()-> view.setBackgroundResource(R.drawable.square_success), 200);
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
        pauseImg.setEnabled(false);
        pauseTimer();
        flipView(view);
        new Handler().postDelayed(()-> view.setBackgroundResource(R.drawable.square_selected),400);
        new Handler().postDelayed(()-> flipReverse(view),2500);
        new Handler().postDelayed(()-> view.setBackgroundResource(R.drawable.back_shulte_item), 2900);
        new Handler().postDelayed(()-> {
            pauseImg.setEnabled(true);
            resumeTimer();
        },3000);
    }

    private void flipView(View view) {
        ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.flipping);
        anim.setTarget(view);
        anim.setDuration(300);
        anim.start();
    }

    private void flipReverse(View view) {
        ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.flipping_reverse);
        anim.setTarget(view);
        anim.setDuration(300);
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

    private Intent myIntent() {
        Intent intent = new Intent(getApplication(), FinishedActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("score", score);
        intent.putExtra("errors", errors);
        String oldScore = SharedPrefManager.getSquareRecord(getApplication());
        SharedPrefManager.setCoin(getApplication(), SharedPrefManager.getCoin(getApplication()) + result(score));
        if (oldScore != null) {
            if (score > Integer.parseInt(oldScore)) {
                SharedPrefManager.setSquareRecord(getApplication(), String.valueOf(score));
                SharedUpdate.setSquareUpdate(getApplication(), String.valueOf(score));
                intent.putExtra("record", getString(R.string.CongratulationNewRecord));
            }
        } else {
            if (score > 0) {
                SharedPrefManager.setSquareRecord(getApplication(), String.valueOf(score));
                SharedUpdate.setSquareUpdate(getApplication(), String.valueOf(score));
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
