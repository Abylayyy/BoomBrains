package kz.almaty.boombrains.ui.main_fragments;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.SpinKitView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.ui.adapters.SubGamesAdapter;
import kz.almaty.boombrains.util.helpers.preference.SharedPrefManager;
import kz.almaty.boombrains.util.helpers.preference.SharedUpdate;
import kz.almaty.boombrains.util.helpers.list_helper.StatefulFragment;
import kz.almaty.boombrains.ui.main_pages.MainActivity;
import kz.almaty.boombrains.data.models.auth_models.register.GetLocalRecords;
import kz.almaty.boombrains.data.models.auth_models.register.LocalRecords;
import kz.almaty.boombrains.data.models.main_models.SubGames;
import kz.almaty.boombrains.data.models.records_model.MyRecordsModel;
import kz.almaty.boombrains.viewmodel.records.record_view_model.RecordView;
import kz.almaty.boombrains.viewmodel.records.record_view_model.RecordViewModel;
import kz.almaty.boombrains.viewmodel.records.update_record.UpdateRecordView;
import kz.almaty.boombrains.viewmodel.records.update_record.UpdateRecordViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class MainFragment extends StatefulFragment implements RecordView, UpdateRecordView {

    @BindView(R.id.underRecycler) RecyclerView typeRecycler;
    @BindView(R.id.progressSpin) SpinKitView progress;
    private List<SubGames> gameTypesList = new ArrayList<>();

    private RecordViewModel recordViewModel;

    private String shulteRecord = "0", zapomniChisloRecord = "0", findNumRecord = "0", numZnakiRecord = "0",
            equationRecord = "0", shulteLetterRecord = "0", remWordsRecord = "0", squareRecord = "0",
            colorRecord = "0", figureRecord = "0";

    public MainFragment() {}

    private MainActivity activity;
    private MainCallBack callBack;
    private UpdateRecordViewModel updateRecordViewModel;

    public interface MainCallBack {
        void loaded();
        void failed();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateRecordViewModel = ViewModelProviders.of(this).get(UpdateRecordViewModel.class);
        if (SharedPrefManager.isNetworkOnline(getActivity()) && SharedPrefManager.isUserLoggedIn(getActivity())) {
            if (SharedUpdate.isRecordExist(getActivity())) {
                updateRecordViewModel.setUpdatedRecords(getActivity(), getModel(), this);
            }
        }

        recordViewModel = ViewModelProviders.of(this).get(RecordViewModel.class);
        if (SharedPrefManager.isNetworkOnline(getActivity()) && SharedPrefManager.isUserLoggedIn(getActivity())) {
            if (!SharedPrefManager.isRecordsLoaded(getActivity())) {
                recordViewModel.getAllRecords(getActivity(), this);
                SharedPrefManager.setRecordsLoaded(getActivity(), true);
                SharedUpdate.setRecordExist(getActivity(), true);
            }
        }

        if (SharedPrefManager.isUserLoggedIn(getContext()) && SharedPrefManager.isNetworkOnline(getContext())) {
            progress.setVisibility(View.VISIBLE);
        }

        loadRecords();
        loadLocale();
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity){
            activity =(MainActivity) context;
        }
        callBack = (MainFragment.MainCallBack) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callBack = null;
    }

    private void loadRecords() {
        if (!SharedPrefManager.isUserLoggedIn(getContext()) || !SharedPrefManager.isNetworkOnline(getContext())) {
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
            loadData();
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

    private void setLocale(String lang, String country) {
        Locale locale = new Locale(lang, country);
        Locale.setDefault(locale);
        Configuration conf = new Configuration();
        conf.setLocale(locale);
        getResources().updateConfiguration(conf, getResources().getDisplayMetrics());
        SharedUpdate.setLanguage(getContext(), lang);
        SharedUpdate.setCountry(getActivity(), country);
    }

    private void loadLocale() {
        String lang = SharedUpdate.getLanguage(getContext());
        String country = SharedUpdate.getCountry(getActivity());
        if (lang != null && country != null) {
            setLocale(lang, country);
        } else {
            setLocale("en", "EN");
        }
    }

    @Override
    public void loadList(MyRecordsModel records) {
        shulteRecord = records.getRecords().get(0).getRecord().toString();
        zapomniChisloRecord = records.getRecords().get(1).getRecord().toString();
        findNumRecord = records.getRecords().get(2).getRecord().toString();
        numZnakiRecord = records.getRecords().get(3).getRecord().toString();
        equationRecord = records.getRecords().get(4).getRecord().toString();
        shulteLetterRecord = records.getRecords().get(5).getRecord().toString();
        remWordsRecord = records.getRecords().get(6).getRecord().toString();
        squareRecord = records.getRecords().get(7).getRecord().toString();
        figureRecord = records.getRecords().get(8).getRecord().toString();
        colorRecord = records.getRecords().get(9).getRecord().toString();
        if (isAdded() && getActivity() != null) {
            saveAllRecords();
            loadData();
        }
    }

    private void saveAllRecords() {
        SharedPrefManager.setShulteRecord(getContext(), shulteRecord);
        SharedPrefManager.setChisoRecord(getContext(), zapomniChisloRecord);
        SharedPrefManager.setFindRecord(getContext(), findNumRecord);
        SharedPrefManager.setNumZnakiRecord(getContext(), numZnakiRecord);
        SharedPrefManager.setEquationRecord(getContext(), equationRecord);
        SharedPrefManager.setShulteLetterRecord(getContext(), shulteLetterRecord);
        SharedPrefManager.setSlovoRecord(getContext(), remWordsRecord);
        SharedPrefManager.setSquareRecord(getContext(), squareRecord);
        SharedPrefManager.setFigureRecord(getContext(), figureRecord);
        SharedPrefManager.setColorRecord(getContext(), colorRecord);
    }

    @Override
    public void showLoading() {
        typeRecycler.setVisibility(View.INVISIBLE);
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        typeRecycler.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
    }

    @Override
    public void errorMessage(int i) {
        switch (i) {
            case 401: {
                SharedPrefManager.clearAndExit(getActivity());
                break;
            }
            case 404: {
                Toast.makeText(getContext(), "Server not found!", Toast.LENGTH_SHORT).show();
                break;
            }
            default: break;
        }
    }

    @Override
    public void success(String message) {
        if (isAdded() && getActivity() != null) {
            if (SharedPrefManager.isRecordsLoaded(getActivity())) {
                recordViewModel.getAllRecords(getActivity(), this);
            }
        }
        Log.d("SUCCESS::", message);
    }

    @Override
    public void error(String message) {
        Log.d("ERROR::", message);
    }

    @Override
    public void errorUpdate(int i) {
        switch (i) {
            case 401: {
                SharedPrefManager.clearAndExit(getActivity());
                break;
            }
            case 404: {
                Toast.makeText(getContext(), "Server not found!", Toast.LENGTH_SHORT).show();
                break;
            }
            default: break;
        }
    }

    private GetLocalRecords getModel() {

        int shulte = SharedUpdate.record(1, getActivity()), chislo = SharedUpdate.record(2, getActivity());
        int find = SharedUpdate.record(3, getActivity()), calc = SharedUpdate.record(4, getActivity());
        int equation = SharedUpdate.record(5, getActivity()), words = SharedUpdate.record(6, getActivity());
        int letters = SharedUpdate.record(7, getActivity()), square = SharedUpdate.record(8, getActivity());
        int color = SharedUpdate.record(9, getActivity()), figure = SharedUpdate.record(10, getActivity());

        GetLocalRecords locals = new GetLocalRecords();
        LocalRecords records = new LocalRecords();

        if (shulte != 0) { records.setShulteTable(shulte); }
        if (chislo != 0) { records.setRememberNum(chislo); }
        if (find != 0) { records.setFindNumbers(find); }
        if (equation != 0) { records.setEquation(equation); }
        if (calc != 0) { records.setCalculation(calc); }
        if (words != 0) { records.setRememberWords(words); }
        if (letters != 0) { records.setShulteLetters(letters); }
        if (square != 0) { records.setMemorySquare(square); }
        if (color != 0) { records.setColorWords(color); }
        if (figure != 0) { records.setColorFigures(figure); }

        locals.setLocalRecords(records);

        return locals;
    }
}
