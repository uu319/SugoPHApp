package com.example.pikot.sugophapp.Both;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pikot.sugophapp.R;
import com.example.pikot.sugophapp.RandomClasses.ShowAlert;

public class TermsAndConditions extends AppCompatActivity {
         TextView txt1, txt2,txt3,txt4,txt5,txt6,txt7,txt8,txt9,txt10,txt11,txt12,txt13,txt14,txt15,txt16,txt17,txt18,txt19,txt20,txt21;
         Button btnContinue;
         CheckBox checkBox;

    private String currentLocation = null;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_and_conditions);
        txt1= findViewById(R.id.t1);
        txt2= findViewById(R.id.t2);
        txt3= findViewById(R.id.t4);
        txt4= findViewById(R.id.t4);
        txt5= findViewById(R.id.t5);
        txt6= findViewById(R.id.t6);
        txt7= findViewById(R.id.t7);
        txt8= findViewById(R.id.t8);
        txt9= findViewById(R.id.t9);
        txt10= findViewById(R.id.t10);
        txt11= findViewById(R.id.t11);
        txt12= findViewById(R.id.t12);
        txt13= findViewById(R.id.t13);
        txt14= findViewById(R.id.t14);
        txt15= findViewById(R.id.t15);
        txt16= findViewById(R.id.t16);
        txt17= findViewById(R.id.t17);
        txt18= findViewById(R.id.t18);
        txt19= findViewById(R.id.t19);
        txt20= findViewById(R.id.t20);
        txt21= findViewById(R.id.t21);
        btnContinue= findViewById(R.id.btnContinue);
        checkBox= findViewById(R.id.termCheck);



        txt1.setText("Terms and Conditions");
        txt2.setText("Terms and Conditions (\"Terms\")");
        txt3.setText("Last updated: (February 20, 2019)");
        txt4.setText("Please read these Terms and Conditions (\"Terms\", \"Terms and Conditions\") carefully before using the SugoPH mobile application (the \"Service\") operated by Sugoph Group (\"us\", \"we\", or \"our\").");
        txt5.setText("Your access to and use of the Service is conditioned on your acceptance of and compliance with these Terms. These Terms apply to all visitors, users and others who access or use the Service.\n");
        txt6.setText("By accessing or using the Service you agree to be bound by these Terms. If you disagree with any part of the terms then you may not access the Service.");
        txt7.setText("Content");
        txt8.setText("Our Service allows you to post, link, store, and otherwise make available certain information, text, graphics, or other material (\"Content\"). You are responsible that the information, text, graphics, or other materials being provided are precise and true. Upon using the service, you agreed that SugoPH are not responsible, and shall have no liability to you, with respect to any information, communication or materials posted by others, including defamatory, offensive or illicit material, including material that violates these Terms. Any information posted through the use of the App is done at your own discretion and risk and you will be solely responsible for any actions happened upon using the App.");
        txt9.setText("Payment");
        txt10.setText("Upon registering to our service, you agreed to use PayPal as our only mode of payment and must be responsible for acquiring a PayPal account before using the App. The total amount of all the transactions are displayed on the eRunners’ wallet. The disbursement of the eRunners’ total earnings will only happen every end of the month, which means eRunners can only withdraw their total earnings every last day of the month. If there are circumstances that the eRunners did not withdraw their earnings from the previous month, they can still withdraw it anytime. Sugoph Group should not allow disbursement of earnings if the month is not yet ended.");
        txt11.setText("Reporting Abuse");
        txt12.setText("If you believe that someone is violating these community standards, you can report them on the Ratings and Feedbacks page after the payment stage. eSeekers can only report eRunners and not vice versa. If you click Report, we'll review your report and take action is appropriate. Not all reports result in removal. All reports will be kept confidential.");
        txt13.setText("Passwords");
        txt14.setText("You are responsible for safeguarding the password that you use to access the Site and for any activities or actions under your password. We encourage you to use \"strong\" passwords (passwords that use a combination of uppercase and lowercase letters, numbers and symbols) with your account. SugoPH Group cannot and will not be liable for any loss or damage arising from your failure to comply with the above requirements. If you believe your password has been compromised or someone has improperly accessed your account, please do email us at retuyajolrey@gmail.com.\n");
        txt15.setText("Links To Other Web Sites");
        txt16.setText("Our Service may contain links to third-party web sites or services that are not owned or controlled by Sugoph Group");
        txt17.setText("Sugoph Group has no control over, and assumes no responsibility for, the content, privacy policies, or practices of any third party web sites or services. You further acknowledge and agree that Sugoph Group shall not be responsible or liable, directly or indirectly, for any damage or loss caused or alleged to be caused by or in connection with use of or reliance on any such content, goods or services available on or through any such web sites or services.");
        txt18.setText("Changes");
        txt19.setText("We reserve the right, at our sole discretion, to modify or replace these Terms at any time. If a revision is material we will try to provide at least 30 days' notice prior to any new terms taking effect. What constitutes a material change will be determined at our sole discretion.");
        txt20.setText("Contact Us");
        txt21.setText("In the event of any problem with the Site or any content, you agree that your sole remedy is to cease using the Site. Please email your report for any violations of the Terms and Conditions (except for claims of copyright infringement, see above for instructions on these) at retuyajolrey@gmail.com.");



        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                progressDialog.dismiss();
                currentLocation=location.getLatitude()+","+location.getLongitude();
                Toast.makeText(TermsAndConditions.this, location.getLatitude()+"", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), RegName.class);
                intent.putExtra("type", "erunner");
                intent.putExtra("currentLocation", currentLocation);
                locationManager.removeUpdates(locationListener);
                Toast.makeText(TermsAndConditions.this, currentLocation, Toast.LENGTH_SHORT).show();
                startActivity(intent);
//
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegName.class);
                if(checkBox.isChecked()){
                    if(getIntent().getStringExtra("type").equals("erunner")){
                        if(isLocationEnabled(TermsAndConditions.this)){
                            if (ActivityCompat.checkSelfPermission(TermsAndConditions.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(TermsAndConditions.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                            }else {
                                progressDialog= new ProgressDialog(TermsAndConditions.this);
                                progressDialog.setMessage("We are currently fetching your location.");
                                progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        locationManager.removeUpdates(locationListener);
                                    }
                                });
                                progressDialog.setTitle("Please wait ");
                                progressDialog.show();;
                                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                            }
                        }else{
                            Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(gpsOptionsIntent);
                        }
                    }else{
                        intent.putExtra("type", "eseeker");
                        currentLocation = "";
                        intent.putExtra("currentLocation", currentLocation);
                        startActivity(intent);
                    }
                }else{
                    ShowAlert.showAlert(TermsAndConditions.this,"You must agree to the terms and conditions to contine.");
//                    Toast.makeText(TermsAndConditions.this, "You must agree to the terms and conditions to contine.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }
}
