package kz.almaty.boombrains.main_pages;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.helpers.SharedPrefManager;
import kz.almaty.boombrains.main_fragments.MainFragment;
import kz.almaty.boombrains.main_fragments.profile_pages.ProfileFragment;
import kz.almaty.boombrains.main_fragments.SettingsFragment;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.mainBtn) ImageView main;
    @BindView(R.id.profile) RelativeLayout profile;
    @BindView(R.id.settings) RelativeLayout settings;
    @BindView(R.id.prof_img) ImageView profImg;
    @BindView(R.id.setting_img) ImageView settingImg;

    Fragment currentFragment;
    private Dialog dialog;
    TextView rateTxt, cancelTxt;
    ConstraintLayout rateStars;
    private static final String BACK_STACK_ROOT_TAG = "root_fragment";

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        if (!SharedPrefManager.getIsFirstUser(getApplication())) {
            SharedPrefManager.clearSettings(getApplication());
            SharedPrefManager.setIsFirstUser(getApplication(), true);
        }

        loadLocale();

        dialog = new Dialog(this, R.style.mytheme);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.setContentView(R.layout.rating_layout);
        rateTxt = dialog.findViewById(R.id.rate_app);
        cancelTxt = dialog.findViewById(R.id.cancel_rate);
        rateStars = dialog.findViewById(R.id.rateStarConst);

        loadGoogleAd();
        showRateAppDialog();

        replaceFragment(new MainFragment(), "main_fragment", false);
    }

    private void loadGoogleAd() {
        MobileAds.initialize(this, initializationStatus -> Log.d("INITIALIZATION::", "COMPLETED"));
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7342268862236285/2044423261");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                showGoogleAdd();
                Log.d("LOADING::", "LOADED");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.d("LOADING::", "FAILED");
            }
            @Override
            public void onAdOpened() { }
            @Override
            public void onAdClicked() { }
            @Override
            public void onAdLeftApplication() { }
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }

    private void showGoogleAdd() {
        if (SharedPrefManager.getAddCount(getApplication()) >= 3) {
            new Handler().postDelayed(() -> {
                mInterstitialAd.show();
                SharedPrefManager.setAddCount(getApplication(), 0);
            }, 30);
        }
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
    }

    private void replaceFragment(Fragment fragment, String fragmentTag, boolean withBackStack){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction  = getSupportFragmentManager().beginTransaction();
        if(withBackStack){
            fragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            transaction.addToBackStack(BACK_STACK_ROOT_TAG);
        } else {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        transaction.replace(R.id.fragment_container, fragment, fragmentTag);
        transaction.commit();
        invalidateOptionsMenu();
    }

    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.mainBtn: {
                setColorsWhenPressed(getResources().getColor(R.color.disabled), getResources().getColor(R.color.disabled));
                currentFragment = new MainFragment();
                replaceFragment(currentFragment, "main_fragment", false);
                break;
            }
            case R.id.settings: {
                setColorsWhenPressed(getResources().getColor(R.color.disabled), getResources().getColor(R.color.pressed));
                currentFragment = new SettingsFragment();
                replaceFragment(currentFragment, "settings_fragment", true);
                break;
            }
            case R.id.profile: {
                setColorsWhenPressed(getResources().getColor(R.color.pressed), getResources().getColor(R.color.disabled));
                currentFragment = new ProfileFragment();
                replaceFragment(currentFragment, "profile_fragment", true);
                break;
            }
        }
    }

    private void setColorsWhenPressed(int prof, int setting) {
        profImg.setColorFilter(prof);
        settingImg.setColorFilter(setting);
    }

    private void showRateAppDialog() {
        if (SharedPrefManager.getPlayCount(getApplication()) >= 5) {
            if (!SharedPrefManager.getNeverShowAgain(getApplication())) {
                new Handler().postDelayed(()-> dialog.show(), 1000);
            }
        }

        rateTxt.setOnClickListener(v -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "kdo.one.boombrains")));
            SharedPrefManager.setNeverShowAgain(getApplication(), true);
            dialog.dismiss();
        });

        rateStars.setOnClickListener(v -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "kdo.one.boombrains")));
            SharedPrefManager.setNeverShowAgain(getApplication(), true);
            dialog.dismiss();
        });

        cancelTxt.setOnClickListener(v -> {
            SharedPrefManager.setPlayCount(getApplication(), 0);
            SharedPrefManager.setNeverShowAgain(getApplication(), false);
            dialog.dismiss();
        });
    }

    @Override
    public void onBackPressed() {
        setColorsWhenPressed(getResources().getColor(R.color.disabled), getResources().getColor(R.color.disabled));
        super.onBackPressed();
    }
}
