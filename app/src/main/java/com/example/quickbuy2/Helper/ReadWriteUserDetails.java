package com.example.quickbuy2.Helper;

public class ReadWriteUserDetails {
    public String fullName, email, mobile;

    public ReadWriteUserDetails() {
    }

    public ReadWriteUserDetails(String textFullName, String textEmail, String textMobile) {
        this.fullName = textFullName;
        this.email = textEmail;
        this.mobile = textMobile;
    }
}

