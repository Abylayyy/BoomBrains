package kz.almaty.boombrains.ui.main_fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.github.ybq.android.spinkit.SpinKitView;
import com.zerobranch.layout.SwipeLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.data.models.profile_model.Achievement;
import kz.almaty.boombrains.data.models.profile_model.ProfileRatingModel;
import kz.almaty.boombrains.data.models.profile_model.ProfileWorldRecord;
import kz.almaty.boombrains.ui.adapters.ChooseFriendAdapter;
import kz.almaty.boombrains.ui.main_pages.GameListActivity;
import kz.almaty.boombrains.ui.main_pages.MainActivity;
import kz.almaty.boombrains.viewmodel.profile_view_model.profile_ratings.ProfileRatingView;
import kz.almaty.boombrains.viewmodel.profile_view_model.profile_ratings.ProfileRatingViewModel;

import static android.app.Activity.RESULT_OK;


public class DuelFragment extends Fragment implements ProfileRatingView {

    // friends layout
    @BindView(R.id.plusTxt) TextView plusTxt;
    @BindView(R.id.spinner) Spinner spinner;

    // games layout
    @BindView(R.id.swipe0) SwipeLayout gameRel0;
    @BindView(R.id.inLayout0) ConstraintLayout inLayout0;
    @BindView(R.id.gameImg0) ImageView gameImg0;
    @BindView(R.id.gameName0) TextView gameName0;

    @BindView(R.id.swipe1) SwipeLayout gameRel1;
    @BindView(R.id.inLayout1) ConstraintLayout inLayout1;
    @BindView(R.id.gameImg1) ImageView gameImg1;
    @BindView(R.id.gameName1) TextView gameName1;

    @BindView(R.id.swipe2) SwipeLayout gameRel2;
    @BindView(R.id.inLayout2) ConstraintLayout inLayout2;
    @BindView(R.id.gameImg2) ImageView gameImg2;
    @BindView(R.id.gameName2) TextView gameName2;

    @BindView(R.id.challengeBtn) Button challengeBtn;
    @BindView(R.id.waitTxt) TextView wait;
    @BindView(R.id.spin_kit) SpinKitView spin;

    private static final int GAME0_RESULT = 11;
    private static final int GAME1_RESULT = 22;
    private static final int GAME2_RESULT = 33;

    private boolean isFriendSelected = false;
    private boolean areGamesSelected = false;
    private MainActivity mActivity;
    private String username = null;
    private List<String> games = new ArrayList<>(Arrays.asList("", "", ""));

    public DuelFragment() {}

    private DuelCallBack mCallback;

    public interface DuelCallBack {
        void sendChallenge(String username, List<String> gameList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_duel, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setViewListeners();
        challengeBtn.setEnabled(false);

        ProfileRatingViewModel viewModel = ViewModelProviders.of(this).get(ProfileRatingViewModel.class);
        viewModel.getProfileRatings(getContext(), this);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setViewListeners() {
        gameRel0.setOnActionsListener(new SwipeLayout.SwipeActionsListener() {
            @Override
            public void onOpen(int direction, boolean isContinuous) {
                gameRel0.close(false);
                setBacks(inLayout0, gameImg0, gameName0);
                games.set(0, "");
            }
            @Override
            public void onClose() { }
        });

        gameRel1.setOnActionsListener(new SwipeLayout.SwipeActionsListener() {
            @Override
            public void onOpen(int direction, boolean isContinuous) {
                gameRel1.close(false);
                setBacks(inLayout1, gameImg1, gameName1);
                games.set(1, "");
            }
            @Override
            public void onClose() { }
        });
        gameRel2.setOnActionsListener(new SwipeLayout.SwipeActionsListener() {
            @Override
            public void onOpen(int direction, boolean isContinuous) {
                gameRel2.close(false);
                setBacks(inLayout2, gameImg2, gameName2);
                games.set(2, "");
            }
            @Override
            public void onClose() { }
        });

        challengeBtn.setOnClickListener(this::onClick);
        inLayout0.setOnClickListener(this::onClick);
        inLayout1.setOnClickListener(this::onClick);
        inLayout2.setOnClickListener(this::onClick);
    }

    private void setBacks(ConstraintLayout l, ImageView i, TextView t) {
        l.setBackgroundResource(R.drawable.trans_color);
        i.setImageResource(R.drawable.trans_color);
        t.setText("");
    }

    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.inLayout0: {
                resultIntent(GAME0_RESULT);
                break;
            }
            case R.id.inLayout1: {
                resultIntent(GAME1_RESULT);
                break;
            }
            case R.id.inLayout2: {
                resultIntent(GAME2_RESULT);
                break;
            }
            case R.id.challengeBtn: {
                List<String> gamesList = new ArrayList<>();
                for (String i : games) {
                    if (!i.equals("")) {
                        gamesList.add(i);
                    }
                }
                if (username != null && gamesList.size() > 0) {
                    mCallback.sendChallenge(username, gamesList);
                    hideLoading();
                    gamesList.clear();
                } else {
                    disableBtn();
                }
                break;
            }
        }
    }

