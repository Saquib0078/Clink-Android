package com.visticsolution.posterbanao.editor.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.editor.adapter.MusicAdapter;
import com.visticsolution.posterbanao.editor.adapter.MusicPagerAdapter;
import com.visticsolution.posterbanao.model.MusicModel;

import java.util.List;


public class MusicFragment extends Fragment {

    MusicPagerAdapter.OnMusicSelect listner;
    List<MusicModel> musics;
    RecyclerView recyclerView;

    public MusicFragment(List<MusicModel> musics, MusicPagerAdapter.OnMusicSelect onMusicSelect) {
        this.listner = onMusicSelect;
        this.musics = musics;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_music, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.recyclerView = view.findViewById(R.id.overlay_artwork);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new MusicAdapter(getActivity(), musics, new MusicPagerAdapter.OnMusicSelect() {
            @Override
            public void onSelect(String path) {
                listner.onSelect(path);
            }

            @Override
            public void onPlay(String path) {
                listner.onPlay(path);
            }

            @Override
            public void onStop() {
                listner.onStop();
            }
        }));

        if (!(musics.size() > 0)){
            view.findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);
        }else {
            view.findViewById(R.id.no_data_layout).setVisibility(View.GONE);
        }
    }
}