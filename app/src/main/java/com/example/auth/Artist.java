package com.example.auth;

public class Artist {

    String personID;
    String personTitle;
    String personContent;
    String personUID;


    public Artist(String personID, String personTitle, String personContent, String personUID) {
        this.personID = personID;
        this.personTitle = personTitle;
        this.personContent = personContent;
        this.personUID = personUID;

    }

    public String getPersonID() {
        return personID;
    }

    public String getPersonTitle() {
        return personTitle;
    }

    public String getPersonContent() {
        return personContent;
    }

    public String getPersonUID() {
        return personUID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public void setPersonTitle(String personTitle) {
        this.personTitle = personTitle;
    }

    public void setPersonContent(String personContent) {
        this.personContent = personContent;
    }

    public void setPersonUID(String personUID) {

        this.personUID = personUID;
    }

    public Artist() {

    }










}
