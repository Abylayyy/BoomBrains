package kz.almaty.boombrains.viewmodel.profile_view_model.profile_add_friends.add_view_model;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import org.jetbrains.annotations.NotNull;
import kz.almaty.boombrains.models.records_model.RecordResponse;
import kz.almaty.boombrains.services.APIService;
import kz.almaty.boombrains.services.RetrofitClass;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFriendViewModel extends ViewModel {
    public void addNewFriends(String name, Context context, AddFriendView view) {
        final MutableLiveData<RecordResponse> data = new MutableLiveData<>();
        APIService service = RetrofitClass.getApiService();
        service.addNewFriend(RetrofitClass.getUserToken(context),
                RetrofitClass.getLang(context), name).enqueue(new Callback<RecordResponse>() {
            @Override
            public void onResponse(@NotNull Call<RecordResponse> call, @NotNull Response<RecordResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        RecordResponse loginModel = response.body();
                        data.setValue(loginModel);
                        if (data.getValue().getStatus() != null) {
                            if (data.getValue().getStatus().equals("accepted")) {
                                view.onAddFriendSuccess();
                            }
                        } else {
                            view.onFieldsNotCorrect(data.getValue());
                        }
                    } else {
                        view.onAddFriendError();
                    }
                } else {
                    switch (response.code()) {
                        case 404: view.onAddFriendErrorCode(404); break;
                        case 401: view.onAddFriendErrorCode(401); break;
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<RecordResponse> call, @NotNull Throwable t) {
                view.onAddFriendError();
            }
        });
    }
}
