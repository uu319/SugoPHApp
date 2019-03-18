package com.example.pikot.sugophapp.Erunner;

public class ErrandOptions {
    private String option_id;
    private String option_name;
    private String status;

    public ErrandOptions(){

    }
    public ErrandOptions(String option_id, String option_name, String status){
        this.option_id= option_id;
        this.option_name= option_name;
        this.status= status;
    }

    public String getOption_id() {
        return option_id;
    }

    public String getOption_name() {
        return option_name;
    }

    public String getStatus() {
        return status;
    }

    public void setOption_id(String option_id) {
        this.option_id = option_id;
    }

    public void setOption_name(String option_name) {
        this.option_name = option_name;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
