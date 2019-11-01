package kz.almaty.boombrains.services;

import kz.almaty.boombrains.models.auth_models.ChangePassModel;
import kz.almaty.boombrains.models.auth_models.MainLoginModel;
import kz.almaty.boombrains.models.auth_models.ResetPassModel;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface APIService {

    @FormUrlEncoded
    @POST("/auth/login")
    Call<MainLoginModel> userLogin(@Header ("Secret-key") String key,
                                   @Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("/auth/register")
    Call<MainLoginModel> registerUser(@Header ("Secret-key") String key, @Field("email") String email,
                                      @Field("password") String password, @Field("username") String username);

    @FormUrlEncoded
    @POST("/auth/reset-password")
    Call<ResetPassModel> resetPassword(@Header ("Secret-key") String key, @Field("email") String email);

    @FormUrlEncoded
    @POST("/auth/change-password")
    Call<ChangePassModel> changePassword(@Header("Authorization") String token,
                                         @Field("previousPassword") String currentPass, @Field("password") String newPass);


}
