package kz.almaty.boombrains.ui.main_pages;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.SpinKitView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.adapters.profile_adapters.games_start.SingleFriendAdapter;
import kz.almaty.boombrains.adapters.profile_adapters.games_start.SingleWorldAdapter;
import kz.almaty.boombrains.ui.game_pages.shulte_page.ShulteLevel;
import kz.almaty.boombrains.ui.game_pages.start_page.AreYouReadyActivity;
import kz.almaty.boombrains.helpers.SharedPrefManager;
import kz.almaty.boombrains.helpers.SharedUpdate;
import kz.almaty.boombrains.models.rating_model.WorldRecord;
import kz.almaty.boombrains.models.rating_model.WorldRecordResponse;
import kz.almaty.boombrains.models.records_model.RecordResponse;
import kz.almaty.boombrains.ui.sign_pages.login_pages.MainLoginActivity;
import kz.almaty.boombrains.viewmodel.profile_view_model.profile_add_friends.add_view_model.AddFriendView;
import kz.almaty.boombrains.viewmodel.profile_view_model.profile_add_friends.add_view_model.AddFriendViewModel;
import kz.almaty.boombrains.viewmodel.rating_view_model.WorldRatingView;
import kz.almaty.boombrains.viewmodel.rating_view_model.WorldRatingViewModel;

