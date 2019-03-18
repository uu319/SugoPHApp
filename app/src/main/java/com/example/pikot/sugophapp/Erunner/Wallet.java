package com.example.pikot.sugophapp.Erunner;

public class Wallet {
    String walletName;
    String walletDate;
    String walletAmount;

    public Wallet(String walletName, String walletDate, String walletAmount) {
        this.walletName = walletName;
        this.walletDate = walletDate;
        this.walletAmount = walletAmount;
    }

    public String getWalletName() {
        return walletName;
    }

    public String getWalletDate() {
        return walletDate;
    }

    public String getWalletAmount() {
        return walletAmount;
    }
}
