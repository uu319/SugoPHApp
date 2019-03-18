package com.example.pikot.sugophapp.Requests;

import android.app.ProgressDialog;
import android.content.Context;
import android.telecom.Call;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pikot.sugophapp.RandomClasses.CallBacks;
import com.example.pikot.sugophapp.RandomClasses.Constants;
import com.example.pikot.sugophapp.RandomClasses.RequestHandler;
import com.example.pikot.sugophapp.RandomClasses.SharedPrefManager;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class GetErunnerLocationRequest {

    private static ProgressDialog progressDialog;
    public static void getErunnerLocation(final Context context, final String username, final CallBacks callBacks){
//        progressDialog= new ProgressDialog(context);
//        progressDialog.setMessage("Fetching location, please wait.");
////        progressDialog.setCancelable(false);
//        progressDialog.show();

        StringRequest stringRequest= new StringRequest(Request.Method.POST, Constants.URL_GET_ERUNNER_LOCATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        progressDialog.dismiss();
                        try {
                            callBacks.onSuccess(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        progressDialog.dismiss();
                        try {
                            callBacks.onSuccess(null);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params= new HashMap<>();
                params.put("username", username);


                return  params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);

    }
}
