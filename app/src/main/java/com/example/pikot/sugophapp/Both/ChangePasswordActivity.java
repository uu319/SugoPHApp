package com.example.pikot.sugophapp.Both;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pikot.sugophapp.R;
import com.example.pikot.sugophapp.RandomClasses.CallBacks;
import com.example.pikot.sugophapp.RandomClasses.ShowAlert;
import com.example.pikot.sugophapp.RandomClasses.Validation;
import com.example.pikot.sugophapp.Requests.ChangPasswordRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText txtOldPass, txtNewPass, txtConfimPass;
    Button btnChange;
    Validation validation= new Validation();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        txtOldPass= findViewById(R.id.txtOldPass);
        txtNewPass= findViewById(R.id.txtNewPass);
        txtConfimPass= findViewById(R.id.txtConfirmPassword);
        btnChange= findViewById(R.id.btnChange);

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Boolean testOldPassword = validation.validateNormalInput(txtOldPass);
                Boolean testnewPassword = validation.validatePassword(txtNewPass);
                final Boolean testConfirmPassword = validation.validatePassword(txtConfimPass);
                if(testOldPassword && testnewPassword && testConfirmPassword && samePassword()){
                    final String oldPass, newPass, confirmPass;
                    oldPass= txtOldPass.getText().toString().trim();
                    newPass= txtNewPass.getText().toString().trim();
                    confirmPass= txtConfimPass.getText().toString().trim();

                    ChangPasswordRequest.changePassword(ChangePasswordActivity.this,oldPass,newPass,new CallBacks() {
                        @Override
                        public void onSuccess(String response) throws JSONException {
                            if(response!=null){
                                JSONObject jsonObject= new JSONObject(response);
                                if(jsonObject.getBoolean("error")){
                                    ShowAlert.showAlert(ChangePasswordActivity.this,jsonObject.getString("msg"));
                                }else{
                                    txtOldPass.setText("");
                                    txtNewPass.setText("");
                                    txtConfimPass.setText("");
                                    txtOldPass.setError(null);
                                    txtNewPass.setError(null);
                                    txtConfimPass.setError(null);
                                    ShowAlert.showAlert(ChangePasswordActivity.this,jsonObject.getString("msg"));
                                }
                            }else{
                                ShowAlert.showAlert(ChangePasswordActivity.this,"Something went wrong, please refresh the page.");
                            }
                        }
                    });
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }

    private Boolean samePassword(){
        Boolean samePass= txtNewPass.getText().toString().trim().equals(txtConfimPass.getText().toString().trim())?true:false;
        if(samePass==false){
            txtConfimPass.setError("Password mismatch");
        }
        return samePass;
    }

}
