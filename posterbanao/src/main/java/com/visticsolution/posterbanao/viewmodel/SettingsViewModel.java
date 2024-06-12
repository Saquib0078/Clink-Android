package com.visticsolution.posterbanao.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.visticsolution.posterbanao.model.SettingModel;
import com.visticsolution.posterbanao.respository.SettingRespository;

import java.util.List;

public class SettingsViewModel extends ViewModel {

    private SettingRespository respository;

    public SettingsViewModel(){
        respository = new SettingRespository();
    }

    public LiveData<List<SettingModel>> getAllSetting(){
        return respository.getData();
    }

}
