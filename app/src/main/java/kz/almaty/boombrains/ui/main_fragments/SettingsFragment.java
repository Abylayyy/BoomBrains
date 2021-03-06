package kz.almaty.boombrains.ui.main_fragments;


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
import androidx.lifecycle.ViewModelProviders;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.util.helpers.preference.SharedPrefManager;
import kz.almaty.boombrains.util.helpers.preference.SharedUpdate;
import kz.almaty.boombrains.util.helpers.list_helper.StatefulFragment;
import kz.almaty.boombrains.viewmodel.auth_view_models.logout_view_model.LogoutView;
import kz.almaty.boombrains.viewmodel.auth_view_models.logout_view_model.LogoutViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class SettingsFragment extends StatefulFragment implements LogoutView {

    @BindView(R.id.languageTxt) TextView languageTxt;
    @BindView(R.id.soundSwitch) Switch soundSwitch;
    @BindView(R.id.back_to_main) RelativeLayout back;
    @BindView(R.id.settingTxt) TextView settings;
    @BindView(R.id.soundTxt) TextView sound;
    @BindView(R.id.langTxt) TextView language;
    @BindView(R.id.vibrationSwitch) Switch vibSwitch;
    @BindView(R.id.vibrationTxt) TextView vibTxt;
    @BindView(R.id.exitBtn) TextView exitBtn;

    private Dialog dialog;
    private TextView ru, en, es, kaz, cancel, diLang;
    LogoutViewModel logoutViewModel;



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
        es = dialog.findViewById(R.id.espanolTxt);
        kaz = dialog.findViewById(R.id.kazakhTxt);
        cancel = dialog.findViewById(R.id.cancelTxt);
        diLang = dialog.findViewById(R.id.diLang);

        logoutViewModel = ViewModelProviders.of(this).get(LogoutViewModel.class);

        languageTxt.setOnClickListener(v -> showDialog());
        language.setOnClickListener(v -> showDialog());

        if (SharedPrefManager.isUserLoggedIn(getActivity())) {
            exitBtn.setVisibility(View.VISIBLE);
        } else {
            exitBtn.setVisibility(View.INVISIBLE);
        }

        back.setOnClickListener(v -> getActivity().onBackPressed());

        exitBtn.setOnClickListener(v -> {
            if (SharedPrefManager.isNetworkOnline(getActivity())) {
                logoutViewModel.logoutFromAccount(getContext(), this);
            } else {
                SharedPrefManager.clearAndExit(getActivity());
            }
        });

        soundSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                SharedPrefManager.setSoundEnabled(getActivity(), true);
            } else {
                SharedPrefManager.setSoundEnabled(getActivity(), false);
            }
        });

        vibSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                SharedPrefManager.setVibrateEnabled(getActivity(), true);
            } else {
                SharedPrefManager.setVibrateEnabled(getActivity(), false);
            }
        });

        vibSwitch.setChecked(SharedPrefManager.isVibrateEnabled(getActivity()));
        soundSwitch.setChecked(SharedPrefManager.isSoundEnabled(getActivity()));
    }

    private void setLocale(String lang, String country) {
        Locale locale = new Locale(lang, country);
        Locale.setDefault(locale);
        Configuration conf = new Configuration();
        conf.setLocale(locale);
        getResources().updateConfiguration(conf, getResources().getDisplayMetrics());
        SharedUpdate.setLanguage(getActivity(), lang);
        SharedUpdate.setCountry(getActivity(), country);
    }

    private void loadLocale() {
        String country = SharedUpdate.getCountry(getActivity());
        String lang = SharedUpdate.getLanguage(getActivity());
        if (lang != null && country != null) {
            setLocale(lang, country);
        } else {
            setLocale("en", "EN");
        }
    }

    private void setAllTexts() {
        languageTxt.setText(getString(R.string.AppLanguageText));
        language.setText(getString(R.string.ChangeLanguage));
        sound.setText(getString(R.string.Sound));
        settings.setText(getString(R.string.Settings));
        cancel.setText(getString(R.string.Cancel));
        diLang.setText(getString(R.string.ChangeLanguage));
        vibTxt.setText(getString(R.string.Vibration));
        exitBtn.setText(getString(R.string.Exit));
    }

    @SuppressLint("SetTextI18n")
    private void showDialog() {
        SharedPrefManager.setIsFirstShown(getActivity(), true);
        ru.setOnClickListener(v -> {
            setLocale("ru", "RU");
            loadLocale();
            setAllTexts();
            dialog.dismiss();
        });
        kaz.setOnClickListener(v -> {
            setLocale("kk", "KZ");
            loadLocale();
            setAllTexts();
            dialog.dismiss();
        });
        en.setOnClickListener(v -> {
            setLocale("en", "EN");
            loadLocale();
            setAllTexts();
            dialog.dismiss();
        });
        es.setOnClickListener(v -> {
            setLocale("es", "ES");
            loadLocale();
            setAllTexts();
            dialog.dismiss();
        });
        cancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dialog.dismiss();
    }

    @Override
    protected boolean hasSavedState() {
        return true;
    }

    @Override
    protected Bundle getStateToSave() {
        return null;
    }

    @Override
    public void success() {
        SharedPrefManager.clearAndExit(getActivity());
    }

    @Override
    public void error() {
        Toasty.info(getActivity(), getString(R.string.LogoutError), Toasty.LENGTH_SHORT).show();
    }
}
