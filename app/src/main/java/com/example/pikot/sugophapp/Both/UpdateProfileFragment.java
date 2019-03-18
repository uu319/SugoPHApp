package com.example.pikot.sugophapp.Both;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pikot.sugophapp.R;
import com.example.pikot.sugophapp.RandomClasses.Constants;
import com.example.pikot.sugophapp.RandomClasses.SessionHandler;
import com.example.pikot.sugophapp.RandomClasses.SharedPrefManager;
import com.example.pikot.sugophapp.RandomClasses.ShowAlert;
import com.example.pikot.sugophapp.RandomClasses.Validation;
import com.example.pikot.sugophapp.Requests.UpdateProfileRequest;
import com.example.pikot.sugophapp.RandomClasses.CallBacks;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;


public class UpdateProfileFragment extends Fragment  implements  AdapterView.OnItemSelectedListener, View.OnClickListener {


    private EditText txtCity, txtStreet, txtBrgy, txtEmail, txtContact;
    private String edLv;
    private MaterialSpinner spnrEdlv;
    private Button btnUpdate;
    private ImageView imgProfpic;


    int IMG_REQUEST= 1;
    private Bitmap bitmap= null;

    private MaterialSpinner spnrEd;
    private List<String> list = new ArrayList<String>();
    private ArrayAdapter<String> adapter;

    private int spinnerPosition;
    Validation validation= new Validation();
    private FragmentManager fragmentManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_profile, container, false);



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(!SharedPrefManager.getInstance(getActivity()).isLoggedIn()) {
            startActivity(new Intent(getActivity(), Login.class));
        }
        imgProfpic = getActivity().findViewById(R.id.imgProfpic);
        txtBrgy = getActivity().findViewById(R.id.txtBrgy);
        txtStreet = getActivity().findViewById(R.id.txtStreet);
        txtCity = getActivity().findViewById(R.id.txtCity);
        txtEmail = getActivity().findViewById(R.id.txtEmail);
        txtContact = getActivity().findViewById(R.id.txtContact);
        spnrEdlv = getActivity().findViewById(R.id.spnrEd);
        btnUpdate = getActivity().findViewById(R.id.btnUpdate);
        imgProfpic= getActivity().findViewById(R.id.imgProfpic);



        list.add("Elementary Level");
        list.add("Junior Highschool Level");
        list.add("Senior Highschool Level");
        list.add("College Level");
        list.add("College Graduate");
        adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_adapter,list);
        adapter.setDropDownViewResource(R.layout.spinner_adapter);
        spnrEdlv.setAdapter(adapter);


        Glide.with(getActivity()).load(Constants.URL_GET_IMAGE + SharedPrefManager.getInstance(getActivity().getApplicationContext()).getKeyUsername() + ".jpg").into(imgProfpic);
        ArrayList<String> data = getArguments().getStringArrayList("data");
        String address = data.get(0);
        String contact = data.get(1);
        String email = data.get(2);
        edLv = data.get(3);

        spinnerPosition= adapter.getPosition(edLv)+1;
        spnrEdlv.setSelection(spinnerPosition);


        String[] str = address.split(", ");
        txtStreet.setText(str[0]);
        txtBrgy.setText(str[1]);
        txtCity.setText(str[2]);
        txtContact.setText(contact);
        txtEmail.setText(email);

        spnrEdlv.setOnItemSelectedListener(this);


        btnUpdate.setOnClickListener(this);
        imgProfpic.setOnClickListener(this);

        fragmentManager= getActivity().getSupportFragmentManager();


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position!=-1) {
            edLv = parent.getItemAtPosition(position).toString();
        }else{
            edLv="";
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent){
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnUpdate:
                Boolean test=   validation.validateNormalInput(txtStreet)&&validation.validateNormalInput(txtBrgy)&&validation.validateNormalInput(txtCity)&&
                                validation.validateEmail(txtEmail)&&validation.validateNumber(txtContact)&&validation.validateMaterialSpinner(spnrEdlv,edLv);
                if(test) {
                    UpdateProfileRequest.updateUser(getActivity(), txtCity.getText().toString().trim(), txtStreet.getText().toString().trim(),
                            txtBrgy.getText().toString().trim(), edLv, txtContact.getText().toString().trim(), txtEmail.getText().toString().trim(), imageToString(bitmap),
                            new CallBacks() {
                                @Override
                                public void onSuccess(String response) throws JSONException {
                                    if(response!=null){
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            if (jsonObject.getBoolean("error")) {
                                                try{
                                                    ShowAlert.showAlert(getActivity(),jsonObject.getString("msg"));
                                                }catch (Exception e){

                                                }

//                                                Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
                                            } else {
//                                                Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
                                                FragmentTransaction fragmentTransaction;
                                                ProfileFragment profileFragment = new ProfileFragment();
                                                fragmentManager.popBackStack();
                                                fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.fragment, profileFragment);
                                                fragmentTransaction.addToBackStack("My Screen");
                                                fragmentTransaction.commit();

                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }else{
                                        ShowAlert.showAlert(getActivity(),"Something went wrong.");
//                                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }

                            }
                            });
                }
                break;
            case R.id.imgProfpic:
                    selectImage();
        }
    }

    public void selectImage(){
        Intent intent= new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMG_REQUEST && resultCode==getActivity().RESULT_OK && data!=null){
            Uri path= data.getData();
            try {
                bitmap= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), path);
                imgProfpic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String imageToString(Bitmap bitmap){
        if(bitmap!=null){
            ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] imgBytes= byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(imgBytes, Base64.DEFAULT);
        }else{
            return null;
        }
    }








}
