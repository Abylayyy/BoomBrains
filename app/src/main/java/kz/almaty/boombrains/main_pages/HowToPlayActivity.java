package kz.almaty.boombrains.main_pages;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.helpers.SharedPrefManager;

public class HowToPlayActivity extends AppCompatActivity {

    @BindView(R.id.back_to_main) RelativeLayout back;
    @BindView(R.id.kakIgratIcon) ImageView kakIcon;
    @BindView(R.id.pravilaTxt) TextView pravila;
    @BindView(R.id.descTxt) TextView description;
    @BindView(R.id.scroll) ScrollView backgroundIcon;

    String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play);
        ButterKnife.bind(this);

        back.setOnClickListener(v -> onBackPressed());

        lang = SharedPrefManager.getLanguage(this);

        int position = getIntent().getIntExtra("position", 0);

        setBackByPosition(position);
    }

    private void setBackByPosition(int position) {
        switch (position) {
            case 0: {
                setShulteInfo();
                break;
            }
            case 1: {
                setZapomniChislo();
                break;
            }
            case 2: {
                setFindNumber();
                break;
            }
            case 3: {
                setNumZnaki();
                break;
            }
            case 4: {
                setEquation();
                break;
            }
            case 5: {
                setShulteLetter();
                break;
            }
            case 6: {
                setRemWords();
                break;
            }
        }
    }

    private void setShulteLetter() {
        setInfoByType(R.drawable.kak_igrat_letter, R.color.shulteLetterColor, R.string.SchulteLetterInfo, R.drawable.shulte_letter_back);
    }

    private void setRemWords() {
        if (lang != null) {
            switch (lang) {
                case "en": case "es": {
                    setInfoByType(R.drawable.kak_igrat_rem_en, R.color.remWordsColor, R.string.RemWordsInfo, R.drawable.rem_words_back);
                    break;
                }
                case "ru": case "kk": {
                    setInfoByType(R.drawable.kak_igrat_rem_ru, R.color.remWordsColor, R.string.RemWordsInfo, R.drawable.rem_words_back);
                }
            }
        } else {
            setInfoByType(R.drawable.kak_igrat_rem_en, R.color.remWordsColor, R.string.RemWordsInfo, R.drawable.rem_words_back);
        }
    }

    private void setEquation() {
        setInfoByType(R.drawable.kak_igrat_equation, R.color.equationColor, R.string.EquationInfo, R.drawable.equation_back);
    }

    private void setZapomniChislo() {
        setInfoByType(R.drawable.kak_igrat_zapomni_chislo_icon, R.color.pamiatColor, R.string.RemNumInfo, R.drawable.zapomni_slovo_back);
    }

    private void setShulteInfo() {
        setInfoByType(R.drawable.kak_igrat_shulte, R.color.vnimanieColor, R.string.SchulteInfo, R.drawable.shulte_back);
    }

    private void setFindNumber() {
        setInfoByType(R.drawable.kak_igrat_find_number, R.color.findColor, R.string.FindNumberInfo, R.drawable.find_back);
    }

    private void setNumZnaki() {
        setInfoByType(R.drawable.kak_igrat_num_znaki, R.color.numZnakiColor, R.string.NumberZnakiInfo, R.drawable.number_znaki_back);
    }

    private void setInfoByType(int resource, int color, int text, int back) {
        pravila.setTextColor(getResources().getColor(color));
        backgroundIcon.setBackgroundResource(back);
        setMeasures(kakIcon, resource);
        description.setText(Html.fromHtml("<span style=\"text-align: justify; \">" + getString(text) + "</span>"));
    }

    private void setMeasures(ImageView image, int resource) {
        image.setImageResource(resource);
        Drawable drawable = getResources().getDrawable(resource);
        int width = drawable.getIntrinsicWidth() / 2 - 20;
        int height = drawable.getIntrinsicHeight() / 2 - 20;
        image.getLayoutParams().width = width;
        image.getLayoutParams().height = height;
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0,0);
    }
}
