package kz.almaty.boombrains.viewmodel.profile_view_model.profile_add_friends.friend_info_view_model;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

import kz.almaty.boombrains.data.models.profile_model.ProfileRatingModel;
import kz.almaty.boombrains.data.services.RetrofitClass;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsInfoViewModel extends ViewModel {
    public void getFriendInfo(String username, int record, Context context, FriendsInfoView view) {
        final MutableLiveData<ProfileRatingModel> data = new MutableLiveData<>();
        view.loadingData();
        RetrofitClass.getApiService().getFriendInfo(RetrofitClass.getUserToken(context),
                RetrofitClass.getLang(context), username, record).enqueue(new Callback<ProfileRatingModel>() {
            @Override
            public void onResponse(@NotNull Call<ProfileRatingModel> call, @NotNull Response<ProfileRatingModel> response) {
                view.loadingFinished();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        ProfileRatingModel loginModel = response.body();
                        data.setValue(loginModel);
                        view.onWholeInfo(data.getValue());
                    } else {
                        view.onFailed();
                    }
                } else {
                    switch (response.code()) {
                        case 404: view.onErrorOccurred(404); break;
                        case 401: view.onErrorOccurred(401); break;
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<ProfileRatingModel> call, @NotNull Throwable t) {
                view.onFailed();
                view.loadingFinished();
            }
        });
    }
}
