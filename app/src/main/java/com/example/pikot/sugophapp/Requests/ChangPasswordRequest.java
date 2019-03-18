package com.example.pikot.sugophapp.Requests;

import android.app.ProgressDialog;
import android.content.Context;

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

public class ChangPasswordRequest {
    private static ProgressDialog progressDialog;

    public static void changePassword(final Context context, final String current_password, final String new_password, final CallBacks callBacks) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Updating");
//        progressDialog.setCancelable(false);
        progressDialog.show();
        String URL = Constants.URL_CHANGE_PASS;

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    callBacks.onSuccess(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                try {
                    callBacks.onSuccess(null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("current_password", current_password);
                params.put("new_password", new_password);
                params.put("username", SharedPrefManager.getInstance(context).getKeyUsername());
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
    }
}
