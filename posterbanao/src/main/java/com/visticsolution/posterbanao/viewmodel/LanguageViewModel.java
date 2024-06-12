package com.visticsolution.posterbanao.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.visticsolution.posterbanao.model.LanguageModel;
import com.visticsolution.posterbanao.respository.LanguageRespository;

import java.util.List;

public class LanguageViewModel extends ViewModel {

    private LanguageRespository respository;

    public LanguageViewModel(){
        respository = new LanguageRespository();
    }

    public LiveData<List<LanguageModel>> getData(){
        return respository.getData();
    }

}
