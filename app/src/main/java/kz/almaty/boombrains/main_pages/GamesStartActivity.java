package kz.almaty.boombrains.main_pages;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.game_pages.equation.EquationActivity;
import kz.almaty.boombrains.game_pages.find_number.FindNumberActivity;
import kz.almaty.boombrains.game_pages.number_znaki.NumberZnakiActivity;
import kz.almaty.boombrains.game_pages.shulte_page.ShulteActivity;
import kz.almaty.boombrains.game_pages.zapomni_chislo_page.ZapomniChisloActivity;
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

    String gameName;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_start);
        ButterKnife.bind(this);

        gameName = getIntent().getStringExtra("gameName");
        position = getIntent().getIntExtra("position", 0);

        nameOfGame.setText(gameName);

        setBackgrounds(position);

        backToMain.setOnClickListener(v -> onBackPressed());

        kakIgratBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), HowToPlayActivity.class);
            intent.putExtra("kakName", gameName);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        playBtn.setOnClickListener(v -> setGames(position));
    }

    private void setBackgrounds(int position) {
        switch (position) {
            case 0: {
                setZapomniChisloBackgrounds();
                break;
            }
            case 1: {
                setShulteBackgrounds();
                break;
            }
            case 2: {
                setFindBackgrounds();
                break;
            }
            case 3: {
                setNumZnakiBackgrounds();
                break;
            }
            case 4: {
                setEquationBackgrounds();
                break;
            }
        }
    }

    private void setGames(int position) {
        switch (position) {
            case 0: {
                startIntent(new Intent(this, ZapomniChisloActivity.class), position);
                break;
            }
            case 1: {
                startIntent(new Intent(this, ShulteActivity.class), position);
                break;
            }
            case 2: {
                startIntent(new Intent(this, FindNumberActivity.class), position);
                break;
            }
            case 3: {
                startIntent(new Intent(this, NumberZnakiActivity.class), position);
                break;
            }
            case 4: {
                startIntent(new Intent(this, EquationActivity.class), position);
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
    }

    private void setEquationBackgrounds() {
        setBackgroundsByType(R.drawable.equation_top_icon, R.color.equationColor, R.drawable.equation_kubok_grad, R.color.bottomEquation, R.drawable.equation_back);
        setRecords(SharedPrefManager.getEquationRecord(getApplication()));
    }

    private void setShulteBackgrounds() {
        setBackgroundsByType(R.drawable.shult_top_icon, R.color.vnimanieColor, R.drawable.shulte_kubok_grad, R.color.bottomShulte, R.drawable.shulte_back);
        setRecords(SharedPrefManager.getShulteRecord(getApplication()));
    }

    private void setFindBackgrounds() {
        setBackgroundsByType(R.drawable.find_top_back, R.color.findColor, R.drawable.find_kubok_grad, R.color.bottomFind, R.drawable.find_back);
        setRecords(SharedPrefManager.getFindRecord(getApplication()));
    }

    private void setZapomniChisloBackgrounds() {
        setBackgroundsByType(R.drawable.zapomni_chislo_top_icon, R.color.pamiatColor, R.drawable.zapomni_kubok_grad, R.color.bottomZapomni, R.drawable.zapomni_slovo_back);
        setRecords(SharedPrefManager.getChisloRecord(getApplication()));
    }

    private void setNumZnakiBackgrounds() {
        setBackgroundsByType(R.drawable.number_znaki_top_icon, R.color.numZnakiColor, R.drawable.number_znaki_kubok_grad, R.color.bottomNumZnak, R.drawable.number_znaki_back);
        setRecords(SharedPrefManager.getNumZnakiRecord(getApplication()));
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
