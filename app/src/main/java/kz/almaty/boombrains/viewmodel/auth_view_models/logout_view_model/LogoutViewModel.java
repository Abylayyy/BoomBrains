package kz.almaty.boombrains.viewmodel.auth_view_models.logout_view_model;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

import kz.almaty.boombrains.data.models.records_model.RecordResponse;
import kz.almaty.boombrains.data.services.APIService;
import kz.almaty.boombrains.data.services.RetrofitClass;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogoutViewModel extends ViewModel {
    public void logoutFromAccount(Context context, LogoutView view) {
        APIService service = RetrofitClass.getApiService();
        service.logoutFromGame(RetrofitClass.getUserToken(context)).enqueue(new Callback<RecordResponse>() {
            @Override
            public void onResponse(@NotNull Call<RecordResponse> call, @NotNull Response<RecordResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("accepted")) {
                            view.success();
                        }
                    } else {
                        showToast(context, "Empty response");
                    }
                } else {
                    showToast(context, "Response is not successful");
                }
            }

            @Override
            public void onFailure(@NotNull Call<RecordResponse> call, @NotNull Throwable t) {
                view.error();
            }
        });
    }

    private void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
