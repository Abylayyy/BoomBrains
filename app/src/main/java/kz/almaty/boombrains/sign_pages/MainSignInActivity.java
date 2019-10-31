package kz.almaty.boombrains.sign_pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import kz.almaty.boombrains.R;
import kz.almaty.boombrains.helpers.DialogHelperActivity;
import kz.almaty.boombrains.helpers.SharedPrefManager;
import kz.almaty.boombrains.main_pages.MainActivity;
import kz.almaty.boombrains.models.auth_models.MainLoginModel;
import kz.almaty.boombrains.viewmodel.login_view_model.LoginViewModel;
import kz.almaty.boombrains.viewmodel.login_view_model.LoginView;

public class MainSignInActivity extends DialogHelperActivity implements LoginView {

    @BindView(R.id.back_to_main) RelativeLayout back;
    @BindView(R.id.log_email_edit) EditText emailEdit;
    @BindView(R.id.log_pass_edit) EditText passEdit;
    @BindView(R.id.log_forgot_pass) TextView forgotTxt;
    @BindView(R.id.log_reg) TextView registerTxt;
    @BindView(R.id.log_loginBtn) Button loginBtn;
    @BindView(R.id.progressSpin) SpinKitView progress;

    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sign_in);
        ButterKnife.bind(this);

        back.setOnClickListener(v -> onBackPressed());
        forgotTxt.setPaintFlags(forgotTxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        registerTxt.setPaintFlags(registerTxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        if (SharedPrefManager.getUserEmail(getApplication()) != null) {
            emailEdit.setText(SharedPrefManager.getUserEmail(getApplication()));
        }

        forgotTxt.setOnClickListener(v -> {
            startActivity(new Intent(this, ResetActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        loginBtn.setOnClickListener(v -> {
            if (SharedPrefManager.isNetworkOnline(getApplication())) {
                checkFields(
                        emailEdit.getText().toString().trim(),
                        passEdit.getText().toString().trim()
                );
            } else {
                showToast(0, getString(R.string.CheckConnection));
            }
        });

        registerTxt.setOnClickListener(v -> {
            startActivity(new Intent(this, MainRegister.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }

    private void checkFields(String email, String password) {
        if (!email.contains("@")) {
            showToast(0, getString(R.string.EmailNotCorrect));
            return;
        }
        if (password.length() < 4) {
            showToast(0, getString(R.string.PasswordNotCorrect));
            return;
        }
        loginViewModel.getLoginLiveData(email, password, this, this);
    }

    @Override
    public void showProgressBar() {
        progress.setVisibility(View.VISIBLE);
        loginBtn.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progress.setVisibility(View.GONE);
        loginBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void goToMain(MainLoginModel model) {
        if (model != null) {
            if (model.getStatus().equals("accepted")) {

                SharedPrefManager.setUserAuthTokenKey(getApplication(), model.getToken());
                SharedPrefManager.setIsUserLoggedIn(getApplication(), true);
                SharedPrefManager.setUserName(this, model.getUsername());
                SharedPrefManager.setUserEmail(this, model.getEmail());

                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finishAffinity();

            } else {
                switch (model.getErrors().get(0).getStatusCode()) {
                    case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10: {
                        showToast(0, model.getErrors().get(0).getMessage());
                        break;
                    }
                }
            }
        }
    }
}
