package com.example.myandroidapplication.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myandroidapplication.R;
import com.example.myandroidapplication.data.SharedPreferenceMaster;

public class SecondActivity extends AppCompatActivity {

    private String name, surname, cellNo, address;
    private TextView txt_name, txt_cellNo, txt_address;

    //declaring shared preferences
    private SharedPreferenceMaster sharedPreferenceMaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_activity);
        init();
    }

    private void init () {
        // initializing shared preferences
        sharedPreferenceMaster = new SharedPreferenceMaster(SecondActivity.this);

        name=getIntent().getStringExtra("Name");
        surname=getIntent().getStringExtra("Surname");
        cellNo=getIntent().getStringExtra("Cell_Number");
        address=getIntent().getStringExtra("Address");

        txt_name=findViewById(R.id.reg_name);
        txt_cellNo=findViewById(R.id.reg_cellNo);
        txt_address=findViewById(R.id.reg_address);

        txt_name.setText("Welcome "+sharedPreferenceMaster.getUserName()+ "" +sharedPreferenceMaster.getUserSurname());
        txt_cellNo.setText("Mobile Number is"+sharedPreferenceMaster.getUserCell());
        txt_address.setText("Address is"+sharedPreferenceMaster.getUserAddress());
    }

}
