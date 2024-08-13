package com.nirmiteepublic.clink.ui.activity.pages;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.nirmiteepublic.clink.databinding.ActivityRolesBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.models.RoleResponse;
import com.nirmiteepublic.clink.models.RoleResponseItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RolesActivity extends AppCompatActivity {
    ActivityRolesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRolesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        fetchAndDisplayRoles();

    }


}