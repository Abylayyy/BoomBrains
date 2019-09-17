package kz.almaty.boombrains.game_pages.equation;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.helpers.DialogHelperActivity;
import kz.almaty.boombrains.helpers.SharedPrefManager;
import kz.almaty.boombrains.main_pages.FinishedActivity;

public class EquationActivity extends DialogHelperActivity {

    @BindView(R.id.shulteRecord) TextView recordTxt;
    @BindView(R.id.slovo_teksts) TextView randomNumberTxt;
    @BindView(R.id.shulteTime) TextView timeTxt;
    @BindView(R.id.pauseBtn) ConstraintLayout pauseImg;
    @BindView(R.id.equationRecycler) RecyclerView findRecycler;
    @BindView(R.id.findNumContainer) ConstraintLayout container;
    @BindView(R.id.nextNumShulte2) TextView levelTxt;

    private int position;
    private int score = 0, errors = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equation);
        ButterKnife.bind(this);
        setupDialog(this, R.style.equationTheme, R.drawable.pause_equation);
        startTimer(10000, timeTxt);

        position = getIntent().getIntExtra("position", 0);
        pauseImg.setOnClickListener(v -> showPauseDialog());
        setupRecycler();

        container.getLayoutParams().height = height() / 5;
    }

    private int height() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    private void setupRecycler() {

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
        intent.putExtra("equationScore", score);
        intent.putExtra("equationErrors", errors);

        String oldScore = SharedPrefManager.getEquationRecord(getApplication());
        if (oldScore != null) {
            if (score > Integer.parseInt(oldScore)) {
                SharedPrefManager.setEquationRecord(getApplication(), String.valueOf(score));
                intent.putExtra("equationRecord", getString(R.string.CongratulationNewRecord));
            }
        } else {
            if (score > 0) {
                SharedPrefManager.setEquationRecord(getApplication(), String.valueOf(score));
                intent.putExtra("equationRecord", getString(R.string.CongratulationNewRecord));
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
}
