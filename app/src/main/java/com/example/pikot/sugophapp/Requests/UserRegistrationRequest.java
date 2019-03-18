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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserRegistrationRequest {
    private static ProgressDialog progressDialog;


    public static void registerUser(final Context context, final CallBacks callBacks, ArrayList<String> data, final String username, final String password) {
        final String
                type = data.get(0),
                fname = data.get(1),
                mname = data.get(2),
                lname = data.get(3),
                bday = data.get(4),
                edLv = data.get(5),
                currentLocation = data.get(6),
                street = data.get(7),
                city = data.get(8),
                brgy = data.get(9),
                contact = data.get(10),
                email = data.get(11),

                imgtoStringBitmap= data.get(12);

        progressDialog = new ProgressDialog(context);
        //progressDialog.setCancelable(false);
        progressDialog.setMessage("Registering");
        progressDialog.show();
        String URL = Constants.URL_REGISTER_USER;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
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
                params.put("type", type);
                params.put("firstname", fname);
                params.put("middlename", mname);
                params.put("lastname", lname);
                params.put("birthdate", bday);
                params.put("city", city);
                params.put("street", street);
                params.put("barangay", brgy);
                params.put("education_level", edLv);
                params.put("contact", contact);
                params.put("email", email);
                params.put("username", username);
                params.put("password", password);
                params.put("current_location", currentLocation);
                params.put("image", imgtoStringBitmap);

                return params;
            }
        };
        RequestHandler.getInstance((context)).addToRequestQueue(stringRequest);
    }
}
