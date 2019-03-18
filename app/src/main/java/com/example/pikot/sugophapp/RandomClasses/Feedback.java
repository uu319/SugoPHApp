package com.example.pikot.sugophapp.RandomClasses;

public class Feedback {
    String errand_id;
    String option_name;
    String rating;
    String feedback;
    String date;

    public Feedback(String errand_id, String option_name, String rating, String feedback, String date) {
        this.errand_id = errand_id;
        this.option_name = option_name;
        this.rating = rating;
        this.feedback = feedback;
        this.date= date;
    }

    public String getErrand_id() {
        return errand_id;
    }



    public String getOption_name() {
        return option_name;
    }



    public String getRating() {
        return rating;
    }



    public String getFeedback() {
        return feedback;
    }


    public String getDate() {
        return date;
    }
}
