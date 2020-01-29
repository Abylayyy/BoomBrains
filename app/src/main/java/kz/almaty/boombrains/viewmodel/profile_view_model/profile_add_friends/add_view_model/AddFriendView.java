package kz.almaty.boombrains.viewmodel.profile_view_model.profile_add_friends.add_view_model;

import kz.almaty.boombrains.data.models.records_model.RecordResponse;

public interface AddFriendView {
    void onAddFriendSuccess();
    void onAddFriendError();
    void onAddFriendErrorCode(int code);
    void onFieldsNotCorrect(RecordResponse response);
}
