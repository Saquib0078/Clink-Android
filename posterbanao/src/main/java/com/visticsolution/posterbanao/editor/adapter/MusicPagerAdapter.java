package com.visticsolution.posterbanao.editor.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.visticsolution.posterbanao.editor.Fragment.MusicFragment;
import com.visticsolution.posterbanao.model.MusicCategoryModel;

import java.util.ArrayList;
import java.util.List;

public class MusicPagerAdapter extends FragmentPagerAdapter {

    List<MusicCategoryModel> list = new ArrayList<>();
    OnMusicSelect listner;

    public interface OnMusicSelect{
        void onSelect(String path);
        void onPlay(String path);
        void onStop();
    }

    public MusicPagerAdapter(@NonNull FragmentManager fm, List<MusicCategoryModel> list, OnMusicSelect listener) {
        super(fm);
        this.list = list;
        this.listner = listener;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new MusicFragment(list.get(position).getMusics(), new OnMusicSelect() {
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
        });
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position).name;
    }
}
