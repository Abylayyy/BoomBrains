package kz.almaty.boombrains.ui.sign_pages.change_user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import androidx.lifecycle.ViewModelProviders;
import com.github.ybq.android.spinkit.SpinKitView;
import butterknife.BindView;
import butterknife.ButterKnife;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.helpers.DialogHelperActivity;
import kz.almaty.boombrains.helpers.SharedPrefManager;
import kz.almaty.boombrains.helpers.SharedUpdate;
import kz.almaty.boombrains.models.auth_models.ChangePassModel;
import kz.almaty.boombrains.viewmodel.password_view_model.change_pass_view_model.ChangePassView;
import kz.almaty.boombrains.viewmodel.password_view_model.change_pass_view_model.ChangeViewModel;

public class ChangePasswordActivity extends DialogHelperActivity implements ChangePassView {

    @BindView(R.id.confirmChange) Button changeBtn;
    @BindView(R.id.currentPass) EditText currentEdit;
    @BindView(R.id.newPass) EditText newEdit;
    @BindView(R.id.confPass) EditText confEdit;
    @BindView(R.id.back_to_main) RelativeLayout back;
    @BindView(R.id.progressSpin) SpinKitView progress;

    ChangeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);

        viewModel = ViewModelProviders.of(this).get(ChangeViewModel.class);

        back.setOnClickListener(v -> onBackPressed());
        changeBtn.setOnClickListener(v -> {
            if (SharedPrefManager.isNetworkOnline(getApplication())) {
                checkFields(
                        currentEdit.getText().toString().trim(),
                        newEdit.getText().toString().trim(),
                        confEdit.getText().toString().trim());
            } else {
                showToast(0, getString(R.string.CheckConnection));
            }
        });
    }

    private void checkFields(String current, String newPass, String confPass) {
        if (!current.equals(SharedPrefManager.getUserPass(this))) {
            showToast(0, getString(R.string.CurrPassNotCorrect));
            return;
        }
        if (newPass.length() < 4) {
            showToast(0, getString(R.string.PasswordNotCorrect));
            return;
        }
        if (!confPass.equals(newPass)) {
            showToast(0, getString(R.string.PassNotMatch));
            return;
        }
        viewModel.changePassword(current, newPass, this, this);
    }

    @Override
    public void showProgressBar() {
        progress.setVisibility(View.VISIBLE);
        changeBtn.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progress.setVisibility(View.GONE);
        changeBtn.setVisibility(View.VISIBLE);

    }

    @Override
    public void goToMain(ChangePassModel model) {
        if (model != null) {
            if (model.getStatus().equals("accepted")) {
                showToast(1, getString(R.string.SuccessChangePass));
                finish();
            } else {
                switch (model.getErrors().get(0).getStatusCode()) {
                    case 1: case 2: case 3: case 4:
                        case 5: case 6: case 7: case 8: {
                        showToast(0, model.getErrors().get(0).getMessage());
                        break;
                    }
                }
            }
        }
    }
}
