package com.example.pikot.sugophapp.Both;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pikot.sugophapp.R;
import com.example.pikot.sugophapp.RandomClasses.CallBacks;
import com.example.pikot.sugophapp.RandomClasses.SharedPrefManager;
import com.example.pikot.sugophapp.Requests.GetUserStatus;
import com.example.pikot.sugophapp.Requests.LogoutRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class ErunnerBannedActivity extends AppCompatActivity {
    Button btnLogout, btnUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erunner_banned);
        btnLogout= findViewById(R.id.btnLogout);
        btnUpdate= findViewById(R.id.btnUpdate);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetUserStatus.getStatus(ErunnerBannedActivity.this, SharedPrefManager.getInstance(ErunnerBannedActivity.this).getKeyUsername(),new CallBacks() {
                    @Override
                    public void onSuccess(String response) throws JSONException {
                        if(response!=null){
                            JSONObject jsonObject= new JSONObject(response);
                            if(jsonObject.getBoolean("error")){
                                Toast.makeText(ErunnerBannedActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            }else{
                                if(jsonObject.getString("msg").equals("active")){
                                    SharedPrefManager.getInstance(ErunnerBannedActivity.this).updKeyStatus("active");
                                    startActivity(new Intent(ErunnerBannedActivity.this, MainActivity.class));
                                }else{
                                    Toast.makeText(ErunnerBannedActivity.this, "Account not active.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }else{
                            Toast.makeText(ErunnerBannedActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogoutRequest.logoutRequest(ErunnerBannedActivity.this);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }
}
