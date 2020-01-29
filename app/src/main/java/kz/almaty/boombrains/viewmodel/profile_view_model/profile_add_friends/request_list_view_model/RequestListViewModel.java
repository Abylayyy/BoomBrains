package kz.almaty.boombrains.viewmodel.profile_view_model.profile_add_friends.request_list_view_model;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import kz.almaty.boombrains.data.models.add_friend_models.RequestListModel;
import kz.almaty.boombrains.data.services.APIService;
import kz.almaty.boombrains.data.services.RetrofitClass;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestListViewModel extends ViewModel {
    public void getAllRequests(Context context, RequestListView view) {
        final MutableLiveData<List<RequestListModel>> data = new MutableLiveData<>();
        APIService service = RetrofitClass.getApiService();
        view.showLoading();
        service.getRequestList(RetrofitClass.getUserToken(context)).enqueue(new Callback<List<RequestListModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<RequestListModel>> call, @NotNull Response<List<RequestListModel>> response) {
                view.hideLoading();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<RequestListModel> listModels = response.body();
                        data.setValue(listModels);
                        view.listSuccess(data.getValue());
                    } else {
                        Log.d("RESPONSE::", "EMPTY");
                    }
                } else {
                    Log.d("RESPONSE::", "NOT SUCCESSFUL");
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<RequestListModel>> call, @NotNull Throwable t) {
                view.listError();
            }
        });
    }
}
