package com.example.pikot.sugophapp.Requests;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pikot.sugophapp.RandomClasses.Constants;
import com.example.pikot.sugophapp.RandomClasses.RequestHandler;
import com.example.pikot.sugophapp.RandomClasses.CallBacks;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class GetPaymentDetailsRequest {
    private static ProgressDialog progressDialog;


    public static void getPaymentDetails(final Context context, final String option_id, final CallBacks callBacks){
        progressDialog= new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
//        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest= new StringRequest(Request.Method.POST, Constants.URL_GET_PAYMENT_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
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
                        progressDialog.dismiss();
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
                params.put("option_id", option_id);


                return  params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
    }
}
