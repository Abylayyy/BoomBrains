package kz.almaty.boombrains.ui.main_fragments.profile_pages;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.ybq.android.spinkit.SpinKitView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.adapters.profile_adapters.profile_start.AchieveAdapter;
import kz.almaty.boombrains.adapters.profile_adapters.profile_start.FriendRequestsAdapter;
import kz.almaty.boombrains.adapters.profile_adapters.profile_start.FriendsAdapter;
import kz.almaty.boombrains.adapters.profile_adapters.profile_start.PotionAdapter;
import kz.almaty.boombrains.adapters.profile_adapters.profile_start.WorldAdapter;
import kz.almaty.boombrains.files.RememberWordsRu;
import kz.almaty.boombrains.helpers.SharedPrefManager;
import kz.almaty.boombrains.helpers.SharedUpdate;
import kz.almaty.boombrains.helpers.SpaceItemDecoration;
import kz.almaty.boombrains.models.profile_model.PotionModel;
import kz.almaty.boombrains.ui.main_pages.MainActivity;
import kz.almaty.boombrains.models.add_friend_models.RequestListModel;
import kz.almaty.boombrains.models.profile_model.Achievement;
import kz.almaty.boombrains.models.profile_model.ProfileRatingModel;
import kz.almaty.boombrains.models.profile_model.ProfileWorldRecord;
import kz.almaty.boombrains.models.records_model.RecordResponse;
import kz.almaty.boombrains.ui.sign_pages.login_pages.MainSignInActivity;
import kz.almaty.boombrains.viewmodel.profile_view_model.profile_add_friends.accept_view_model.AcceptView;
import kz.almaty.boombrains.viewmodel.profile_view_model.profile_add_friends.accept_view_model.AcceptViewModel;
import kz.almaty.boombrains.viewmodel.profile_view_model.profile_add_friends.add_view_model.AddFriendView;
import kz.almaty.boombrains.viewmodel.profile_view_model.profile_add_friends.add_view_model.AddFriendViewModel;
import kz.almaty.boombrains.viewmodel.profile_view_model.profile_add_friends.reject_view_model.RejectView;
import kz.almaty.boombrains.viewmodel.profile_view_model.profile_add_friends.reject_view_model.RejectViewModel;
import kz.almaty.boombrains.viewmodel.profile_view_model.profile_add_friends.request_list_view_model.RequestListView;
import kz.almaty.boombrains.viewmodel.profile_view_model.profile_add_friends.request_list_view_model.RequestListViewModel;
import kz.almaty.boombrains.viewmodel.profile_view_model.profile_ratings.ProfileRatingView;
import kz.almaty.boombrains.viewmodel.profile_view_model.profile_ratings.ProfileRatingViewModel;

/**
 * A simple {@link Fragment} subclass.
 */

