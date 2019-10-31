package kz.almaty.boombrains.sign_pages;

import android.content.Intent;
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
import kz.almaty.boombrains.models.auth_models.ResetPassModel;
import kz.almaty.boombrains.viewmodel.reset_view_model.ResetView;
import kz.almaty.boombrains.viewmodel.reset_view_model.ResetViewModel;

public class ResetActivity extends DialogHelperActivity implements ResetView {

    @BindView(R.id.reset_email_edit) EditText emailEdit;
    @BindView(R.id.confirmBtn) Button confirm;
    @BindView(R.id.back_to_main) RelativeLayout back;
    @BindView(R.id.progressSpin) SpinKitView progress;

    ResetViewModel resetViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        ButterKnife.bind(this);

        resetViewModel = ViewModelProviders.of(this).get(ResetViewModel.class);

        confirm.setOnClickListener(v -> {
            if (SharedPrefManager.isNetworkOnline(getApplication())) {
                checkFields(emailEdit.getText().toString().trim());
            } else {
                showToast(0, getString(R.string.CheckConnection));
            }
        });

        back.setOnClickListener(v -> onBackPressed());
    }

    private void checkFields(String email) {
        if (email.length() == 0) {
            showToast(0, getString(R.string.EmptyFields));
            return;
        }
        if (!email.contains("@")) {
            showToast(0, getString(R.string.EmailNotCorrect));
            return;
        }
        resetViewModel.getResetMessage(email, this, this);
    }

    @Override
    public void showProgressBar() {
        progress.setVisibility(View.VISIBLE);
        confirm.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progress.setVisibility(View.GONE);
        confirm.setVisibility(View.VISIBLE);
    }

    @Override
    public void goToMain(ResetPassModel model) {
        if (model != null) {
            if (model.getStatus().equals("accepted")) {
                showToast(1, model.getMessage());
                startActivity(new Intent(this, MainSignInActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();

            } else {
                switch (model.getErrors().get(0).getStatusCode()) {
                    case 1: case 2: case 3: case 4: case 5: {
                        showToast(0, model.getErrors().get(0).getMessage());
                        break;
                    }
                }
            }
        }
    }
}
