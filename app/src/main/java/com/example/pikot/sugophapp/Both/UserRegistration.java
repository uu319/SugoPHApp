package com.example.pikot.sugophapp.Both;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pikot.sugophapp.Erunner.ErunnerPendingRegistration;
import com.example.pikot.sugophapp.R;
import com.example.pikot.sugophapp.RandomClasses.SessionHandler;
import com.example.pikot.sugophapp.RandomClasses.SharedPrefManager;
import com.example.pikot.sugophapp.RandomClasses.ShowAlert;
import com.example.pikot.sugophapp.RandomClasses.Validation;
import com.example.pikot.sugophapp.Requests.UserRegistrationRequest;
import com.example.pikot.sugophapp.RandomClasses.CallBacks;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class UserRegistration extends AppCompatActivity implements View.OnClickListener{

    private EditText txtUsername,txtPassword, txtPassword2;
    private Button btnRegister;
    private Intent intent;
    private ArrayList<String> data;

    private ImageView imgProfpic;
    private int IMG_REQUEST= 1;
    private Bitmap bitmap;
    Validation validation= new Validation();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(SharedPrefManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
        }
        intent= getIntent();
        data= intent.getStringArrayListExtra("data");
        setContentView(R.layout.activity_reg_user);
        txtUsername= findViewById(R.id.txtUsername);
        txtPassword= findViewById(R.id.txtPassword);
        txtPassword2= findViewById(R.id.txtPassword2);
        imgProfpic= findViewById(R.id.imgProfpic);


        btnRegister= findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(this);
        imgProfpic.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        if(SharedPrefManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
        }
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:

                if(bitmap!=null){

                    Boolean testUsername = validation.validateUsername(txtUsername);
                    Boolean testPassword = validation.validatePassword(txtPassword);
                    if (testUsername && testPassword && samePassword()) {
                        data.add(imageToString(bitmap));
                        registerUser(txtUsername.getText().toString().trim(),txtPassword.getText().toString().trim());
                    }
                }else{
                    ShowAlert.showAlert(this,"Please provide a photo");
//                    Toast.makeText(this, "Please choose a photo", Toast.LENGTH_LONG).show();
                }

                    break;
                    case R.id.imgProfpic:
                        selectImage();
                }
        }

    public void selectImage(){
        Intent intent= new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
    }

    private Boolean samePassword(){
        Boolean samePass= txtPassword.getText().toString().trim().equals(txtPassword2.getText().toString().trim())?true:false;
        if(samePass==false){
            txtPassword2.setError("Password mismatch");
        }
        return samePass;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMG_REQUEST && resultCode==RESULT_OK && data!=null){
            Uri path= data.getData();
            try {
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                imgProfpic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgBytes= byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

    private void registerUser(String username, String password){
        UserRegistrationRequest.registerUser(this, new CallBacks() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if(response!=null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean("error")) {
                            ShowAlert.showAlert(UserRegistration.this, jsonObject.getString("msg"));
                            txtUsername.setText("");
                        } else {
                            startActivity(new Intent(getApplicationContext(), Login.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    ShowAlert.showAlert(UserRegistration.this,"Something went wrong, please try again.");
//                    Toast.makeText(UserRegistration.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        },data, username, password);
    }


}
