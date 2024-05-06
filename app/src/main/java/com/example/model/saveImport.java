package com.example.model;

public class saveImport {
   private String id;
   private int  totalPrice;
   private String createAt;

    public saveImport() {
    }

    public saveImport(String id, int totalPrice, String createAt) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.createAt = createAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }
}
