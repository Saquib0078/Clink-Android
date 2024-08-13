package com.visticsolution.posterbanao.model;

public class UserModel {

    public String id, name, profile_pic, email,balance, number,designation, category_id, state, district, refer_id, refered, subscription_name, subscription_price, subscription_date, subscription_end_date, active_at, social, social_id, auth_token, device_token, status, updated_at, created_at;
    public int posts_limit=0,business_limit=0,political_limit=0,daily_limit;
    public int festival,business,political,video;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getRefer_id() {
        return refer_id;
    }

    public void setRefer_id(String refer_id) {
        this.refer_id = refer_id;
    }

    public String getRefered() {
        return refered;
    }

    public void setRefered(String refered) {
        this.refered = refered;
    }

    public String getSubscription_name() {
        return subscription_name;
    }

    public void setSubscription_name(String subscription_name) {
        this.subscription_name = subscription_name;
    }

    public String getSubscription_price() {
        return subscription_price;
    }

    public void setSubscription_price(String subscription_price) {
        this.subscription_price = subscription_price;
    }

    public String getSubscription_date() {
        return subscription_date;
    }

    public void setSubscription_date(String subscription_date) {
        this.subscription_date = subscription_date;
    }

    public String getSubscription_end_date() {
        return subscription_end_date;
    }

    public void setSubscription_end_date(String subscription_end_date) {
        this.subscription_end_date = subscription_end_date;
    }

    public int getPosts_limit() {
        return posts_limit;
    }

    public void setPosts_limit(int posts_limit) {
        this.posts_limit = posts_limit;
    }

    public int getBusiness_limit() {
        return business_limit;
    }

    public void setBusiness_limit(int business_limit) {
        this.business_limit = business_limit;
    }

    public int getPolitical_limit() {
        return political_limit;
    }

    public void setPolitical_limit(int political_limit) {
        this.political_limit = political_limit;
    }

    public String getSocial() {
        return social;
    }

    public void setSocial(String social) {
        this.social = social;
    }

    public String getSocial_id() {
        return social_id;
    }

    public void setSocial_id(String social_id) {
        this.social_id = social_id;
    }

    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getActive_at() {
        return active_at;
    }

    public void setActive_at(String active_at) {
        this.active_at = active_at;
    }

    public int getDaily_limit() {
        return daily_limit;
    }

    public void setDaily_limit(int daily_limit) {
        this.daily_limit = daily_limit;
    }

    public int getFestival() {
        return festival;
    }

    public void setFestival(int festival) {
        this.festival = festival;
    }

    public int getBusiness() {
        return business;
    }

    public void setBusiness(int business) {
        this.business = business;
    }

    public int getPolitical() {
        return political;
    }

    public void setPolitical(int political) {
        this.political = political;
    }

    public int getVideo() {
        return video;
    }

    public void setVideo(int video) {
        this.video = video;
    }
}
