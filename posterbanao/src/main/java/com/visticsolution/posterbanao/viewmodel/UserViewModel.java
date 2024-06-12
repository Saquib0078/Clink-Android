package com.visticsolution.posterbanao.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.visticsolution.posterbanao.responses.UserResponse;
import com.visticsolution.posterbanao.respository.UserRespository;

import org.json.JSONException;
import org.json.JSONObject;

public class UserViewModel extends ViewModel {

    private UserRespository respository;

    public UserViewModel() {
        respository = new UserRespository();
    }

    public LiveData<UserResponse> getUserProfile(String uid) {
        return respository.getProfileData(uid);
    }

    public LiveData<UserResponse> updateProfilePic(String uid, String path) {
        return respository.updateProfilePic(uid, path);
    }

    public LiveData<UserResponse> getUserPosts(String uid) {
        return respository.getUserPosts(uid);
    }

    public LiveData<UserResponse> uploadUserPost(String uid, String path) {
        return respository.uploadUserPost(uid, path);
    }

    public LiveData<UserResponse> updateProfile(String uid, String name, String email, String number,String designation, String refercode, String state, String dist, String category) {
        return respository.updateProfile(uid, name, email, number,designation, refercode, state, dist, category);
    }

    public LiveData<UserResponse> addContact(String uid, String number, String message) {
        return respository.addContact(uid, number, message);
    }

    public LiveData<UserResponse> addInquiry(String uid, String service_id, String number, String message) {
        return respository.addInquiry(uid, service_id, number, message);
    }

    public LiveData<UserResponse> getUserBusiness(String uid, String type) {
        return respository.getUserBusiness(uid, type);
    }

    public LiveData<UserResponse> getBusinessDetail(String uid, String id) {
        return respository.getBusinessDetail(uid, id);
    }

    public LiveData<UserResponse> addUserBussiness(JSONObject jsonObject) {
        try {
            return respository.addUserBussiness(jsonObject);
        } catch (JSONException e) {
            return null;
        }
    }

    public LiveData<UserResponse> updateUserSubscription(String uid, String type, String sid, String tid, String promocode,String amount) {
        return respository.updateSuscription(uid, type, sid, tid, promocode,amount);
    }

    public LiveData<UserResponse> offlineSubscription(String uid, String type, String sid, String screenshot, String promocode,String amount) {
        return respository.offlineSuscription(uid, type, sid, screenshot, promocode, amount);
    }

    public LiveData<UserResponse> getUserFrames(String uid) {
        return respository.getUserFrames(uid);
    }

    public LiveData<UserResponse> getInvitedUser(String uid) {
        return respository.getInvitedUser(uid);
    }

    public LiveData<UserResponse> getWithdrawRequest(String uid) {
        return respository.getWithdrawRequest(uid);
    }
    public LiveData<UserResponse> getTransactionRequest(String uid) {
        return respository.getTransactionRequest(uid);
    }
    public LiveData<UserResponse> withdrawRequest(String uid, int balance, String s) {
        return respository.withdrawRequest(uid,balance,s);
    }

    public LiveData<UserResponse> cheakReferCode(String code) {
        return respository.cheakReferCode(code);
    }

    public LiveData<UserResponse> getUserCategory(String category_id) {
        return respository.getUserCategory(category_id);
    }

    public LiveData<UserResponse> login(JSONObject object) {
        try {
            return respository.login(object);
        } catch (JSONException e) {
            return null;
        }
    }
}
