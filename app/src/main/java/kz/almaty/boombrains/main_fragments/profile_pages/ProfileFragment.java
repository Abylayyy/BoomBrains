package kz.almaty.boombrains.main_fragments.profile_pages;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.helpers.SharedPrefManager;
import kz.almaty.boombrains.sign_pages.ChangePasswordActivity;
import kz.almaty.boombrains.sign_pages.MainSignInActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    @BindView(R.id.back_to_main) RelativeLayout back;
    @BindView(R.id.editBtn) RelativeLayout editBtn;
    @BindView(R.id.loginBtn) Button loginBtn;
    @BindView(R.id.mainLayout) ConstraintLayout mainLayout;
    @BindView(R.id.infoLayout) ConstraintLayout infoLayout;
    @BindView(R.id.prof_user_name) TextView userNameTxt;
    @BindView(R.id.prof_email) TextView emailTxt;

    public ProfileFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (SharedPrefManager.isUserLoggedIn(getContext())) {
            mainLayout.setVisibility(View.VISIBLE);
            infoLayout.setVisibility(View.GONE);
        } else {
            mainLayout.setVisibility(View.GONE);
            infoLayout.setVisibility(View.VISIBLE);
        }

        editBtn.setOnClickListener(v -> startActivity(new Intent(getActivity(), ChangePasswordActivity.class)));

        setUserData();

        back.setOnClickListener(v -> getActivity().onBackPressed());

        loginBtn.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), MainSignInActivity.class));
            Objects.requireNonNull(getActivity()).overridePendingTransition(0,0);
        });
    }

    private void setUserData() {
        String username = SharedPrefManager.getUserName(getContext());
        String email = SharedPrefManager.getUserEmail(getContext());

        if (username != null) {
            userNameTxt.setText(username);
        }
        if (email != null) {
            emailTxt.setText(email);
        }
    }
}
