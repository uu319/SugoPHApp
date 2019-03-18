package com.example.pikot.sugophapp.Requests;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pikot.sugophapp.RandomClasses.Constants;
import com.example.pikot.sugophapp.RandomClasses.RequestHandler;
import com.example.pikot.sugophapp.RandomClasses.CallBacks;

import org.json.JSONException;
import org.json.JSONObject;

public class GetErrandCategoryRequest {

    private static ProgressDialog progressDialog;
    public static void getErrandCategory(final Context context, final CallBacks callBacks) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
//        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_GET_ERRANDS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
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
                });

        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
    }
}