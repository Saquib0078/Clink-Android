package com.visticsolution.posterbanao.respository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.visticsolution.posterbanao.classes.Constants;
import com.visticsolution.posterbanao.model.VideoTamplateModel;
import com.visticsolution.posterbanao.network.ApiClient;
import com.visticsolution.posterbanao.network.ApiService;
import com.visticsolution.posterbanao.responses.HomeResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeRespository {


    private ApiService apiService;

    public HomeRespository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<HomeResponse> getData(String uid, String language, String category) {
        MutableLiveData<HomeResponse> data = new MutableLiveData<>();
        apiService.getHomeData(Constants.API_KEY, language, uid,category).enqueue(new Callback<HomeResponse>() {
            @Override
            public void onResponse(Call<HomeResponse> call, Response<HomeResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<HomeResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<HomeResponse> cheakPromo(String promo) {
        MutableLiveData<HomeResponse> data = new MutableLiveData<>();
        apiService.cheakPromo(Constants.API_KEY, promo).enqueue(new Callback<HomeResponse>() {
            @Override
            public void onResponse(Call<HomeResponse> call, Response<HomeResponse> response) {
                Log.d("response___t", "" + response.body());
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<HomeResponse> call, Throwable t) {
                data.setValue(null);
                Log.d("response___t", "E-> " + t.getMessage());
            }
        });
        return data;
    }

    public LiveData<HomeResponse> getGreetingData(String language,String search, int pagecount) {
        MutableLiveData<HomeResponse> data = new MutableLiveData<>();
        apiService.getGreetingData(Constants.API_KEY, language,search, pagecount).enqueue(new Callback<HomeResponse>() {
            @Override
            public void onResponse(Call<HomeResponse> call, Response<HomeResponse> response) {
                Log.d("onResponse___", " -> " + response.body().message);
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<HomeResponse> call, Throwable t) {
                Log.d("onResponse___", " E-> " + t.getMessage());
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<HomeResponse> getBusinessCards() {
        MutableLiveData<HomeResponse> data = new MutableLiveData<>();
        apiService.getBusinessCards(Constants.API_KEY).enqueue(new Callback<HomeResponse>() {
            @Override
            public void onResponse(Call<HomeResponse> call, Response<HomeResponse> response) {
                Log.d("onResponse___", " -> " + response.body().message);
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<HomeResponse> call, Throwable t) {
                Log.d("onResponse___", " E-> " + t.getMessage());
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<HomeResponse> getServices() {
        MutableLiveData<HomeResponse> data = new MutableLiveData<>();
        apiService.getServices(Constants.API_KEY).enqueue(new Callback<HomeResponse>() {
            @Override
            public void onResponse(Call<HomeResponse> call, Response<HomeResponse> response) {
                Log.d("onResponse___", " -> " + response.body().message);
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<HomeResponse> call, Throwable t) {
                Log.d("onResponse___", " E-> " + t.getMessage());
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<HomeResponse> getInvitationCategories(String query) {
        MutableLiveData<HomeResponse> data = new MutableLiveData<>();
        apiService.getInvitationCategories(Constants.API_KEY, query).enqueue(new Callback<HomeResponse>() {
            @Override
            public void onResponse(Call<HomeResponse> call, Response<HomeResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<HomeResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<HomeResponse> getInvitationCardsByCatId(String id) {
        MutableLiveData<HomeResponse> data = new MutableLiveData<>();
        apiService.getInvitationCardsByCatId(Constants.API_KEY, id).enqueue(new Callback<HomeResponse>() {
            @Override
            public void onResponse(Call<HomeResponse> call, Response<HomeResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<HomeResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<HomeResponse> getPremiumPostByCategory(String type) {
        MutableLiveData<HomeResponse> data = new MutableLiveData<>();
        apiService.getPremiumPostByCategory(Constants.API_KEY, type).enqueue(new Callback<HomeResponse>() {
            @Override
            public void onResponse(Call<HomeResponse> call, Response<HomeResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<HomeResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<HomeResponse> getSubscriptions() {
        MutableLiveData<HomeResponse> data = new MutableLiveData<>();
        apiService.getSubscriptions(Constants.API_KEY).enqueue(new Callback<HomeResponse>() {
            @Override
            public void onResponse(Call<HomeResponse> call, Response<HomeResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<HomeResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<HomeResponse> getCategoriesByPage(String type, String search, int page) {
        MutableLiveData<HomeResponse> data = new MutableLiveData<>();
        apiService.getCategoriesByPage(Constants.API_KEY, page, type, search).enqueue(new Callback<HomeResponse>() {
            @Override
            public void onResponse(Call<HomeResponse> call, Response<HomeResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<HomeResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<HomeResponse> getVideoTamplateCategoriesByPage(String type, String search, int page) {
        MutableLiveData<HomeResponse> data = new MutableLiveData<>();
        apiService.getVideoTamplateCategoriesByPage(Constants.API_KEY, page, type, search).enqueue(new Callback<HomeResponse>() {
            @Override
            public void onResponse(Call<HomeResponse> call, Response<HomeResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<HomeResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }


    public LiveData<HomeResponse> getPostByPage(String type, String language, String post_type, String item_id, String subcategory, String search,String postid, int page) {
        MutableLiveData<HomeResponse> data = new MutableLiveData<>();
        apiService.getPostByPage(Constants.API_KEY, page, type, language, post_type, item_id,subcategory,postid, search).enqueue(new Callback<HomeResponse>() {
            @Override
            public void onResponse(Call<HomeResponse> call, Response<HomeResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<HomeResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<HomeResponse> getBusinessPoliticalCategory(String search, String type) {
        MutableLiveData<HomeResponse> data = new MutableLiveData<>();
        apiService.getBusinessPoliticalCategory(Constants.API_KEY, search, type).enqueue(new Callback<HomeResponse>() {
            @Override
            public void onResponse(Call<HomeResponse> call, Response<HomeResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<HomeResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<HomeResponse> getDailyPosts(String search, String language, String type, int pageCount) {
        MutableLiveData<HomeResponse> data = new MutableLiveData<>();
        apiService.getDailyPosts(Constants.API_KEY, search, language, type, pageCount).enqueue(new Callback<HomeResponse>() {
            @Override
            public void onResponse(Call<HomeResponse> call, Response<HomeResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<HomeResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<List<VideoTamplateModel>> getVideoTamplates(String id) {
        MutableLiveData<List<VideoTamplateModel>> data = new MutableLiveData<>();
        apiService.getVideoTamplates(Constants.API_KEY, id).enqueue(new Callback<List<VideoTamplateModel>>() {
            @Override
            public void onResponse(Call<List<VideoTamplateModel>> call, Response<List<VideoTamplateModel>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<VideoTamplateModel>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
