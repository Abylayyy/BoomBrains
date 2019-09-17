package kz.almaty.boombrains.main_pages;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        loadLocale();

        replaceFragment(new MainFragment(), "main_fragment", false);
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
                Toast.makeText(getApplication(), getString(R.string.not_ready), Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }
}
