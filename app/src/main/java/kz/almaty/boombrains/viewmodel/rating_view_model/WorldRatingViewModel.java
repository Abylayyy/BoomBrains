package kz.almaty.boombrains.viewmodel.rating_view_model;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;
import kz.almaty.boombrains.models.rating_model.WorldRecordResponse;
import kz.almaty.boombrains.services.APIService;
import kz.almaty.boombrains.services.RetrofitClass;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorldRatingViewModel extends ViewModel {
    public void getWorldRatings(String name, Context context, WorldRatingView view) {
        final MutableLiveData<WorldRecordResponse> data = new MutableLiveData<>();
        APIService service = RetrofitClass.getApiService();
        view.showLoading();
        service.getCurrentRating(RetrofitClass.getUserToken(context), name).enqueue(new Callback<WorldRecordResponse>() {
            @Override
            public void onResponse(@NotNull Call<WorldRecordResponse> call, @NotNull Response<WorldRecordResponse> response) {
                view.hideLoading();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        WorldRecordResponse loginModel = response.body();
                        data.setValue(loginModel);
                        view.success(data.getValue());
                    } else {
                        view.error("Empty response");
                    }
                } else {
                    switch (response.code()) {
                        case 404: view.errorMessage(404); break;
                        case 401: view.errorMessage(401); break;
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<WorldRecordResponse> call, @NotNull Throwable t) {
                view.error("Couldn't load the data");
                view.hideLoading();
            }
        });
    }
}
