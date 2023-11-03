package com.nirmiteepublic.clink.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nirmiteepublic.clink.adapters.pagers.UserAdapter;
import com.nirmiteepublic.clink.databinding.FragmentNetworkBinding;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaFragment;
import com.nirmiteepublic.clink.models.UserItem;

import java.util.ArrayList;
import java.util.List;


public class NetworkFragment extends PegaFragment {
    FragmentNetworkBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNetworkBinding.inflate(inflater, container, false);
        List<UserItem> items = new ArrayList<>();
        items.add(new UserItem("John", "nd kjc", "https://cdn.pixabay.com/photo/2023/10/15/13/59/walnuts-8316999_1280.jpg"));
        items.add(new UserItem("John", "nd kjc", "https://cdn.pixabay.com/photo/2023/10/16/07/55/animal-8318650_1280.jpg"));
        items.add(new UserItem("John", "nd kjc", "https://cdn.pixabay.com/photo/2023/10/16/07/55/animal-8318650_1280.jpg"));
        items.add(new UserItem("John", "nd kjc", "https://cdn.pixabay.com/photo/2023/10/16/07/55/animal-8318650_1280.jpg"));
        items.add(new UserItem("John", "nd kjc", "https://cdn.pixabay.com/photo/2023/10/16/07/55/animal-8318650_1280.jpg"));
        items.add(new UserItem("John", "nd kjc", "https://cdn.pixabay.com/photo/2023/10/16/07/55/animal-8318650_1280.jpg"));
        items.add(new UserItem("John", "nd kjc", "https://cdn.pixabay.com/photo/2023/10/16/07/55/animal-8318650_1280.jpg"));
        items.add(new UserItem("John", "nd kjc", "https://cdn.pixabay.com/photo/2023/10/16/07/55/animal-8318650_1280.jpg"));

        UserAdapter adapter = new UserAdapter(items);
        binding.networkRecyclerview.setAdapter(adapter);
        return binding.getRoot();
    }
}