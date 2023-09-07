package com.zonek.apps.gfxherror.Models;

/**
 * This project create by SAID MOTYA on 06/17/2020.
 * contact on Facebook : https://web.facebook.com/motya.said
 * contact on Email : zonek.app@hotmail.com or zonek.app@gmail.com
 * it a free code source for member secret gfx
 */

public class ModelsTips {

    private String preview;
    private String title;
    private String position;

    private String content;
    private String image;
    private String order;
    private String text_size;
    private String color;
    private String style;
    private String gravity;
    private String left ;
    private String isLink;
    private String linkTitle;
    private String setLink;
    private String setNativeAds ;

    public ModelsTips(String preview, String title, String position) {
        this.preview = preview;
        this.title = title;
        this.position = position;
    }

    public ModelsTips(String content, String image, String order, String text_size, String color, String style, String gravity,String left, String isLink, String linkTitle, String setLink, String setNativeAds) {
        this.content = content;
        this.image = image;
        this.order = order;
        this.text_size = text_size;
        this.color = color;
        this.style = style;
        this.gravity = gravity;
        this.left = left;
        this.isLink = isLink;
        this.linkTitle = linkTitle;
        this.setLink = setLink;
        this.setNativeAds = setNativeAds;
    }

    public String getPreview() {
        return preview;
    }

    public String getTitle() {
        return title;
    }

    public String getPosition() {
        return position;
    }

    public String getContent() {
        return content;
    }

    public String getImage() {
        return image;
    }

    public String getOrder() {
        return order;
    }

    public String getText_size() {
        return text_size;
    }

    public String getColor() {
        return color;
    }

    public String getStyle() {
        return style;
    }

    public String getGravity() {
        return gravity;
    }

    public String getLeft() {
        return left;
    }

    public String getIsLink() {
        return isLink;
    }

    public String getLinkTitle() {
        return linkTitle;
    }

    public String getSetLink() {
        return setLink;
    }

    public String getSetNativeAds() {
        return setNativeAds;
    }
}
