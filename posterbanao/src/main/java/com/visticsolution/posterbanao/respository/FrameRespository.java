package com.visticsolution.posterbanao.respository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.visticsolution.posterbanao.classes.Constants;
import com.visticsolution.posterbanao.network.ApiClient;
import com.visticsolution.posterbanao.network.ApiService;
import com.visticsolution.posterbanao.responses.FrameResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FrameRespository {


    private ApiService apiService;
    public FrameRespository(){
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<FrameResponse> getFrames(String ratio){
        MutableLiveData<FrameResponse> data = new MutableLiveData<>();
        apiService.getFrames(Constants.API_KEY,ratio).enqueue(new Callback<FrameResponse>() {
            @Override
            public void onResponse(Call<FrameResponse> call, Response<FrameResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<FrameResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<FrameResponse> getFramesByType(String type,String ratio,boolean featured,String uid){
        MutableLiveData<FrameResponse> data = new MutableLiveData<>();
        apiService.getFramesByType(Constants.API_KEY,type,ratio, String.valueOf(featured),uid).enqueue(new Callback<FrameResponse>() {
            @Override
            public void onResponse(Call<FrameResponse> call, Response<FrameResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<FrameResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<FrameResponse> getStickers(String search){
        MutableLiveData<FrameResponse> data = new MutableLiveData<>();
        apiService.getStickersByCategory(Constants.API_KEY,search).enqueue(new Callback<FrameResponse>() {
            @Override
            public void onResponse(Call<FrameResponse> call, Response<FrameResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<FrameResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<FrameResponse> getLogos(){
        MutableLiveData<FrameResponse> data = new MutableLiveData<>();
        apiService.getLogosByCategory(Constants.API_KEY).enqueue(new Callback<FrameResponse>() {
            @Override
            public void onResponse(Call<FrameResponse> call, Response<FrameResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<FrameResponse> call, Throwable t) {
//                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<FrameResponse> getMusicByCategory(){
        MutableLiveData<FrameResponse> data = new MutableLiveData<>();
        apiService.getMusicByCategory(Constants.API_KEY).enqueue(new Callback<FrameResponse>() {
            @Override
            public void onResponse(Call<FrameResponse> call, Response<FrameResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<FrameResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<FrameResponse> getBackgrounds(){
        MutableLiveData<FrameResponse> data = new MutableLiveData<>();
        apiService.getBackgrounds(Constants.API_KEY).enqueue(new Callback<FrameResponse>() {
            @Override
            public void onResponse(Call<FrameResponse> call, Response<FrameResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<FrameResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
