package kz.almaty.boombrains.viewmodel.reset_view_model;

import kz.almaty.boombrains.models.auth_models.ResetPassModel;

public interface ResetView {
    void showProgressBar();
    void hideProgressBar();
    void goToMain(ResetPassModel model);
}
