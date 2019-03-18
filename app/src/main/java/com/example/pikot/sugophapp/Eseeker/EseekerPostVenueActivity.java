package com.example.pikot.sugophapp.Eseeker;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pikot.sugophapp.Both.Login;
import com.example.pikot.sugophapp.Both.MainActivity;
import com.example.pikot.sugophapp.R;
import com.example.pikot.sugophapp.RandomClasses.SharedPrefManager;
import com.example.pikot.sugophapp.RandomClasses.Validation;

public class EseekerPostVenueActivity extends AppCompatActivity implements View.OnClickListener {
    EditText txtCapacity, txtBudget, txtDescription, txtVenue;
    Button btnPostErrand;
    Validation validation= new Validation();
    String optionName;
    ImageButton btnHelp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_eseeker_post_venue);
        Intent intent= getIntent();
        optionName= intent.getStringExtra("option_name");

        if(!SharedPrefManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(this, Login.class));
        }


        txtCapacity = findViewById(R.id.txtCapacity);
        txtBudget= findViewById(R.id.txtBudget);
        txtDescription= findViewById(R.id.txtDescription);
        txtVenue= findViewById(R.id.txtVenue);
        btnPostErrand= findViewById(R.id.btnPostErrand);
        btnPostErrand.setOnClickListener(this);
        btnHelp= findViewById(R.id.btnHelp);

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflate = LayoutInflater.from(EseekerPostVenueActivity.this);
                View alertView = inflate.inflate(R.layout.help_dialog, null);
                final Dialog alertDialog = new Dialog(EseekerPostVenueActivity.this);
                Button btnBack= alertView.findViewById(R.id.btnBack);
                TextView txtHelp1,txtHelp2, txtHelp3, txtHelp4;

                txtHelp1= alertView.findViewById(R.id.txtHelp1);
                txtHelp2= alertView.findViewById(R.id.txtHelp2);
                txtHelp3= alertView.findViewById(R.id.txtHelp3);
                txtHelp4= alertView.findViewById(R.id.txtHelp4);

                txtHelp1.setText("Does the venue allow children under 8 years old?");
                txtHelp2.setText("You must provide accurate location(ex. location near Lourdes Village Punta Princesa, Cebu City)");
                txtHelp3.setText("Does it have parking space for 2 cars?");
                txtHelp4.setText("Do they allow bringing of foods from outside?");
                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setContentView(alertView);
                alertDialog.getWindow().setBackgroundDrawableResource(R.color.azur);
                alertDialog.getWindow().setLayout(1000,1200);
                alertDialog.show();
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(!SharedPrefManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(this, Login.class));
        }

    }


    @Override
    public void onClick(View v) {
        if(validation.validateNormalInput(txtVenue) && validation.validateNormalInput(txtCapacity) && validation.validateNormalInput(txtBudget) && validation.validateNormalInput(txtDescription)) {
        String description=txtVenue.getText().toString().trim()+"~"+txtCapacity.getText().toString().trim()+"~"+txtBudget.getText().toString().trim()+"~"+
                    txtDescription.getText().toString().trim();
            Intent intent= new Intent(this, EseekerExpDateActivity.class);
            intent.putExtra("option_name", optionName);
            intent.putExtra("description", description);
            startActivity(intent);
        }

    }
}
