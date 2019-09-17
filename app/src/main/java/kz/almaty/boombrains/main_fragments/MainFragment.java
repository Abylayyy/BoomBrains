package kz.almaty.boombrains.main_fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.adapters.SubGamesAdapter;
import kz.almaty.boombrains.helpers.SharedPrefManager;
import kz.almaty.boombrains.models.SubGames;

/**
 * A simple {@link Fragment} subclass.
 */

public class MainFragment extends Fragment {

    @BindView(R.id.underRecycler) RecyclerView typeRecycler;

    SubGamesAdapter adapter;
    List<SubGames> gameTypesList = new ArrayList<>();
    String shulteRecord = "0", zapomniChisloRecord = "0", findNumRecord = "0", numZnakiRecord = "0", equationRecord = "0";

    public MainFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        loadRecords();
        loadData();

        return view;
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
    }

    private void loadData() {
        gameTypesList.clear();

        gameTypesList = new ArrayList<>(Arrays.asList(
                new SubGames(getString(R.string.MemoryRemNum), zapomniChisloRecord, R.drawable.zap_chislo_icon, R.drawable.zapomni_draw, R.drawable.zapomni_draw_back),
                new SubGames(getString(R.string.AttentionSchulteTable), shulteRecord, R.drawable.shulte_icon, R.drawable.shulte_draw, R.drawable.shulte_draw_back),
                new SubGames(getString(R.string.AttentionFigure), findNumRecord, R.drawable.find_icon, R.drawable.find_num_draw, R.drawable.find_num_draw_back)/*,
                new SubGames(getString(R.string.NumberZnaki), numZnakiRecord, R.drawable.num_znaki_icon, R.drawable.num_znaki_draw, R.drawable.num_znaki_draw_back),
                new SubGames(getString(R.string.Equation), equationRecord, R.drawable.equation_icon, R.drawable.equation_draw, R.drawable.equation_draw_back))*/
        ));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        adapter = new SubGamesAdapter(gameTypesList, getContext());
        typeRecycler.setAdapter(adapter);
        typeRecycler.setLayoutManager(layoutManager);
        typeRecycler.setItemAnimator(new DefaultItemAnimator());
        adapter.notifyDataSetChanged();
    }
}
