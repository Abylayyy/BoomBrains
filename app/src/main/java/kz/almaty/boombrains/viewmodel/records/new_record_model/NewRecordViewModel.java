package kz.almaty.boombrains.viewmodel.records.new_record_model;

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

public class NewRecordViewModel extends ViewModel {

    public void setNewRecord(String game, Integer record, Context context, NewRecordView view) {
        final MutableLiveData<RecordResponse> data = new MutableLiveData<>();
        APIService service = RetrofitClass.getApiService();
        service.setNewRecord(RetrofitClass.getUserToken(context), game, record).enqueue(new Callback<RecordResponse>() {
            @Override
            public void onResponse(@NotNull Call<RecordResponse> call, @NotNull Response<RecordResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        data.setValue(response.body());
                        view.success();
                    } else {
                        view.error("Empty response!");
                    }
                } else {
                    view.error("Response is not successful!");
                }
            }

            @Override
            public void onFailure(@NotNull Call<RecordResponse> call, @NotNull Throwable t) {
                view.error("Error with loading data!");
            }
        });
    }
}
