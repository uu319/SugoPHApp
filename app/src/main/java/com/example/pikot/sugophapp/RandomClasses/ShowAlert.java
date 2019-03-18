package com.example.pikot.sugophapp.RandomClasses;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import com.example.pikot.sugophapp.Both.MainActivity;
import com.example.pikot.sugophapp.Eseeker.EseekerErrandPaymentDetails;
import com.example.pikot.sugophapp.Requests.CancelErrandRequest;
import com.example.pikot.sugophapp.Requests.GetMatchRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.spec.ECField;

public class ShowAlert {
    public static void showAlert(Context context, String msg){
        try{
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Alert");
        alertDialog.setCancelable(false);
        alertDialog.setMessage(msg);
        alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

            alertDialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
