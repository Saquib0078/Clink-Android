package com.nirmiteepublic.clink.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.adapters.pagers.TaskAdapter;
import com.nirmiteepublic.clink.databinding.FragmentTaskBinding;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaFragment;
import com.nirmiteepublic.clink.models.TaskModel;

import java.util.ArrayList;
import java.util.List;


public class TaskFragment extends PegaFragment {
FragmentTaskBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentTaskBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        List<TaskModel> item= new ArrayList<>();
        item.add(new TaskModel("demoTask","The code you've provided appears to be checking if a user with a specific UID exists in a ","10/02/2023","9:00AM","https://cdn.pixabay.com/photo/2023/10/18/00/59/mountain-8322610_1280.jpg"));
        item.add(new TaskModel("demoTask","The code you've provided appears to be checking if a user with a specific UID exists in a ","10/02/2023","9:00AM","https://cdn.pixabay.com/photo/2023/10/18/00/59/mountain-8322610_1280.jpg"));
        item.add(new TaskModel("demoTask","The code you've provided appears to be checking if a user with a specific UID exists in a ","10/02/2023","9:00AM","https://cdn.pixabay.com/photo/2023/10/18/00/59/mountain-8322610_1280.jpg"));
        item.add(new TaskModel("demoTask","The code you've provided appears to be checking if a user with a specific UID exists in a ","10/02/2023","9:00AM","https://cdn.pixabay.com/photo/2023/10/18/00/59/mountain-8322610_1280.jpg"));
        item.add(new TaskModel("demoTask","The code you've provided appears to be checking if a user with a specific UID exists in a ","10/02/2023","9:00AM","https://cdn.pixabay.com/photo/2023/10/18/00/59/mountain-8322610_1280.jpg"));
        item.add(new TaskModel("demoTask","The code you've provided appears to be checking if a user with a specific UID exists in a ","10/02/2023","9:00AM","https://cdn.pixabay.com/photo/2023/10/18/00/59/mountain-8322610_1280.jpg"));
        item.add(new TaskModel("demoTask","The code you've provided appears to be checking if a user with a specific UID exists in a ","10/02/2023","9:00AM","https://cdn.pixabay.com/photo/2023/10/18/00/59/mountain-8322610_1280.jpg"));
        item.add(new TaskModel("demoTask","The code you've provided appears to be checking if a user with a specific UID exists in a ","10/02/2023","9:00AM","https://cdn.pixabay.com/photo/2023/10/18/00/59/mountain-8322610_1280.jpg"));
        item.add(new TaskModel("demoTask","The code you've provided appears to be checking if a user with a specific UID exists in a ","10/02/2023","9:00AM","https://cdn.pixabay.com/photo/2023/10/18/00/59/mountain-8322610_1280.jpg"));
        item.add(new TaskModel("demoTask","The code you've provided appears to be checking if a user with a specific UID exists in a ","10/02/2023","9:00AM","https://cdn.pixabay.com/photo/2023/10/18/00/59/mountain-8322610_1280.jpg"));
        item.add(new TaskModel("demoTask","The code you've provided appears to be checking if a user with a specific UID exists in a ","10/02/2023","9:00AM","https://cdn.pixabay.com/photo/2023/10/18/00/59/mountain-8322610_1280.jpg"));
        item.add(new TaskModel("demoTask","The code you've provided appears to be checking if a user with a specific UID exists in a ","10/02/2023","9:00AM","https://cdn.pixabay.com/photo/2023/10/18/00/59/mountain-8322610_1280.jpg"));
        item.add(new TaskModel("demoTask","The code you've provided appears to be checking if a user with a specific UID exists in a ","10/02/2023","9:00AM","https://cdn.pixabay.com/photo/2023/10/18/00/59/mountain-8322610_1280.jpg"));
        item.add(new TaskModel("demoTask","The code you've provided appears to be checking if a user with a specific UID exists in a ","10/02/2023","9:00AM","https://cdn.pixabay.com/photo/2023/10/18/00/59/mountain-8322610_1280.jpg"));
        item.add(new TaskModel("demoTask","The code you've provided appears to be checking if a user with a specific UID exists in a ","10/02/2023","9:00AM","https://cdn.pixabay.com/photo/2023/10/18/00/59/mountain-8322610_1280.jpg"));
        item.add(new TaskModel("demoTask","The code you've provided appears to be checking if a user with a specific UID exists in a ","10/02/2023","9:00AM","https://cdn.pixabay.com/photo/2023/10/18/00/59/mountain-8322610_1280.jpg"));
        item.add(new TaskModel("demoTask","The code you've provided appears to be checking if a user with a specific UID exists in a ","10/02/2023","9:00AM","https://cdn.pixabay.com/photo/2023/10/18/00/59/mountain-8322610_1280.jpg"));
        item.add(new TaskModel("demoTask","The code you've provided appears to be checking if a user with a specific UID exists in a ","10/02/2023","9:00AM","https://cdn.pixabay.com/photo/2023/10/18/00/59/mountain-8322610_1280.jpg"));
        item.add(new TaskModel("demoTask","The code you've provided appears to be checking if a user with a specific UID exists in a ","10/02/2023","9:00AM","https://cdn.pixabay.com/photo/2023/10/18/00/59/mountain-8322610_1280.jpg"));


        TaskAdapter adapter=new TaskAdapter(item);
        binding.taskrecyclerview.setAdapter(adapter);
        return binding.getRoot();
    }
}