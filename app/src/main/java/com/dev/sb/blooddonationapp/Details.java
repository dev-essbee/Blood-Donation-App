package com.dev.sb.blooddonationapp;

public class Details {
    private String text;
    int imageSrc;

    public Details(){}
    public Details(String text, int imageSrc) {
        this.text = text;
        this.imageSrc = imageSrc;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(int imageSrc) {
        this.imageSrc = imageSrc;
    }
}
