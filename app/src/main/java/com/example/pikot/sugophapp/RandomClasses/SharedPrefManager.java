package com.example.pikot.sugophapp.RandomClasses;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME= "mysharedpref";
    private static final String KEY_USERNAME ="username";
    private static final String KEY_TYPE ="type";
    private static final String KEY_TOKEN="token";
    private static final String KEY_STATUS="status";
    private static final String KEY_LOCATION_UPDATE= "update";


    private SharedPrefManager(Context context){
        mCtx=context;
    }

    public static synchronized SharedPrefManager getInstance(Context context){
        if(mInstance==null){
            mInstance= new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean userLogin(String username, String type, String token){
        SharedPreferences sharedPreferences= mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);

        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_TYPE, type);
        editor.commit();
        editor.apply();

        return true;
    }



    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences= mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if(sharedPreferences.getString(KEY_USERNAME,null)!=null){
            return true;
        }
        return false;
    }

    public boolean logout(){
        SharedPreferences sharedPreferences= mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }
    public String getKeyUsername(){
        SharedPreferences sharedPreferences= mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null);
    }
    public String getKeyType(){
        SharedPreferences sharedPreferences= mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_TYPE, null);
    }
    public void setKeyStatus(String status){
        SharedPreferences sharedPreferences= mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        try{
            editor.remove(KEY_STATUS);
        }catch (Exception e){
            e.printStackTrace();
        }

        editor.putString(KEY_STATUS, status);
        editor.commit();
        editor.apply();
    }
    public String getKeyStatus(){
        SharedPreferences sharedPreferences= mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_STATUS, null);
    }

    public void updKeyStatus(String status){
        SharedPreferences sharedPreferences= mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.remove(KEY_STATUS);
        editor.commit();
        editor.apply();
        editor.putString(KEY_STATUS, status);
        editor.commit();
        editor.apply();
    }



}
