package kz.almaty.boombrains.ui.game_pages.color_words;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import butterknife.BindView;
import butterknife.ButterKnife;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.util.helpers.dialog_helper.DialogHelperActivity;
import kz.almaty.boombrains.util.helpers.preference.SharedPrefManager;
import kz.almaty.boombrains.util.helpers.preference.SharedUpdate;
import kz.almaty.boombrains.ui.main_pages.FinishedActivity;
import kz.almaty.boombrains.data.models.game_models.ColorModel;

@SuppressLint("SetTextI18n")
public class ColorWordsActivity extends DialogHelperActivity implements ColorsAdapter.ColorsListener {

    private ColorsAdapter adapter;
    private List<ColorModel> numbersList;
    private List<String> colorNames = new ArrayList<>();
    private List<Integer> colors = new ArrayList<>();
    private HashMap<String, Integer> colorMap = new HashMap<>();
    private int position;

    @BindView(R.id.shulteRecord) TextView recordTxt;
    @BindView(R.id.shulteTime) TextView timeTxt;
    @BindView(R.id.nextNumShulte) TextView nextNum;
    @BindView(R.id.pauseBtn) ConstraintLayout pauseImg;
    @BindView(R.id.shulteRecycler) RecyclerView shulteRecycler;
    @BindView(R.id.remConst) ConstraintLayout background;
    @BindView(R.id.slovo_teksts) TextView slovo;
    @BindView(R.id.wordConst) ConstraintLayout wordLayout;
    @BindView(R.id.resImg) ImageView resultImg;

    @BindView(R.id.life1) ImageView life1;
    @BindView(R.id.life2) ImageView life2;
    @BindView(R.id.life3) ImageView life3;

    private int lifes = 3;
    private boolean lifeUsed = false;

    private int currentLevel = 1;
    private int score = 0;
    private int selectedColor;
    private int errors = 0;

