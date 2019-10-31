package kz.almaty.boombrains.viewmodel.reset_view_model;

import android.content.Context;
import android.widget.Toast;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import org.jetbrains.annotations.NotNull;
import kz.almaty.boombrains.models.auth_models.ResetPassModel;
import kz.almaty.boombrains.services.APIService;
import kz.almaty.boombrains.services.RetrofitClass;
import kz.almaty.boombrains.viewmodel.login_view_model.LoginView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetViewModel extends ViewModel {

    public void getResetMessage(String email, Context context, ResetView view) {
        final MutableLiveData<ResetPassModel> data = new MutableLiveData<>();
        APIService service = RetrofitClass.getApiService();
        view.showProgressBar();
        service.resetPassword("BoomBrains", email).enqueue(new Callback<ResetPassModel>() {
            @Override
            public void onResponse(@NotNull Call<ResetPassModel> call, @NotNull Response<ResetPassModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        ResetPassModel loginModel = response.body();
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
            public void onFailure(@NotNull Call<ResetPassModel> call, @NotNull Throwable t) {
                showToast(context, t.getMessage());
                view.hideProgressBar();
            }
        });
    }

    private void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
