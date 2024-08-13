package com.nirmiteepublic.clink.ui.activity.pages;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ActivityRequestUserBinding;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaAppCompatActivity;
import com.nirmiteepublic.clink.ui.fragments.AcceptedFragment;
import com.nirmiteepublic.clink.ui.fragments.RejectedFragment;
import com.nirmiteepublic.clink.ui.fragments.RequestedFragment;

import javax.annotation.Nullable;

public class RequestUserActivity extends PegaAppCompatActivity {
    ActivityRequestUserBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRequestUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setWindowThemeSecond();
        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);



        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new RequestedFragment();
                    case 1:
                        return new AcceptedFragment(); // Replace Fragment2 with your second fragment class
                    case 2:
                        return new RejectedFragment(); // Replace Fragment3 with your third fragment class
                    default:
                        return new RequestedFragment();
                }
            }

            @Override
            public int getCount() {
                return 3; // Number of tabs
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                // Set tab titles
                switch (position) {
                    case 0:
                        return "Requested User";
                    case 1:
                        return "Accepted";
                    case 2:
                        return "Rejected";
                    default:
                        return "Requested User";
                }
            }
        };

// Set the adapter to the ViewPager
        viewPager.setAdapter(adapter);

// Connect the TabLayout with the ViewPager
        tabLayout.setupWithViewPager(viewPager);


    }


}