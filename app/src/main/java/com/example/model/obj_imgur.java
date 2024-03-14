package com.example.model;
import com.google.gson.*;
import com.google.gson.annotations.SerializedName;

public class obj_imgur {
    @SerializedName("data")
    private ImgurData data;

    public ImgurData getData() {
        return data;
    }

    public void setData(ImgurData data) {
        this.data = data;
    }
    public String getLink() {
        return data.getLink();
    }
}

class ImgurData {
    @SerializedName("link")
    private String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}