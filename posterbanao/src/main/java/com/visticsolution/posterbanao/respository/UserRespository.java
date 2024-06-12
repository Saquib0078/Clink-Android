package com.visticsolution.posterbanao.respository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.visticsolution.posterbanao.classes.Constants;
import com.visticsolution.posterbanao.network.ApiClient;
import com.visticsolution.posterbanao.network.ApiService;
import com.visticsolution.posterbanao.responses.UserResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRespository {


    private ApiService apiService;

    public UserRespository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<UserResponse> getProfileData(String uid) {
        MutableLiveData<UserResponse> data = new MutableLiveData<>();
        apiService.geUserProfile(Constants.API_KEY, uid).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                Log.d("onResponse___", uid + " -> " + response.body());
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.d("onResponse___", uid + " E-> " + t.getMessage());
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<UserResponse> getUserPosts(String uid) {
        MutableLiveData<UserResponse> data = new MutableLiveData<>();
        apiService.getUserPosts(Constants.API_KEY, uid).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                Log.d("onResponse___", uid + " -> " + response.body());
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.d("onResponse___", uid + " E-> " + t.getMessage());
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<UserResponse> updateProfilePic(String uid, String path) {
        MutableLiveData<UserResponse> data = new MutableLiveData<>();

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), new File(path));
        MultipartBody.Part imageBody =
                MultipartBody.Part.createFormData("image", new File(path).getName(), requestFile);
        RequestBody userid =
                RequestBody.create(MediaType.parse("multipart/form-data"), uid);

        apiService.updateProfilePic(Constants.API_KEY, userid, imageBody).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<UserResponse> offlineSuscription(String uid, String typ, String planid, String screenshot, String promocod,String amnt) {
        MutableLiveData<UserResponse> data = new MutableLiveData<>();

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), new File(screenshot));
        MultipartBody.Part imageBody = MultipartBody.Part.createFormData("image", new File(screenshot).getName(), requestFile);

        RequestBody userid = RequestBody.create(MediaType.parse("multipart/form-data"), uid);
        RequestBody type = RequestBody.create(MediaType.parse("multipart/form-data"), typ);
        RequestBody sid = RequestBody.create(MediaType.parse("multipart/form-data"), planid);
        RequestBody promocode = RequestBody.create(MediaType.parse("multipart/form-data"), promocod);
        RequestBody amount = RequestBody.create(MediaType.parse("multipart/form-data"), amnt);

        apiService.offlineSuscription(Constants.API_KEY, userid,type,sid,promocode,amount,imageBody).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<UserResponse> uploadUserPost(String uid, String path) {

        MutableLiveData<UserResponse> data = new MutableLiveData<>();

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), new File(path));

        MultipartBody.Part fileBody = null;
        if (path.endsWith(".mp4")) {
            fileBody = MultipartBody.Part.createFormData("video", new File(path).getName(), requestFile);
        } else {
            fileBody = MultipartBody.Part.createFormData("image", new File(path).getName(), requestFile);
        }

        RequestBody userid = RequestBody.create(MediaType.parse("multipart/form-data"), uid);

        apiService.uploadUserPost(Constants.API_KEY, userid, fileBody).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }

    public LiveData<UserResponse> login(JSONObject object) throws JSONException {
        MutableLiveData<UserResponse> data = new MutableLiveData<>();
        apiService.login(
                Constants.API_KEY,
                "" + object.getString("social"),
                "" + object.getString("social_id"),
                "" + object.getString("auth_token"),
                "" + object.getString("email"),
                "" + object.getString("number"),
                "" + object.getString("profile_pic"),
                "" + object.getString("name"),
                "" + object.getString("device_token")).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<UserResponse> updateProfile(String uid, String name, String email, String number,String designation, String refercode, String state, String dist, String category) {
        MutableLiveData<UserResponse> data = new MutableLiveData<>();
        apiService.updateProfile(
                Constants.API_KEY,
                "" + uid,
                "" + name,
                "" + email,
                "" + number,
                ""+designation,
                "" + refercode,
                "" + state,
                "" + dist,
                Integer.parseInt(category)).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<UserResponse> addContact(String uid, String number, String message) {
        MutableLiveData<UserResponse> data = new MutableLiveData<>();
        apiService.addContact(
                Constants.API_KEY,
                "" + uid,
                "" + number,
                "" + message).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<UserResponse> getBusinessDetail(String uid, String bid) {
        MutableLiveData<UserResponse> data = new MutableLiveData<>();
        apiService.getBusinessDetail(
                Constants.API_KEY,
                "" + uid,
                "" + bid).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<UserResponse> updateSuscription(String uid, String type, String sid, String tid, String promocode, String amount) {
        MutableLiveData<UserResponse> data = new MutableLiveData<>();
        apiService.updateSubscription(
                Constants.API_KEY,
                "" + uid,
                "" + type,
                "" + sid,
                "" + tid,
                "" + promocode,
                ""+amount).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<UserResponse> getUserFrames(String uid) {
        MutableLiveData<UserResponse> data = new MutableLiveData<>();
        apiService.getUserFrames(
                Constants.API_KEY,
                "" + uid).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<UserResponse> getInvitedUser(String uid) {
        MutableLiveData<UserResponse> data = new MutableLiveData<>();
        apiService.getInvitedUser(
                Constants.API_KEY,
                "" + uid).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<UserResponse> getWithdrawRequest(String uid) {
        MutableLiveData<UserResponse> data = new MutableLiveData<>();
        apiService.getWithdrawRequest(
                Constants.API_KEY,
                "" + uid).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<UserResponse> getTransactionRequest(String uid) {
        MutableLiveData<UserResponse> data = new MutableLiveData<>();
        apiService.getTransactionRequest(
                Constants.API_KEY,
                "" + uid).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<UserResponse> withdrawRequest(String uid, int balance, String s) {
        MutableLiveData<UserResponse> data = new MutableLiveData<>();
        apiService.withdrawRequest(
                Constants.API_KEY,
                "" + uid,
                balance,
                s).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<UserResponse> cheakReferCode(String code) {
        MutableLiveData<UserResponse> data = new MutableLiveData<>();
        apiService.cheakReferCode(
                Constants.API_KEY,
                "" + code).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<UserResponse> getUserCategory(String category_id) {
        MutableLiveData<UserResponse> data = new MutableLiveData<>();
        apiService.getUserCategory(
                Constants.API_KEY,
                "" + category_id).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<UserResponse> getUserBusiness(String uid, String type) {
        MutableLiveData<UserResponse> data = new MutableLiveData<>();
        apiService.getUserBusiness(
                Constants.API_KEY,
                "" + uid,
                "" + type).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<UserResponse> addUserBussiness(JSONObject jsonObject) throws JSONException {
        MutableLiveData<UserResponse> data = new MutableLiveData<>();

        MultipartBody.Part fileBody = null;
        if (!jsonObject.getString("image").isEmpty()) {
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), new File(jsonObject.getString("image")));
            fileBody = MultipartBody.Part.createFormData("image", new File(jsonObject.getString("image")).getName(), requestFile);
        }
        RequestBody id = RequestBody.create(MediaType.parse("multipart/form-data"), jsonObject.getString("id"));
        RequestBody userid = RequestBody.create(MediaType.parse("multipart/form-data"), jsonObject.getString("user_id"));
        RequestBody company = RequestBody.create(MediaType.parse("multipart/form-data"), jsonObject.getString("company"));
        RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), jsonObject.getString("name"));
        RequestBody number = RequestBody.create(MediaType.parse("multipart/form-data"), jsonObject.getString("number"));
        RequestBody email = RequestBody.create(MediaType.parse("multipart/form-data"), jsonObject.getString("email"));
        RequestBody website = RequestBody.create(MediaType.parse("multipart/form-data"), jsonObject.getString("website"));
        RequestBody address = RequestBody.create(MediaType.parse("multipart/form-data"), jsonObject.getString("address"));
        RequestBody whatsapp = RequestBody.create(MediaType.parse("multipart/form-data"), jsonObject.getString("whatsapp"));
        RequestBody facebook = RequestBody.create(MediaType.parse("multipart/form-data"), jsonObject.getString("facebook"));
        RequestBody twitter = RequestBody.create(MediaType.parse("multipart/form-data"), jsonObject.getString("twitter"));
        RequestBody youtube = RequestBody.create(MediaType.parse("multipart/form-data"), jsonObject.getString("youtube"));
        RequestBody instagram = RequestBody.create(MediaType.parse("multipart/form-data"), jsonObject.getString("instagram"));
        RequestBody about = RequestBody.create(MediaType.parse("multipart/form-data"), jsonObject.getString("about"));
        RequestBody type = RequestBody.create(MediaType.parse("multipart/form-data"), jsonObject.getString("type"));
        RequestBody designation = RequestBody.create(MediaType.parse("multipart/form-data"), jsonObject.getString("designation"));
        RequestBody category_id = RequestBody.create(MediaType.parse("multipart/form-data"), jsonObject.getString("category_id"));
        Log.d("RequestBody___", jsonObject.getString("address"));
        apiService.addBusiness(
                Constants.API_KEY,
                fileBody,
                id,
                userid,
                company,
                name,
                number,
                email,
                website,
                address,
                whatsapp,
                facebook,
                twitter,
                youtube,
                instagram,
                about,
                type,
                designation,
                category_id).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<UserResponse> addInquiry(String uid, String service_id, String number, String message) {
        MutableLiveData<UserResponse> data = new MutableLiveData<>();
        apiService.addInquiry(
                Constants.API_KEY,
                "" + uid,
                "" + service_id,
                "" + number,
                "" + message).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
