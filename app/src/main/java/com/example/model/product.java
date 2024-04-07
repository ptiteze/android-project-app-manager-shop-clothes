package com.example.model;

import java.io.Serializable;
import java.util.List;

public class product implements Serializable {
    private String id;
    private String name;
    private String category;
    private String material;
    private String origin;
    private String description;
    private String color;
    private int price;
    private int stock;
    private String image;
    private boolean state;

    public product() {
    }
    //, List<com.example.model.size_color> size_color


    public product(String id, String name, String category, String material, String origin, String description, String color, int price, int stock, String image, boolean state) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.material = material;
        this.origin = origin;
        this.description = description;
        this.color = color;
        this.price = price;
        this.stock = stock;
        this.image = image;
        this.state = state;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", material='" + material + '\'' +
                ", origin='" + origin + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", image='" + image + '\'' +
                ", state=" + state +
                '}';
    }
}
