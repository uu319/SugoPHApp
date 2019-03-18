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

public class RegContact extends AppCompatActivity{
    private EditText txtEmail, txtContact;

    private Button btnNext;
    Validation validation= new Validation();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_contact);
        if(SharedPrefManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
        }
        txtContact= findViewById(R.id.txtContact);
        txtEmail= findViewById(R.id.txtEmail);


        btnNext= findViewById(R.id.btnNext);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean number= validation.validateNumber(txtContact);
                Boolean email= validation.validateEmail(txtEmail);
                if(number && email) {
                    Intent intent1 = getIntent();
                    final ArrayList<String> data = intent1.getStringArrayListExtra("data");
                    data.add(txtContact.getText().toString().trim());
                    data.add(txtEmail.getText().toString().trim());

                    Intent intent = new Intent(getApplicationContext(), UserRegistration.class);
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
