package com.visticsolution.posterbanao.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.visticsolution.posterbanao.fragments.InvitationPagerFragment;
import com.visticsolution.posterbanao.model.InvitationCategoryModel;
import com.visticsolution.posterbanao.model.InvitationModel;

import java.util.ArrayList;
import java.util.List;

public class InvitationPagerAdapter extends FragmentPagerAdapter {

    List<InvitationCategoryModel> list = new ArrayList<>();
    OnCardSelect listner;
    public interface OnCardSelect{
        void onSelect(InvitationModel frameModel);
    }

    public InvitationPagerAdapter(@NonNull FragmentManager fm, List<InvitationCategoryModel> list, OnCardSelect listener) {
        super(fm);
        this.list = list;
        this.listner = listener;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new InvitationPagerFragment(list.get(position).getInvitationcards(), new OnCardSelect() {
            @Override
            public void onSelect(InvitationModel frameModel) {
                listner.onSelect(frameModel);
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
        return list.get(position).getName();
    }
}
