package com.example.pikot.sugophapp.RandomClasses;

public class Errands{
    private String errandId;
    private String optionName;
    private String datePublished;
    private String status;

    private String username;
    private String fullname;
    private String errand_category_id;
    private String viewed;

    public Errands( String errandId, String optionName,String datePublished, String status, String username, String fullname, String errand_category_id, String viewed) {
        this.errandId= errandId;
        this.optionName = optionName;
        this.datePublished= datePublished;
        this.status= status;
        this.username= username;
        this.fullname= fullname;
        this.errand_category_id= errand_category_id;
        this.viewed= viewed;


    }

    public String getUsername() {
        return username;
    }

    public String getFullname() {
        return fullname;
    }

    public String getErrandId() {
        return errandId;
    }

    public String getOptionName() {
        return optionName;
    }

    public String getViewed() {
        return viewed;
    }

    public String getDatePublished() {
        return datePublished;
    }
    public String getStatus() {
        return status;
    }

    public String getErrand_category_id() {
        return errand_category_id;
    }
}

