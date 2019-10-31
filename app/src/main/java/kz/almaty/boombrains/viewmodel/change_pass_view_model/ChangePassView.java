package kz.almaty.boombrains.viewmodel.change_pass_view_model;

import kz.almaty.boombrains.models.auth_models.ChangePassModel;

public interface ChangePassView {
    void showProgressBar();
    void hideProgressBar();
    void goToMain(ChangePassModel model);
}
