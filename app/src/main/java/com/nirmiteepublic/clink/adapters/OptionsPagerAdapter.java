package com.nirmiteepublic.clink.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.nirmiteepublic.clink.adapters.pagerviewholder.AddressPagerHolder;
import com.nirmiteepublic.clink.adapters.pagerviewholder.BLOPagerHolder;
import com.nirmiteepublic.clink.adapters.pagerviewholder.PersonalInfoPagerHolder;
import com.nirmiteepublic.clink.adapters.pagerviewholder.SocialInfoPagerHolder;
import com.nirmiteepublic.clink.databinding.PagerItemUserDetailsBinding;
import com.nirmiteepublic.clink.databinding.PagerItemUserDetailsInputBinding;
import com.nirmiteepublic.clink.functions.listeners.option.OptionsCallBackListener;
import com.nirmiteepublic.clink.functions.listeners.option.OptionsListener;

import java.util.ArrayList;
import java.util.List;

public class OptionsPagerAdapter extends PagerAdapter {

    private final Context context;

    private final OptionsCallBackListener optionsCallBackListener;

    private final List<OptionsListener> optionsListenerList = new ArrayList<>();

    public OptionsPagerAdapter(Context context, OptionsCallBackListener optionsCallBackListener) {
        this.context = context;
        this.optionsCallBackListener = optionsCallBackListener;
    }

    @Override
    public int getCount() {
        return 4;
    }

    public void onNext(int position) {
        optionsListenerList.get(position).onNext();

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        switch (position) {
            case 0:
                PagerItemUserDetailsBinding detailsBinding1 = PagerItemUserDetailsBinding.inflate(((Activity) context).getLayoutInflater(), container, false);
                PersonalInfoPagerHolder personalInfoPagerHolder = new PersonalInfoPagerHolder(position, detailsBinding1, optionsCallBackListener, context);
                personalInfoPagerHolder.bind();
                optionsListenerList.add(personalInfoPagerHolder::onNext);
                container.addView(detailsBinding1.getRoot());
                return detailsBinding1.getRoot();
            case 1:
                PagerItemUserDetailsBinding detailsBinding2 = PagerItemUserDetailsBinding.inflate(((Activity) context).getLayoutInflater(), container, false);
                AddressPagerHolder addressPagerHolder = new AddressPagerHolder(position, detailsBinding2, optionsCallBackListener, context);
                addressPagerHolder.bind();
                optionsListenerList.add(addressPagerHolder::onNext);
                container.addView(detailsBinding2.getRoot());
                return detailsBinding2.getRoot();
            case 2:
                PagerItemUserDetailsInputBinding inputBinding1 = PagerItemUserDetailsInputBinding.inflate(((Activity) context).getLayoutInflater(), container, false);
                BLOPagerHolder bloPagerHolder = new BLOPagerHolder(position, inputBinding1, optionsCallBackListener, context);
                bloPagerHolder.bind();
                optionsListenerList.add(bloPagerHolder::onNext);
                container.addView(inputBinding1.getRoot());
                return inputBinding1.getRoot();
            default:
                PagerItemUserDetailsInputBinding inputBinding2 = PagerItemUserDetailsInputBinding.inflate(((Activity) context).getLayoutInflater(), container, false);
                SocialInfoPagerHolder socialInfoPagerHolder = new SocialInfoPagerHolder(position, inputBinding2, optionsCallBackListener, context);
                socialInfoPagerHolder.bind();
                optionsListenerList.add(socialInfoPagerHolder::onNext);
                container.addView(inputBinding2.getRoot());
                return inputBinding2.getRoot();
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View) object);

    }
}