@SuppressLint("SetTextI18n")
public class GamesStartActivity extends AppCompatActivity implements WorldRatingView, AddFriendView,
        SingleFriendAdapter.SingleFriendsListener, SingleWorldAdapter.SingleWorldListener {

    @BindView(R.id.name_of_game) TextView nameOfGame;
    @BindView(R.id.gameRecordTxt) TextView gameRecord;
    @BindView(R.id.playBtn) Button playBtn;
    @BindView(R.id.game_image) ImageView gameImage;
    @BindView(R.id.startBackConst) ConstraintLayout backConst;
    @BindView(R.id.kubok_grad_id) ConstraintLayout recordLayout;
    @BindView(R.id.bottomLayerConst) ConstraintLayout bottomLayer;
    @BindView(R.id.back_to_main) RelativeLayout backToMain;
    @BindView(R.id.kakIgratConst) ConstraintLayout kakIgratBtn;
    @BindView(R.id.scrollStart) ScrollView scroll;
    @BindView(R.id.progressSpin2) SpinKitView progress;
    @BindView(R.id.progressFriend) SpinKitView progressFriend;
    @BindView(R.id.networkTxt) TextView net1;
    @BindView(R.id.networkTxt2) TextView net2;
    @BindView(R.id.friendsRV) RecyclerView friendsRv;
    @BindView(R.id.worldRV) RecyclerView worldRv;
    @BindView(R.id.addFriendLayout) ConstraintLayout addFriend;

    @BindView(R.id.myRecordConst) ConstraintLayout myRecordLayout;
    @BindView(R.id.myWorldRecordConst) ConstraintLayout myWorldLayout;
    @BindView(R.id.friendName) TextView friendNameTxt;
    @BindView(R.id.friendRecord) TextView friendRecord;
    @BindView(R.id.worldName) TextView worldNameTxt;
    @BindView(R.id.worldRecord) TextView worldRecordTxt;

    SingleFriendAdapter friendsAdapter;
    SingleWorldAdapter worldAdapter;
    AddFriendViewModel addFriendViewModel;

    String gameName;
    int position;
    WorldRatingViewModel worldRatingViewModel;

    private EditText userDialogEdit;
    private Button addDialogBtn;
    private TextView errorTxt;
    private Dialog dialogAdd;
    private ConstraintLayout closeAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        position = getIntent().getIntExtra("position", 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_start);
        ButterKnife.bind(this);

        worldRatingViewModel = ViewModelProviders.of(this).get(WorldRatingViewModel.class);
        addFriendViewModel = ViewModelProviders.of(this).get(AddFriendViewModel.class);

        gameName = getIntent().getStringExtra("gameName");
        nameOfGame.setText(gameName);
        setBackgrounds(position);

        setNetWorkData();

        loadAdFriendDialog();

        backToMain.setOnClickListener(v -> onBackPressed());

        worldRatingViewModel.getWorldRatings(getGameName(position), this, this);

        playBtn.setOnClickListener(v -> setGames(position));

        if (SharedPrefManager.isUserLoggedIn(this)) {
            if (SharedPrefManager.isNetworkOnline(this)) {
                net1.setVisibility(View.GONE);
                net2.setVisibility(View.GONE);
            } else {
                net1.setVisibility(View.VISIBLE);
                net2.setVisibility(View.VISIBLE);
            }
        } else {
            net1.setVisibility(View.VISIBLE);
            net1.setText(getString(R.string.MainProfInfo));
            net2.setVisibility(View.VISIBLE);
            net2.setText(getString(R.string.MainProfInfo));
        }

        kakIgratBtn.setOnClickListener(v -> {
            startIntent(new Intent(getApplication(), HowToPlayActivity.class), position);
            overridePendingTransition(0,0);
        });

        setClickListeners();
    }

    private void setClickListeners() {
        addFriend.setOnClickListener(v -> dialogAdd.show());

        addDialogBtn.setOnClickListener(v -> {
            if (SharedPrefManager.isUserLoggedIn(getApplication()) && SharedPrefManager.isNetworkOnline(getApplication())) {
                if (!userDialogEdit.getText().toString().equals("")) {
                    addFriendViewModel.addNewFriends(userDialogEdit.getText().toString(), getApplication(), this);
                } else {
                    SharedUpdate.showToast(0, getString(R.string.EmptyFields), getApplication());
                }
            } else {
                SharedUpdate.showToast(4, getString(R.string.CheckConnection), getApplication());
            }
        });

        closeAdd.setOnClickListener(v -> {
            dialogAdd.dismiss();
            errorTxt.setVisibility(View.GONE);
            userDialogEdit.setText("");
        });
    }

    private void loadAdFriendDialog() {
        dialogAdd = new Dialog(this, R.style.dialogTheme);
        dialogAdd.setContentView(R.layout.add_friend_layout);
        addDialogBtn = dialogAdd.findViewById(R.id.addNewFriend);
        errorTxt = dialogAdd.findViewById(R.id.errorTxt);
        userDialogEdit = dialogAdd.findViewById(R.id.userNameEdit);
        closeAdd = dialogAdd.findViewById(R.id.closeConst);
        dialogAdd.setCanceledOnTouchOutside(true);
    }

    private String getGameName(int position) {
        String name = "";
        switch (position) {
            case 0: name = "shulteTable"; break;
            case 1: name = "rememberNumber"; break;
            case 2: name = "findNumber"; break;
            case 3: name = "calculation"; break;
            case 4: name = "equation"; break;
            case 5: name = "shulteLetters"; break;
            case 6: name = "rememberWords"; break;
            case 7: name = "memorySquare"; break;
            case 8: name = "coloredWords"; break;
            case 9: name = "coloredFigures"; break;
        }
        return name;
    }

    private void setBackgrounds(int position) {
        switch (position) {
            case 0: { setShulteBackgrounds();break; }
            case 1: { setZapomniChisloBackgrounds();break; }
            case 2: { setFindBackgrounds();break; }
            case 3: { setNumZnakiBackgrounds();break; }
            case 4: { setEquationBackgrounds();break; }
            case 5: { setShulteLetterBackgrounds();break; }
            case 6: { setRemWordsBackgrounds();break; }
            case 7: { setSquareBackgrounds();break; }
            case 8: { setColorBackgrounds();break; }
            case 9: { setFigureBackgrounds();break; }
        }
    }

    private void setGames(int position) {
        switch (position) {
            case 0: {
                startIntent(new Intent(this, ShulteLevel.class), position);
                break;
            }
            case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: {
                startIntent(new Intent(this, AreYouReadyActivity.class), position);
                break;
            }
        }
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    private void startIntent(Intent intent, int position) {
        intent.putExtra("position", position);
        startActivity(intent);
    }

    private void setBackgroundsByType(int game, int play, int kubok, int bottom, int back) {
        playBtn.setTextColor(getResources().getColor(play));
        gameImage.setImageResource(game);
        recordLayout.setBackgroundResource(kubok);
        bottomLayer.setBackgroundResource(bottom);
        backConst.setBackgroundResource(back);
        scroll.setBackgroundResource(bottom);
    }

    private void setFigureBackgrounds() {
        setBackgroundsByType(R.drawable.shape_top_icon, R.color.topShape, R.drawable.figure_kubok_grad, R.color.underShape,
                R.drawable.shape_back);
        setRecords(SharedPrefManager.getFigureRecord(getApplication()));
    }

    private void setColorBackgrounds() {
        setBackgroundsByType(R.drawable.color_top_icon, R.color.topColor, R.drawable.color_kubok_grad, R.color.underColor,
                R.drawable.color_back);
        setRecords(SharedPrefManager.getColorRecord(getApplication()));
    }

    private void setEquationBackgrounds() {
        setBackgroundsByType(R.drawable.equation_top_icon, R.color.equationColor, R.drawable.equation_kubok_grad, R.color.underEquation,
                R.drawable.equation_back);
        setRecords(SharedPrefManager.getEquationRecord(getApplication()));
    }

    private void setShulteBackgrounds() {
        setBackgroundsByType(R.drawable.shult_top_icon, R.color.vnimanieColor, R.drawable.shulte_kubok_grad, R.color.underShulte,
                R.drawable.shulte_back);
        setRecords(SharedPrefManager.getShulteRecord(getApplication()));
    }

    private void setFindBackgrounds() {
        setBackgroundsByType(R.drawable.find_top_back, R.color.findColor, R.drawable.find_kubok_grad, R.color.underFind,
                R.drawable.find_back);
        setRecords(SharedPrefManager.getFindRecord(getApplication()));
    }

    private void setZapomniChisloBackgrounds() {
        setBackgroundsByType(R.drawable.zapomni_chislo_top_icon, R.color.pamiatColor, R.drawable.zapomni_kubok_grad, R.color.underSlovo,
                R.drawable.zapomni_slovo_back);
        setRecords(SharedPrefManager.getChisloRecord(getApplication()));
    }

    private void setNumZnakiBackgrounds() {
        setBackgroundsByType(R.drawable.number_znaki_top_icon, R.color.numZnakiColor, R.drawable.num_znaki_kubok_grad, R.color.underNumZnaki,
                R.drawable.number_znaki_back);
        setRecords(SharedPrefManager.getNumZnakiRecord(getApplication()));
    }

    private void setShulteLetterBackgrounds() {
        setBackgroundsByType(R.drawable.shulte_letter_top_icon, R.color.shulteLetterColor, R.drawable.shulte_letter_kubok_grad, R.color.underShulteLetter,
                R.drawable.shulte_letter_back);
        setRecords(SharedPrefManager.getShulteLetterRecord(getApplication()));
    }

    private void setRemWordsBackgrounds() {
        setBackgroundsByType(R.drawable.rem_words_top_icon, R.color.remWordsColor, R.drawable.rem_words_kubok_grad, R.color.underRemWords,
                R.drawable.rem_words_back);
        setRecords(SharedPrefManager.getSlovoRecord(getApplication()));
    }

    private void setSquareBackgrounds() {
        setBackgroundsByType(R.drawable.square_top_icon, R.color.topSquare, R.drawable.square_kubok_grad, R.color.underSquare,
                R.drawable.square_back);
        setRecords(SharedPrefManager.getSquareRecord(getApplication()));
    }

    @SuppressLint("SetTextI18n")
    private void setRecords(String record) {
        if (record != null) {
            gameRecord.setText(record);
        } else {
            gameRecord.setText("0");
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void success(WorldRecordResponse response) {
        String username = SharedPrefManager.getUserName(this);
        if (SharedPrefManager.isNetworkOnline(getApplication())) {
            if (SharedPrefManager.isUserLoggedIn(getApplication())) {
                List<WorldRecord> list = response.getWorldRecords();
                for (WorldRecord record : list) {
                    if (record.getUsername().equals(username)) {
                        record.setMe(true);
                    }
                }
                List<WorldRecord> friendList = response.getFriendRecords();
                for (WorldRecord record : friendList) {
                    if (record.getUsername().equals(username)) {
                        record.setMe(true);
                    }
                }
                setFriendRecords(friendList);
                setMyRecords(friendList, myRecordLayout, friendNameTxt, friendRecord);
                setWorldRecords(list);
                setMyRecords(list, myWorldLayout, worldNameTxt, worldRecordTxt);
            }
        }
    }

    private void setMyRecords(List<WorldRecord> records, ConstraintLayout layout, TextView nameTxt, TextView recordTxt) {
        for (WorldRecord record : records) {
            String name = record.getUsername();
            if (name.equals(SharedPrefManager.getUserName(getApplication()))) {
                if (record.getPosition() > 4) {
                    layout.setVisibility(View.VISIBLE);
                    nameTxt.setText(record.getPosition() + ". " + record.getUsername());
                    recordTxt.setText(record.getRecord() + "");
                } else {
                    layout.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    private void setFriendRecords(List<WorldRecord> records) {
        friendsAdapter = new SingleFriendAdapter(this, records, this);
        friendsRv.setAdapter(friendsAdapter);
        friendsRv.setLayoutManager(new LinearLayoutManager(this));
        friendsRv.setItemAnimator(new DefaultItemAnimator());
        friendsAdapter.notifyDataSetChanged();
    }

    private void setWorldRecords(List<WorldRecord> records) {
        worldAdapter = new SingleWorldAdapter(this, records, this);
        worldRv.setAdapter(worldAdapter);
        worldRv.setLayoutManager(new LinearLayoutManager(this));
        worldRv.setItemAnimator(new DefaultItemAnimator());
        worldAdapter.notifyDataSetChanged();
    }

    private void setNetWorkData() {
        if (!SharedPrefManager.isNetworkOnline(getApplication())) {
            myRecordLayout.setVisibility(View.INVISIBLE);
            myWorldLayout.setVisibility(View.INVISIBLE);
        }
        if (!SharedPrefManager.isUserLoggedIn(getApplication())) {
            myRecordLayout.setVisibility(View.INVISIBLE);
            myWorldLayout.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void error(String message) {
        Log.d("ERROR::", message);
    }

    @Override
    public void showLoading() {
        progressFriend.setVisibility(View.VISIBLE);
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressFriend.setVisibility(View.GONE);
        progress.setVisibility(View.GONE);
    }

    @Override
    public void errorMessage(int code) {
        if (SharedPrefManager.isNetworkOnline(getApplication())) {
            if (SharedPrefManager.isUserLoggedIn(getApplication())) {
                if (code == 401) {
                    SharedPrefManager.clear(getApplication());
                    SharedPrefManager.clearUserData(getApplication());
                    startActivity(new Intent(getApplication(), MainLoginActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                } else if (code == 404) {
                    Toast.makeText(getApplication(), "Server not found!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void setWorldItems(View view, RecyclerView rv) {
        int height = rv.getHeight();
        view.getLayoutParams().height = height / 4;
    }

    @Override
    public void onAddFriendSuccess() {
        errorTxt.setVisibility(View.VISIBLE);
        errorTxt.setTextColor(Color.parseColor("#25C82C"));
        errorTxt.setText("Friend request sent!");
        new Handler().postDelayed(()-> {
            errorTxt.setVisibility(View.GONE);
            userDialogEdit.setText("");
            dialogAdd.dismiss();
        }, 1200);
    }

    @Override
    public void onAddFriendError() {
        SharedUpdate.showToast(0, "Error with adding friends!", getApplication());
    }

    @Override
    public void onAddFriendErrorCode(int code) {
        switch (code) {
            case 401: {
                SharedUpdate.showToast(4, "Another user logged in", getApplication());
                SharedPrefManager.clearAndExit(this);
                break;
            }
            case 404: {
                SharedUpdate.showToast(4, "Server not found", getApplication());
                break;
            }
            default: break;
        }
    }

    @Override
    public void onFieldsNotCorrect(RecordResponse response) {
        errorTxt.setVisibility(View.VISIBLE);
        errorTxt.setTextColor(Color.parseColor("#FF6969"));
        switch (response.getStatusCode()) {
            case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10: {
                errorTxt.setText(response.getMessage());
                break;
            }
        }
    }

    @Override
    public void setFriendSize(View view) {
        setWorldItems(view, friendsRv);
    }

    @Override
    public void setFriendClicked(WorldRecord record) { }

    @Override
    public void setWorldSize(View view) {
        setWorldItems(view, worldRv);
    }

    @Override
    public void setWorldClicked(WorldRecord record) { }
}
