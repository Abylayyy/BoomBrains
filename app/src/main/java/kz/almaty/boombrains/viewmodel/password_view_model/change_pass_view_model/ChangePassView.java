package kz.almaty.boombrains.viewmodel.password_view_model.change_pass_view_model;

import kz.almaty.boombrains.data.models.auth_models.ChangePassModel;

public interface ChangePassView {
    void showProgressBar();
    void hideProgressBar();
    void goToMain(ChangePassModel model);
}
