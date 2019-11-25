package kz.almaty.boombrains.viewmodel.profile_view_model.profile_add_friends.friend_info_view_model;

import java.util.List;

import kz.almaty.boombrains.models.profile_model.Achievement;
import kz.almaty.boombrains.models.profile_model.ProfileRatingModel;
import kz.almaty.boombrains.models.profile_model.ProfileWorldRecord;

public interface FriendsInfoView {
    void loadingData();
    void loadingFinished();
    void onWholeInfo(ProfileRatingModel model);
    void onErrorOccurred(int error);
    void onFailed();
}