    private void disableBtn() {
        challengeBtn.setEnabled(false);
        areGamesSelected = false;
        challengeBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.disbled_color)));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case GAME0_RESULT: {
                if (resultCode == RESULT_OK) {
                    areGamesSelected = true;
                    setGame0Info(data);
                }
                break;
            }
            case GAME1_RESULT: {
                if (resultCode == RESULT_OK) {
                    areGamesSelected = true;
                    setGame1Info(data);
                }
                break;
            }
            case GAME2_RESULT: {
                if (resultCode == RESULT_OK) {
                    areGamesSelected = true;
                    setGame2Info(data);
                }
                break;
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mActivity = (MainActivity) context;
        }
        mCallback = (DuelFragment.DuelCallBack) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setGame2Info(Intent intent) {
        gameImg2.setImageResource(intent.getIntExtra("icon", 0));
        gameName2.setText(getString(intent.getIntExtra("name", 0)));
        inLayout2.setBackgroundResource(intent.getIntExtra("back", 0));
        inLayout2.setVisibility(View.VISIBLE);
        setButtonEnabled();
        addGame(2, intent);
    }

    private void setGame1Info(Intent intent) {
        gameImg1.setImageResource(intent.getIntExtra("icon", 0));
        gameName1.setText(getString(intent.getIntExtra("name", 0)));
        inLayout1.setBackgroundResource(intent.getIntExtra("back", 0));
        inLayout1.setVisibility(View.VISIBLE);
        setButtonEnabled();
        addGame(1, intent);
    }

    private void setGame0Info(Intent intent) {
        gameImg0.setImageResource(intent.getIntExtra("icon", 0));
        gameName0.setText(getString(intent.getIntExtra("name", 0)));
        inLayout0.setBackgroundResource(intent.getIntExtra("back", 0));
        inLayout0.setVisibility(View.VISIBLE);
        setButtonEnabled();
        addGame(0, intent);
    }

    private void addGame(int index, Intent intent) {
        String name = getGameName(intent.getIntExtra("name", 0));
        games.set(index, name);
    }

    private void setButtonEnabled() {
        if (isFriendSelected && areGamesSelected) {
            challengeBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.addFriendColor)));
            challengeBtn.setEnabled(true);
        }
    }



    private String getGameName(int game) {
        String name = "";
        switch (game) {
            case R.string.AttentionSchulteTable: name = "shulteTable"; break;
            case R.string.MemoryRemNum: name = "rememberNumber"; break;
            case R.string.AttentionFigure: name = "findNumber"; break;
            case R.string.NumberZnaki: name = "calculation"; break;
            case R.string.Equation: name = "equation"; break;
            case R.string.ShulteLetters: name = "shulteLetters"; break;
            case R.string.RememberWords: name = "rememberWords"; break;
            case R.string.SquareMemory: name = "memorySquare"; break;
            case R.string.Colors: name = "coloredWords"; break;
            case R.string.Figures: name = "coloredFigures"; break;
        }
        return name;
    }

    private void hideLoading() {
        wait.setVisibility(View.VISIBLE);
        spin.setVisibility(View.VISIBLE);
        challengeBtn.setVisibility(View.INVISIBLE);

        if (getActivity() != null && isAdded()) {
            new Handler().postDelayed(() -> getActivity().runOnUiThread(() -> {
                wait.setVisibility(View.INVISIBLE);
                spin.setVisibility(View.INVISIBLE);
                challengeBtn.setVisibility(View.VISIBLE);
            }), 2000);
        }
    }

    private void showSpinner(List<ProfileWorldRecord> list) {
        ChooseFriendAdapter adapter = new ChooseFriendAdapter(getContext(), list);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    plusTxt.setVisibility(View.VISIBLE);
                } else {
                    ProfileWorldRecord model = (ProfileWorldRecord) parent.getItemAtPosition(position);
                    isFriendSelected = true;
                    username = model.getUsername();
                    plusTxt.setVisibility(View.INVISIBLE);
                    setButtonEnabled();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                plusTxt.setVisibility(View.VISIBLE);
            }
        });
    }

    private void resultIntent(int result) {
        startActivityForResult(new Intent(getActivity(), GameListActivity.class), result);
    }

    @Override
    public void onWorldRecords(List<ProfileWorldRecord> records) { }

    @Override
    public void onFriendsRecord(List<ProfileWorldRecord> records) {
        if (isAdded() && getActivity() != null) { showSpinner(records); }
    }

    // Not necessary callbacks
    @Override
    public void onAchievements(List<Achievement> achievements) { }

    @Override
    public void onWholeInfo(ProfileRatingModel model) { }

    @Override
    public void onErrorOccurred(int error) { }

    @Override
    public void onFailed() { }

    @Override
    public void loadingData() { }

    @Override
    public void loadingFinished() { }
}
