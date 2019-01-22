package com.project.innovator.momsrecipe.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Recipe {

    private String name;
    private String ingredients;
    private String condiment;
    private String contents;
    private HashMap<String, String> images;

    public Recipe() {

    }

    public Recipe(String name, String ingredients, String condiment, String contents, HashMap<String, String> images) {
        this.name = name;
        this.ingredients = ingredients;
        this.condiment = condiment;
        this.contents = contents;
        this.images = images;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setCondiment(String condiment) {
        this.condiment = condiment;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public void setImages(HashMap<String, String> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getCondiment() {
        return condiment;
    }

    public String getContents() {
        return contents;
    }

    public HashMap<String, String> getImages() {
        return images;
    }

    public List<String> getImagePath() {
        if (images != null && !images.isEmpty()) {
            List<String> result = new ArrayList<>();
            for (String path : getImagesKeys()) {
                result.add(images.get(path));
            }
            return result;
        }
        return null;
    }

    public List<String> getImagesKeys() {
        if (images != null && !images.isEmpty()) {
            List<String> result = new ArrayList<>();
            for(String key : images.keySet()) {
                result.add(key);
            }
            return result;
        }
        return null;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("ingredients", ingredients);
        result.put("condiment", condiment);
        result.put("contents", contents);
        result.put("images", images);
        return result;
    }

    public boolean equals(Recipe recipe) {
        if(this.name.equals(recipe.getName())
                && this.ingredients.equals(recipe.getIngredients())
                && this.condiment.equals(recipe.getCondiment())
                && this.contents.equals(recipe.getContents())
                && this.images.equals(recipe.getImages()))
            return true;

        return false;
    }
}