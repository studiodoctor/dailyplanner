package com.example.myandroidapplication.activities.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myandroidapplication.R;
import com.example.myandroidapplication.activities.adapters.AdapterNotifications;
import com.example.myandroidapplication.models.Notifications.Notification;
import com.example.myandroidapplication.models.Notifications.NotificationResponse;
import com.example.myandroidapplication.retrofit.RetrofitClient;
import com.example.myandroidapplication.retrofit.RetrofitInstance;
import com.google.gson.JsonObject;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallAPIActivity extends AppCompatActivity {

    private Button btn_call_api;
    private RecyclerView recycler_data_from_API;
    private AdapterNotifications adapterNotifications;
    private ArrayList<Notification> listNotifications;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setListeners();
    }

    private void init()
    {
        btn_call_api=findViewById(R.id.btn_call_api);
        recycler_data_from_API=findViewById(R.id.recycler_data_from_api);

        listNotifications= new ArrayList<Notification>();

        recycler_data_from_API.setHasFixedSize(true);
        recycler_data_from_API.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void setListeners(){

       btn_call_api.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               callAPI();
           }
       });

    }

    private void callAPI(){

        listNotifications.clear();

        JsonObject jsonToGetNotifications= new JsonObject();
        jsonToGetNotifications.addProperty("userID", "3");
        jsonToGetNotifications.addProperty("page_no", "1");

        RetrofitInstance networkInterface =
                RetrofitClient.getRetrofit().create(RetrofitInstance.class);

       Call<NotificationResponse> call = networkInterface.getNotifications(jsonToGetNotifications);
       call.enqueue(new Callback<NotificationResponse>() {
           @Override
           public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {

               assert response.body() != null;

               if (response.body() != null) {
                   NotificationResponse notificationResponse =response.body();

                   listNotifications.addAll(notificationResponse.getNotifications());

                   adapterNotifications = new AdapterNotifications(listNotifications);

                   recycler_data_from_API.setAdapter(adapterNotifications);
               }
           }

           @Override
           public void onFailure(Call<NotificationResponse> call, Throwable t) {
               Log.e("Error", "Error");
           }
       });
    }
}
