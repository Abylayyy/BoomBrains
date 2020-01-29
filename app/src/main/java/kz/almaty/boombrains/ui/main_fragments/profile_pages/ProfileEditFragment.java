package kz.almaty.boombrains.ui.main_fragments.profile_pages;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.util.helpers.SharedPrefManager;
import kz.almaty.boombrains.ui.main_pages.MainActivity;
import kz.almaty.boombrains.ui.sign_pages.change_user.ChangePasswordActivity;
import kz.almaty.boombrains.ui.sign_pages.change_user.ChangeUserActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileEditFragment extends Fragment {

    @BindView(R.id.back_to_main) RelativeLayout back;
    @BindView(R.id.editUserTxt) TextView usernameTxt;
    @BindView(R.id.editUserId) TextView userIdTxt;
    @BindView(R.id.editUserEmail) TextView emailTxt;
    @BindView(R.id.editUserPass) TextView userPassTxt;
    @BindView(R.id.shareAppTxt) TextView share;

    public interface ProfEditCallback {
        void onEditSuccess();
        void onEditError();
    }

    private ProfEditCallback mCallback;
    private MainActivity mActivity;

    public ProfileEditFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_edit, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        back.setOnClickListener(v -> getActivity().onBackPressed());
        setAllData();

        share.setOnClickListener(v -> shareTextUrl());
        usernameTxt.setOnClickListener(v -> startActivity(new Intent(getActivity(), ChangeUserActivity.class)));
        userPassTxt.setOnClickListener(v -> startActivity(new Intent(getActivity(), ChangePasswordActivity.class)));
    }

    private void setAllData() {
        usernameTxt.setText(SharedPrefManager.getUserName(getActivity()));
        userIdTxt.setText(SharedPrefManager.getUserId(getActivity()));
        usernameTxt.setText(SharedPrefManager.getUserName(getActivity()));
        emailTxt.setText(SharedPrefManager.getUserEmail(getActivity()));
    }

    private void shareTextUrl() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, "Check out Boom Brains");
        share.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=kdo.one.boombrains");
        startActivity(Intent.createChooser(share, "Share the game with friends!"));
    }

    @Override
    public void onResume() {
        super.onResume();
        usernameTxt.setText(SharedPrefManager.getUserName(getActivity()));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mActivity = (MainActivity) context;
        }
        mCallback = (ProfileEditFragment.ProfEditCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }
}
