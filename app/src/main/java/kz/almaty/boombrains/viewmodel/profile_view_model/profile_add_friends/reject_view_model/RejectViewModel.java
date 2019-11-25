package kz.almaty.boombrains.viewmodel.profile_view_model.profile_add_friends.reject_view_model;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

import kz.almaty.boombrains.models.records_model.RecordResponse;
import kz.almaty.boombrains.services.APIService;
import kz.almaty.boombrains.services.RetrofitClass;
import kz.almaty.boombrains.viewmodel.profile_view_model.profile_add_friends.accept_view_model.AcceptView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RejectViewModel extends ViewModel {
    public void rejectFriends(String id, Context context, RejectView view) {
        final MutableLiveData<RecordResponse> data = new MutableLiveData<>();
        APIService service = RetrofitClass.getApiService();
        service.rejectFriend(RetrofitClass.getUserToken(context), id).enqueue(new Callback<RecordResponse>() {
            @Override
            public void onResponse(@NotNull Call<RecordResponse> call, @NotNull Response<RecordResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        RecordResponse loginModel = response.body();
                        data.setValue(loginModel);
                        if (data.getValue().getStatus() != null) {
                            if (data.getValue().getStatus().equals("accepted")) {
                                view.rejectSuccess();
                            }
                        } else {
                            view.rejectError();
                        }
                    } else {
                        view.rejectError();
                    }
                } else {
                    view.rejectError();
                }
            }

            @Override
            public void onFailure(@NotNull Call<RecordResponse> call, @NotNull Throwable t) {
                view.rejectError();
            }
        });
    }
}
