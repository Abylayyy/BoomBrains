package kz.almaty.boombrains.viewmodel.login_view_model;

import kz.almaty.boombrains.models.auth_models.MainLoginModel;

public interface LoginView {
    void showProgressBar();
    void hideProgressBar();
    void goToMain(MainLoginModel model);
}
