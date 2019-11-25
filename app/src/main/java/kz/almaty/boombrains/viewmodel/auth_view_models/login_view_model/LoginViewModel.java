package kz.almaty.boombrains.viewmodel.auth_view_models.login_view_model;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import org.jetbrains.annotations.NotNull;

import kz.almaty.boombrains.helpers.SharedPrefManager;
import kz.almaty.boombrains.models.auth_models.MainLoginModel;
import kz.almaty.boombrains.services.APIService;
import kz.almaty.boombrains.services.RetrofitClass;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    public void getLoginLiveData(String email, String password, Context context, LoginView view) {
        final MutableLiveData<MainLoginModel> data = new MutableLiveData<>();
        APIService service = RetrofitClass.getApiService();
        view.showProgressBar();
        service.userLogin("BoomBrains", RetrofitClass.getLang(context), email, password).enqueue(new Callback<MainLoginModel>() {
            @Override
            public void onResponse(@NotNull Call<MainLoginModel> call, @NotNull Response<MainLoginModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("accepted")) {
                            SharedPrefManager.setUserPass(context, password);
                        }
                        MainLoginModel loginModel = response.body();
                        data.setValue(loginModel);
                        view.goToMain(data.getValue());
                        view.hideProgressBar();
                    } else {
                        showToast(context, "Response is empty");
                    }
                } else {
                    showToast(context, "Response not successful!");
                    view.hideProgressBar();
                }
            }

            @Override
            public void onFailure(@NotNull Call<MainLoginModel> call, @NotNull Throwable t) {
                showToast(context, t.getMessage());
                view.hideProgressBar();
            }
        });
    }

    private void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
