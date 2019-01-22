package com.project.innovator.momsrecipe.models;

public class Material {
    private String name;
    private String amount;
    private int resource;

    public Material(String name, String amount, int resource) {
        this.name = name;
        this.amount = amount;
        this.resource = resource;
    }

    public void setName(String name ) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public int getResource() {
        return resource;
    }
}