    public ColorWordsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_words);
        ButterKnife.bind(this);
        position = getIntent().getIntExtra("position", 0);

        setupDialog(this, R.style.colorTheme, R.drawable.pause_color, position, "");
        startTimer(60000, timeTxt);
        setCount();
        loadGoogleAd();

        setupLifeDialog(this, R.color.topColor);
        loadAddForLife();

        initMap();

        colorNames = new ArrayList<>(Arrays.asList(
                getString(R.string.ColorRed), getString(R.string.ColorBlue), getString(R.string.ColorOrange),
                getString(R.string.ColorBlack), getString(R.string.ColorBrown), getString(R.string.ColorGreen),
                getString(R.string.ColorPink), getString(R.string.ColorViolet)
        ));

        colors = new ArrayList<>(Arrays.asList(
                R.color.redColor, R.color.blueColor, R.color.blackColor, R.color.orangeColor,
                R.color.brownColor, R.color.pinkColor, R.color.violetColor, R.color.greenColor
        ));

        numbersList = new ArrayList<>();
        wordLayout.getLayoutParams().height = height() / 4;
        setRecyclerItem();

        pauseImg.setOnClickListener(v -> showPauseDialog());
        nextNum.setText(getString(R.string.Level) + " " + currentLevel);
    }

    private int height() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    private void setRecyclerItem() {
        slovo.setText(colorNames.get(new Random().nextInt(colorNames.size())));
        selectedColor = colors.get(new Random().nextInt(colors.size()));
        slovo.setTextColor(getResources().getColor(selectedColor));

        Collections.shuffle(colors);
        Collections.shuffle(colorNames);

        numbersList.clear();

        for (int i = 0; i < colors.size(); i++) {
            numbersList.add(new ColorModel(colors.get(i), colorNames.get(i)));
        }
        adapter = new ColorsAdapter(numbersList, this, this);
        shulteRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        shulteRecycler.setItemAnimator(new DefaultItemAnimator());
        shulteRecycler.setAdapter(adapter);
    }

    @Override
    public void setSize(View view) {
        setSizes(view);
    }

    @Override
    public void getSlovo(View view, TextView textview, String soz) {
        if (colorMap.get(soz) == selectedColor) {
            setSuccess();
            success(view, textview);
        } else {
            setErrorClicked();
            error(view, textview);
        }
    }

    private void success(View view, TextView text) {
        setAudio(R.raw.level_complete);
        view.setBackgroundResource(R.drawable.square_success);
        text.setTextColor(Color.WHITE);
        showAndHide(true);
    }

    private void error(View view, TextView text) {
        setAudio(R.raw.wrong_clicked);
        vibrate(100);
        view.setBackgroundResource(R.drawable.square_error);
        text.setTextColor(Color.WHITE);
        showAndHide(false);
    }

    private void showAndHide(boolean check) {
        if (check) {setMeasures(resultImg, R.drawable.true_icon);}
        else {setMeasures(resultImg, R.drawable.false_icon);}

        slovo.setVisibility(View.INVISIBLE);
        resultImg.setVisibility(View.VISIBLE);

        new Handler().postDelayed(()->{
            slovo.setVisibility(View.VISIBLE);
            resultImg.setVisibility(View.INVISIBLE);
        },200);
    }

    private void setMeasures(ImageView image, int resource) {
        image.setImageResource(resource);
        Drawable drawable = getResources().getDrawable(resource);
        int width = drawable.getIntrinsicWidth() / 2 - 10;
        int height = drawable.getIntrinsicHeight() / 2 - 10;
        image.getLayoutParams().width = width;
        image.getLayoutParams().height = height;
    }

    private void setErrorClicked() {
        new Handler().postDelayed(()-> {
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
            recordTxt.setText(""+score);
            setRecyclerItem();
        },200);
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

    private void setSuccess() {
        new Handler().postDelayed(()-> {
            currentLevel += 1;
            score += 100;
            recordTxt.setText(""+score);
            nextNum.setText(getString(R.string.Level) + " " + currentLevel);
            setRecyclerItem();
        },200);
    }

    @Override
    public void onBackPressed() {
        if (!isPaused()) {
            showPauseDialog();
        }
    }

    private Intent myIntent() {
        Intent intent = new Intent(getApplication(), FinishedActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("score", score);
        intent.putExtra("errors", errors);
        String oldScore = SharedPrefManager.getColorRecord(getApplication());

        SharedPrefManager.setCoin(getApplication(), SharedPrefManager.getCoin(getApplication()) + result(score));
        if (oldScore != null) {
            if (score > Integer.parseInt(oldScore)) {
                SharedPrefManager.setColorRecord(getApplication(), String.valueOf(score));
                SharedUpdate.setColorUpdate(getApplication(), String.valueOf(score));
                intent.putExtra("record", getString(R.string.CongratulationNewRecord));
            }
        } else {
            if (score > 0) {
                SharedPrefManager.setColorRecord(getApplication(), String.valueOf(score));
                SharedUpdate.setColorUpdate(getApplication(), String.valueOf(score));
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

    @Override
    public void onStop() {
        super.onStop();
        if (!isPaused()) {
            showPauseDialog();
        }
    }

    private void setSizes(View view) {
        int width = shulteRecycler.getWidth();
        int height = shulteRecycler.getHeight();
        view.getLayoutParams().width = width / 2 - 12;
        view.getLayoutParams().height = height / 4 - 10;
    }

    private void initMap() {
        colorMap.put(getString(R.string.ColorRed), R.color.redColor);
        colorMap.put(getString(R.string.ColorBlue), R.color.blueColor);
        colorMap.put(getString(R.string.ColorBlack), R.color.blackColor);
        colorMap.put(getString(R.string.ColorOrange), R.color.orangeColor);
        colorMap.put(getString(R.string.ColorBrown), R.color.brownColor);
        colorMap.put(getString(R.string.ColorPink), R.color.pinkColor);
        colorMap.put(getString(R.string.ColorViolet), R.color.violetColor);
        colorMap.put(getString(R.string.ColorGreen), R.color.greenColor);
    }
}
