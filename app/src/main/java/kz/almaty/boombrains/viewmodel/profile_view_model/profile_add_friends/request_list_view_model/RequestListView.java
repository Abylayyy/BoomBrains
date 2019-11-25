package kz.almaty.boombrains.viewmodel.profile_view_model.profile_add_friends.request_list_view_model;

import java.util.List;

import kz.almaty.boombrains.models.add_friend_models.RequestListModel;

public interface RequestListView {
    void showLoading();
    void hideLoading();
    void listError();
    void listSuccess(List<RequestListModel> list);

}
