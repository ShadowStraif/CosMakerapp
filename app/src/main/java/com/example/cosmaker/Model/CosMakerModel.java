package com.example.cosmaker.Model;

import android.graphics.Bitmap;

import java.sql.Date;

public class CosMakerModel {

    private int id;
private Bitmap imageres;
    private String characterName, fandom, startDate ;

    public Bitmap getImageres() {
        return imageres;
    }

    public void setImageres(Bitmap imageres) {
        this.imageres = imageres;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public String getFandom() {
        return fandom;
    }

    public void setFandom(String fandom) {
        this.fandom = fandom;
    }


}
