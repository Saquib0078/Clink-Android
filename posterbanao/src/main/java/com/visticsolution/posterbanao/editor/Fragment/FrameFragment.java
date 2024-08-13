package com.visticsolution.posterbanao.editor.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.editor.adapter.FramePagerAdapter;
import com.visticsolution.posterbanao.editor.adapter.RecyclerFrameAdapter;
import com.visticsolution.posterbanao.model.FrameModel;

import java.util.List;


public class FrameFragment extends Fragment {

    FramePagerAdapter.OnFrameSelect listner;
    List<FrameModel> frameModelslist;
    RecyclerView recyclerView;

    public FrameFragment(List<FrameModel> frameModelslist , FramePagerAdapter.OnFrameSelect onMusicSelect) {
        this.frameModelslist = frameModelslist;
        this.listner = onMusicSelect;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_frame, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.recyclerView = view.findViewById(R.id.overlay_artwork);
        this.recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new RecyclerFrameAdapter(getContext(), frameModelslist, new FramePagerAdapter.OnFrameSelect() {
            @Override
            public void onSelect(FrameModel frameModel) {
                listner.onSelect(frameModel);
            }

            @Override
            public void onPersonalSelect(String framepath) {

            }
        }));

        if (!(frameModelslist.size() > 0)){
            view.findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);
        }
    }
}