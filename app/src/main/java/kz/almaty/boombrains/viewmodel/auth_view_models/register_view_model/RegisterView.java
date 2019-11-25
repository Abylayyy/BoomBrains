package kz.almaty.boombrains.viewmodel.auth_view_models.register_view_model;

import kz.almaty.boombrains.models.auth_models.MainLoginModel;

public interface RegisterView {
    void showProgressBar();
    void hideProgressBar();
    void goToMain(MainLoginModel model);
}
