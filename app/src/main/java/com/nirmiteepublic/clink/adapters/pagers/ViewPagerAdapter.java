package com.nirmiteepublic.clink.adapters.pagers;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.nirmiteepublic.clink.ui.fragment.MeetingFragment;
import com.nirmiteepublic.clink.ui.fragment.PersonalMeetingFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MeetingFragment();
            case 1:
                return new PersonalMeetingFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2; // Number of tabs
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Return the title for each tab
        switch (position) {
            case 0:
                return "Meeting";
            case 1:
                return "Personal Meeting";
            default:
                return null;
        }
    }
}

