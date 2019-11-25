package kz.almaty.boombrains.viewmodel.records.record_view_model;

import java.util.List;

import kz.almaty.boombrains.models.records_model.MyRecordsModel;

public interface RecordView {
    void loadList(MyRecordsModel records);
    void showLoading();
    void hideLoading();
    void errorMessage(int code);
}
