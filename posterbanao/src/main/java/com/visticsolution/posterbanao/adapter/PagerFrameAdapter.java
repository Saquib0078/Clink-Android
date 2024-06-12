package com.visticsolution.posterbanao.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.visticsolution.posterbanao.editor.adapter.FramePagerAdapter;
import com.visticsolution.posterbanao.fragments.LoadFrameFragment;
import com.visticsolution.posterbanao.model.FrameModel;

import java.util.ArrayList;
import java.util.List;

public class PagerFrameAdapter extends FragmentPagerAdapter {

    List<FrameModel> list = new ArrayList<>();
    float wr = 1.0f;
    float hr = 1.0f;

    public interface OnLogoSelect{
        void sticker(String path);
    }

    public PagerFrameAdapter(@NonNull FragmentManager fm, List<FrameModel> list, float wr, float hr) {
        super(fm);
        this.list = list;
        this.wr = wr;
        this.hr = hr;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new LoadFrameFragment(list.get(position), wr, hr, new FramePagerAdapter.OnFrameSelect() {
            @Override
            public void onSelect(FrameModel frameModel) {

            }

            @Override
            public void onPersonalSelect(String framepath) {

            }
        });
    }

    @Override
    public int getCount() {
        return list.size();
    }

}
