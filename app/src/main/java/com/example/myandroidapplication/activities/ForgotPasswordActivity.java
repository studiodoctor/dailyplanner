package com.example.myandroidapplication.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myandroidapplication.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText email_id;
    private Button btn_continue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot_password);
        init();
        setListeners();
    }

    private void init() {
       email_id=findViewById(R.id.email_id);
       btn_continue=findViewById(R.id.btn_login);
    }

    private void setListeners () {
       btn_continue.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

           }
       });
    }
}
