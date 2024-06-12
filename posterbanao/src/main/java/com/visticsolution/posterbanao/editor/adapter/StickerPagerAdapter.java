package com.visticsolution.posterbanao.editor.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.visticsolution.posterbanao.editor.Fragment.StickerFragment;
import com.visticsolution.posterbanao.model.StickerModelCategory;

import java.util.ArrayList;
import java.util.List;

public class StickerPagerAdapter extends FragmentPagerAdapter {

    List<StickerModelCategory> list = new ArrayList<>();
    OnStickerSelect stickerListener;
    public interface OnStickerSelect{
        void sticker(String path);
    }

    public StickerPagerAdapter(@NonNull FragmentManager fm, List<StickerModelCategory> list, OnStickerSelect listener) {
        super(fm);
        this.list = list;
        this.stickerListener = listener;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new StickerFragment(list.get(position).getStickers(), new OnStickerSelect() {
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
