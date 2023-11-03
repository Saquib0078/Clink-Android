package com.nirmiteepublic.clink.ui.activity.pages;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.FragmentTransaction;

import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ActivityMainBinding;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaFragment;
import com.nirmiteepublic.clink.ui.fragment.BroadCastFragment;
import com.nirmiteepublic.clink.ui.fragment.GraphicsFragment;
import com.nirmiteepublic.clink.ui.fragment.NetworkFragment;
import com.nirmiteepublic.clink.ui.fragment.TaskFragment;
import com.nirmiteepublic.clink.ui.fragment.TaskMeetFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    TaskFragment taskFragment = new TaskFragment();
    BroadCastFragment broadCastFragment = new BroadCastFragment();
    TaskMeetFragment taskMeetFragment = new TaskMeetFragment();
    NetworkFragment networkFragment = new NetworkFragment();
    GraphicsFragment graphicsFragment = new GraphicsFragment();

    /**
     * Fragment list
     */
    List<PegaFragment> fragments = new ArrayList<>();
    int currentSelection = -1;
    /**
     * Click Listener for bottom navigation bar
     */
    View.OnClickListener navigationClick = v -> selectPage(getNavIndexById(v.getId()));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /* List of fragments */
        fragments.add(broadCastFragment);
        fragments.add(networkFragment);
        fragments.add(graphicsFragment);
        fragments.add(taskMeetFragment);
        fragments.add(taskFragment);


        /* Set the Click events for the bottom navigation buttons */
        for (int i = 0; i < binding.bottomNavigationBar.getChildCount(); i++) {
            binding.bottomNavigationBar.getChildAt(i).setOnClickListener(navigationClick);
        }

        selectPage(0);

    }


    private void selectPage(int navIndexById) {
        int currentPage = currentSelection;
        if (navIndexById != currentPage) {
            changeNavState(currentPage, navIndexById);
            currentSelection = navIndexById;
            setPage(navIndexById);
            return;
        }
        fragments.get(currentPage).onBackPressed();
    }

    private void setPage(int navIndexById) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragments.get(navIndexById));
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private void changeNavState(int currentPage, int navIndexById) {
        if (currentPage != -1) {
            LinearLayout currentNavLayout = (LinearLayout) binding.bottomNavigationBar.getChildAt(currentPage);
            ImageView currentNavImage = (ImageView) currentNavLayout.getChildAt(0);
            TextView currentNavTitle = (TextView) currentNavLayout.getChildAt(1);

            ImageViewCompat.setImageTintList(currentNavImage, ColorStateList.valueOf(ContextCompat.getColor(this, R.color.fade)));
            currentNavTitle.setTextColor(ContextCompat.getColor(this, R.color.fade));
        }

        LinearLayout navLayout = (LinearLayout) binding.bottomNavigationBar.getChildAt(navIndexById);
        ImageView navImage = (ImageView) navLayout.getChildAt(0);
        TextView navTitle = (TextView) navLayout.getChildAt(1);

        ImageViewCompat.setImageTintList(navImage, ColorStateList.valueOf(ContextCompat.getColor(this, R.color.cyan_process)));
        navTitle.setTextColor(ContextCompat.getColor(this, R.color.cyan_process));
    }

    private int getNavIndexById(int value) {
        for (int i = 0; i < binding.bottomNavigationBar.getChildCount(); i++) {
            if (binding.bottomNavigationBar.getChildAt(i).getId() == value) {
                return i;
            }
        }
        return -1;
    }


}