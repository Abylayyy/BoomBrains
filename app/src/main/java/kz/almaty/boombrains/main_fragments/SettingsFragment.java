package kz.almaty.boombrains.main_fragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.helpers.SharedPrefManager;

/**
 * A simple {@link Fragment} subclass.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class SettingsFragment extends Fragment {

    @BindView(R.id.languageTxt) TextView languageTxt;
    @BindView(R.id.soundSwitch) Switch soundSwitch;
    @BindView(R.id.back_to_main) RelativeLayout back;
    @BindView(R.id.settingTxt) TextView settings;
    @BindView(R.id.soundTxt) TextView sound;
    @BindView(R.id.langTxt) TextView language;

    private Dialog dialog;
    private TextView ru, en, kaz, cancel, diLang;

    public SettingsFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dialog = new Dialog(getActivity(), R.style.mytheme);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.setContentView(R.layout.language_layout);
        ru = dialog.findViewById(R.id.russionTxt);
        en = dialog.findViewById(R.id.englishTxt);
        kaz = dialog.findViewById(R.id.kazakhTxt);
        cancel = dialog.findViewById(R.id.cancelTxt);
        diLang = dialog.findViewById(R.id.diLang);

        languageTxt.setText(getString(R.string.AppLanguageText));
        languageTxt.setOnClickListener(v -> showDialog());
        language.setOnClickListener(v -> showDialog());

        back.setOnClickListener(v -> getActivity().onBackPressed());

        soundSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                SharedPrefManager.setSoundEnabled(getActivity(), true);
            } else {
                SharedPrefManager.setSoundEnabled(getActivity(), false);
            }
        });

        soundSwitch.setChecked(SharedPrefManager.isSoundEnabled(getActivity()));
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration conf = new Configuration();
        conf.setLocale(locale);
        getResources().updateConfiguration(conf, getResources().getDisplayMetrics());
        SharedPrefManager.setLanguage(getActivity(), lang);
    }

    private void loadLocale() {
        String lang = SharedPrefManager.getLanguage(getActivity());
        if (lang != null) {
            setLocale(lang);
        } else {
            setLocale("ru");
        }
    }

    private void setAllTexts() {
        languageTxt.setText(getString(R.string.AppLanguageText));
        language.setText(getString(R.string.ChangeLanguage));
        sound.setText(getString(R.string.Sound));
        settings.setText(getString(R.string.Settings));
        cancel.setText(getString(R.string.Cancel));
        diLang.setText(getString(R.string.ChangeLanguage));
    }

    @SuppressLint("SetTextI18n")
    private void showDialog() {
        ru.setOnClickListener(v -> {
            setLocale("ru");
            loadLocale();
            setAllTexts();
            dialog.dismiss();
        });
        kaz.setOnClickListener(v -> {
            setLocale("kk");
            loadLocale();
            setAllTexts();
            dialog.dismiss();
        });
        en.setOnClickListener(v -> {
            setLocale("en");
            loadLocale();
            setAllTexts();
            dialog.dismiss();
        });
        cancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
