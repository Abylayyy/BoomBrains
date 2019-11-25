package kz.almaty.boombrains.sign_pages.login_pages;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.helpers.SharedPrefManager;
import kz.almaty.boombrains.helpers.SharedUpdate;
import kz.almaty.boombrains.main_pages.MainActivity;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class MainLoginActivity extends AppCompatActivity {

    @BindView(R.id.mainPlayBtn) Button playBtn;
    @BindView(R.id.loginLink) TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        ButterKnife.bind(this);

        loadLocale();

        playBtn.setText(getString(R.string.PlayBtn));
        loginLink.setText(getString(R.string.Authorization));

        playBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition( 0, 0);
            finishAffinity();
        });

        loginLink.setPaintFlags(loginLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        loginLink.setOnClickListener(v -> {
            startActivity(new Intent(this, MainSignInActivity.class));
            overridePendingTransition(0,0);
        });
    }

    private void loadLocale() {
        String lang = SharedUpdate.getLanguage(getApplication());
        if (lang != null) {
            setLocale(lang);
        } else {
            setLocale("en");
        }
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration conf = new Configuration();
        conf.setLocale(locale);
        getResources().updateConfiguration(conf, getResources().getDisplayMetrics());
        SharedUpdate.setLanguage(getApplication(), lang);
    }
}
