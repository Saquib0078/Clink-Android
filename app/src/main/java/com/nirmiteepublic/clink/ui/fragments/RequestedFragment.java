package com.nirmiteepublic.clink.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.adapters.UserRequestAdapter;
import com.nirmiteepublic.clink.databinding.FragmentRequestedBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.models.Response;

import retrofit2.Call;
import retrofit2.Callback;


public class RequestedFragment extends Fragment {

    FragmentRequestedBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentRequestedBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment

        RetrofitClient.getInstance(requireContext()).getApiInterfaces().getallUser().enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response.isSuccessful()){
                    UserRequestAdapter adapter=new UserRequestAdapter(response.body().getUser());
                    binding.recview.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Toast.makeText(requireContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        return binding.getRoot();
    }
}