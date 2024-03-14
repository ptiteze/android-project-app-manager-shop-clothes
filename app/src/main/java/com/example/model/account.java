package com.example.model;

public class account {
    private String acc;
    private String pass;
    private  String user_id;
    private  boolean state;
    private  String rule;
    public account(String acc, String pass, String user_id, boolean state, String rule) {
        this.acc = acc;
        this.pass = pass;
        this.user_id = user_id;
        this.state = state;
        this.rule = rule;
    }

    public String getRule() {
        return rule;
    }
    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public account() {
    }

    public String getAcc() {
        return acc;
    }

    public void setAcc(String acc) {
        this.acc = acc;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }


}
