package kz.almaty.boombrains.ui.sign_pages.change_user;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.github.ybq.android.spinkit.SpinKitView;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.util.helpers.dialog_helper.DialogHelperActivity;
import kz.almaty.boombrains.util.helpers.preference.SharedPrefManager;
import kz.almaty.boombrains.viewmodel.password_view_model.username_view_model.ChangeUserView;
import kz.almaty.boombrains.viewmodel.password_view_model.username_view_model.ChangeUserViewModel;

public class ChangeUserActivity extends DialogHelperActivity implements ChangeUserView{

    @BindView(R.id.back_to_main) RelativeLayout back;
    @BindView(R.id.changeUserBtn) Button changeBtn;
    @BindView(R.id.userNameChange) EditText editName;
    @BindView(R.id.progressSpin) SpinKitView progress;

    ChangeUserViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user);
        ButterKnife.bind(this);

        viewModel = ViewModelProviders.of(this).get(ChangeUserViewModel.class);

        back.setOnClickListener(v -> onBackPressed());

        editName.setText(SharedPrefManager.getUserName(getApplication()));

        changeBtn.setOnClickListener(v -> {
            if (SharedPrefManager.isNetworkOnline(getApplication())) {
                checkFields(editName.getText().toString().trim());
            } else {
                showToast(0, getString(R.string.CheckConnection));
            }
        });
    }

    private void checkFields(String current) {
        if (current.equals(SharedPrefManager.getUserName(this))) {
            showToast(0, getString(R.string.NotChanged));
            return;
        }
        if (TextUtils.isEmpty(current)) {
            showToast(0, getString(R.string.EmptyFields));
            return;
        }
        if (current.length() < 4) {
            showToast(0, getString(R.string.UserNameNotCorrect));
            return;
        }
        viewModel.changeUsername(current, this, this);
    }

    @Override
    public void showLoading() {
        progress.setVisibility(View.VISIBLE);
        changeBtn.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideLoading() {
        progress.setVisibility(View.GONE);
        changeBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void success() {
        Log.d("USERNAME::", "CHANGED");
        new Handler().postDelayed(this::finish, 1000);
    }
}
