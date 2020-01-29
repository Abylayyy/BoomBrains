package kz.almaty.boombrains.data.services;

import java.util.List;

import kz.almaty.boombrains.data.models.auth_models.ChangePassModel;
import kz.almaty.boombrains.data.models.auth_models.MainLoginModel;
import kz.almaty.boombrains.data.models.auth_models.ResetPassModel;
import kz.almaty.boombrains.data.models.auth_models.register.GetLocalRecords;
import kz.almaty.boombrains.data.models.auth_models.register.RegisterModel;
import kz.almaty.boombrains.data.models.add_friend_models.RequestListModel;
import kz.almaty.boombrains.data.models.profile_model.ProfileRatingModel;
import kz.almaty.boombrains.data.models.rating_model.WorldRecordResponse;
import kz.almaty.boombrains.data.models.records_model.MyRecordsModel;
import kz.almaty.boombrains.data.models.records_model.RecordResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface APIService {

    @FormUrlEncoded
    @POST("/auth/login")
    Call<MainLoginModel> userLogin(@Header ("Secret-key") String key, @Header("App-Language") String lang,
                                   @Field("email") String email, @Field("password") String password);

    @POST("/auth/register")
    Call<MainLoginModel> registerWithRecords(@Header("Secret-key") String key, @Header("App-Language") String lang, @Body RegisterModel model);

    @FormUrlEncoded
    @POST("/auth/reset-password")
    Call<ResetPassModel> resetPassword(@Header ("Secret-key") String key, @Header("App-Language") String lang, @Field("email") String email);

    @FormUrlEncoded
    @POST("/auth/change-password")
    Call<ChangePassModel> changePassword(@Header("Authorization") String token, @Header("App-Language") String lang,
                                         @Field("previousPassword") String currentPass, @Field("password") String newPass);

    @GET("/api/games-list")
    Call<MyRecordsModel> getListOfRecords(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("/api/record")
    Call<RecordResponse> setNewRecord(@Header("Authorization") String token,
                                      @Field("game") String game, @Field("record") Integer record);

    @GET("/auth/logout")
    Call<RecordResponse> logoutFromGame(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("/auth/username")
    Call<RecordResponse> updateUserName(@Header("Authorization") String token, @Field("username") String username);

    @POST("/api/update-record")
    Call<ResponseBody> updateRecords(@Header("Authorization") String token, @Body GetLocalRecords records);

    @FormUrlEncoded
    @POST("/api/game-info")
    Call<WorldRecordResponse> getCurrentRating(@Header("Authorization") String token, @Field("game") String game);

    @GET("/api/game-info")
    Call<ProfileRatingModel> getProfileRatings(@Header("Authorization") String token, @Header("App-Language") String lang);

    @FormUrlEncoded
    @POST("/api/request")
    Call<RecordResponse> addNewFriend(@Header("Authorization") String token, @Header("App-Language") String lang, @Field("usernameOrId") String user);

    @FormUrlEncoded
    @POST("/api/accept")
    Call<RecordResponse> acceptFriend(@Header("Authorization") String token, @Field("id") String requestId);

    @FormUrlEncoded
    @POST("/api/reject")
    Call<RecordResponse> rejectFriend(@Header("Authorization") String token, @Field("id") String requestId);

    @FormUrlEncoded
    @POST("/api/friend-delete")
    Call<RecordResponse>  deleteFriend(@Header("Authorization") String token, @Field("username") String username);

    @GET("/api/request-list")
    Call<List<RequestListModel>> getRequestList(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("/api/foreign-game-info")
    Call<ProfileRatingModel> getFriendInfo(@Header("Authorization") String token, @Header("App-Language") String lang,
                                           @Field("username") String username, @Field("totalRecord") Integer totalRecord);
    @FormUrlEncoded
    @POST("/api/week")
    Call<RecordResponse> sendGameSeconds(@Header("Authorization") String token, @Field("seconds") String seconds);
}
