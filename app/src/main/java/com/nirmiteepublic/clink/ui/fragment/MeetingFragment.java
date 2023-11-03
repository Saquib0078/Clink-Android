package com.nirmiteepublic.clink.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.adapters.pagers.TaskMeetAdapter;
import com.nirmiteepublic.clink.databinding.FragmentMeetingBinding;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaFragment;
import com.nirmiteepublic.clink.models.TaskMeetModel;

import java.util.ArrayList;
import java.util.List;

public class MeetingFragment extends PegaFragment {

  FragmentMeetingBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding=FragmentMeetingBinding.inflate(inflater, container, false);
        List<TaskMeetModel>models=new ArrayList<>();
        models.add(new TaskMeetModel("Demo Meeting", "hello we are here to demo meet", "Date :" + "20-10-2023", "Time :" + "10:30AM", "https://cdn.pixabay.com/photo/2023/10/16/10/51/fox-8318961_1280.png"));
        models.add(new TaskMeetModel("Demo Meeting", "hello we are here to demo meet", "20-10-2023", "10:30AM", "https://cdn.pixabay.com/photo/2023/10/16/10/51/fox-8318961_1280.png"));
        models.add(new TaskMeetModel("Demo Meeting", "hello we are here to demo meet", "20-10-2023", "10:30AM", "https://cdn.pixabay.com/photo/2023/10/16/10/51/fox-8318961_1280.png"));
        models.add(new TaskMeetModel("Demo Meeting", "hello we are here to demo meet", "20-10-2023", "10:30AM", "https://cdn.pixabay.com/photo/2023/10/16/10/51/fox-8318961_1280.png"));
        models.add(new TaskMeetModel("Demo Meeting", "hello we are here to demo meet", "20-10-2023", "10:30AM", "https://cdn.pixabay.com/photo/2023/10/16/10/51/fox-8318961_1280.png"));
        models.add(new TaskMeetModel("Demo Meeting", "hello we are here to demo meet", "20-10-2023", "10:30AM", "https://cdn.pixabay.com/photo/2023/10/16/10/51/fox-8318961_1280.png"));
        models.add(new TaskMeetModel("Demo Meeting", "hello we are here to demo meet", "20-10-2023", "10:30AM", "https://cdn.pixabay.com/photo/2023/10/16/10/51/fox-8318961_1280.png"));
        models.add(new TaskMeetModel("Demo Meeting", "hello we are here to demo meet", "20-10-2023", "10:30AM", "https://cdn.pixabay.com/photo/2023/10/16/10/51/fox-8318961_1280.png"));
        models.add(new TaskMeetModel("Demo Meeting", "hello we are here to demo meet", "20-10-2023", "10:30AM", "https://cdn.pixabay.com/photo/2023/10/16/10/51/fox-8318961_1280.png"));
        models.add(new TaskMeetModel("Demo Meeting", "hello we are here to demo meet", "20-10-2023", "10:30AM", "https://cdn.pixabay.com/photo/2023/10/16/10/51/fox-8318961_1280.png"));
        models.add(new TaskMeetModel("Demo Meeting", "hello we are here to demo meet", "20-10-2023", "10:30AM", "https://cdn.pixabay.com/photo/2023/10/16/10/51/fox-8318961_1280.png"));
        models.add(new TaskMeetModel("Demo Meeting", "hello we are here to demo meet", "20-10-2023", "10:30AM", "https://cdn.pixabay.com/photo/2023/10/16/10/51/fox-8318961_1280.png"));
        models.add(new TaskMeetModel("Demo Meeting", "hello we are here to demo meet", "20-10-2023", "10:30AM", "https://cdn.pixabay.com/photo/2023/10/16/10/51/fox-8318961_1280.png"));
        models.add(new TaskMeetModel("Demo Meeting", "hello we are here to demo meet", "20-10-2023", "10:30AM", "https://cdn.pixabay.com/photo/2023/10/16/10/51/fox-8318961_1280.png"));
        models.add(new TaskMeetModel("Demo Meeting", "hello we are here to demo meet", "20-10-2023", "10:30AM", "https://cdn.pixabay.com/photo/2023/10/16/10/51/fox-8318961_1280.png"));
        models.add(new TaskMeetModel("Demo Meeting", "hello we are here to demo meet", "20-10-2023", "10:30AM", "https://cdn.pixabay.com/photo/2023/10/16/10/51/fox-8318961_1280.png"));
        models.add(new TaskMeetModel("Demo Meeting", "hello we are here to demo meet", "20-10-2023", "10:30AM", "https://cdn.pixabay.com/photo/2023/10/16/10/51/fox-8318961_1280.png"));
        models.add(new TaskMeetModel("Demo Meeting", "hello we are here to demo meet", "20-10-2023", "10:30AM", "https://cdn.pixabay.com/photo/2023/10/16/10/51/fox-8318961_1280.png"));
        models.add(new TaskMeetModel("Demo Meeting", "hello we are here to demo meet", "20-10-2023", "10:30AM", "https://cdn.pixabay.com/photo/2023/10/16/10/51/fox-8318961_1280.png"));
        models.add(new TaskMeetModel("Demo Meeting", "hello we are here to demo meet", "20-10-2023", "10:30AM", "https://cdn.pixabay.com/photo/2023/10/16/10/51/fox-8318961_1280.png"));
        models.add(new TaskMeetModel("Demo Meeting", "hello we are here to demo meet", "20-10-2023", "10:30AM", "https://cdn.pixabay.com/photo/2023/10/16/10/51/fox-8318961_1280.png"));
        models.add(new TaskMeetModel("Demo Meeting", "hello we are here to demo meet", "20-10-2023", "10:30AM", "https://cdn.pixabay.com/photo/2023/10/16/10/51/fox-8318961_1280.png"));
        models.add(new TaskMeetModel("Demo Meeting", "hello we are here to demo meet", "20-10-2023", "10:30AM", "https://cdn.pixabay.com/photo/2023/10/16/10/51/fox-8318961_1280.png"));
        models.add(new TaskMeetModel("Demo Meeting", "hello we are here to demo meet", "20-10-2023", "10:30AM", "https://cdn.pixabay.com/photo/2023/10/16/10/51/fox-8318961_1280.png"));
        models.add(new TaskMeetModel("Demo Meeting", "hello we are here to demo meet", "20-10-2023", "10:30AM", "https://cdn.pixabay.com/photo/2023/10/16/10/51/fox-8318961_1280.png"));

        TaskMeetAdapter adapter=new TaskMeetAdapter(models);
        binding.taskmeetRecyclerview.setAdapter(adapter);
        return binding.getRoot();
    }
}