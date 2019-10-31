package kz.almaty.boombrains.main_fragments;


import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.adapters.SubGamesAdapter;
import kz.almaty.boombrains.helpers.SharedPrefManager;
import kz.almaty.boombrains.helpers.StatefulFragment;
import kz.almaty.boombrains.models.main_models.SubGames;

/**
 * A simple {@link Fragment} subclass.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class MainFragment extends StatefulFragment {

    @BindView(R.id.underRecycler) RecyclerView typeRecycler;
    List<SubGames> gameTypesList = new ArrayList<>();

    private String shulteRecord = "0", zapomniChisloRecord = "0", findNumRecord = "0", numZnakiRecord = "0",
            equationRecord = "0", shulteLetterRecord = "0", remWordsRecord = "0", squareRecord = "0",
            colorRecord = "0", figureRecord = "0";

    public MainFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadRecords();
        loadLocale();
        loadData();
    }

    private void loadRecords() {
        if (SharedPrefManager.getShulteRecord(getContext()) != null) {
            shulteRecord = SharedPrefManager.getShulteRecord(getContext());
        }
        if (SharedPrefManager.getChisloRecord(getContext()) != null) {
            zapomniChisloRecord = SharedPrefManager.getChisloRecord(getContext());
        }
        if (SharedPrefManager.getFindRecord(getContext()) != null) {
            findNumRecord = SharedPrefManager.getFindRecord(getContext());
        }
        if (SharedPrefManager.getNumZnakiRecord(getContext()) != null) {
            numZnakiRecord = SharedPrefManager.getNumZnakiRecord(getContext());
        }
        if (SharedPrefManager.getEquationRecord(getContext()) != null) {
            equationRecord = SharedPrefManager.getEquationRecord(getContext());
        }
        if (SharedPrefManager.getShulteLetterRecord(getContext()) != null) {
            shulteLetterRecord = SharedPrefManager.getShulteLetterRecord(getContext());
        }
        if (SharedPrefManager.getSlovoRecord(getContext()) != null) {
            remWordsRecord = SharedPrefManager.getSlovoRecord(getContext());
        }
        if (SharedPrefManager.getSquareRecord(getContext()) != null) {
            squareRecord = SharedPrefManager.getSquareRecord(getContext());
        }
        if (SharedPrefManager.getColorRecord(getContext()) != null) {
            colorRecord = SharedPrefManager.getColorRecord(getContext());
        }
        if (SharedPrefManager.getFigureRecord(getContext()) != null) {
            figureRecord = SharedPrefManager.getFigureRecord(getContext());
        }
    }

    private void loadData() {
        if(gameTypesList != null && gameTypesList.size() > 0){
            gameTypesList.clear();
        }
        gameTypesList = new ArrayList<>(Arrays.asList(
                new SubGames(getString(R.string.AttentionSchulteTable), shulteRecord, R.drawable.shulte_icon, R.drawable.shulte_draw, R.drawable.shulte_draw_back),
                new SubGames(getString(R.string.MemoryRemNum), zapomniChisloRecord, R.drawable.find_icon, R.drawable.zapomni_draw, R.drawable.zapomni_draw_back),
                new SubGames(getString(R.string.AttentionFigure), findNumRecord, R.drawable.zap_chislo_icon, R.drawable.find_num_draw, R.drawable.find_num_draw_back),
                new SubGames(getString(R.string.NumberZnaki), numZnakiRecord, R.drawable.num_znaki_icon, R.drawable.num_znaki_draw, R.drawable.num_znaki_draw_back),
                new SubGames(getString(R.string.Equation), equationRecord, R.drawable.equation_icon, R.drawable.equation_draw, R.drawable.equation_draw_back),
                new SubGames(getString(R.string.ShulteLetters), shulteLetterRecord, R.drawable.shulte_letter_icon, R.drawable.letter_draw, R.drawable.letter_draw_back),
                new SubGames(getString(R.string.RememberWords), remWordsRecord, R.drawable.rem_words_icon, R.drawable.rem_words_draw, R.drawable.rem_words_draw_back),
                new SubGames(getString(R.string.SquareMemory), squareRecord, R.drawable.square_icon, R.drawable.square_draw, R.drawable.square_draw_back),
                new SubGames(getString(R.string.Colors), colorRecord, R.drawable.color_icon, R.drawable.color_draw, R.drawable.color_draw_back),
                new SubGames(getString(R.string.Figures), figureRecord, R.drawable.shape_icon, R.drawable.shape_draw, R.drawable.shape_draw_back)
        ));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        SubGamesAdapter adapter = new SubGamesAdapter(gameTypesList, getContext());
        typeRecycler.setAdapter(adapter);
        typeRecycler.setLayoutManager(layoutManager);
        typeRecycler.setItemAnimator(new DefaultItemAnimator());
        adapter.notifyDataSetChanged();
    }

    @Override
    protected boolean hasSavedState() {
        return true;
    }

    @Override
    protected Bundle getStateToSave() {
        return null;
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration conf = new Configuration();
        conf.setLocale(locale);
        getResources().updateConfiguration(conf, getResources().getDisplayMetrics());
        SharedPrefManager.setLanguage(getContext(), lang);
    }

    private void loadLocale() {
        String lang = SharedPrefManager.getLanguage(getContext());
        if (lang != null) {
            setLocale(lang);
        } else {
            setLocale("en");
        }
    }
}
