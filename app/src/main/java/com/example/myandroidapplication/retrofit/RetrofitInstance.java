package com.example.myandroidapplication.retrofit;

import com.example.myandroidapplication.models.Notifications.NotificationResponse;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RetrofitInstance {
    @Headers({"Authorization: key=qCoOgOqmzs3peg1TQvm92M71MbFDFLqu1RxuxI55PCuaxQdutnBpTVGdLcyPPoMV5oSirlPfd00pm57oOxJ7n66i3S5rYo9hEzjt",
    "Content-Type: application/json"})
    @POST("getNotifications")
    Call<NotificationResponse> getNotifications(@Body JsonObject jsonObject);
}
