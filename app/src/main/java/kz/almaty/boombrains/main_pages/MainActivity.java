package kz.almaty.boombrains.main_pages;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.helpers.SharedPrefManager;
import kz.almaty.boombrains.main_fragments.MainFragment;
import kz.almaty.boombrains.main_fragments.SettingsFragment;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.mainBtn) ImageView main;
    @BindView(R.id.profile) RelativeLayout profile;
    @BindView(R.id.settings) RelativeLayout settings;

    Fragment currentFragment;
    private Dialog dialog;
    TextView rateTxt, cancelTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        loadLocale();
        replaceFragment(new MainFragment(), "main_fragment", false);

        dialog = new Dialog(this, R.style.mytheme);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.setContentView(R.layout.rating_layout);
        rateTxt = dialog.findViewById(R.id.rate_app);
        cancelTxt = dialog.findViewById(R.id.cancel_rate);

        showRateAppDialog();
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
            setLocale("ru");
        }
    }

    private static final String BACK_STACK_ROOT_TAG = "root_fragment";

    public void replaceFragment(Fragment fragment, String fragmentTag, boolean withBackStack){
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction transaction  = getSupportFragmentManager().beginTransaction();
        if(withBackStack){
            fragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            transaction.addToBackStack(BACK_STACK_ROOT_TAG);
        }
        transaction.replace(R.id.fragment_container, fragment,fragmentTag);
        transaction.commit();
    }

    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.mainBtn: {
                currentFragment = new MainFragment();
                replaceFragment(currentFragment, "main_fragment", false);
                break;
            }
            case R.id.settings: {
                currentFragment = new SettingsFragment();
                replaceFragment(currentFragment, "settings_fragment", true);
                break;
            }
            case R.id.profile: {
                shareTextUrl();
                break;
            }
        }
    }

    private void showRateAppDialog() {
        if (SharedPrefManager.getPlayCount(getApplication()) >= 5) {
            if (!SharedPrefManager.getNeverShowAgain(getApplication())) {
                new Handler().postDelayed(()-> {
                    dialog.show();
                }, 1000);
            }
        }

        rateTxt.setOnClickListener(v -> {
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

    private void shareTextUrl() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, "Check out Boom Brains");
        share.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=kdo.one.boombrains");
        startActivity(Intent.createChooser(share, "Share the game with friends!"));
    }

    @Override
    public void onResume() {
        super.onResume();
        loadLocale();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
