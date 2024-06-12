package com.nirmiteepublic.clink.adapters.pagers;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.nirmiteepublic.clink.ui.fragments.viewpager.MeetingFragment;
import com.nirmiteepublic.clink.ui.fragments.viewpager.PersonalMeetingsFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new MeetingFragment();
        }
        return new PersonalMeetingsFragment();
    }

    @Override
    public int getCount() {
        return 2; // Number of tabs
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Meeting";
        }
        return "Personal Meeting";
    }
}

