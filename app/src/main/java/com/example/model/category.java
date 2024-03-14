package com.example.model;

public class category {
    private String name;
    private String descrip;

    public String getName() {
        return name;
    }

    public category(String name, String descrip) {
        this.name = name;
        this.descrip = descrip;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public category() {
    }
}
