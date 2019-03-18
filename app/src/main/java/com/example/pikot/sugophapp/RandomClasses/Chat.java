package com.example.pikot.sugophapp.RandomClasses;

public class Chat {
    private boolean left;
    private String messaage;
    private String date;

    public Chat(boolean left, String messaage, String date) {
        this.left = left;
        this.date= date;
        this.messaage = messaage;
    }

    public boolean isLeft() {
        return left;
    }

    public String getMessaage() {
        return messaage;
    }

    public String getDate(){
        return date;
    }
}
