package kz.almaty.boombrains.game_pages.start_page;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.ankushgrover.hourglass.Hourglass;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.game_pages.color_figures.FiguresActivity;
import kz.almaty.boombrains.game_pages.color_words.ColorWordsActivity;
import kz.almaty.boombrains.game_pages.equation.EquationActivity;
import kz.almaty.boombrains.game_pages.find_number.FindNumberActivity;
import kz.almaty.boombrains.game_pages.number_znaki.NumberZnakiActivity;
import kz.almaty.boombrains.game_pages.rem_words.RememberWordsActivity;
import kz.almaty.boombrains.game_pages.shulte_letter.ShulteLetterActivity;
import kz.almaty.boombrains.game_pages.shulte_page.ShulteActivity;
import kz.almaty.boombrains.game_pages.square_memory.SquareMemory;
import kz.almaty.boombrains.game_pages.zapomni_chislo_page.ZapomniChisloActivity;

import static android.view.animation.Animation.INFINITE;
import static android.view.animation.Animation.RELATIVE_TO_SELF;
import static android.view.animation.Animation.REVERSE;

@SuppressLint({"DefaultLocale", "SetTextI18n"})
public class AreYouReadyActivity extends AppCompatActivity {

    @BindView(R.id.mainLayout) ConstraintLayout layout;
    @BindView(R.id.numberStart) TextView numStart;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int position = getIntent().getIntExtra("position", 0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_are_you_ready);
        name = getIntent().getStringExtra("name");

        ButterKnife.bind(this);
        setBackgrounds(position);
        startTimer(2000, numStart, position);
    }

    private void zoomInZoomOut(TextView textView) {
        Animation anim = new ScaleAnimation(0.0f, 1.8f, 0.0f, 1.8f,
                RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(true);
        anim.setDuration(500);
        anim.setRepeatCount(INFINITE);
        anim.setRepeatMode(REVERSE);
        textView.startAnimation(anim);
    }

    @Override
    public void onBackPressed() {

    }

    public void startTimer(int millSec, TextView timeTxt, int position) {
        Hourglass countDownTimer = new Hourglass(millSec, 1000) {

            @Override
            public void onTimerTick(long timeRemaining) {
                int seconds = (int) (timeRemaining / 1000 + 1);
                timeTxt.setText("" + seconds);
                zoomInZoomOut(timeTxt);
            }

            @Override
            public void onTimerFinish() {
                timeTxt.clearAnimation();
                timeTxt.setVisibility(View.INVISIBLE);
                setGames(position);
            }
        };
        countDownTimer.startTimer();
    }

    private void setBackgrounds(int position) {
        switch (position) {
            case 0: { setShulteBackgrounds();break; }
            case 1: { setZapomniChisloBackgrounds();break; }
            case 2: { setFindBackgrounds();break; }
            case 3: { setNumZnakiBackgrounds();break; }
            case 4: { setEquationBackgrounds();break; }
            case 5: { setShulteLetterBackgrounds();break; }
            case 6: { setRemWordBackgrounds();break; }
            case 7: { setSquareBackgrounds();break; }
            case 8: { setColorBackgrounds();break; }
            case 9: { setFigureBackgrounds();break; }
        }
    }

    private void setGames(int position) {
        switch (position) {
            case 0: { startShulteIntent(new Intent(this, ShulteActivity.class), position);break; }
            case 1: { startIntent(new Intent(this, ZapomniChisloActivity.class), position);break; }
            case 2: { startIntent(new Intent(this, FindNumberActivity.class), position);break; }
            case 3: { startIntent(new Intent(this, NumberZnakiActivity.class), position);break; }
            case 4: { startIntent(new Intent(this, EquationActivity.class), position);break; }
            case 5: { startIntent(new Intent(this, ShulteLetterActivity.class), position);break; }
            case 6: { startIntent(new Intent(this, RememberWordsActivity.class), position);break; }
            case 7: { startIntent(new Intent(this, SquareMemory.class), position);break; }
            case 8: { startIntent(new Intent(this, ColorWordsActivity.class), position);break; }
            case 9: { startIntent(new Intent(this, FiguresActivity.class), position);break; }
        }
    }

    private void startShulteIntent(Intent intent, int position) {
        intent.putExtra("position", position);
        intent.putExtra("name", name);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    private void startIntent(Intent intent, int position) {
        intent.putExtra("position", position);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    private void setBackgroundsByType(int back, int color) {
        layout.setBackgroundResource(back);
        numStart.setTextColor(getResources().getColor(color));
    }

    private void setEquationBackgrounds() {
        setBackgroundsByType(R.drawable.equation_back, R.color.underEquation);
    }

    private void setColorBackgrounds() {
        setBackgroundsByType(R.drawable.color_back, R.color.underColor);
    }

    private void setFigureBackgrounds() {
        setBackgroundsByType(R.drawable.shape_back, R.color.underShape);
    }

    private void setSquareBackgrounds() {
        setBackgroundsByType(R.drawable.square_back, R.color.underSquare);
    }

    private void setShulteBackgrounds() {
        setBackgroundsByType(R.drawable.shulte_back, R.color.underShulte);
    }

    private void setShulteLetterBackgrounds() {
        setBackgroundsByType(R.drawable.shulte_letter_back, R.color.shulteLetterColor);
    }

    private void setRemWordBackgrounds() {
        setBackgroundsByType(R.drawable.rem_words_back, R.color.remWordsColor);
    }

    private void setFindBackgrounds() {
        setBackgroundsByType(R.drawable.find_back, R.color.findColor);
    }

    private void setZapomniChisloBackgrounds() {
        setBackgroundsByType(R.drawable.zapomni_slovo_back, R.color.underSlovo);
    }

    private void setNumZnakiBackgrounds() {
        setBackgroundsByType(R.drawable.number_znaki_back, R.color.underNumZnaki);
    }
}
