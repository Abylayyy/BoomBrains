package kz.almaty.boombrains.services;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;

import kz.almaty.boombrains.helpers.SharedPrefManager;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClass {

    private static final String URL = "https://boom-brains-api.kdo.one";

    private static Retrofit getRetrofit() {

        Gson gson = new GsonBuilder().setLenient().create();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).addInterceptor(interceptor).build();

        return new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client).build();
    }

    public static APIService getApiService() {
        return getRetrofit().create(APIService.class);
    }

    public static String getUserToken(Context context) {
        return "Bearer " + SharedPrefManager.getUserAuthTokenKey(context);
    }
}
