package kz.almaty.boombrains.sign_pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.main_pages.MainActivity;

public class MainLoginActivity extends AppCompatActivity {

    @BindView(R.id.mainPlayBtn) Button playBtn;
    @BindView(R.id.loginLink) TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        ButterKnife.bind(this);

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
}
