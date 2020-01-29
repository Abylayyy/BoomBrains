package kz.almaty.boombrains.viewmodel.records.send_minutes;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

import kz.almaty.boombrains.data.models.records_model.RecordResponse;
import kz.almaty.boombrains.data.services.RetrofitClass;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendSecondViewModel extends ViewModel {

    public void sendStatistics(String second, Context context, SendSecondView view) {
        RetrofitClass.getApiService().sendGameSeconds(RetrofitClass.getUserToken(context), second).enqueue(new Callback<RecordResponse>() {
            @Override
            public void onResponse(@NotNull Call<RecordResponse> call, @NotNull Response<RecordResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("added")) {
                            view.successSecond();
                        }
                    }
                } else {
                    view.errorSecond();
                }
            }

            @Override
            public void onFailure(@NotNull Call<RecordResponse> call, @NotNull Throwable t) {
                view.errorSecond();
            }
        });
    }

}
