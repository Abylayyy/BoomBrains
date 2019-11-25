package kz.almaty.boombrains.viewmodel.records.record_view_model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import org.jetbrains.annotations.NotNull;
import kz.almaty.boombrains.models.records_model.MyRecordsModel;
import kz.almaty.boombrains.services.APIService;
import kz.almaty.boombrains.services.RetrofitClass;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecordViewModel extends ViewModel {

    public void getAllRecords(Context context, RecordView view) {
        final MutableLiveData<MyRecordsModel> data = new MutableLiveData<>();
        APIService service = RetrofitClass.getApiService();
        view.showLoading();
        service.getListOfRecords(RetrofitClass.getUserToken(context)).enqueue(new Callback<MyRecordsModel>() {
            @Override
            public void onResponse(@NotNull Call<MyRecordsModel> call, @NotNull Response<MyRecordsModel> response) {
                view.hideLoading();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d("EVERY::", "GOOD");
                        MyRecordsModel loginModel = response.body();
                        data.setValue(loginModel);
                        view.loadList(data.getValue());
                    } else {
                        showToast(context, "Result is empty!");
                    }
                } else {
                    switch (response.code()) {
                        case 404: view.errorMessage(404); break;
                        case 401: view.errorMessage(401); break;
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<MyRecordsModel> call, @NotNull Throwable t) {
                view.hideLoading();
                showToast(context, t.getMessage());
            }
        });
    }

    private void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
