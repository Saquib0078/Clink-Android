package com.nirmiteepublic.clink.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ActivityMainBinding;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaAppCompatActivity;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaFragment;
import com.nirmiteepublic.clink.ui.activity.pages.AdminGraphActivity;
import com.nirmiteepublic.clink.ui.fragments.BroadCastFragment;
import com.nirmiteepublic.clink.ui.fragments.GraphicsFragment;
import com.nirmiteepublic.clink.ui.fragments.MeetFragment;
import com.nirmiteepublic.clink.ui.fragments.NetworkFragment;
import com.nirmiteepublic.clink.ui.fragments.TaskFragment;
import com.visticsolution.posterbanao.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends PegaAppCompatActivity {
    ActivityMainBinding binding;
    TaskFragment taskFragment = new TaskFragment();
    BroadCastFragment broadCastFragment = new BroadCastFragment();
    MeetFragment meetFragment = new MeetFragment();
    NetworkFragment networkFragment = new NetworkFragment();
    GraphicsFragment graphicsFragment = new GraphicsFragment();
    private DatabaseReference mDatabase;
    private HashMap<String, Long> onlineStatusMap;
    /**
     * Fragment list
     */
    List<PegaFragment> fragments = new ArrayList<>();
    int currentSelection = -1;
    private FirebaseAnalytics mFirebaseAnalytics;
    /**
     * Click Listener for bottom navigation bar
     */
    View.OnClickListener navigationClick = v -> selectPage(getNavIndexById(v.getId()));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String title = getIntent().getStringExtra("title");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        onlineStatusMap = new HashMap<>();


        // Pass the title to your fragment
//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                int onlineUsersCount = getNumberOfOnlineUsers(dataSnapshot);
////                Toast.makeText(MainActivity.this, ""+onlineUsersCount, Toast.LENGTH_SHORT).show();
////                Intent intent = new Intent(MainActivity.this, AdminGraphActivity.class);
////                intent.putExtra("onlineUsersCount", onlineUsersCount);
////                startActivity(intent);
//            }

//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(MainActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, UserUtils.getUserId());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, UserUtils.getUserFirstName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        FirebaseApp.initializeApp(this);
        setWindowThemeSecond();
        setAsLastActivity();
        if (!getIntent().hasExtra("data")) {
            System.exit(0);
            return;
        }

        initUtils();

        /* List of fragments */
        fragments.add(broadCastFragment);
        fragments.add(networkFragment);
        fragments.add(graphicsFragment);
        fragments.add(meetFragment);
        fragments.add(taskFragment);


        /* Set the Click events for the bottom navigation buttons */
        for (int i = 0; i < binding.bottomNavigationBar.getChildCount(); i++) {
            binding.bottomNavigationBar.getChildAt(i).setOnClickListener(navigationClick);
        }

        selectPage(0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BroadCastFragment.REQUEST_CODE_BROADCAST_PUBLISHED && data != null) {
            setPage(0);
        }
    }

    private void initUtils() {
        try {
            JSONObject object = new JSONObject(getIntent().getStringExtra("data"));
            System.out.println(object.toString(2));
            UserUtils.setUserFirstName(object.getString("fName"));
            UserUtils.setUserLastName(object.getString("lName"));
            UserUtils.setUserNumber(object.getString("num"));
            UserUtils.setUserRole(object.getString("role"));
            UserUtils.setGreeting(object.getString("greeting"));
            UserUtils.setUserId(object.getString("id"));

            FirebaseMessaging.getInstance().subscribeToTopic(UserUtils.getUserNumber())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Subscribed", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Not", Toast.LENGTH_SHORT).show();

                        }
                    });
            System.out.println("obj" + object);
            if (object.has("dp")) {
                UserUtils.setUserDp(object.getString("dp"));
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }


    private void logUserOnlineStatus(boolean isOnline) {
        if (UserUtils.getUserId() != null) {
            String userId = UserUtils.getUserId();
            DatabaseReference userRef = mDatabase.child("users").child(userId);
            DatabaseReference totalTimeRef = userRef.child("totalTimeOnline");

            if (isOnline) {
                // User is online, log online timestamp
                onlineStatusMap.put(userId, System.currentTimeMillis());
                totalTimeRef.child("online").setValue(true);

            } else {
                // User is offline, calculate session duration and update total online time
                Long onlineTimestamp = onlineStatusMap.get(userId);
                if (onlineTimestamp != null) {
                    long currentTime = System.currentTimeMillis();
                    long sessionDuration = currentTime - onlineTimestamp;
                    onlineStatusMap.remove(userId);

                    // Convert sessionDuration to simple time format
                    String sessionDurationString = convertToSimpleTime(sessionDuration);
                    totalTimeRef.child("time").setValue(sessionDurationString);

                    totalTimeRef.child("online").setValue(false);
                }
            }
        }
    }

    private int getNumberOfOnlineUsers(DataSnapshot dataSnapshot) {
        int onlineUsersCount = 0;
        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
            Boolean isOnline = userSnapshot.child("totalTimeOnline").child("online").getValue(Boolean.class);
            if (isOnline != null && isOnline) {
                onlineUsersCount++;
            }
        }
        return onlineUsersCount;
    }
    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isUserOnline", true);
        editor.apply();
        logUserOnlineStatus(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        logUserOnlineStatus(false);
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        logUserOnlineStatus(false);
//
//    }

    @Override
    protected void onResume() {
        super.onResume();
        logUserOnlineStatus(true);

    }

    @Override
    protected void onPause() {
        super.onPause();
        logUserOnlineStatus(false);

    }

    // Helper method to convert milliseconds to simple time format (HH:MM:SS)
    private String convertToSimpleTime(long milliseconds) {
        long seconds = milliseconds / 1000;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
    }

    private void selectPage(int navIndexById) {
        int currentPage = currentSelection;
        if(navIndexById!=2) {
            if (navIndexById != currentPage) {
                changeNavState(currentPage, navIndexById);
                currentSelection = navIndexById;
                setPage(navIndexById);
                return;
            }
            fragments.get(currentPage).onBackPressed();
        }else{
                    openActivityWithRightAnim(HomeActivity.class);
        }
    }

//    private void setPage(int navIndexById) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//
//        Fragment fragment = fragmentManager.findFragmentByTag("fragment" + navIndexById);
//
//        if (fragment == null) {
//            fragment = fragments.get(navIndexById);
//        }
//
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.replace(R.id.fragmentContainer, fragment, "fragment" + navIndexById);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }

    private void setPage(int navIndexById) {
        try {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, fragments.get(navIndexById).getClass().newInstance());
            transaction.addToBackStack(null);
            transaction.commit();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }


    private void changeNavState(int currentPage, int navIndexById) {
        changeWindowBars(navIndexById);
        if (currentPage != -1) {
            LinearLayout currentNavLayout = (LinearLayout) binding.bottomNavigationBar.getChildAt(currentPage);
            ImageView currentNavImage = (ImageView) currentNavLayout.getChildAt(0);
            TextView currentNavTitle = (TextView) currentNavLayout.getChildAt(1);

            ImageViewCompat.setImageTintList(currentNavImage, ColorStateList.valueOf(ContextCompat.getColor(this, R.color.dim)));
            currentNavTitle.setTextColor(ContextCompat.getColor(this, R.color.dim));
        }

        LinearLayout navLayout = (LinearLayout) binding.bottomNavigationBar.getChildAt(navIndexById);
        ImageView navImage = (ImageView) navLayout.getChildAt(0);
        TextView navTitle = (TextView) navLayout.getChildAt(1);

        ImageViewCompat.setImageTintList(navImage, ColorStateList.valueOf(ContextCompat.getColor(this, R.color.cyan_process)));
        navTitle.setTextColor(ContextCompat.getColor(this, R.color.cyan_process));
    }

    private void changeWindowBars(int navIndexById) {
        if (navIndexById == 1 || navIndexById == 4) {
            setWindowThemeMain();
            return;
        }

        setWindowThemeSecond();
    }

    private int getNavIndexById(int value) {
        for (int i = 0; i < binding.bottomNavigationBar.getChildCount(); i++) {
            if (binding.bottomNavigationBar.getChildAt(i).getId() == value) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onRestartRequest() {
        setPage(0);
    }

    @Override
    public void onBackPressed() {
        if (currentSelection == 0) {
            super.onBackPressed();
            return;
        }

        if (fragments.get(currentSelection).onBackPressed()) {
            return;
        }

        selectPage(0);

    }


}