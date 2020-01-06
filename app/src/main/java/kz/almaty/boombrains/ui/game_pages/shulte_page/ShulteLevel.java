package kz.almaty.boombrains.ui.game_pages.shulte_page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.ui.game_pages.start_page.AreYouReadyActivity;

public class ShulteLevel extends AppCompatActivity {

    @BindView(R.id.easyBtn) TextView easy;
    @BindView(R.id.mediumBtn) TextView medium;
    @BindView(R.id.hardBtn) TextView hard;

    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shulte_level);
        ButterKnife.bind(this);

        position = getIntent().getIntExtra("position", 0);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.easyBtn: {
                startIntent(new Intent(this, AreYouReadyActivity.class), easy.getText().toString());
                break;
            }
            case R.id.mediumBtn: {
                startIntent(new Intent(this, AreYouReadyActivity.class), medium.getText().toString());
                break;
            }
            case R.id.hardBtn: {
                startIntent(new Intent(this, AreYouReadyActivity.class), hard.getText().toString());
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    private void startIntent(Intent intent, String name) {
        intent.putExtra("name", name);
        intent.putExtra("position", position);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
}
