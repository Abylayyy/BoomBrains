package kz.almaty.boombrains.viewmodel.profile_view_model.profile_ratings;

import java.util.List;

import kz.almaty.boombrains.models.profile_model.Achievement;
import kz.almaty.boombrains.models.profile_model.ProfileRatingModel;
import kz.almaty.boombrains.models.profile_model.ProfileWorldRecord;

public interface ProfileRatingView {
    void onWorldRecords(List<ProfileWorldRecord> records);
    void onFriendsRecord(List<ProfileWorldRecord> records);
    void onAchievements(List<Achievement> achievements);
    void onWholeInfo(ProfileRatingModel model);
    void onErrorOccurred(int error);
    void onFailed();
    void loadingData();
    void loadingFinished();
}
