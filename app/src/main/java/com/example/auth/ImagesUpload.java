package com.example.auth;

public class ImagesUpload {
    private String mName;
    private String mImageURL;
    private String mImageID;



    public ImagesUpload() {

    }


    public ImagesUpload(String mName, String mImageURL, String mImageID) {

        if (mName.trim().equals(""))
        {
            mName = "No_Name";
        }

        this.mName = mName;
        this.mImageURL = mImageURL;
        this.mImageID = mImageID;
    }

    public String getmImageID() {
        return mImageID;
    }

    public void setmImageID(String mImageID) {
        this.mImageID = mImageID;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmImageURL() {
        return mImageURL;
    }

    public void setmImageURL(String mImageURL) {
        this.mImageURL = mImageURL;
    }
}
