package com.example.pikot.sugophapp.Eseeker;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.pikot.sugophapp.Both.RegName;
import com.example.pikot.sugophapp.R;
import com.example.pikot.sugophapp.RandomClasses.Validation;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EseekerExpDateActivity extends AppCompatActivity implements View.OnClickListener {
    EditText txtStartDate, txtStartTime, txtEndDate, txtEndTime;
    Button btnContinue;
    private DatePickerDialog.OnDateSetListener mDateListener;
    private DatePickerDialog dpd;
    String startDate, startTime, endDate, endTime;
    String description, data;
    Validation validation= new Validation();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eseeker_exp_date);

        final Intent intent = getIntent();
        description = intent.getStringExtra("description");
        data = intent.getStringExtra("option_name");

        txtStartDate= findViewById(R.id.txtStartDate);
        txtStartTime= findViewById(R.id.txtStartTime);
        txtEndDate= findViewById(R.id.txtEndDate);
        txtEndTime= findViewById(R.id.txtEndTime);
        btnContinue= findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean startDateTest= validation.validateNormalInput(txtStartDate);
                Boolean startTimeTest= validation.validateNormalInput(txtStartTime);
                Boolean endDateTest= validation.validateNormalInput(txtEndDate);
                Boolean endTimeTest= validation.validateNormalInput(txtEndTime);
                if(startDateTest && startTimeTest && endDateTest && endTimeTest){
                    Intent intent1= new Intent(EseekerExpDateActivity.this, EseekerErrandPaymentDetails.class);
                    intent1.putExtra("description",description);
                    intent1.putExtra("option_name",data);
                    intent1.putExtra("start", txtStartDate.getText().toString().trim()+" at "+ txtStartTime.getText().toString().trim());
                    intent1.putExtra("end",txtEndDate.getText().toString().trim()+" at "+txtEndTime.getText().toString().trim());
                    startActivity(intent1);
                }

            }
        });

        txtStartDate.setOnClickListener(this);
        txtStartTime.setOnClickListener(this);
        txtEndDate.setOnClickListener(this);
        txtEndTime.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Calendar c= Calendar.getInstance();
        int day2= c.get(Calendar.DAY_OF_MONTH);
        int month2= c.get(Calendar.MONTH);
        int year2= c.get(Calendar.YEAR);

        switch (v.getId()){
            case R.id.txtStartDate:

                dpd= new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog, mDateListener, year2, month2, day2);
                dpd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dpd.show();
                mDateListener= new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        SimpleDateFormat monthParse = new SimpleDateFormat("MM");
                        SimpleDateFormat monthDisplay = new SimpleDateFormat("MMMM");
                        String month2="";
                        try {
                            month2=monthDisplay.format(monthParse.parse(month+""));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String date= month2+" "+dayOfMonth+", "+year;

                        txtStartDate.setText(date);

                    }
                };
                break;
            case R.id.txtStartTime:
                TimePickerDialog timePickerDialog = new TimePickerDialog(EseekerExpDateActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                String AM_PM ;
                                if(hourOfDay < 12) {
                                    AM_PM = "AM";
                                } else {
                                    hourOfDay=hourOfDay-12;
                                    AM_PM = "PM";
                                }
                                txtStartTime.setText(hourOfDay + ":" + minute+AM_PM);
                            }
                        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
                timePickerDialog.show();
                break;
            case R.id.txtEndDate:
                dpd= new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog, mDateListener, year2, month2, day2);
                dpd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dpd.show();
                mDateListener= new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        SimpleDateFormat monthParse = new SimpleDateFormat("MM");
                        SimpleDateFormat monthDisplay = new SimpleDateFormat("MMMM");
                        String month2="";
                        try {
                            month2=monthDisplay.format(monthParse.parse(month+""));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                             String date= month2+" "+dayOfMonth+", "+year;

                            txtEndDate.setText(date);

                    }
                };
                break;
            case R.id.txtEndTime:
                TimePickerDialog timePickerDialog2 = new TimePickerDialog(EseekerExpDateActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                String AM_PM ;
                                if(hourOfDay < 12) {
                                    AM_PM = "AM";
                                } else {
                                    hourOfDay=hourOfDay-12;
                                    AM_PM = "PM";
                                }
                                txtEndTime.setText(hourOfDay + ":" + minute+AM_PM);
                            }
                        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
                timePickerDialog2.show();
                break;
        }
    }
}
