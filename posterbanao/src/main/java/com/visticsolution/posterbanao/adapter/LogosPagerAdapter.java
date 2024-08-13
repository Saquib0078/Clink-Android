package com.visticsolution.posterbanao.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.visticsolution.posterbanao.fragments.LogosListFragment;
import com.visticsolution.posterbanao.model.StickerModelCategory;

import java.util.ArrayList;
import java.util.List;

public class LogosPagerAdapter extends FragmentPagerAdapter {

    List<StickerModelCategory> list = new ArrayList<>();
    OnLogoSelect stickerListener;
    String name;

    public interface OnLogoSelect{
        void sticker(String path);
    }

    public LogosPagerAdapter(@NonNull FragmentManager fm, List<StickerModelCategory> list, OnLogoSelect listener, String name) {
        super(fm);
        this.list = list;
        this.stickerListener = listener;
        this.name = name;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new LogosListFragment(name,list.get(position).getLogos(), new OnLogoSelect() {
            @Override
            public void sticker(String path) {
                stickerListener.sticker(path);
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
