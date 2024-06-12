package com.visticsolution.posterbanao.respository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.visticsolution.posterbanao.classes.Constants;
import com.visticsolution.posterbanao.model.SettingModel;
import com.visticsolution.posterbanao.network.ApiClient;
import com.visticsolution.posterbanao.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingRespository {


    private ApiService apiService;
    public SettingRespository(){
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<List<SettingModel>> getData(){
        MutableLiveData<List<SettingModel>> data = new MutableLiveData<>();
        apiService.getSettings(Constants.API_KEY).enqueue(new Callback<List<SettingModel>>() {
            @Override
            public void onResponse(Call<List<SettingModel>> call, Response<List<SettingModel>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<SettingModel>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
