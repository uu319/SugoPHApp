package com.example.pikot.sugophapp.Both;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pikot.sugophapp.R;
import com.example.pikot.sugophapp.RandomClasses.SharedPrefManager;
import com.example.pikot.sugophapp.RandomClasses.ShowAlert;
import com.example.pikot.sugophapp.RandomClasses.Validation;
import com.example.pikot.sugophapp.Requests.GetUserStatus;
import com.example.pikot.sugophapp.Requests.LoginRequest;
import com.example.pikot.sugophapp.RandomClasses.CallBacks;
import com.example.pikot.sugophapp.Requests.TokenRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;



public class Login extends AppCompatActivity implements View.OnClickListener {
    private TextView txtRegErunner, txtRegEseeker;
    private EditText txtUsername, txtPassword;
    private ProgressDialog progressDialog;
    Validation validation = new Validation();
    private Button btnLogin;

    private String token;

    private String currentLocation = null;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private String globalStatus;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        getActionBar().setBackgroundDrawable("@drawa");

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
        }
        getToken();



        txtRegErunner = findViewById(R.id.txtRegErunner);
        txtRegEseeker = findViewById(R.id.txtRegEseeker);
        txtPassword = findViewById(R.id.txtPassword);
        txtUsername = findViewById(R.id.txtUsername);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(this);
        txtRegEseeker.setOnClickListener(this);
        txtRegErunner.setOnClickListener(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (SharedPrefManager.getInstance(this).getKeyUsername() != null) {
            startActivity(new Intent(this, MainActivity.class));
            return;
        }

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void getToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FirebaseMessage", "getInstanceId failed", task.getException());
                            return;
                        }
                        token = task.getResult().getToken();
                        insertToken(token);
                        Log.d("FirebaseMessage", token, task.getException());
                    }
                });
    }
    private void insertToken(String token){
        TokenRequest.insertToken(this, token);
    }
    private void login(final String username, final String password) {
        final ProgressDialog progressDialog;
        progressDialog= new ProgressDialog(Login.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
//        Log.d("Token Here: ", token);
        try{
            LoginRequest.login(this, new CallBacks() {
                @Override
                public void onSuccess(String response) throws JSONException {
                    progressDialog.dismiss();
                    if (response != null) {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean("error")) {

                            ShowAlert.showAlert(Login.this,jsonObject.getString("msg"));

                        } else {
                            String type= jsonObject.getString("type");
                            String status= jsonObject.getString("status");
                            SharedPrefManager.getInstance(Login.this).userLogin(username,type,token);
                            SharedPrefManager.getInstance(Login.this).setKeyStatus(status);
                            try{
                                if(status.equals("denied")){
                                    ShowAlert.showAlert(Login.this, "Your application has been denied, sorry.");
                                } else if(status.equals("pending")) {
                                    startActivity(new Intent(Login.this, ErunnerPendingActivity.class));
                                }else if(status.equals("banned")){
                                    startActivity(new Intent(Login.this, ErunnerBannedActivity.class));
                                }else if(status.equals("suspended")){
                                    startActivity(new Intent(Login.this, ErunnerSuspendedActivity.class));
                                }else{
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }



                        }
                    } else {
                        ShowAlert.showAlert(Login.this, "Something went wrong.");
//                        Toast.makeText(Login.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }, username, password, token);
        }catch(Exception e){
            e.printStackTrace();
        }

    }



    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), RegName.class);
        switch (v.getId()) {
            case R.id.txtRegErunner:
                startActivity(new Intent(this, TermsAndConditions.class).putExtra("type", "erunner"));
                break;
            case R.id.txtRegEseeker:
                startActivity(new Intent(this, TermsAndConditions.class).putExtra("type", "eseeker"));
                break;
            case R.id.btnLogin:
                if (validation.validateNormalInput(txtUsername) && validation.validateNormalInput(txtPassword)) {
                    final String username = txtUsername.getText().toString().trim();
                    final String password = txtPassword.getText().toString().trim();
                    login(username, password);
                }
                break;
        }
    }

}




