package kz.almaty.boombrains.ui.main_pages;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.helpers.SharedPrefManager;
import kz.almaty.boombrains.ui.sign_pages.login_pages.MainLoginActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (SharedPrefManager.isUserLoggedIn(this)) {
            setActivity(new Intent(getApplication(), MainActivity.class));
        } else {
            setActivity(new Intent(getApplication(), MainLoginActivity.class));
        }
    }

    private void setActivity(Intent intent) {
        new Handler().postDelayed(()-> {
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }, 1700);
    }
}
