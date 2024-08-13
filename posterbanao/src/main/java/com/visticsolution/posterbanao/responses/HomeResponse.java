package com.visticsolution.posterbanao.responses;

import com.google.gson.annotations.SerializedName;
import com.visticsolution.posterbanao.model.BusinessCardModel;
import com.visticsolution.posterbanao.model.CategoryModel;
import com.visticsolution.posterbanao.model.InvitationCategoryModel;
import com.visticsolution.posterbanao.model.InvitationModel;
import com.visticsolution.posterbanao.model.OfferDialogModel;
import com.visticsolution.posterbanao.model.PostsModel;
import com.visticsolution.posterbanao.model.PromocodeModel;
import com.visticsolution.posterbanao.model.SectionModel;
import com.visticsolution.posterbanao.model.ServicesModel;
import com.visticsolution.posterbanao.model.SliderModel;
import com.visticsolution.posterbanao.model.SubscriptionModel;
import com.visticsolution.posterbanao.model.TamplateModel;
import com.visticsolution.posterbanao.model.VideoTamplateCategory;

import java.util.List;

public class HomeResponse {

    @SerializedName("code")
    public int code;

    @SerializedName("message")
    public String message;

    @SerializedName("slider")
    public List<SliderModel> sliderdata;

    @SerializedName("section")
    public List<SectionModel> section;

    @SerializedName("upcoming_event")
    public List<CategoryModel> upcoming_event;

    @SerializedName("subcategories")
    public List<CategoryModel> subcategories;

    @SerializedName("festival_category")
    public List<CategoryModel> festival_category;

    @SerializedName("video_tamplate_category")
    public List<VideoTamplateCategory> video_tamplate_category;

    @SerializedName("business_category")
    public List<CategoryModel> business_category;

    @SerializedName("political_category")
    public List<CategoryModel> political_category;

    @SerializedName("custom_category")
    public List<CategoryModel> custom_category;

    @SerializedName("daily_post")
    public List<PostsModel> daily_post;

    @SerializedName("business_political_category")
    public List<CategoryModel> business_political_category;

    @SerializedName("recent")
    public List<PostsModel> recent;

    @SerializedName("greeting_section")
    public List<SectionModel> greeting_section;

    @SerializedName("subscriptions")
    public List<SubscriptionModel> subscriptions;

    @SerializedName("categories")
    public List<CategoryModel> categories;

    @SerializedName("offerdialog")
    public OfferDialogModel offerdialog;

    @SerializedName("posts")
    public List<PostsModel> posts;

    @SerializedName("promocode")
    public PromocodeModel promocode;

    @SerializedName("foryou")
    public List<PostsModel> foryou;

    @SerializedName("businesscardtamplate")
    public List<TamplateModel> businesscardtamplate;

    @SerializedName("businesscarddigital")
    public List<BusinessCardModel> businesscarddigital;

    @SerializedName("services")
    public List<ServicesModel> services;

    @SerializedName("invitationcategories")
    public List<InvitationCategoryModel> invitationcategories;

    @SerializedName("customTamplateCategory")
    public List<InvitationCategoryModel> customTamplateCategory;

    @SerializedName("customTamplates")
    public List<InvitationModel> customTamplates;

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

    public List<SliderModel> getSliderdata() {
        return sliderdata;
    }

    public void setSliderdata(List<SliderModel> sliderdata) {
        this.sliderdata = sliderdata;
    }

    public List<SectionModel> getSection() {
        return section;
    }

    public void setSection(List<SectionModel> section) {
        this.section = section;
    }

    public List<CategoryModel> getUpcoming_event() {
        return upcoming_event;
    }

