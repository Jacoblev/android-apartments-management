package com.example.auth;

public class Users {
    String ultimateID;
    String ultimateEmail;
    String userLevel;

    public Users() {

    }

    public Users(String ultimateID, String ultimateEmail, String userLevel) {
        this.ultimateID = ultimateID;
        this.ultimateEmail = ultimateEmail;
        this.userLevel = userLevel;
    }



    public String getUltimateID() {
        return ultimateID;
    }

    public void setUltimateID(String ultimateID) {
        this.ultimateID = ultimateID;
    }

    public String getUltimateEmail() {
        return ultimateEmail;
    }

    public void setUltimateEmail(String ultimateEmail) {
        this.ultimateEmail = ultimateEmail;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    }