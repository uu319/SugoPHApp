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

public class EseekerPostServicesActivity extends AppCompatActivity implements View.OnClickListener{
    EditText txtServiceName, txtLocation, txtBudget, txtDescription;
    Button btnPostErrand;
    ImageButton btnHelp;
    Validation validation= new Validation();

    String optionName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_eseeker_post_services);
        if(!SharedPrefManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(this, Login.class));
        }

        Intent intent= getIntent();
        optionName= intent.getStringExtra("option_name");
        txtServiceName= findViewById(R.id.txtServiceName);
        txtLocation= findViewById(R.id.txtLocation);
        txtBudget= findViewById(R.id.txtBudget);
        txtDescription= findViewById(R.id.txtDescription);
        btnPostErrand= findViewById(R.id.btnPostErrand);
        btnPostErrand.setOnClickListener(this);
        btnHelp= findViewById(R.id.btnHelp);

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflate = LayoutInflater.from(EseekerPostServicesActivity.this);
                View alertView = inflate.inflate(R.layout.help_dialog, null);
                final Dialog alertDialog = new Dialog(EseekerPostServicesActivity.this);
                Button btnBack= alertView.findViewById(R.id.btnBack);
                TextView txtHelp1,txtHelp2, txtHelp3, txtHelp4;

                txtHelp1= alertView.findViewById(R.id.txtHelp1);
                txtHelp2= alertView.findViewById(R.id.txtHelp2);

                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                txtHelp1.setText("I need the service this coming March 01, 2019 ");
                txtHelp2.setText("It should be at 12:30 sharp ");
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setContentView(alertView);
                alertDialog.getWindow().setBackgroundDrawableResource(R.color.azur);
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
        if(validation.validateNormalInput(txtBudget) && validation.validateNormalInput(txtDescription)&&
                validation.validateNormalInput(txtLocation)&&
                validation.validateNormalInput(txtServiceName)) {
            String description = txtServiceName.getText().toString().trim() + "~" + txtLocation.getText().toString().trim()+"~"+txtBudget.getText().toString().trim() + "~" +
                    txtDescription.getText().toString().trim();
            Intent intent= new Intent(this, EseekerExpDateActivity.class);
            intent.putExtra("option_name", optionName);
            intent.putExtra("description", description);
            startActivity(intent);
        }
    }
}
