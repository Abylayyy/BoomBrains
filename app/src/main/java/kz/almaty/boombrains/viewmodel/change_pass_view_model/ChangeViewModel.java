package kz.almaty.boombrains.viewmodel.change_pass_view_model;

import android.content.Context;
import android.widget.Toast;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import org.jetbrains.annotations.NotNull;

import kz.almaty.boombrains.helpers.SharedPrefManager;
import kz.almaty.boombrains.models.auth_models.ChangePassModel;
import kz.almaty.boombrains.models.auth_models.ResetPassModel;
import kz.almaty.boombrains.services.APIService;
import kz.almaty.boombrains.services.RetrofitClass;
import kz.almaty.boombrains.viewmodel.reset_view_model.ResetView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeViewModel extends ViewModel {

    public void changePassword(String current, String newPass, Context context, ChangePassView view) {

        final MutableLiveData<ChangePassModel> data = new MutableLiveData<>();
        APIService service = RetrofitClass.getApiService();
        view.showProgressBar();

        service.changePassword(RetrofitClass.getUserToken(context), current, newPass).enqueue(new Callback<ChangePassModel>() {
            @Override
            public void onResponse(@NotNull Call<ChangePassModel> call, @NotNull Response<ChangePassModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("accepted")) {
                            SharedPrefManager.setUserPass(context, newPass);
                        }
                        ChangePassModel loginModel = response.body();
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
            public void onFailure(@NotNull Call<ChangePassModel> call, @NotNull Throwable t) {
                showToast(context, t.getMessage());
                view.hideProgressBar();
            }
        });
    }

    private void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
