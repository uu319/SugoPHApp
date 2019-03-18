package com.example.pikot.sugophapp.Eseeker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pikot.sugophapp.Both.Login;
import com.example.pikot.sugophapp.Both.MainActivity;
import com.example.pikot.sugophapp.R;
import com.example.pikot.sugophapp.RandomClasses.SharedPrefManager;
import com.example.pikot.sugophapp.RandomClasses.ShowAlert;
import com.example.pikot.sugophapp.Requests.GetOptionDescriptionRequest;
import com.example.pikot.sugophapp.Requests.GetErrandOptionsRequest;
import com.example.pikot.sugophapp.RandomClasses.CallBacks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.ganfra.materialspinner.MaterialSpinner;

public class EseekerPostDocumentActivity extends AppCompatActivity  implements  AdapterView.OnItemSelectedListener, View.OnClickListener {
    private MaterialSpinner spnrEd;
    private String catId;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapter2;
    private ListView listRequirements;
    private String optionName="";
    private Button btnPostErrand;
    private Boolean responseError= false;
    private String description="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_eseeker_post_document);
        if(!SharedPrefManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(this, Login.class));
        }

        btnPostErrand = findViewById(R.id.btnPostErrand);
        spnrEd= (MaterialSpinner) findViewById(R.id.spnrEd);
        listRequirements= findViewById(R.id.listRequirements);
        Intent intent= getIntent();
        catId=intent.getStringExtra("errand_category_id");
        description="";


        spnrEd.setOnItemSelectedListener(this);
        btnPostErrand.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!SharedPrefManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(this, Login.class));
        }

        try{
            getErrandOptions(catId);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position!=-1) {
            optionName = parent.getItemAtPosition(position).toString();
            try{
                getRequirements();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private boolean validateSpinner(){
        if(!optionName.equals(""))
            return true;
        else
            spnrEd.setError("Please choose document name");
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPostErrand:
                if(validateSpinner()&&!responseError){
                Intent intent= new Intent(this, EseekerExpDateActivity.class);
                intent.putExtra("option_name", optionName);
                intent.putExtra("description",description);
                startActivity(intent);
                }
        }

    }

    private void getErrandOptions(String errcat){
        GetErrandOptionsRequest.getErrandOptions(this, errcat, new CallBacks() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if (response != null) {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean error = jsonObject.getBoolean("error");
                    if(error){
                        try{
                            ShowAlert.showAlert(EseekerPostDocumentActivity.this,jsonObject.getString("msg"));
                        }catch (Exception e){
                            e.printStackTrace();
                        }

//                        Toast.makeText(getApplicationContext(),jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
                    }else{
                        JSONArray options = jsonObject.getJSONArray("msg");

                        ArrayList<String> list = new ArrayList<String>();
                        for (int i = 0; i < options.length(); i++) {
                            list.add(options.getJSONObject(i).getString("option_name"));
                        }
                        try{
                            adapter= new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_adapter, list);
                            adapter.setDropDownViewResource(R.layout.spinner_adapter);
                            spnrEd.setAdapter(adapter);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                } else {
                    ShowAlert.showAlert(EseekerPostDocumentActivity.this, "Something went wrong with your connection.");
//                    Toast.makeText(EseekerPostDocumentActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getRequirements(){
        GetOptionDescriptionRequest.getOptionDescription(this, new CallBacks() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if (response != null) {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean error = jsonObject.getBoolean("error");
                    if(error){
                        Toast.makeText(getApplicationContext(),jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
                    }else{
                    ArrayList<String> list2 = new ArrayList<String>();
                    JSONArray jsonArray= jsonObject.getJSONArray("msg").getJSONObject(0).getJSONArray("option_description");
                    int num= 1;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String num2= String.valueOf(num)+". ";
                        list2.add(num2+jsonArray.getString(i));
                        num++;
                    }
                    try {
                        adapter2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.eseeker_document_requirements_adapter, list2);
                        listRequirements.setAdapter(adapter2);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    }
                } else {
                    ShowAlert.showAlert(EseekerPostDocumentActivity.this,"Something went wrong with your conneccton.");

//                    Toast.makeText(EseekerPostDocumentActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    responseError=true;
                }
            }
        }, optionName);
    }
}
