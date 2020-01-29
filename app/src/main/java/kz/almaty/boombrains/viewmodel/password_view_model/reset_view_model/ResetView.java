package kz.almaty.boombrains.viewmodel.password_view_model.reset_view_model;

import kz.almaty.boombrains.data.models.auth_models.ResetPassModel;

public interface ResetView {
    void showProgressBar();
    void hideProgressBar();
    void goToMain(ResetPassModel model);
}
