package kz.almaty.boombrains.sign_pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import kz.almaty.boombrains.viewmodel.register_view_model.RegisterView;
import kz.almaty.boombrains.viewmodel.register_view_model.RegisterViewModel;

public class MainRegister extends DialogHelperActivity implements RegisterView {

    @BindView(R.id.back_to_main) RelativeLayout back;
    @BindView(R.id.reg_name_edit) EditText nameEdit;
    @BindView(R.id.reg_email_edit) EditText emailEdit;
    @BindView(R.id.reg_pass_edit) EditText regPassEdit;
    @BindView(R.id.reg_pass_conf) EditText passConfEdit;
    @BindView(R.id.reg_regBtn) Button regBtn;
    @BindView(R.id.checkReg) CheckBox acceptBox;
    @BindView(R.id.progressSpin) SpinKitView progressBar;
    @BindView(R.id.agreeTxt) TextView agreeTxt;

    RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register);
        ButterKnife.bind(this);

        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);

        back.setOnClickListener(v -> onBackPressed());

        regBtn.setOnClickListener(v -> {
            if (SharedPrefManager.isNetworkOnline(getApplication())) {
                checkFields(
                        emailEdit.getText().toString().trim(),
                        nameEdit.getText().toString().trim(),
                        regPassEdit.getText().toString().trim(),
                        passConfEdit.getText().toString().trim()
                );
            } else {
                showToast(0, getString(R.string.CheckConnection));
            }
        });

        agreeTxt.setOnClickListener(v-> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://kdo.one/terms"));
            startActivity(browserIntent);
        });
    }

    private void checkFields(String email, String username, String password, String repass) {
        if (!email.contains("@")) {
            showToast(0, getString(R.string.EmailNotCorrect));
            return;
        }
        if (username.length() < 3) {
            showToast(0, getString(R.string.UserNameNotCorrect));
            return;
        }
        if (password.length() < 4) {
            showToast(0, getString(R.string.PasswordNotCorrect));
            return;
        }
        if (repass.length() < 4 || !repass.equals(password)) {
            showToast(0, getString(R.string.PassNotMatch));
            return;
        }
        if (!acceptBox.isChecked()) {
            showToast(0, getString(R.string.AcceptAgreement));
            return;
        }
        registerViewModel.getRegisterLiveData(email, password, username, this, this);
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        regBtn.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        regBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void goToMain(MainLoginModel model) {
        if (model != null) {
            if (model.getStatus().equals("accepted")) {
                SharedPrefManager.setUserName(this, model.getUsername());
                SharedPrefManager.setUserEmail(this, model.getEmail());

                startActivity(new Intent(this, MainSignInActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();

            } else {
                switch (model.getErrors().get(0).getStatusCode()) {
                    case 1: case 2: case 3: case 4: case 5: case 6:
                        case 7: case 8: case 9: case 10: case 11: case 12: {
                        showToast(0, model.getErrors().get(0).getMessage());
                        break;
                    }
                }
            }
        }
    }
}
