package com.example.rodoggx.hotfirechat;

/**
 * Created by RodoggX on 11/24/2017.
 */

public class MessageItem {

    private String text;
    private String name;
    private String photoUrl;

    public MessageItem() {
    }

    public MessageItem(String text, String name, String photoUrl) {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
