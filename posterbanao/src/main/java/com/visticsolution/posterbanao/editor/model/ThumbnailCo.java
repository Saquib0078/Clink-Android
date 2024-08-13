package com.visticsolution.posterbanao.editor.model;

import com.google.gson.annotations.SerializedName;
import com.visticsolution.posterbanao.editor.View.text.TextInfo;

import java.io.Serializable;
import java.util.ArrayList;

public class ThumbnailCo implements Serializable {

    @SerializedName("id")
    String id;

    @SerializedName("title")
    String title;

    @SerializedName("thumb")
    String thumb;

    @SerializedName("category_id")
    String category_id;

    @SerializedName("views")
    int views;

    @SerializedName("ratio")
    String ratio;

    @SerializedName("template_w_h_ratio")
    String template_w_h_ratio;

    public String backgroundImg;

    private ArrayList<Sticker_info> sticker_info;
    private ArrayList<TextInfo> text_Info;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getTemplate_w_h_ratio() {
        return template_w_h_ratio;
    }

    public void setTemplate_w_h_ratio(String template_w_h_ratio) {
        this.template_w_h_ratio = template_w_h_ratio;
    }

    public ArrayList<Sticker_info> getSticker_info() {
        return sticker_info;
    }

    public void setSticker_info(ArrayList<Sticker_info> sticker_info) {
        this.sticker_info = sticker_info;
    }

    public ArrayList<TextInfo> getText_Info() {
        return text_Info;
    }

    public void setText_Info(ArrayList<TextInfo> text_Info) {
        this.text_Info = text_Info;
    }
}
