package com.example.API;

import com.example.model.obj_imgur;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    // https://api.imgur.com/3/image
    public static final String DO_MAIN = "https://api.imgur.com/";
    public static final String CLIENT_ID = "ace5fa66141a632";
    Gson gson = new GsonBuilder().setDateFormat("yyyy MM dd HH:mm:ss").create();
    ApiService apiService = new Retrofit.Builder()
            .baseUrl(DO_MAIN)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @Multipart
    @POST("3/image")
    Call<obj_imgur> registerImg(
            @Header("Authorization") String clientId,
            @Part MultipartBody.Part image,
            @Part("type") RequestBody type,
            @Part("title") RequestBody title,
            @Part("description") RequestBody description);
}
