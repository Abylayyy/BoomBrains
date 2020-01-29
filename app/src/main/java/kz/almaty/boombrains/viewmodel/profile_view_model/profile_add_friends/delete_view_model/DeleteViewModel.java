package kz.almaty.boombrains.viewmodel.profile_view_model.profile_add_friends.delete_view_model;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

import kz.almaty.boombrains.data.models.records_model.RecordResponse;
import kz.almaty.boombrains.data.services.RetrofitClass;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteViewModel extends ViewModel {
    public void deleteFriend(String username, Context context, DeleteView view) {
        view.loading();
        RetrofitClass.getApiService().deleteFriend(RetrofitClass.getUserToken(context), username).enqueue(new Callback<RecordResponse>() {
            @Override
            public void onResponse(@NotNull Call<RecordResponse> call, @NotNull Response<RecordResponse> response) {
                view.hide();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("accepted")) {
                            view.successDelete();
                        }
                    }
                } else {
                    view.errorDelete();
                }
            }

            @Override
            public void onFailure(@NotNull Call<RecordResponse> call, @NotNull Throwable t) {
                view.errorDelete();
            }
        });
    }
}
