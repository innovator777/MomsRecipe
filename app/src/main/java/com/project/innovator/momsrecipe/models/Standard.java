package com.project.innovator.momsrecipe.models;

import android.graphics.Bitmap;

import java.util.HashMap;

public class Standard {
    private String name;
    private String image;

    public Standard() {

    }

    public Standard(String name, String image) {
        this.name = name;
        this.image = image;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("image", image);
        return result;
    }

    public boolean equals(Standard standard) {
        if(this.name.equals(standard.getName())
                && this.image.equals(standard.getImage()))
            return true;

        return false;
    }
}
