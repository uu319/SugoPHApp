package com.example.pikot.sugophapp.Both;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pikot.sugophapp.R;
import com.example.pikot.sugophapp.RandomClasses.SessionHandler;
import com.example.pikot.sugophapp.RandomClasses.SharedPrefManager;
import com.example.pikot.sugophapp.RandomClasses.Validation;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class RegName extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private String currentLocation;
    private EditText txtLname, txtFname, txtMname, txtBday;
    private MaterialSpinner spnrEd;
    private Button btnNext;
    private DatePickerDialog dpd;
    private List<String> list = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private String type, ed_level="";
    private DatePickerDialog.OnDateSetListener mDateListener;
    private int birthDay, birthMonth, birthYear;
    Validation validation= new Validation();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_name);
        if(SharedPrefManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
        }
        txtFname= findViewById(R.id.txtFname);
        txtMname= findViewById(R.id.txtMname);
        txtLname= findViewById(R.id.txtLname);
        txtBday= findViewById(R.id.txtBday);
        spnrEd= (MaterialSpinner) findViewById(R.id.spnrEd);
        btnNext= findViewById(R.id.btnNext);


        list.add("Elementary Level");
        list.add("Junior Highschool Level");
        list.add("Senior Highschool Level");
        list.add("College Level");
        list.add("College Graduate");
        adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrEd.setAdapter(adapter);


        Intent intent= getIntent();
        type=intent.getStringExtra("type");
        currentLocation= intent.getStringExtra(             "currentLocation");

        spnrEd.setOnItemSelectedListener(this);
        btnNext.setOnClickListener(this);
        txtBday.setOnClickListener(this);


    }
    @Override
    protected void onResume() {
        super.onResume();
        if(SharedPrefManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txtBday:
                Calendar c= Calendar.getInstance();
                 int day2= c.get(Calendar.DAY_OF_MONTH);
                 int month2= c.get(Calendar.MONTH);
                 int year2= c.get(Calendar.YEAR);
                dpd= new DatePickerDialog(RegName.this, android.R.style.Theme_Holo_Light_Dialog, mDateListener, year2, month2, day2);
                dpd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dpd.show();
                mDateListener= new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        birthYear=year;
                        birthMonth= month+1;
                        birthDay= dayOfMonth;

                        String date= year+"-"+month+"-"+dayOfMonth;
                        txtBday.setText(date);
                    }
                };
                break;
            case R.id.btnNext:
                String firstname= txtFname.getText().toString().trim();
                String middlename= txtMname.getText().toString().trim();
                String lastname= txtLname.getText().toString().trim();
                String birthday= txtBday.getText().toString().trim();
                Boolean testFirst=validation.validateNormalInput(txtFname);
                Boolean testMiddle=validation.validateNormalInput(txtMname);
                Boolean testLast=validation.validateNormalInput(txtLname);
                Boolean testBday=validation.validateNormalInput(txtBday);
                Boolean testBdayAge=getAge(birthYear,birthMonth,birthDay);
                if(!testBdayAge){
                    txtBday.setError("Should be 18 years old or above");
                }
//                Toast.makeText(this, getAge(birthYear,birthMonth,birthDay)+"", Toast.LENGTH_SHORT).show();
                Boolean testEdlv= validateSpinner();
                    if (testFirst && testMiddle && testLast && testBday && testEdlv &&testBdayAge) {
                        ArrayList<String> data = new ArrayList<String>();
                        data.add(type);
                        data.add(firstname);
                        data.add(middlename);
                        data.add(lastname);
                        data.add(birthday);
                        data.add(ed_level);
                        data.add(currentLocation);

                        Intent intent = new Intent(getApplicationContext(), RegAddress.class);
                        intent.putStringArrayListExtra("data", data);
                        startActivity(intent);
                    }

                break;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position!=-1) {
            ed_level = parent.getItemAtPosition(position).toString();
            }else{
            ed_level="";
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
    private boolean validateSpinner(){
        if(ed_level!="")
            return true;
        else
            spnrEd.setError("Please choose educational level");
        return false;
    }



    private boolean getAge(int year, int month, int day) {
        try {
            Calendar dob = Calendar.getInstance();
            Calendar today = Calendar.getInstance();
            dob.set(year, month, day);
            int monthToday = today.get(Calendar.MONTH) + 1;
            int monthDOB = dob.get(Calendar.MONTH)+1;
            int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
//            Toast.makeText(this, age+"sds"    , Toast.LENGTH_SHORT).show();
            if (age > 18) {
                return true;
            } else if (age == 18) {
                if (monthDOB > monthToday) {
                    return true;
                } else if (monthDOB == monthToday) {
                    int todayDate = today.get(Calendar.DAY_OF_MONTH);
                    int dobDate = dob.get(Calendar.DAY_OF_MONTH);
                    if (dobDate <= todayDate) { // should be less then
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



}
