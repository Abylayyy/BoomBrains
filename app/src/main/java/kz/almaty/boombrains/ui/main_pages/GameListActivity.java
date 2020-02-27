package kz.almaty.boombrains.ui.main_pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.data.models.main_models.SubGames;
import kz.almaty.boombrains.ui.adapters.SubGamesAdapter;
import kz.almaty.boombrains.util.helpers.preference.SharedPrefManager;
import kz.almaty.boombrains.util.helpers.preference.SharedUpdate;

public class GameListActivity extends AppCompatActivity implements SubGamesAdapter.SubGameListener {

    @BindView(R.id.underRecycler) RecyclerView gameRecycler;
    @BindView(R.id.back_to_main) RelativeLayout back;

    private String shulteRecord = "0", zapomniChisloRecord = "0", findNumRecord = "0", numZnakiRecord = "0",
            equationRecord = "0", shulteLetterRecord = "0", remWordsRecord = "0", squareRecord = "0",
            colorRecord = "0", figureRecord = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
        ButterKnife.bind(this);

        loadRecords();
        loadLocale();

        back.setOnClickListener(v -> onBackPressed());
    }

    private void loadData() {
        List<SubGames> gameTypesList = new ArrayList<>(Arrays.asList(
                new SubGames(R.string.AttentionSchulteTable, shulteRecord, R.drawable.shulte_icon, R.drawable.shulte_draw, R.drawable.shulte_draw_back),
                new SubGames(R.string.MemoryRemNum, zapomniChisloRecord, R.drawable.find_icon, R.drawable.zapomni_draw, R.drawable.zapomni_draw_back),
                new SubGames(R.string.AttentionFigure, findNumRecord, R.drawable.zap_chislo_icon, R.drawable.find_num_draw, R.drawable.find_num_draw_back),
                new SubGames(R.string.NumberZnaki, numZnakiRecord, R.drawable.num_znaki_icon, R.drawable.num_znaki_draw, R.drawable.num_znaki_draw_back),
                new SubGames(R.string.Equation, equationRecord, R.drawable.equation_icon, R.drawable.equation_draw, R.drawable.equation_draw_back),
                new SubGames(R.string.ShulteLetters, shulteLetterRecord, R.drawable.shulte_letter_icon, R.drawable.letter_draw, R.drawable.letter_draw_back),
                new SubGames(R.string.RememberWords, remWordsRecord, R.drawable.rem_words_icon, R.drawable.rem_words_draw, R.drawable.rem_words_draw_back),
                new SubGames(R.string.SquareMemory, squareRecord, R.drawable.square_icon, R.drawable.square_draw, R.drawable.square_draw_back),
                new SubGames(R.string.Colors, colorRecord, R.drawable.color_icon, R.drawable.color_draw, R.drawable.color_draw_back),
                new SubGames(R.string.Figures, figureRecord, R.drawable.shape_icon, R.drawable.shape_draw, R.drawable.shape_draw_back)
        ));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        SubGamesAdapter adapter = new SubGamesAdapter(gameTypesList, this, this);
        gameRecycler.setAdapter(adapter);
        gameRecycler.setLayoutManager(layoutManager);
        gameRecycler.setItemAnimator(new DefaultItemAnimator());
        adapter.notifyDataSetChanged();
    }

    private void loadRecords() {
        if (SharedPrefManager.isUserLoggedIn(this) || SharedPrefManager.isNetworkOnline(this)) {
            if (SharedPrefManager.getShulteRecord(this) != null) {
                shulteRecord = SharedPrefManager.getShulteRecord(this);
            }
            if (SharedPrefManager.getChisloRecord(this) != null) {
                zapomniChisloRecord = SharedPrefManager.getChisloRecord(this);
            }
            if (SharedPrefManager.getFindRecord(this) != null) {
                findNumRecord = SharedPrefManager.getFindRecord(this);
            }
            if (SharedPrefManager.getNumZnakiRecord(this) != null) {
                numZnakiRecord = SharedPrefManager.getNumZnakiRecord(this);
            }
            if (SharedPrefManager.getEquationRecord(this) != null) {
                equationRecord = SharedPrefManager.getEquationRecord(this);
            }
            if (SharedPrefManager.getShulteLetterRecord(this) != null) {
                shulteLetterRecord = SharedPrefManager.getShulteLetterRecord(this);
            }
            if (SharedPrefManager.getSlovoRecord(this) != null) {
                remWordsRecord = SharedPrefManager.getSlovoRecord(this);
            }
            if (SharedPrefManager.getSquareRecord(this) != null) {
                squareRecord = SharedPrefManager.getSquareRecord(this);
            }
            if (SharedPrefManager.getColorRecord(this) != null) {
                colorRecord = SharedPrefManager.getColorRecord(this);
            }
            if (SharedPrefManager.getFigureRecord(this) != null) {
                figureRecord = SharedPrefManager.getFigureRecord(this);
            }
            loadData();
        }
    }

    private void setLocale(String lang, String country) {
        Locale locale = new Locale(lang, country);
        Locale.setDefault(locale);
        Configuration conf = new Configuration();
        conf.setLocale(locale);
        getResources().updateConfiguration(conf, getResources().getDisplayMetrics());
        SharedUpdate.setLanguage(this, lang);
        SharedUpdate.setCountry(this, country);
    }

    private void loadLocale() {
        String lang = SharedUpdate.getLanguage(this);
        String country = SharedUpdate.getCountry(this);
        if (lang != null && country != null) {
            setLocale(lang, country);
        } else {
            setLocale("en", "EN");
        }
    }

    @Override
    public void onSubGameClicked(int position, SubGames types) {
        Intent intent = new Intent();
        intent.putExtra("name", types.getName());
        intent.putExtra("back", types.getIn());
        intent.putExtra("icon", types.getImage());
        setResult(RESULT_OK, intent);
        finish();
    }
}
