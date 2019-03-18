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
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pikot.sugophapp.Both.Login;
import com.example.pikot.sugophapp.Both.MainActivity;
import com.example.pikot.sugophapp.R;
import com.example.pikot.sugophapp.RandomClasses.Constants;
import com.example.pikot.sugophapp.RandomClasses.SharedPrefManager;
import com.example.pikot.sugophapp.RandomClasses.Validation;

public class EseekerPostProductActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText txtProdName, txtQuantity, txtUnit, txtDescription, txtBudget;
    private Button btnPostErrand;
    private Intent intent;
    private Validation validation= new Validation();
    String optionName;
    private ImageButton btnHelp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_eseeker_post_product);
        if(!SharedPrefManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(this, Login.class));
        }

        intent= getIntent();
        optionName=intent.getStringExtra("option_name");

        txtProdName= findViewById(R.id.txtProdName);
        txtQuantity= findViewById(R.id.txtQuantity);
        txtUnit= findViewById(R.id.txtUnit);
        txtDescription= findViewById(R.id.txtDescription);
        txtBudget= findViewById(R.id.txtBudget);
        btnPostErrand= findViewById(R.id.btnPostErrand);
        btnHelp= findViewById(R.id.btnHelp);

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflate = LayoutInflater.from(EseekerPostProductActivity.this);
                View alertView = inflate.inflate(R.layout.help_dialog, null);
                ImageView imgHelp= alertView.findViewById(R.id.imgHelp);
                Button btnBack= alertView.findViewById(R.id.btnBack);
                TextView txtHelp1,txtHelp2, txtHelp3, txtHelp4;

                txtHelp1= alertView.findViewById(R.id.txtHelp1);
                txtHelp2= alertView.findViewById(R.id.txtHelp2);
                txtHelp3= alertView.findViewById(R.id.txtHelp3);
                txtHelp4= alertView.findViewById(R.id.txtHelp4);

                txtHelp1.setText("It should be a second hand android phone with no major damages.");
                txtHelp2.setText("Do they allow paying using my credit card?");
                txtHelp3.setText("Does it come with a warranty?");
                txtHelp4.setText("Do they offer free delivery service?");
                final Dialog alertDialog = new Dialog(EseekerPostProductActivity.this);
                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setContentView(alertView);
                alertDialog.getWindow().setBackgroundDrawableResource(R.color.azur);
                alertDialog.show();
            }
        });

        btnPostErrand.setOnClickListener(this);
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
            if(validation.validateNormalInput(txtProdName) && validation.validateNormalInput(txtQuantity) && validation.validateNormalInput(txtUnit) &&
                    validation.validateNormalInput(txtDescription) && validation.validateNormalInput(txtBudget)) {
                        String description= txtProdName.getText().toString().trim()+"~"+txtQuantity.getText().toString().trim()+"~"+
                                txtUnit.getText().toString().trim()+"~"+txtBudget.getText().toString().trim()+"~"+txtDescription.getText().toString().trim();
                Intent intent= new Intent(this, EseekerExpDateActivity.class);
                intent.putExtra("option_name", optionName);
                intent.putExtra("description", description);
                startActivity(intent);
            }

    }
}
