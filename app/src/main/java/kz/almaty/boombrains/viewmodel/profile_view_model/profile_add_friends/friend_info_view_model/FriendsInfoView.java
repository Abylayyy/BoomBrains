package kz.almaty.boombrains.viewmodel.profile_view_model.profile_add_friends.friend_info_view_model;

import kz.almaty.boombrains.data.models.profile_model.ProfileRatingModel;

public interface FriendsInfoView {
    void loadingData();
    void loadingFinished();
    void onWholeInfo(ProfileRatingModel model);
    void onErrorOccurred(int error);
    void onFailed();
}
