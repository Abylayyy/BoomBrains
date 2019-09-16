package kz.almaty.boombrains.main_pages;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
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
import kz.almaty.boombrains.models.SubGames;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.underRecycler) RecyclerView typeRecycler;
    @BindView(R.id.languages) TextView languageTxt;
    SubGamesAdapter adapter;
    List<SubGames> gameTypesList;
    Dialog dialog;
    TextView ru, en, kaz, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        dialog = new Dialog(this, R.style.mytheme);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.setContentView(R.layout.language_layout);
        ru = dialog.findViewById(R.id.russionTxt);
        en = dialog.findViewById(R.id.englishTxt);
        kaz = dialog.findViewById(R.id.kazakhTxt);
        cancel = dialog.findViewById(R.id.cancelTxt);
        gameTypesList = new ArrayList<>();
        loadLocale();

        languageTxt.setText(getString(R.string.AppLanguageText));
        languageTxt.setPaintFlags(languageTxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        languageTxt.setOnClickListener(v -> showDialog());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLocale();
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration conf = new Configuration();
        conf.setLocale(locale);
        getResources().updateConfiguration(conf, getResources().getDisplayMetrics());
        SharedPrefManager.setLanguage(getApplication(), lang);
    }

    private void loadLocale() {
        String lang = SharedPrefManager.getLanguage(getApplication());
        if (lang != null) {
            setLocale(lang);
        } else {
            setLocale("en");
        }
        loadData();
    }

    private void loadData() {
        gameTypesList.clear();
        gameTypesList = new ArrayList<>(Arrays.asList(
                new SubGames(getString(R.string.MemoryRemNum), R.drawable.zapomni_chislo),
                new SubGames(getString(R.string.AttentionFigure), R.drawable.find_number),
                new SubGames(getString(R.string.AttentionSchulteTable), R.drawable.shulte)
        ));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        adapter = new SubGamesAdapter(gameTypesList, getApplication());
        typeRecycler.setAdapter(adapter);
        typeRecycler.setLayoutManager(layoutManager);
        typeRecycler.setItemAnimator(new DefaultItemAnimator());
        adapter.notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    private void showDialog() {
        ru.setOnClickListener(v -> {
            setLocale("ru");
            loadLocale();
            languageTxt.setText(getString(R.string.AppLanguageText));
            dialog.dismiss();
        });
        kaz.setOnClickListener(v -> {
            setLocale("kk");
            loadLocale();
            languageTxt.setText(getString(R.string.AppLanguageText));
            dialog.dismiss();
        });
        en.setOnClickListener(v -> {
            setLocale("en");
            loadLocale();
            languageTxt.setText(getString(R.string.AppLanguageText));
            dialog.dismiss();
        });
        cancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
