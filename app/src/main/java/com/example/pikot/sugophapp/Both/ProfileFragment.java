package com.example.pikot.sugophapp.Both;



import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pikot.sugophapp.R;
import com.example.pikot.sugophapp.RandomClasses.Constants;
import com.example.pikot.sugophapp.RandomClasses.SessionHandler;
import com.example.pikot.sugophapp.RandomClasses.SharedPrefManager;
import com.example.pikot.sugophapp.RandomClasses.ShowAlert;
import com.example.pikot.sugophapp.Requests.ProfileRequest;
import com.example.pikot.sugophapp.RandomClasses.CallBacks;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;


public class ProfileFragment extends Fragment {

    private TextView txtNameP, txtAgeP, txtAddressP, txtEmailP, txtContactP, txtRating, txtEdLvP;
    private ImageButton btnUpdate;
    private ImageView imgProfpic;
    private RatingBar ratingBar;
    private ProgressDialog progressDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(!SharedPrefManager.getInstance(getActivity()).isLoggedIn()) {
            startActivity(new Intent(getActivity(), Login.class));
        }
//        txtUsernameP= getActivity().findViewById(R.id.txtUsernameP);
        txtNameP= getActivity().findViewById(R.id.txtNameP);
        txtAgeP=getActivity().findViewById(R.id.txtAgeP);
        txtAddressP= getActivity().findViewById(R.id.txtAddressP);
        txtEmailP= getActivity().findViewById(R.id.txtEmailP1);
        txtContactP= getActivity().findViewById(R.id.txtContactP);
        txtEdLvP= getActivity().findViewById(R.id.txtEdLvP);
        btnUpdate= getActivity().findViewById(R.id.btnUpdate);
        imgProfpic= getActivity().findViewById(R.id.imgProfpic);
        ratingBar=getActivity().findViewById(R.id.ratingBar);
        txtRating= getActivity().findViewById(R.id.txtRating);
        txtRating.setVisibility(View.INVISIBLE);
        ratingBar.setVisibility(View.INVISIBLE);




        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                UpdateProfileFragment _updateProfileFragment = new UpdateProfileFragment();
                fragmentManager.popBackStack();
                Bundle args = new Bundle();
                ArrayList<String> data = new ArrayList<String>();
                data.add(txtAddressP.getText().toString());
                data.add(txtContactP.getText().toString());
                data.add(txtEmailP.getText().toString());
                data.add(txtEdLvP.getText().toString());

                args.putStringArrayList("data",data);

                _updateProfileFragment.setArguments(args);
                FragmentTransaction fragmentTransaction;
                fragmentTransaction= fragmentManager.beginTransaction().replace(R.id.fragment, _updateProfileFragment);
                fragmentTransaction.addToBackStack("My Screen");
                fragmentTransaction.commit();
            }
        });
        Glide.with(getActivity()).load(Constants.URL_GET_IMAGE+SharedPrefManager.getInstance(getActivity().getApplicationContext()).getKeyUsername()+".jpg").into(imgProfpic);

    }

    @Override
    public void onResume() {
        super.onResume();
        try{
            getProfile();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getProfile(){
        ProfileRequest.getProfileByUsername(getActivity(), new CallBacks() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if(response!=null){
                    try {
                        JSONObject jsonObject = new JSONObject(response);


                        if (jsonObject.getBoolean("error")) {
                            try{
                                ShowAlert.showAlert(getActivity(),jsonObject.getJSONArray("msg").getString(0));
                            }catch (Exception e){
                                e.printStackTrace();
                            }

//                            Toast.makeText(getActivity(), jsonObject.getJSONArray("msg").getString(0), Toast.LENGTH_LONG).show();
                        } else {
                            String[] str = (jsonObject.getJSONArray("msg").getJSONObject(0).getString("birthdate").toString().trim()).split("-");
                            String year = str[0];
                            String month = str[1];
                            String day = str[2];

//                            txtUsernameP.setText(jsonObject.getJSONArray("msg").getJSONObject(0).getString("username").trim());

                            txtNameP.setText(
                                    jsonObject.getJSONArray("msg").getJSONObject(0).getString("firstname").trim() + " "
                                            + jsonObject.getJSONArray("msg").getJSONObject(0).getString("middlename").trim() + " "
                                            + jsonObject.getJSONArray("msg").getJSONObject(0).getString("lastname").trim());

                            txtAgeP.setText(getAge(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day)));

                            txtAddressP.setText(
                                    jsonObject.getJSONArray("msg").getJSONObject(0).getString("street").trim() + ", "
                                            + jsonObject.getJSONArray("msg").getJSONObject(0).getString("barangay").trim() + ", "
                                            + jsonObject.getJSONArray("msg").getJSONObject(0).getString("city").trim());
                            txtEdLvP.setText(jsonObject.getJSONArray("msg").getJSONObject(0).getString("education_level").trim());

                            txtEmailP.setText(jsonObject.getJSONArray("msg").getJSONObject(0).getString("email").trim());

                            txtContactP.setText(jsonObject.getJSONArray("msg").getJSONObject(0).getString("contact").trim());
                            try {
                                ratingBar.setRating(Float.parseFloat(jsonObject.getJSONArray("msg").getJSONObject(0).getString("rating")));
                            }catch (Exception e){
                                e.printStackTrace();
                            }



                            if (jsonObject.getJSONArray("msg").getJSONObject(0).getString("type").trim().equals("erunner")) {
                                ratingBar.setVisibility(View.VISIBLE);
                                txtRating.setVisibility(View.VISIBLE);
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else{
                    ShowAlert.showAlert(getActivity(),"Something went wrong");
//                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age)+1;
        String ageS = ageInt.toString();

        return ageS;
    }




}
