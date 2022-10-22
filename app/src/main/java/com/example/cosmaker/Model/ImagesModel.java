package com.example.cosmaker.Model;

import android.graphics.Bitmap;

public class ImagesModel {
    private int id,characterid;
    private Bitmap imageres;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCharacterid() {
        return characterid;
    }

    public void setCharacterid(int characterid) {
        this.characterid = characterid;
    }

    public Bitmap getImageres() {
        return imageres;
    }

    public void setImageres(Bitmap imageres) {
        this.imageres = imageres;
    }
}
