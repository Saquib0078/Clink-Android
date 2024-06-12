package com.visticsolution.posterbanao.editor.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.editor.adapter.FramePagerAdapter;
import com.visticsolution.posterbanao.editor.adapter.RecyclerFrameAdapter;
import com.visticsolution.posterbanao.fragments.ContactFragment;
import com.visticsolution.posterbanao.model.FrameModel;
import com.visticsolution.posterbanao.responses.UserResponse;
import com.visticsolution.posterbanao.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class PersonalFrameFragment extends Fragment {


    public PersonalFrameFragment(FramePagerAdapter.OnFrameSelect onMusicSelect) {
        this.listner = onMusicSelect;
    }

    FramePagerAdapter.OnFrameSelect listner;
    List<FrameModel> frameModelslist = new ArrayList<>();
    RecyclerView recyclerView;
    UserViewModel userViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_frame, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        this.recyclerView = view.findViewById(R.id.overlay_artwork);
        this.recyclerView.setHasFixedSize(true);


        userViewModel.getUserFrames(Functions.getUID(getContext())).observe(getViewLifecycleOwner(), new Observer<UserResponse>() {
            @Override
            public void onChanged(UserResponse response) {
                if (response != null) {
                    frameModelslist.clear();
                    if (response.getUserframes().size() > 0) {
                        for (int i = 0; i < response.getUserframes().size(); i++) {
                            FrameModel frameModel = new FrameModel();
                            frameModel.setId(response.userframes.get(i).getId());
                            frameModel.setThumbnail(response.userframes.get(i).getItem_url());
                            frameModel.setCreated_at(response.userframes.get(i).getCreated_at());
                            frameModel.setPremium("0");
                            frameModelslist.add(frameModel);
                        }
                        setAdapter();
                    }
                    if (!(frameModelslist.size() > 0)){
                        view.findViewById(R.id.no_personalFrameFound).setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        view.findViewById(R.id.no_personalFrameFound).setOnClickListener(view2 -> {
            showContactFragment();
        });
    }

    private void setAdapter() {
        recyclerView.setAdapter(new RecyclerFrameAdapter(getContext(), frameModelslist, new FramePagerAdapter.OnFrameSelect() {
            @Override
            public void onSelect(FrameModel frameModel) {
                listner.onPersonalSelect(frameModel.getThumbnail());
            }

            @Override
            public void onPersonalSelect(String framepath) {

            }
        }));
    }


    private void showContactFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        transaction.addToBackStack(null);
        transaction.replace(android.R.id.content, new ContactFragment()).commit();
    }
}