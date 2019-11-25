package kz.almaty.boombrains.viewmodel.records.update_record;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

import kz.almaty.boombrains.models.auth_models.register.GetLocalRecords;
import kz.almaty.boombrains.services.APIService;
import kz.almaty.boombrains.services.RetrofitClass;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateRecordViewModel extends ViewModel {

    public void setUpdatedRecords(Context context, GetLocalRecords model, UpdateRecordView view) {
        APIService service = RetrofitClass.getApiService();
        service.updateRecords(RetrofitClass.getUserToken(context), model).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        view.success("SUCCESS!!!");
                    } else {
                        view.error("RESPONSE IS EMPTY!!!");
                    }
                } else {
                    view.error("Response not successful!");
                    switch (response.code()) {
                        case 404: view.errorUpdate(404); break;
                        case 401: view.errorUpdate(401); break;
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                view.error("Error with loading data");
            }
        });
    }
}
