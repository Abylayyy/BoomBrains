package kz.almaty.boombrains.viewmodel.profile_view_model.profile_add_friends.accept_view_model;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

import kz.almaty.boombrains.data.models.records_model.RecordResponse;
import kz.almaty.boombrains.data.services.APIService;
import kz.almaty.boombrains.data.services.RetrofitClass;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AcceptViewModel extends ViewModel {
    public void acceptFriends(String id, Context context, AcceptView view) {
        final MutableLiveData<RecordResponse> data = new MutableLiveData<>();
        APIService service = RetrofitClass.getApiService();
        service.acceptFriend(RetrofitClass.getUserToken(context), id).enqueue(new Callback<RecordResponse>() {
            @Override
            public void onResponse(@NotNull Call<RecordResponse> call, @NotNull Response<RecordResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        RecordResponse loginModel = response.body();
                        data.setValue(loginModel);
                        if (data.getValue().getStatus() != null) {
                            if (data.getValue().getStatus().equals("accepted")) {
                                view.acceptSuccess();
                            }
                        } else {
                            view.acceptError();
                        }
                    } else {
                        view.acceptError();
                    }
                } else {
                    view.acceptError();
                }
            }

            @Override
            public void onFailure(@NotNull Call<RecordResponse> call, @NotNull Throwable t) {
                view.acceptError();
            }
        });
    }
}
