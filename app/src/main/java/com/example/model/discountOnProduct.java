package com.example.model;


import java.io.Serializable;

public class discountOnProduct implements Serializable {
    private String id;
    private String name;
    private String timeStart;
    private String timeEnd;
    private  int percent;
    private  String des;
    private  boolean status;


    public discountOnProduct(String id, String name, String timeStart, String timeEnd, int percent, String des, boolean status) {
        this.id = id;
        this.name = name;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.percent = percent;
        this.des = des;
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public discountOnProduct() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public int getPercent() {
        return percent;
    }

    public String getDes() {
        return des;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public void setDes(String des) {
        this.des = des;
    }

    @Override
    public String toString() {
        return "discountOnProduct{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", timeStart='" + timeStart + '\'' +
                ", timeEnd='" + timeEnd + '\'' +
                ", percent=" + percent +
                ", des='" + des + '\'' +
                '}';
    }
}
