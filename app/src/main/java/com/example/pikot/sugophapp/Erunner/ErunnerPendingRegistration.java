package com.example.pikot.sugophapp.Erunner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.pikot.sugophapp.Both.ErunnerPendingActivity;
import com.example.pikot.sugophapp.Both.Login;
import com.example.pikot.sugophapp.R;

public class ErunnerPendingRegistration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erunner_pending_registration);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
                        startActivity(new Intent(this, Login.class));
    }
}
