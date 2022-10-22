package com.example.cosmaker.Model;

import android.graphics.Bitmap;

public class SupportModel {
    private int id,supportcharid;
    private Bitmap finalimage;
    private String eddate,budget;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getSupportcharid() {
        return supportcharid;
    }

    public void setSupportcharid(int supportcharid) {
        this.supportcharid = supportcharid;
    }

    public Bitmap getFinalimage() {
        return finalimage;
    }

    public void setFinalimage(Bitmap finalimage) {
        this.finalimage = finalimage;
    }

    public String getEddate() {
        return eddate;
    }

    public void setEddate(String eddate) {
        this.eddate = eddate;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }
}
