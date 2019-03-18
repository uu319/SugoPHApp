package com.example.pikot.sugophapp.Requests;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pikot.sugophapp.Both.Login;
import com.example.pikot.sugophapp.RandomClasses.Constants;
import com.example.pikot.sugophapp.RandomClasses.RequestHandler;
import com.example.pikot.sugophapp.RandomClasses.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LogoutRequest {
   private static ProgressDialog progressDialog;


    public static void logoutRequest(final Context context) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_LOGOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("error")) {
                                Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
                                SharedPrefManager.getInstance(context).logout();
                                Intent intent = new Intent(context, Login.class);
                                context.startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", SharedPrefManager.getInstance(context).getKeyUsername());

                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
    }
}
