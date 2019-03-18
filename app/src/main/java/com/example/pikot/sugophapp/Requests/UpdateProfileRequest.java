package com.example.pikot.sugophapp.Requests;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pikot.sugophapp.Both.ProfileFragment;
import com.example.pikot.sugophapp.RandomClasses.Constants;
import com.example.pikot.sugophapp.RandomClasses.RequestHandler;
import com.example.pikot.sugophapp.RandomClasses.SharedPrefManager;
import com.example.pikot.sugophapp.RandomClasses.CallBacks;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class UpdateProfileRequest extends ProfileFragment{
    private static ProgressDialog progressDialog;

    public static void updateUser(final Context context, final String city,
                                  final String street, final String brgy, final String edLv,
                                  final String contact, final String email, final String image,
                                  final CallBacks callBacks) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Updating your profile, please wait.");
//        progressDialog.setCancelable(false);
        progressDialog.show();
        String URL = Constants.URL_CHANGE_PROFILE;

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

                params.put("city", city);
                params.put("street", street);
                params.put("barangay", brgy);
                params.put("education_level", edLv);
                params.put("contact", contact);
                params.put("email", email);
                params.put("username", SharedPrefManager.getInstance(context).getKeyUsername());
                if (image != null) {
                    params.put("image", image);
                }

                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);

    }





}
