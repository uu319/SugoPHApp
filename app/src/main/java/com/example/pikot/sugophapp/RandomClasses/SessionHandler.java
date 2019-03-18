package com.example.pikot.sugophapp.RandomClasses;

import android.content.Context;
import android.content.Intent;

import com.example.pikot.sugophapp.Both.Login;
import com.example.pikot.sugophapp.Both.MainActivity;

public class SessionHandler {
    public static void loginRequired(Context context){
        if(SharedPrefManager.getInstance(context).getKeyUsername()==null){
            context.startActivity(new Intent(context, Login.class));
        }
    }
    public static void checkSession(Context context){
        if(SharedPrefManager.getInstance(context).getKeyUsername()!=null){
            context.startActivity(new Intent(context, MainActivity.class));
        }
    }
}
