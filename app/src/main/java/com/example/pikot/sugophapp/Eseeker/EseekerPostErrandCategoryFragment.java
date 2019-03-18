package com.example.pikot.sugophapp.Eseeker;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pikot.sugophapp.Both.Login;
import com.example.pikot.sugophapp.RandomClasses.CanvasOptions;
import com.example.pikot.sugophapp.RandomClasses.CanvasOptionsAdapter;
import com.example.pikot.sugophapp.RandomClasses.Chat;
import com.example.pikot.sugophapp.RandomClasses.ErrandCategoryListAdapter;
import com.example.pikot.sugophapp.R;
import com.example.pikot.sugophapp.RandomClasses.Category;
import com.example.pikot.sugophapp.RandomClasses.SharedPrefManager;
import com.example.pikot.sugophapp.RandomClasses.ShowAlert;
import com.example.pikot.sugophapp.Requests.GetErrandCategoryRequest;
import com.example.pikot.sugophapp.RandomClasses.CallBacks;
import com.example.pikot.sugophapp.Requests.GetErrandOptionsRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class EseekerPostErrandCategoryFragment extends Fragment{

    ArrayList<Category> categoryArrayList= new ArrayList<>();
    Category category;
    SwipeRefreshLayout pullToRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_eseeker_errand_category, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(!SharedPrefManager.getInstance(getActivity()).isLoggedIn()) {
            startActivity(new Intent(getActivity(), Login.class));
        }
        try{
            pullToRefresh= getActivity().findViewById(R.id.pullToRefresh);

            pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    getErrands();
                    pullToRefresh.setRefreshing(false);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            getErrands();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void getErrands(){
        try {
            categoryArrayList.clear();
        }catch (Exception e){
            e.printStackTrace();
        }
        GetErrandCategoryRequest.getErrandCategory(getActivity(), new CallBacks() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if(response!=null){
                    JSONObject jsonObject= new JSONObject(response);
                    if(jsonObject.getBoolean("error")){
                        Toast.makeText(getActivity(), jsonObject.getString("msg"),Toast.LENGTH_LONG).show();

                    }else{
                       JSONArray jsonArray=jsonObject.getJSONArray("msg");

                       for(int i=0;i<jsonArray.length();i++){
                           String categoryName=jsonArray.getJSONObject(i).getString("errand_name");
                           String categoryDescripion= jsonArray.getJSONObject(i).getString("description");
                           String categodyId= jsonArray.getJSONObject(i).getString("errand_category_id");
                           category= new Category(categoryName,categoryDescripion,categodyId);
                           categoryArrayList.add(category);
                       }
                        final ListView listViewCategory= getActivity().findViewById(R.id.listviewCategory);
                        final ErrandCategoryListAdapter adapter = new ErrandCategoryListAdapter(getActivity(), categoryArrayList);
                        try{
                            listViewCategory.setAdapter(adapter);
                            listViewCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Category category= (Category) adapter.getItem(position);
                                    if(category.getCategoryId().equals("5")) {
                                        Intent intent = new Intent(getActivity(), EseekerPostDocumentActivity.class);
                                        intent.putExtra("errand_category_id",category.getCategoryId());
                                        startActivity(intent);
                                    }else if(category.getCategoryId().equals("11")){
                                        Intent intent = new Intent(getActivity(), EseekerPostBillsPaymentActivity.class);
                                        intent.putExtra("errand_category_id",category.getCategoryId());
                                        startActivity(intent);
                                    }else{
                                        fillOptions("12");
                                    }

                                }
                            });
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }else{
                    ShowAlert.showAlert(getActivity(), "Something went wrong with your connection.");
//                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fillOptions(String errcat){
        GetErrandOptionsRequest.getErrandOptions(getActivity(), errcat, new CallBacks() {
            @Override
            public void onSuccess(String response) throws JSONException {
                if (response != null) {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean error = jsonObject.getBoolean("error");
                    if(error){
                        try{
                            ShowAlert.showAlert(getActivity(),jsonObject.getString("msg"));
                        }catch (Exception e){
                            e.printStackTrace();
                        }

//                        Toast.makeText(getActivity(),jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
                    }else{
                        JSONArray options = jsonObject.getJSONArray("msg");
                        ListView listView;
                        Button button;
                        final CanvasOptionsAdapter canvasOptionsAdapter;
                        ArrayList<CanvasOptions> list = new ArrayList<>();
                        for (int i = 0; i < options.length(); i++) {
                            list.add(new CanvasOptions(options.getJSONObject(i).getString("option_name"), options.getJSONObject(i).getString("option_id")));
                        }
                        canvasOptionsAdapter = new CanvasOptionsAdapter(getActivity(), list);
                        LayoutInflater inflate = LayoutInflater.from(getActivity());
                        View alertView = inflate.inflate(R.layout.canvas_options_dialog, null);
                        listView= alertView.findViewById(R.id.listViewOptions);
                        button= alertView.findViewById(R.id.btnCancel);
                        try {
                            listView.setAdapter(canvasOptionsAdapter);

                            final Dialog alertDialog = new Dialog(getActivity());
                            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            alertDialog.setContentView(alertView);
                            alertDialog.getWindow().setBackgroundDrawableResource(R.color.azur);
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                }
                            });
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    CanvasOptions canvasOptions= (CanvasOptions) canvasOptionsAdapter.getItem(position);
                                    if(canvasOptions.getOptionId().equals("20")) {
                                        Intent intent = new Intent(getActivity(), EseekerPostProductActivity.class);
                                        intent.putExtra("option_name", canvasOptions.getOptionName());
                                        startActivity(intent);
                                    }else if(canvasOptions.getOptionId().equals("19")){
                                        Intent intent = new Intent(getActivity(), EseekerPostVenueActivity.class);
                                        intent.putExtra("option_name", canvasOptions.getOptionName());
                                        startActivity(intent);
                                    }else{
                                        Intent intent = new Intent(getActivity(), EseekerPostServicesActivity.class);
                                        intent.putExtra("option_name", canvasOptions.getOptionName());
                                        startActivity(intent);
                                    }
                                }
                            });

                            alertDialog.show();
                        }catch (Exception e){
                            e.printStackTrace();
                        }



                    }
                } else {
                    Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
