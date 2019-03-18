package com.example.pikot.sugophapp.Both;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pikot.sugophapp.R;
import com.example.pikot.sugophapp.RandomClasses.SessionHandler;
import com.example.pikot.sugophapp.RandomClasses.SharedPrefManager;
import com.example.pikot.sugophapp.RandomClasses.Validation;

import java.util.ArrayList;

public class RegAddress extends AppCompatActivity {
    private EditText txtCity, txtStreet, txtBrgy;

    private Button btnNext;
    Validation validation= new Validation();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_address);
        if(SharedPrefManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
        }
        txtStreet= findViewById(R.id.txtStreet);
        txtBrgy= findViewById(R.id.txtBrgy);
        txtCity= findViewById(R.id.txtCity);




        btnNext= findViewById(R.id.btnNext);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean testStreet = validation.validateNormalInput(txtStreet);
                Boolean testCity = validation.validateNormalInput(txtCity);
                Boolean testBrgy = validation.validateNormalInput(txtBrgy);
                if (testStreet && testBrgy && testCity) {
                    Intent intent1 = getIntent();
                    ArrayList<String> data = intent1.getStringArrayListExtra("data");
                    data.add(txtStreet.getText().toString().trim());
                    data.add(txtCity.getText().toString().trim());
                    data.add(txtBrgy.getText().toString().trim());

                    Intent intent = new Intent(getApplicationContext(), RegContact.class);
                    intent.putStringArrayListExtra("data", data);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        if(SharedPrefManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
        }
        super.onResume();
    }
}
