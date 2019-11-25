package kz.almaty.boombrains.viewmodel.rating_view_model;

import kz.almaty.boombrains.models.rating_model.WorldRecordResponse;

public interface WorldRatingView {
    void success(WorldRecordResponse response);
    void error(String message);
    void showLoading();
    void hideLoading();
    void errorMessage(int code);
}
