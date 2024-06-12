package com.visticsolution.posterbanao.responses;

import com.google.gson.annotations.SerializedName;
import com.visticsolution.posterbanao.model.BussinessModel;
import com.visticsolution.posterbanao.model.CategoryModel;
import com.visticsolution.posterbanao.model.TransactionModel;
import com.visticsolution.posterbanao.model.UserFrameModel;
import com.visticsolution.posterbanao.model.UserModel;
import com.visticsolution.posterbanao.model.UserPostModel;
import com.visticsolution.posterbanao.model.WithdrawModel;

import java.util.List;

public class UserResponse {

    @SerializedName("code")
    public int code;

    @SerializedName("message")
    public String message;

    @SerializedName("balance")
    public int balance;

    @SerializedName("total_withdraw")
    public int total_withdraw;

    @SerializedName("user")
    public UserModel userModel;

    @SerializedName("business")
    public BussinessModel business;

    @SerializedName("businesses")
    public List<BussinessModel> businesses;

    @SerializedName("userframes")
    public List<UserFrameModel> userframes;

    @SerializedName("userposts")
    public List<UserPostModel> userposts;

    @SerializedName("usercategory")
    public CategoryModel usercategory;

    @SerializedName("userslist")
    public List<UserModel> userslist;

    @SerializedName("withdrawlist")
    public List<WithdrawModel> withdrawlist;

    @SerializedName("transactionlist")
    public List<TransactionModel> transactionlist;

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getTotal_withdraw() {
        return total_withdraw;
    }

    public void setTotal_withdraw(int total_withdraw) {
        this.total_withdraw = total_withdraw;
    }

    public List<BussinessModel> getBusinesses() {
        return businesses;
    }

    public void setBusinesses(List<BussinessModel> businesses) {
        this.businesses = businesses;
    }

    public BussinessModel getBusiness() {
        return business;
    }

    public void setBusiness(BussinessModel business) {
        this.business = business;
    }

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

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public List<UserFrameModel> getUserframes() {
        return userframes;
    }

    public void setUserframes(List<UserFrameModel> userframes) {
        this.userframes = userframes;
    }

    public List<UserPostModel> getUserposts() {
        return userposts;
    }

    public void setUserposts(List<UserPostModel> userposts) {
        this.userposts = userposts;
    }

    public CategoryModel getUsercategory() {
        return usercategory;
    }

    public void setUsercategory(CategoryModel usercategory) {
        this.usercategory = usercategory;
    }

    public List<UserModel> getUserslist() {
        return userslist;
    }

    public void setUserslist(List<UserModel> userslist) {
        this.userslist = userslist;
    }

    public List<WithdrawModel> getWithdrawlist() {
        return withdrawlist;
    }

    public void setWithdrawlist(List<WithdrawModel> withdrawlist) {
        this.withdrawlist = withdrawlist;
    }

    public List<TransactionModel> getTransactionlist() {
        return transactionlist;
    }

    public void setTransactionlist(List<TransactionModel> transactionlist) {
        this.transactionlist = transactionlist;
    }
}
