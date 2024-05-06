package com.example.model;

import java.io.Serializable;

public class user implements Serializable {
    private String user_id;
    private String name;
    private String birth;
    private String sex;
    private String address;
    private String phone;
    private String avata;
    private String email;
    private String  rule;

    public user() {
    }

    public user(String user_id, String name, String birth, String sex, String address, String phone, String avata, String email, String rule) {
        this.user_id = user_id;
        this.name = name;
        this.birth = birth;
        this.sex = sex;
        this.address = address;
        this.phone = phone;
        this.avata = avata;
        this.email = email;
        this.rule = rule;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvata() {
        return avata;
    }

    public void setAvata(String avata) {
        this.avata = avata;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }
}
