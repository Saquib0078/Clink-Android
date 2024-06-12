package com.visticsolution.posterbanao.responses;

import com.google.gson.annotations.SerializedName;
import com.visticsolution.posterbanao.model.BackgroundModel;
import com.visticsolution.posterbanao.model.FrameCategoryModel;
import com.visticsolution.posterbanao.model.FrameModel;
import com.visticsolution.posterbanao.model.MusicCategoryModel;
import com.visticsolution.posterbanao.model.StickerModel;
import com.visticsolution.posterbanao.model.StickerModelCategory;
import com.visticsolution.posterbanao.model.UserFrameModel;

import java.util.List;

public class FrameResponse {

    @SerializedName("code")
    public int code;

    @SerializedName("message")
    public String message;

    @SerializedName("frames")
    public List<FrameModel> frames;

    @SerializedName("userframes")
    public List<UserFrameModel> userframes;

    @SerializedName("framecategories")
    public List<FrameCategoryModel> framecategories;

    @SerializedName("stickercategory")
    public List<StickerModelCategory> stickercategory;

    @SerializedName("stickers")
    public List<StickerModel> stickers;

    @SerializedName("musiccategories")
    public List<MusicCategoryModel> musiccategories;

    @SerializedName("logoscategory")
    public List<StickerModelCategory> logoscategory;

    @SerializedName("backgrounds")
    public List<BackgroundModel> backgrounds;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FrameModel> getFrames() {
        return frames;
    }

    public void setFrames(List<FrameModel> frames) {
        this.frames = frames;
    }

    public List<UserFrameModel> getUserframes() {
        return userframes;
    }

    public void setUserframes(List<UserFrameModel> userframes) {
        this.userframes = userframes;
    }

    public List<FrameCategoryModel> getFramecategories() {
        return framecategories;
    }

    public void setFramecategories(List<FrameCategoryModel> framecategories) {
        this.framecategories = framecategories;
    }

    public List<StickerModelCategory> getStickercategory() {
        return stickercategory;
    }

    public void setStickercategory(List<StickerModelCategory> stickercategory) {
        this.stickercategory = stickercategory;
    }

    public List<StickerModelCategory> getLogoscategory() {
        return logoscategory;
    }

    public void setLogoscategory(List<StickerModelCategory> logoscategory) {
        this.logoscategory = logoscategory;
    }

    public List<MusicCategoryModel> getMusiccategories() {
        return musiccategories;
    }

    public void setMusiccategories(List<MusicCategoryModel> musiccategories) {
        this.musiccategories = musiccategories;
    }

    public List<BackgroundModel> getBackgrounds() {
        return backgrounds;
    }

    public void setBackgrounds(List<BackgroundModel> backgrounds) {
        this.backgrounds = backgrounds;
    }
}
