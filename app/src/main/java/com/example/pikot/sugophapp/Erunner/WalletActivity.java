package com.example.pikot.sugophapp.Erunner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pikot.sugophapp.Both.MainActivity;
import com.example.pikot.sugophapp.Eseeker.EseekerPostDocumentActivity;
import com.example.pikot.sugophapp.R;
import com.example.pikot.sugophapp.RandomClasses.CallBacks;
import com.example.pikot.sugophapp.RandomClasses.ShowAlert;
import com.example.pikot.sugophapp.Requests.GetWalletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WalletActivity extends AppCompatActivity {
    ListView listView;
    TextView txtCurrentWallet, txtCashout, txtTotalWallet, txtCashoutDate;
    ArrayList<Wallet> wallets;
    WalletAdapter walletAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
                        listView= findViewById(R.id.transactionListView);
                        txtCurrentWallet= findViewById(R.id.currentWallet);
                                     txtCashout= findViewById(R.id.recentCashout);
                        txtTotalWallet= findViewById(R.id.totalWallet);
                                      txtCashoutDate= findViewById(R.id.cashoutDate);

        wallets= new ArrayList<>();


    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            getTransactions();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        startActivity(new Intent(this, MainActivity.class));
//    }

    private void getTransactions(){
        GetWalletRequest.getWallet(this, new CallBacks() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if(response!=null){
                    JSONObject jsonObject= new JSONObject(response);
                    JSONArray jsonArray= jsonObject.getJSONArray("msg");
                    for(int i=0;i<jsonArray.length();i++) {
                        wallets.add(new Wallet(jsonArray.getJSONObject(i).getString("option_name"),
                                jsonArray.getJSONObject(i).getString("date"), jsonArray.getJSONObject(i).getString("total_fee")));
                    }
                    try{
                        txtCurrentWallet.setText(jsonObject.getString("mywallet"));
                                        txtCashout.setText(jsonObject.getString("recent_cashout"));
                        txtTotalWallet.setText(jsonObject.getString("total_earning"));
                                    txtCashoutDate.setText(jsonObject.getString("recent_cashout_date"));
                        walletAdapter= new WalletAdapter(getApplicationContext(),wallets);
                        listView.setAdapter(walletAdapter);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else{
                    ShowAlert.showAlert(WalletActivity.this, "Something went wrong with your connection.");
                }
            }
        });
    }
}
