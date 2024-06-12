package com.visticsolution.posterbanao.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.visticsolution.posterbanao.model.VideoTamplateModel;
import com.visticsolution.posterbanao.responses.HomeResponse;
import com.visticsolution.posterbanao.respository.HomeRespository;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private HomeRespository respository;

    public HomeViewModel() {
        respository = new HomeRespository();
    }

    public LiveData<HomeResponse> getData(String uid,String language,String category) {
        return respository.getData(uid,language,category);
    }

    public LiveData<HomeResponse> cheakPromo(String promo) {
        return respository.cheakPromo(promo);
    }

    public LiveData<HomeResponse> getGreetingData(String language,String search,int pagecount) {
        return respository.getGreetingData(language,search,pagecount);
    }

    public LiveData<HomeResponse> getPremiumPostByCategory(String type) {
        return respository.getPremiumPostByCategory(type);
    }

    public LiveData<HomeResponse> getSubscriptions() {
        return respository.getSubscriptions();
    }

    public LiveData<HomeResponse> getPostByPage(String type,String language,String post_type,String item_id,String subcategory,String search,String postid,int page) {
        return respository.getPostByPage(type,language,post_type,item_id,subcategory,search,postid,page);
    }

    public LiveData<HomeResponse> getCategoriesByPage(String type,String search,int page) {
        return respository.getCategoriesByPage(type,search,page);
    }

    public LiveData<HomeResponse> getVideoTamplateCategoriesByPage(String type,String search,int page) {
        return respository.getVideoTamplateCategoriesByPage(type,search,page);
    }

    public LiveData<HomeResponse> getDailyPosts(String search, String language, String param, int pageCount) {
        return respository.getDailyPosts(search,language,param,pageCount);
    }

    public LiveData<HomeResponse> getBusinessPoliticalCategories(String search,String type) {
        return respository.getBusinessPoliticalCategory(search,type);
    }

    public LiveData<HomeResponse> getBusinessCards() {
        return respository.getBusinessCards();
    }

    public LiveData<HomeResponse> getServices() {
        return respository.getServices();
    }

    public LiveData<HomeResponse> getInvitationCategories(String query) {
        return respository.getInvitationCategories(query);
    }

    public LiveData<HomeResponse> getInvitationCardsByCatId(String id) {
        return respository.getInvitationCardsByCatId(id);
    }

    public LiveData<List<VideoTamplateModel>> getVideoTamplates(String id) {
        return respository.getVideoTamplates(id);
    }
}
