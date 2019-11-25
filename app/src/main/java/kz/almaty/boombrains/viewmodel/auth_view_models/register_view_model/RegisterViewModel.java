package kz.almaty.boombrains.viewmodel.auth_view_models.register_view_model;

import android.content.Context;
import android.widget.Toast;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;
import kz.almaty.boombrains.models.auth_models.MainLoginModel;
import kz.almaty.boombrains.models.auth_models.register.RegisterModel;
import kz.almaty.boombrains.services.APIService;
import kz.almaty.boombrains.services.RetrofitClass;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterViewModel extends ViewModel {

    public void setRegisterWithRecords(RegisterModel model, Context context, RegisterView view) {
        final MutableLiveData<MainLoginModel> data = new MutableLiveData<>();
        APIService service = RetrofitClass.getApiService();
        view.showProgressBar();
        service.registerWithRecords("BoomBrains", RetrofitClass.getLang(context), model).enqueue(new Callback<MainLoginModel>() {
            @Override
            public void onResponse(@NotNull Call<MainLoginModel> call, @NotNull Response<MainLoginModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        MainLoginModel loginModel = response.body();
                        data.setValue(loginModel);
                        view.goToMain(data.getValue());
                        view.hideProgressBar();
                    } else {
                        showToast(context, "Response is empty!");
                        view.hideProgressBar();
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