    public List<CategoryModel> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<CategoryModel> subcategories) {
        this.subcategories = subcategories;
    }

    public void setUpcoming_event(List<CategoryModel> upcoming_event) {
        this.upcoming_event = upcoming_event;
    }

    public List<CategoryModel> getFestival_category() {
        return festival_category;
    }

    public void setFestival_category(List<CategoryModel> festival_category) {
        this.festival_category = festival_category;
    }

    public List<CategoryModel> getBusiness_category() {
        return business_category;
    }

    public void setBusiness_category(List<CategoryModel> business_category) {
        this.business_category = business_category;
    }

    public List<CategoryModel> getPolitical_category() {
        return political_category;
    }

    public void setPolitical_category(List<CategoryModel> political_category) {
        this.political_category = political_category;
    }

    public List<CategoryModel> getCustom_category() {
        return custom_category;
    }

    public void setCustom_category(List<CategoryModel> custom_category) {
        this.custom_category = custom_category;
    }

    public List<PostsModel> getDaily_post() {
        return daily_post;
    }

    public void setDaily_post(List<PostsModel> daily_post) {
        this.daily_post = daily_post;
    }

    public List<CategoryModel> getBusiness_political_category() {
        return business_political_category;
    }

    public void setBusiness_political_category(List<CategoryModel> business_political_category) {
        this.business_political_category = business_political_category;
    }

    public List<PostsModel> getRecent() {
        return recent;
    }

    public void setRecent(List<PostsModel> recent) {
        this.recent = recent;
    }

    public List<SectionModel> getGreeting_section() {
        return greeting_section;
    }

    public void setGreeting_section(List<SectionModel> greeting_section) {
        this.greeting_section = greeting_section;
    }

    public List<SubscriptionModel> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<SubscriptionModel> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public List<CategoryModel> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryModel> categories) {
        this.categories = categories;
    }

    public List<PostsModel> getPosts() {
        return posts;
    }

    public void setPosts(List<PostsModel> posts) {
        this.posts = posts;
    }

    public OfferDialogModel getOfferdialog() {
        return offerdialog;
    }

    public void setOfferdialog(OfferDialogModel offerdialog) {
        this.offerdialog = offerdialog;
    }

    public PromocodeModel getPromocode() {
        return promocode;
    }

    public void setPromocode(PromocodeModel promocode) {
        this.promocode = promocode;
    }

    public List<PostsModel> getForyou() {
        return foryou;
    }

    public void setForyou(List<PostsModel> foryou) {
        this.foryou = foryou;
    }

    public List<TamplateModel> getBusinesscardtamplate() {
        return businesscardtamplate;
    }

    public void setBusinesscardtamplate(List<TamplateModel> businesscardtamplate) {
        this.businesscardtamplate = businesscardtamplate;
    }

    public List<BusinessCardModel> getBusinesscarddigital() {
        return businesscarddigital;
    }

    public void setBusinesscarddigital(List<BusinessCardModel> businesscarddigital) {
        this.businesscarddigital = businesscarddigital;
    }

    public List<VideoTamplateCategory> getVideo_tamplate_category() {
        return video_tamplate_category;
    }

    public void setVideo_tamplate_category(List<VideoTamplateCategory> video_tamplate_category) {
        this.video_tamplate_category = video_tamplate_category;
    }

    public List<ServicesModel> getServices() {
        return services;
    }

    public void setServices(List<ServicesModel> services) {
        this.services = services;
    }

    public List<InvitationCategoryModel> getInvitationcategories() {
        return invitationcategories;
    }

    public void setInvitationcategories(List<InvitationCategoryModel> invitationcategories) {
        this.invitationcategories = invitationcategories;
    }

    public List<InvitationCategoryModel> getCustomTamplateCategory() {
        return customTamplateCategory;
    }

    public void setCustomTamplateCategory(List<InvitationCategoryModel> customTamplateCategory) {
        this.customTamplateCategory = customTamplateCategory;
    }

    public List<InvitationModel> getCustomTamplates() {
        return customTamplates;
    }

    public void setCustomTamplates(List<InvitationModel> customTamplates) {
        this.customTamplates = customTamplates;
    }
}
