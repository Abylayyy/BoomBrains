package kz.almaty.boombrains.main_pages;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.almaty.boombrains.R;

public class HowToPlayActivity extends AppCompatActivity {

    @BindView(R.id.back_to_main) RelativeLayout back;
    @BindView(R.id.kakIgratGradient) ConstraintLayout gradient;
    @BindView(R.id.kakIgratIcon) ImageView kakIcon;
    @BindView(R.id.pravilaTxt) TextView pravila;
    @BindView(R.id.descTxt) TextView description;
    @BindView(R.id.scroll) ScrollView backgroundIcon;

    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play);
        ButterKnife.bind(this);

        back.setOnClickListener(v -> onBackPressed());

        name = getIntent().getStringExtra("kakName");

        setDatas(name);
    }

    private void setDatas(String name) {
        if (name.equals(getString(R.string.AttentionSchulteTable))) {
            setShulteInfo();
        } else if (name.equals(getString(R.string.MemoryRemNum))) {
            setZapomniChislo();
        } else if (name.equals(getString(R.string.AttentionFigure))) {
            setFindNumber();
        }
    }

    private void setZapomniChislo() {
        setInfoByType(R.drawable.zapomni_slovo_gradient, R.drawable.kak_igrat_zapomni_chislo_icon, R.color.kakIgratZapomni,
                R.drawable.zapomni_slovo_back, R.string.RemNumInfo);
    }

    private void setShulteInfo() {
        setInfoByType(R.drawable.shulte_gradient, R.drawable.kak_igrat_shulte, R.color.kakIgratShulte, R.drawable.shulte_back, R.string.SchulteInfo);
    }

    private void setFindNumber() {
        setInfoByType(R.drawable.shulte_gradient, R.drawable.kak_igrat_find_number, R.color.kakIgratShulte, R.drawable.shulte_back, R.string.FindNumberInfo);
    }

    private void setInfoByType(int grad, int measure, int prav, int back, int text) {
        gradient.setBackgroundResource(grad);
        setMeasures(kakIcon, measure);
        pravila.setTextColor(getResources().getColor(prav));
        backgroundIcon.setBackgroundResource(back);
        description.setText(getString(text));
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0,0);
    }

    private void setMeasures(ImageView image, int resource) {
        image.setImageResource(resource);
        Drawable drawable = getResources().getDrawable(resource);
        int width = drawable.getIntrinsicWidth() / 2 - 50;
        int height = drawable.getIntrinsicHeight() / 2 - 50;
        image.getLayoutParams().width = width;
        image.getLayoutParams().height = height;
    }
}
