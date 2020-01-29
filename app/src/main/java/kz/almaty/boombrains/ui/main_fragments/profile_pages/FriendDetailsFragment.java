package kz.almaty.boombrains.ui.main_fragments.profile_pages;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.ui.adapters.profile_adapters.profile_start.AchieveAdapter;
import kz.almaty.boombrains.util.files.RememberWordsRu;
import kz.almaty.boombrains.util.helpers.SharedPrefManager;
import kz.almaty.boombrains.util.helpers.SharedUpdate;
import kz.almaty.boombrains.ui.main_pages.MainActivity;
import kz.almaty.boombrains.data.models.profile_model.Achievement;
import kz.almaty.boombrains.data.models.profile_model.ProfileRatingModel;
import kz.almaty.boombrains.viewmodel.profile_view_model.profile_add_friends.delete_view_model.DeleteView;
import kz.almaty.boombrains.viewmodel.profile_view_model.profile_add_friends.delete_view_model.DeleteViewModel;
import kz.almaty.boombrains.viewmodel.profile_view_model.profile_add_friends.friend_info_view_model.FriendsInfoView;
import kz.almaty.boombrains.viewmodel.profile_view_model.profile_add_friends.friend_info_view_model.FriendsInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendDetailsFragment extends Fragment implements FriendsInfoView, DeleteView, AchieveAdapter.AchieveListener {

    @BindView(R.id.prof_user_name) TextView usernameTxt;
    @BindView(R.id.ratingRecord) TextView recordTxt;
    @BindView(R.id.back_to_main) RelativeLayout back;
    @BindView(R.id.deleteBtn) RelativeLayout deleteBtn;
    @BindView(R.id.prof_email) TextView userIdTxt;
    @BindView(R.id.leagueIcon) ImageView leagueImg;
    @BindView(R.id.achieveRecycler) RecyclerView achieveRecycler;
    @BindView(R.id.mainLayout) ConstraintLayout mainLayout;
    @BindView(R.id.mainLoading2) SpinKitView loadingIcon;

    private AchieveAdapter adapter;
    private FriendsInfoViewModel friendsInfoViewModel;
    private DeleteViewModel deleteViewModel;
    private int record;
    private String username;

    // Dialog items
    private Dialog dialog;
    private Button deleteDialog, cancelDialog;
    private ConstraintLayout close;
    private SpinKitView progress;
    private TextView tv;

    public FriendDetailsFragment() { }

    @Override
    public void setViewSize(View view) {
        setSizes(view);
    }

    @Override
    public void setViewClicked(Achievement model) {

    }

    public interface FriendDetailsCallback {
        void onFriendSuccess();
        void onFriendError();
    }

    private FriendDetailsCallback mCallback;
    private MainActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_details, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        back.setOnClickListener(v -> getActivity().onBackPressed());
        mCallback.onFriendSuccess();

        username = getArguments().getString("username");
        record = getArguments().getInt("record");
        String world = getArguments().getString("world");

        if (world != null) {
            if (world.equals("world")) {
                deleteBtn.setVisibility(View.INVISIBLE);
            }
        }

        friendsInfoViewModel = ViewModelProviders.of(this).get(FriendsInfoViewModel.class);
        deleteViewModel = ViewModelProviders.of(this).get(DeleteViewModel.class);
        if (SharedPrefManager.isNetworkOnline(getActivity())) {
            friendsInfoViewModel.getFriendInfo(username, record, getActivity(), this);
        }

        setupDialog();

        deleteBtn.setOnClickListener(v -> dialog.show());
        setListeners(username);
    }

    private void setListeners(String user) {
        close.setOnClickListener(v -> dialog.dismiss());
        cancelDialog.setOnClickListener(v -> dialog.cancel());
        deleteDialog.setOnClickListener(v -> deleteViewModel.deleteFriend(user, getActivity(), this));
    }

    private void setupDialog() {
        dialog = new Dialog(getActivity(), R.style.dialogTheme);
        dialog.setContentView(R.layout.delete_alert);
        deleteDialog = dialog.findViewById(R.id.deleteDialog);
        cancelDialog = dialog.findViewById(R.id.cancelDialog);
        close = dialog.findViewById(R.id.closeConst);
        progress = dialog.findViewById(R.id.progressRequest);
        tv = dialog.findViewById(R.id.textView16);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mActivity = (MainActivity) context;
        }
        mCallback = (FriendDetailsFragment.FriendDetailsCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void successDelete() {
        Log.d("SUCCESS::", "DELETED");
        SharedUpdate.showToast(5, getString(R.string.DeleteSuccess), getActivity());
        new Handler().postDelayed(()-> getActivity().onBackPressed(),1000);
    }

    @Override
    public void errorDelete() {
        SharedUpdate.showToast(5, getString(R.string.ErrDeleteUser), getActivity());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onWholeInfo(ProfileRatingModel model) {
        usernameTxt.setText(model.getUsername());
        userIdTxt.setText(model.getUid());
        recordTxt.setText("" + record);
        setLeagues(model.getLeague(), model.getStar());

        if (isAdded() && getActivity() != null) {
            List<Achievement> new_achievs = new ArrayList<>();
            for (int i = 0; i < model.getAchievements().size(); i++) {
                if (model.getAchievements().size() >= 12) {
                    Achievement achievement = model.getAchievements().get(i);
                    achievement.setResource(RememberWordsRu.list_on.get(i));
                    new_achievs.add(achievement);
                }
            }
            setAchievements(new_achievs);
        }
    }

    @Override
    public void loading() {
        progress.setVisibility(View.VISIBLE);
        showAndHide(View.INVISIBLE);
    }

    @Override
    public void hide() {
        progress.setVisibility(View.GONE);
    }

    @Override
    public void loadingData() {
        mainLayout.setVisibility(View.INVISIBLE);
        loadingIcon.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadingFinished() {
        mainLayout.setVisibility(View.VISIBLE);
        loadingIcon.setVisibility(View.GONE);
    }

    private void showAndHide(int v) {
        cancelDialog.setVisibility(v);
        deleteDialog.setVisibility(v);
        tv.setVisibility(v);
    }

    @Override
    public void onErrorOccurred(int error) {
        switch (error) {
            case 401: {
                SharedUpdate.showToast(4, getString(R.string.TokenDeleted), getContext());
                SharedPrefManager.clearAndExit(getActivity());
                break;
            }
            case 404: {
                SharedUpdate.showToast(4, getString(R.string.ServerNotFound), getContext());
                break;
            }
            default: break;
        }
    }

    @Override
    public void onFailed() {
        showAndHide(View.VISIBLE);
        progress.setVisibility(View.GONE);
        Log.d("FAILED::", "TO LOAD THE DATA");
    }

    private void setAchievements(List<Achievement> list) {
        adapter = new AchieveAdapter(list, getActivity(), this);
        achieveRecycler.setAdapter(adapter);
        achieveRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        achieveRecycler.setItemAnimator(new DefaultItemAnimator());
        adapter.notifyDataSetChanged();
    }

    private void setLeagues(String league, Integer star) {
        switch (league) {
            case "bronze": {
                switch (star) {
                    case 1: setStar(R.drawable.bronze1); break;
                    case 2: setStar(R.drawable.bronze2); break;
                    case 3: setStar(R.drawable.bronze3); break;
                }
                break;
            }
            case "silver": {
                switch (star) {
                    case 1: setStar(R.drawable.silver1); break;
                    case 2: setStar(R.drawable.silver2); break;
                    case 3: setStar(R.drawable.silver3); break;
                }
                break;
            }
            case "gold": {
                switch (star) {
                    case 1: setStar(R.drawable.gold1); break;
                    case 2: setStar(R.drawable.gold2); break;
                    case 3: setStar(R.drawable.gold3);break;
                }
                break;
            }
            default: {
                setStar(R.drawable.gold3);
                break;
            }
        }
    }

    private void setStar(int res) {
        leagueImg.setImageResource(res);
    }

    private void setSizes(View view) {
        int width = achieveRecycler.getWidth();
        view.getLayoutParams().width = width / 3 - 35;
        view.getLayoutParams().height = width / 3 + 20;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dialog.dismiss();
    }
}
