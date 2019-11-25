package kz.almaty.boombrains.viewmodel.records.update_record;

public interface UpdateRecordView {
    void success(String message);
    void error(String message);
    void errorUpdate(int i);
}