@SuppressLint("SetTextI18n")
public class ProfileFragment extends Fragment implements ProfileRatingView,
        AchieveAdapter.AchieveListener, WorldAdapter.WorldListener,
        FriendsAdapter.FriendsListener, FriendRequestsAdapter.RequestsListener,
        AddFriendView, RequestListView, AcceptView, RejectView, PotionAdapter.PotionListener, SwipeRefreshLayout.OnRefreshListener {

    // profMainInfo
    @BindView(R.id.swipe) SwipeRefreshLayout swipeLayout;
    @BindView(R.id.back_to_main) RelativeLayout back;
    @BindView(R.id.editBtn) RelativeLayout editBtn;
    @BindView(R.id.loginBtn) Button loginBtn;
    @BindView(R.id.mainLayout) ConstraintLayout mainLayout;
    @BindView(R.id.infoLayout) ConstraintLayout infoLayout;
    @BindView(R.id.prof_user_name) TextView userNameTxt;
    @BindView(R.id.prof_email) TextView emailTxt;
    // leagueInfo
    @BindView(R.id.leagueIcon) ImageView leagueImg;
    @BindView(R.id.ratingRecord) TextView yourRating;
    // achievementInfo
    @BindView(R.id.achieveRecycler) RecyclerView achieveRecycler;
    @BindView(R.id.checkF) TextView checkF;
    @BindView(R.id.checkF2) TextView checkF2;
    @BindView(R.id.checkF3) TextView checkF3;
    @BindView(R.id.checkP) TextView checkP;
    @BindView(R.id.potionRecycler) RecyclerView potionRecycler;
    // ratingInfo
    @BindView(R.id.friendsRecycler) RecyclerView friendRecycler;
    @BindView(R.id.requestBtn) ConstraintLayout requestBtn;
    @BindView(R.id.addFriendBtn) ConstraintLayout addFriendBtn;
    @BindView(R.id.requestCount) TextView requestCount;
    @BindView(R.id.worldRecycler) RecyclerView worldRecycler;
    @BindView(R.id.myRecordConst) ConstraintLayout myRecordLayout;
    @BindView(R.id.myWorldConst) ConstraintLayout myWorldLayout;
    @BindView(R.id.friendName) TextView friendNameTxt;
    @BindView(R.id.friendRecord) TextView friendRecord;
    @BindView(R.id.mainLoading) SpinKitView loadingIcon;
    @BindView(R.id.worldName) TextView worldNameTxt;
    @BindView(R.id.worldRecord) TextView worldRecordTxt;

    // stat
    @BindView(R.id.moreSt) TextView moreStat;
    @BindView(R.id.moreTxt) TextView moreTxt;
    // dialog views
    private RecyclerView requestRecycler;
    private EditText userDialogEdit;
    private Button addDialogBtn;
    private TextView errorTxt;
    private Dialog dialogAdd, dialogList;
    private SpinKitView progress;
    private ConstraintLayout closeAdd, closeList;
    private TextView emptyTxt;

    private MainActivity mActivity;
    private ProfileCallBack mCallback;

    // viewModels
    private AddFriendViewModel addFriendViewModel;
    private AcceptViewModel acceptViewModel;
    private RejectViewModel rejectViewModel;
    private RequestListViewModel requestListViewModel;
    private ProfileRatingViewModel profileRatingViewModel;

    public ProfileFragment() { }

    public interface ProfileCallBack {
        void recordLoaded();
        void loadingFailed();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCallback.recordLoaded();

        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        profileRatingViewModel = ViewModelProviders.of(this).get(ProfileRatingViewModel.class);
        loadAdFriendDialog();
        loadListDialog();

        addFriendViewModel = ViewModelProviders.of(this).get(AddFriendViewModel.class);
        acceptViewModel = ViewModelProviders.of(this).get(AcceptViewModel.class);
        rejectViewModel = ViewModelProviders.of(this).get(RejectViewModel.class);
        requestListViewModel = ViewModelProviders.of(this).get(RequestListViewModel.class);
        myRecordLayout.setVisibility(View.INVISIBLE);

        setMainInfo();
        setNetworkDatas();

        requestCount.setVisibility(View.INVISIBLE);
        editBtn.setOnClickListener(v -> replaceFragment(new ProfileEditFragment(), "edit_fragment", true));

        setUserData();

        back.setOnClickListener(v -> getActivity().onBackPressed());
        loginBtn.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), MainSignInActivity.class));
            Objects.requireNonNull(getActivity()).overridePendingTransition(0,0);
        });

        moreStat.setOnClickListener(v-> goToWebPage());

        setClickListeners();
    }

    private void goToWebPage() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://boom-brains.kdo.one/"));
        startActivity(browserIntent);
    }

    private void setMainInfo() {
        if (SharedPrefManager.isUserLoggedIn(getContext())) {
            mainLayout.setVisibility(View.VISIBLE);
            infoLayout.setVisibility(View.GONE);
        } else {
            mainLayout.setVisibility(View.GONE);
            infoLayout.setVisibility(View.VISIBLE);
        }

        if (SharedPrefManager.isNetworkOnline(getActivity())) {
            if (SharedPrefManager.isUserLoggedIn(getActivity())) {
                profileRatingViewModel.getProfileRatings(getActivity(), this);
            }
        } else { setNetworkOfflineRecords(); }

        if (SharedPrefManager.isUserLoggedIn(getActivity()) && SharedPrefManager.isNetworkOnline(getActivity())) {
            requestListViewModel.getAllRequests(getActivity(), this);
        }
    }

    @Override
    public void onRefresh() {
        if (SharedPrefManager.isNetworkOnline(getActivity())) {
            setMainInfo();
            setNetworkDatas();
        } else {
            SharedUpdate.showToast(5, getString(R.string.CheckConnection), getActivity());
            swipeLayout.setRefreshing(false);
        }
    }

    private void setNetworkOfflineRecords() {
        String league = "", rating = "0";
        if (SharedPrefManager.getLeagueName(getActivity()) != null) {
            league = SharedPrefManager.getLeagueName(getActivity());
        }
        if (SharedPrefManager.getWholeRecord(getActivity()) != null) {
            rating = SharedPrefManager.getWholeRecord(getActivity());
        }
        int count = SharedPrefManager.getLeagueCount(getActivity());
        setLeagues(league, count);
        yourRating.setText(rating);
    }

    private void loadAdFriendDialog() {
        dialogAdd = new Dialog(getActivity(), R.style.dialogTheme);
        dialogAdd.setContentView(R.layout.add_friend_layout);
        addDialogBtn = dialogAdd.findViewById(R.id.addNewFriend);
        errorTxt = dialogAdd.findViewById(R.id.errorTxt);
        userDialogEdit = dialogAdd.findViewById(R.id.userNameEdit);
        closeAdd = dialogAdd.findViewById(R.id.closeConst);
        dialogAdd.setCanceledOnTouchOutside(true);
    }

    private void loadListDialog() {
        dialogList = new Dialog(getActivity(), R.style.dialogTheme);
        dialogList.setContentView(R.layout.friends_requests_layout);
        requestRecycler = dialogList.findViewById(R.id.requestsRecycler);
        progress = dialogList.findViewById(R.id.progressRequest);
        closeList = dialogList.findViewById(R.id.closeConst);
        emptyTxt = dialogList.findViewById(R.id.emptyTxt);
        dialogList.setCanceledOnTouchOutside(true);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mActivity = (MainActivity) context;
        }
        mCallback = (ProfileFragment.ProfileCallBack) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    private void setUserData() {
        String username = SharedPrefManager.getUserName(getContext());
        String userId = SharedPrefManager.getUserId(getContext());

        if (username != null) { userNameTxt.setText(username); }
        if (userId != null) { emailTxt.setText(userId); }
    }

    private void setClickListeners() {

        addFriendBtn.setOnClickListener(v -> dialogAdd.show());
        requestBtn.setOnClickListener(v -> {
            dialogList.show();
            if (SharedPrefManager.isUserLoggedIn(getActivity()) && SharedPrefManager.isNetworkOnline(getActivity())) {
                requestListViewModel.getAllRequests(getActivity(), this);
            } else {
                SharedUpdate.showToast(4, getString(R.string.CheckConnection), getActivity());
            }
        });

        addDialogBtn.setOnClickListener(v -> {
            if (SharedPrefManager.isUserLoggedIn(getActivity()) && SharedPrefManager.isNetworkOnline(getActivity())) {
                if (!userDialogEdit.getText().toString().equals("")) {
                    addFriendViewModel.addNewFriends(userDialogEdit.getText().toString(), getActivity(), this);
                } else {
                    SharedUpdate.showToast(0, getString(R.string.EmptyFields), getActivity());
                }
            } else {
                SharedUpdate.showToast(4, getString(R.string.CheckConnection), getActivity());
            }
        });

        closeAdd.setOnClickListener(v -> {
            dialogAdd.dismiss();
            errorTxt.setVisibility(View.GONE);
            userDialogEdit.setText("");
        });

        closeList.setOnClickListener(v -> dialogList.dismiss());
    }
    /* Profile rating responses */

    @Override
    public void loadingData() {
        mainLayout.setVisibility(View.INVISIBLE);
        loadingIcon.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadingFinished() {
        swipeLayout.setRefreshing(false);
        mainLayout.setVisibility(View.VISIBLE);
        loadingIcon.setVisibility(View.GONE);
    }

    @Override
    public void onWorldRecords(List<ProfileWorldRecord> records) {
        List<ProfileWorldRecord> new_records = new ArrayList<>();
        if (isAdded() && getActivity() != null) {
            for (ProfileWorldRecord record : records) {
                if (record.getTotalRecord() != 0) {
                    new_records.add(record);
                }
            }
            for (ProfileWorldRecord record : new_records) {
                if (record.getUsername().equals(SharedPrefManager.getUserName(getActivity()))) {
                    record.setMe(true);
                }
            }
            setPotions(getPotionList());
            setWorldRecords(new_records);
            setMyRecords(records, myWorldLayout, worldNameTxt, worldRecordTxt);
        }
    }

    private List<PotionModel> getPotionList() {
        return new ArrayList<>(Arrays.asList(
                new PotionModel("0 PC", R.drawable.potion_10sec, false),
                new PotionModel("0 PC", R.drawable.potion_life, true),
                new PotionModel("0 PC", R.drawable.potion_x2, false),
                new PotionModel("0 PC", R.drawable.potion_x5, true)
        ));
    }

    @Override
    public void onFriendsRecord(List<ProfileWorldRecord> records) {
        if (isAdded() && getActivity() != null) {
            for (ProfileWorldRecord record : records) {
                if (record.getUsername().equals(SharedPrefManager.getUserName(getActivity()))) {
                    record.setMe(true);
                }
            }
            setFriendRecords(records);
            setMyRecords(records, myRecordLayout, friendNameTxt, friendRecord);
        }
    }

    private void setMyRecords(List<ProfileWorldRecord> records, ConstraintLayout layout, TextView nameTxt, TextView recordTxt) {
        for (ProfileWorldRecord record : records) {
            String name = record.getUsername();
            if (name.equals(SharedPrefManager.getUserName(getActivity()))) {
                if (record.getPosition() > 5) {
                    layout.setVisibility(View.VISIBLE);
                    nameTxt.setText(record.getPosition() + ". " + record.getUsername());
                    recordTxt.setText(record.getTotalRecord() + "");
                } else {
                    layout.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    @Override
    public void onAchievements(List<Achievement> achievements) {
        if (isAdded() && getActivity() != null) {
            List<Achievement> new_achievs = new ArrayList<>();
            for (int i = 0; i < achievements.size(); i++) {
                if (achievements.size() >= 12) {
                    Achievement achievement = achievements.get(i);
                    achievement.setResource(RememberWordsRu.list_on.get(i));
                    new_achievs.add(achievement);
                }
            }
            setAchievements(new_achievs);
        }
    }

    @Override
    public void onWholeInfo(ProfileRatingModel model) {
        if (isAdded() && getActivity() != null) {
            SharedPrefManager.setLeagueName(getActivity(), model.getLeague());
            SharedPrefManager.setLeagueCount(getActivity(), model.getStar());
            SharedPrefManager.setWholeRecord(getActivity(), model.getMyWorldRecord().getTotalRecord().toString());
        }
        setLeagues(model.getLeague(), model.getStar());
        yourRating.setText(model.getMyWorldRecord().getTotalRecord() + "");
    }

    @Override
    public void onErrorOccurred(int error) { Log.d("ERROR::", "OCCURRED!"); }

    @Override
    public void onFailed() { Log.d("REQUEST::", "FAILED!"); }

    /* Achieve adapter listeners */
    @Override
    public void setViewSize(View view) { setSizes(view); }

    @Override
    public void setViewClicked(Achievement model) { }

    /* Potion listeners */
    @Override
    public void setPotionSize(View view) {
        potionSize(view);
    }

    /* Friend and world adapter listeners */
    @Override
    public void setFriendSize(View view) { setFriendItems(view, friendRecycler); }

    @Override
    public void setFriendClicked(ProfileWorldRecord record) {
        Bundle bundle=new Bundle();
        bundle.putString("username", record.getUsername());
        bundle.putInt("record", record.getTotalRecord());
        FriendDetailsFragment detailsFragment = new FriendDetailsFragment();
        detailsFragment.setArguments(bundle);
        replaceFragment(detailsFragment, "friend_details", true);
    }

    @Override
    public void setWorldSize(View view) {
        setWorldItems(view, worldRecycler);
    }

    @Override
    public void setWorldClicked(ProfileWorldRecord record) {
        Bundle bundle=new Bundle();
        bundle.putString("username", record.getUsername());
        bundle.putInt("record", record.getTotalRecord());
        bundle.putString("world", "world");
        FriendDetailsFragment detailsFragment = new FriendDetailsFragment();
        detailsFragment.setArguments(bundle);
        replaceFragment(detailsFragment, "friend_details", true);
    }

    /* All dialog listeners */
    @Override
    public void onAddBtnClicked(String userId) {
        if (SharedPrefManager.isNetworkOnline(getActivity())) {
            acceptViewModel.acceptFriends(userId, getContext(), this);
        }
    }

    @Override
    public void onRejectBtnClicked(String userId) {
        if (SharedPrefManager.isNetworkOnline(getActivity())) {
            rejectViewModel.rejectFriends(userId, getContext(), this);
        }
    }

    /* Add friends view interfaces */
    @Override
    public void onAddFriendSuccess() {
        errorTxt.setVisibility(View.VISIBLE);
        errorTxt.setTextColor(Color.parseColor("#25C82C"));
        errorTxt.setText(getString(R.string.RequestSend));
        new Handler().postDelayed(()-> {
            errorTxt.setVisibility(View.GONE);
            userDialogEdit.setText("");
            dialogAdd.dismiss();
        }, 1200);
    }

    @Override
    public void onAddFriendError() { SharedUpdate.showToast(0, getString(R.string.ErrorAdFriends), getContext()); }

    @Override
    public void onAddFriendErrorCode(int code) {
        switch (code) {
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

    /* Request list interface methods */
    @Override
    public void showLoading() {
        requestRecycler.setVisibility(View.INVISIBLE);
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        swipeLayout.setRefreshing(false);
        progress.setVisibility(View.GONE);
        requestRecycler.setVisibility(View.VISIBLE);
    }

    @Override
    public void listError() { Log.d("Error::", "with loading data!"); }

    @Override
    public void listSuccess(List<RequestListModel> list) {
        if (list.size() == 0) {
            emptyTxt.setVisibility(View.VISIBLE);
        } else {
            emptyTxt.setVisibility(View.GONE);
        }
        setRequestList(list);

        if (list.size() > 0) {
            requestCount.setVisibility(View.VISIBLE);
            requestCount.setText("" + list.size());
        } else {
            requestCount.setVisibility(View.INVISIBLE);
        }
    }

    /* Accept request interfaces */
    @Override
    public void acceptSuccess() {
        SharedUpdate.showToast(1, getString(R.string.RequestAccepted), getContext());
        if (SharedPrefManager.isNetworkOnline(getContext()) && SharedPrefManager.isUserLoggedIn(getContext())) {
            requestListViewModel.getAllRequests(getContext(), this);
            profileRatingViewModel.getProfileRatings(getActivity(), this);
        }
    }

    @Override
    public void acceptError() { SharedUpdate.showToast(0, getString(R.string.ErrAccepting), getContext()); }

    /* Reject request interfaces */
    @Override
    public void rejectSuccess() {
        SharedUpdate.showToast(1, getString(R.string.RequestDeclined), getContext());
        if (SharedPrefManager.isNetworkOnline(getContext()) && SharedPrefManager.isUserLoggedIn(getContext())) {
            requestListViewModel.getAllRequests(getContext(), this);
        }
    }

    @Override
    public void rejectError() { SharedUpdate.showToast(0, getString(R.string.ErrRejecting), getContext()); }

    private void setRequestList(List<RequestListModel> list) {
        FriendRequestsAdapter requestsAdapter = new FriendRequestsAdapter(list, getActivity(), this);
        requestRecycler.setAdapter(requestsAdapter);
        requestRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        requestRecycler.setItemAnimator(new DefaultItemAnimator());
        requestsAdapter.notifyDataSetChanged();
    }

    private void setWorldRecords(List<ProfileWorldRecord> records) {
        WorldAdapter worldAdapter = new WorldAdapter(getActivity(), records, this);
        worldRecycler.setAdapter(worldAdapter);
        worldRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        worldRecycler.setItemAnimator(new DefaultItemAnimator());
        worldAdapter.notifyDataSetChanged();
    }

    private void setFriendRecords(List<ProfileWorldRecord> records) {
        FriendsAdapter friendsAdapter = new FriendsAdapter(getActivity(), records, this);
        friendRecycler.setAdapter(friendsAdapter);
        friendRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        friendRecycler.setItemAnimator(new DefaultItemAnimator());
        friendsAdapter.notifyDataSetChanged();
    }

    private void setAchievements(List<Achievement> list) {
        // adapters
        AchieveAdapter achieveAdapter = new AchieveAdapter(list, getActivity(), this);
        achieveRecycler.setAdapter(achieveAdapter);
        achieveRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        achieveRecycler.setItemAnimator(new DefaultItemAnimator());
        achieveAdapter.notifyDataSetChanged();
    }

    private void setPotions(List<PotionModel> list) {
        // adapters
        PotionAdapter achieveAdapter = new PotionAdapter(list, this);
        potionRecycler.setAdapter(achieveAdapter);
        potionRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        potionRecycler.setItemAnimator(new DefaultItemAnimator());
        achieveAdapter.notifyDataSetChanged();
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

    private void setStar(int res) { leagueImg.setImageResource(res); }

    private void setSizes(View view) {
        int width = achieveRecycler.getWidth();
        view.getLayoutParams().width = width / 3 - 35;
        view.getLayoutParams().height = width / 3 + 20;
    }

    private void potionSize(View view) {
        int width = potionRecycler.getWidth();
        view.getLayoutParams().width = width / 4 - 20;
    }

    private void setWorldItems(View view, RecyclerView rv) {
        int height = rv.getHeight();
        view.getLayoutParams().height = height / 5;
    }

    private void setFriendItems(View view, RecyclerView rv) {
        int height = rv.getHeight();
        view.getLayoutParams().height = height / 5;
    }

    private static final String BACK_STACK_ROOT_TAG = "main";

    private void replaceFragment(Fragment fragment, String fragmentTag, boolean withBackStack){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction  = getActivity().getSupportFragmentManager().beginTransaction();
        if(withBackStack){
            fragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            transaction.addToBackStack(BACK_STACK_ROOT_TAG);
        }
        transaction.replace(R.id.fragment_container, fragment,fragmentTag);
        transaction.commit();
    }

    private void setNetworkDatas() {
        if (SharedPrefManager.isNetworkOnline(getActivity())) {
            checkF.setVisibility(View.GONE);
            checkF2.setVisibility(View.GONE);
            checkF3.setVisibility(View.GONE);
            checkP.setVisibility(View.GONE);
        } else {
            checkF.setVisibility(View.VISIBLE);
            checkF2.setVisibility(View.VISIBLE);
            checkF3.setVisibility(View.VISIBLE);
            checkP.setVisibility(View.GONE);
            myRecordLayout.setVisibility(View.INVISIBLE);
            myWorldLayout.setVisibility(View.INVISIBLE);
        }
    }
}
