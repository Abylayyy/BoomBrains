package kz.almaty.boombrains.viewmodel.password_view_model.username_view_model;

import android.content.Context;
import android.widget.Toast;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import org.jetbrains.annotations.NotNull;
import kz.almaty.boombrains.util.helpers.preference.SharedPrefManager;
import kz.almaty.boombrains.data.models.records_model.RecordResponse;
import kz.almaty.boombrains.data.services.APIService;
import kz.almaty.boombrains.data.services.RetrofitClass;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeUserViewModel extends ViewModel {
    public void changeUsername(String username, Context context, ChangeUserView view) {

        final MutableLiveData<RecordResponse> data = new MutableLiveData<>();
        APIService service = RetrofitClass.getApiService();
        view.showLoading();

        service.updateUserName(RetrofitClass.getUserToken(context), username).enqueue(new Callback<RecordResponse>() {
            @Override
            public void onResponse(@NotNull Call<RecordResponse> call, @NotNull Response<RecordResponse> response) {
                view.hideLoading();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("accepted")) {
                            SharedPrefManager.setUserName(context, username);
                            view.success();
                        }
                        RecordResponse loginModel = response.body();
                        data.setValue(loginModel);
                    } else {
                        showToast(context, "Response is empty");
                    }
                } else {
                    showToast(context, "Response not successful!");
                }
            }

            @Override
            public void onFailure(@NotNull Call<RecordResponse> call, @NotNull Throwable t) {
                showToast(context, t.getMessage());
                view.hideLoading();
            }
        });
    }

    private void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
