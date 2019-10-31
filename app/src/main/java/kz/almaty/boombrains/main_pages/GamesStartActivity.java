package kz.almaty.boombrains.main_pages;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.game_pages.shulte_page.ShulteLevel;
import kz.almaty.boombrains.game_pages.start_page.AreYouReadyActivity;
import kz.almaty.boombrains.helpers.SharedPrefManager;

public class GamesStartActivity extends AppCompatActivity {

    @BindView(R.id.name_of_game) TextView nameOfGame;
    @BindView(R.id.gameRecordTxt) TextView gameRecord;
    @BindView(R.id.playBtn) Button playBtn;
    @BindView(R.id.game_image) ImageView gameImage;
    @BindView(R.id.startBackConst) ConstraintLayout backConst;
    @BindView(R.id.kubok_grad_id) ConstraintLayout recordLayout;
    @BindView(R.id.bottomLayerConst) ConstraintLayout bottomLayer;
    @BindView(R.id.back_to_main) RelativeLayout backToMain;
    @BindView(R.id.kakIgratConst) ConstraintLayout kakIgratBtn;
    @BindView(R.id.scrollStart) ScrollView scroll;

    String gameName;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        position = getIntent().getIntExtra("position", 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_start);
        ButterKnife.bind(this);

        gameName = getIntent().getStringExtra("gameName");
        nameOfGame.setText(gameName);
        setBackgrounds(position);

        backToMain.setOnClickListener(v -> onBackPressed());

        playBtn.setOnClickListener(v -> {
            setGames(position);
        });

        kakIgratBtn.setOnClickListener(v -> {
            startIntent(new Intent(getApplication(), HowToPlayActivity.class), position);
            overridePendingTransition(0,0);
        });
    }

    private void setBackgrounds(int position) {
        switch (position) {
            case 0: { setShulteBackgrounds();break; }
            case 1: { setZapomniChisloBackgrounds();break; }
            case 2: { setFindBackgrounds();break; }
            case 3: { setNumZnakiBackgrounds();break; }
            case 4: { setEquationBackgrounds();break; }
            case 5: { setShulteLetterBackgrounds();break; }
            case 6: { setRemWordsBackgrounds();break; }
            case 7: { setSquareBackgrounds();break; }
            case 8: { setColorBackgrounds();break; }
            case 9: { setFigureBackgrounds();break; }
        }
    }

    private void setGames(int position) {
        switch (position) {
            case 0: {
                startIntent(new Intent(this, ShulteLevel.class), position);
                break;
            }
            case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: {
                startIntent(new Intent(this, AreYouReadyActivity.class), position);
                break;
            }
        }
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    private void startIntent(Intent intent, int position) {
        intent.putExtra("position", position);
        startActivity(intent);
    }

    private void setBackgroundsByType(int game, int play, int kubok, int bottom, int back) {
        playBtn.setTextColor(getResources().getColor(play));
        gameImage.setImageResource(game);
        recordLayout.setBackgroundResource(kubok);
        bottomLayer.setBackgroundResource(bottom);
        backConst.setBackgroundResource(back);
        scroll.setBackgroundResource(bottom);
    }

    private void setFigureBackgrounds() {
        setBackgroundsByType(R.drawable.shape_top_icon, R.color.topShape, R.drawable.figure_kubok_grad, R.color.underShape,
                R.drawable.shape_back);
        setRecords(SharedPrefManager.getFigureRecord(getApplication()));
    }

    private void setColorBackgrounds() {
        setBackgroundsByType(R.drawable.color_top_icon, R.color.topColor, R.drawable.color_kubok_grad, R.color.underColor,
                R.drawable.color_back);
        setRecords(SharedPrefManager.getColorRecord(getApplication()));
    }

    private void setEquationBackgrounds() {
        setBackgroundsByType(R.drawable.equation_top_icon, R.color.equationColor, R.drawable.equation_kubok_grad, R.color.underEquation,
                R.drawable.equation_back);
        setRecords(SharedPrefManager.getEquationRecord(getApplication()));
    }

    private void setShulteBackgrounds() {
        setBackgroundsByType(R.drawable.shult_top_icon, R.color.vnimanieColor, R.drawable.shulte_kubok_grad, R.color.underShulte,
                R.drawable.shulte_back);
        setRecords(SharedPrefManager.getShulteRecord(getApplication()));
    }

    private void setFindBackgrounds() {
        setBackgroundsByType(R.drawable.find_top_back, R.color.findColor, R.drawable.find_kubok_grad, R.color.underFind,
                R.drawable.find_back);
        setRecords(SharedPrefManager.getFindRecord(getApplication()));
    }

    private void setZapomniChisloBackgrounds() {
        setBackgroundsByType(R.drawable.zapomni_chislo_top_icon, R.color.pamiatColor, R.drawable.zapomni_kubok_grad, R.color.underSlovo,
                R.drawable.zapomni_slovo_back);
        setRecords(SharedPrefManager.getChisloRecord(getApplication()));
    }

    private void setNumZnakiBackgrounds() {
        setBackgroundsByType(R.drawable.number_znaki_top_icon, R.color.numZnakiColor, R.drawable.num_znaki_kubok_grad, R.color.underNumZnaki,
                R.drawable.number_znaki_back);
        setRecords(SharedPrefManager.getNumZnakiRecord(getApplication()));
    }

    private void setShulteLetterBackgrounds() {
        setBackgroundsByType(R.drawable.shulte_letter_top_icon, R.color.shulteLetterColor, R.drawable.shulte_letter_kubok_grad, R.color.underShulteLetter,
                R.drawable.shulte_letter_back);
        setRecords(SharedPrefManager.getShulteLetterRecord(getApplication()));
    }

    private void setRemWordsBackgrounds() {
        setBackgroundsByType(R.drawable.rem_words_top_icon, R.color.remWordsColor, R.drawable.rem_words_kubok_grad, R.color.underRemWords,
                R.drawable.rem_words_back);
        setRecords(SharedPrefManager.getSlovoRecord(getApplication()));
    }

    private void setSquareBackgrounds() {
        setBackgroundsByType(R.drawable.square_top_icon, R.color.topSquare, R.drawable.square_kubok_grad, R.color.underSquare,
                R.drawable.square_back);
        setRecords(SharedPrefManager.getSquareRecord(getApplication()));
    }

    @SuppressLint("SetTextI18n")
    private void setRecords(String record) {
        if (record != null) {
            gameRecord.setText(record);
        } else {
            gameRecord.setText("0");
        }
    }
}
