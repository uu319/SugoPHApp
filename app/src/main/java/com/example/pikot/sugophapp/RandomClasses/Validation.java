package com.example.pikot.sugophapp.RandomClasses;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

import fr.ganfra.materialspinner.MaterialSpinner;

public class Validation{

    private static final Pattern PASSWORD_PATTERN=Pattern.compile(
            "^"+                //start of string
            "(?=.*[0-9])"+      //atleast 1 digit
            "(?=.*[a-z])"+      //atleast 1 lower case letter
            "(?=.*[A-Z])"+      //atleast 1 upper case letter
            "(?=\\S+$)"+        //no whitespace
            ".{6,}"+            //atleast 6 characters
            "$");               //end of string

    private static final Pattern USERNAME_PATTERN=Pattern.compile(
            "^"+                //start of string
            "(?=.*[a-zA-Z])"+   //any letter
            "(?=\\S+$)"+        //no whitespace
            ".{6,}"+            //atleast 6 characters
            "$");               //end of string

    private static final Pattern NUMBER_PATTERN=Pattern.compile("^(09|\\+639)\\d{9}$");


    public boolean validatePassword(final EditText editText){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editText.getText().toString().trim().isEmpty()){
                    editText.setError("Field can't be empty");
                }else if(!PASSWORD_PATTERN.matcher(editText.getText().toString().trim()).matches()){
                    editText.setError("Password too weak");
                }else editText.setError(null);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if(editText.getText().toString().trim().isEmpty()){
            editText.setError("Field can't be empty");
            return false;
        }else if(!PASSWORD_PATTERN.matcher(editText.getText().toString().trim()).matches()){
            editText.setError("Password too weak");
            return false;
        }else return true;
    }

    public boolean validateUsername(final EditText editText){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editText.getText().toString().trim().isEmpty()){
                    editText.setError("Field can't be empty");
                }else if(!USERNAME_PATTERN.matcher(editText.getText().toString().trim()).matches()){
                    editText.setError("Invalid username");
                }else editText.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if(editText.getText().toString().trim().isEmpty()){
            editText.setError("Field can't be empty");
            return false;
        }else if(!USERNAME_PATTERN.matcher(editText.getText().toString().trim()).matches()){
            editText.setError("Invalid username");
            return false;
        }else return true;
    }

    public boolean validateNumber(final EditText editText){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editText.getText().toString().trim().isEmpty()){
                    editText.setError("Field can't be empty");
                }else if(!NUMBER_PATTERN.matcher(editText.getText().toString().trim()).matches()){
                    editText.setError("Invalid Phone Number");
                }else
                    editText.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s){}
        });

        if(editText.getText().toString().trim().isEmpty()){
            editText.setError("Field can't be empty");
            return false;
        }else if(!NUMBER_PATTERN.matcher(editText.getText().toString().trim()).matches()){
            editText.setError("Invalid Phone Number");
            return false;
        }else return true;
    }

    public boolean validateEmail(final EditText editText){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editText.getText().toString().trim().isEmpty()){
                    editText.setError("Field can't be empty");
                }else if(!Patterns.EMAIL_ADDRESS.matcher(editText.getText().toString().trim()).matches()){
                    editText.setError("Invalid email address");
                }else editText.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if(editText.getText().toString().trim().isEmpty()){
            editText.setError("Field can't be empty");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(editText.getText().toString().trim()).matches()){
            editText.setError("Invalid Email Address");
            return false;
        }else return true;
    }



    public boolean validateNormalInput(final EditText editText){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                editText.setError("Field can't be empty");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editText.getText().toString().trim().isEmpty()){
                    editText.setError("Field can't be empty");
                }else
                editText.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if(editText.getText().toString().trim().isEmpty()){
            editText.setError("Field can't be empty");

            return false;
        }else{
        return true;
        }
    }



    public boolean validateMaterialSpinner(MaterialSpinner materialSpinner, String content){
        if(content!="")
            return true;
        else
            materialSpinner.setError("Please choose educational level");
        return false;
    }

}
