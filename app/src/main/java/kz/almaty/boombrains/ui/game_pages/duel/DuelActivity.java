package kz.almaty.boombrains.ui.game_pages.duel;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.almaty.boombrains.R;

public class DuelActivity extends AppCompatActivity {

    @BindView(R.id.duelTxt) TextView duelTxt;
    @BindView(R.id.girlImg) CircularImageView girlImg;
    @BindView(R.id.boyImg) CircularImageView boyImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.duel_activity);
        ButterKnife.bind(this);

        Glide.with(this).load(R.drawable.boy_1)
                .into(boyImg);

        Glide.with(this).load(R.drawable.girl_1)
                .into(girlImg);
    }
}
