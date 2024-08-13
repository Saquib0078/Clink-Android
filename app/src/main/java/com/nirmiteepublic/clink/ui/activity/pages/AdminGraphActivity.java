package com.nirmiteepublic.clink.ui.activity.pages;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.exoplayer2.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nirmiteepublic.clink.databinding.ActivityAdminGraphBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.functions.viewmanagers.PegaAppCompatActivity;
import com.nirmiteepublic.clink.models.UserCountResponse;
import com.nirmiteepublic.clink.ui.activity.actions.OtpActivity;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminGraphActivity extends PegaAppCompatActivity {
    ActivityAdminGraphBinding binding;
    private DatabaseReference mDatabase;
    DatabaseReference onlineStatusRef;
    BarChart lineChart;
    private DatabaseReference usersRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminGraphBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setWindowThemeSecond();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Set the online status for the current user
        onlineStatusRef = FirebaseDatabase.getInstance().getReference().child("users").child(UserUtils.getUserId()).child("totalTimeOnline").child("online");

        onlineStatusRef.setValue(true);
        lineChart = binding.barChart3;

        binding.count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminGraphActivity.this,UsersCountActivity.class));
            }
        });
        usersRef = FirebaseDatabase.getInstance().getReference("users");


        binding.otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminGraphActivity.this, ViewOtp.class));
            }
        });


        RetrofitClient.getInstance(this).getApiInterfaces().getUserCount().enqueue(new Callback<UserCountResponse>() {
            @Override
            public void onResponse(Call<UserCountResponse> call, Response<UserCountResponse> response) {
                if (response.isSuccessful()) {
                    UserCountResponse userCountResponse = response.body();
                    if (userCountResponse != null) {
                        int userCount = userCountResponse.getTotalusers();
                        binding.userCount.setText(String.valueOf(userCount));
                    }
                } else {
                    Toast.makeText(AdminGraphActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UserCountResponse> call, Throwable t) {
                Toast.makeText(AdminGraphActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        fetchAndDisplayAverageTime();
        countOnlineUsers();
    }

    private void countOnlineUsers() {
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int onlineCount = 0;
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    DataSnapshot totalTimeOnlineSnapshot = userSnapshot.child("totalTimeOnline");

                    Boolean online = totalTimeOnlineSnapshot.child("online").getValue(Boolean.class);
                    if (online != null && online.equals(true)) {
                        onlineCount++;
                    }
                }
                binding.onlineusers.setText(String.valueOf(onlineCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminGraphActivity.this, String.valueOf(databaseError), Toast.LENGTH_SHORT).show();            }
        });
    }

    private void fetchAndDisplayAverageTime() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long totalOnlineTime = 0;
                long totalOnlineUsers = 0;

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    DataSnapshot totalTimeOnlineSnapshot = userSnapshot.child("totalTimeOnline");
                    if (totalTimeOnlineSnapshot.exists()) {
                        boolean isOnline = totalTimeOnlineSnapshot.child("online").getValue(Boolean.class);
                            String timeString = totalTimeOnlineSnapshot.child("time").getValue(String.class);
                            long timeInMillis = convertToMilliseconds(timeString);
                            totalOnlineTime += timeInMillis;
                            totalOnlineUsers++;
                            if(!isOnline){
                                binding.onlineusers.setText(0+"");

                            }else{
                                binding.onlineusers.setText(String.valueOf(isOnline));

                            }

                    }
                }

                if (totalOnlineUsers > 0) {
                    long averageTime = totalOnlineTime / totalOnlineUsers;
                    displayAverageTimeInGraph(totalOnlineTime);
                    binding.average.setText(convertToSimpleTime(averageTime));

                } else {
                    Log.w("Firebase", "No users with totalTimeOnline data found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Log.e("Firebase", "Error fetching data: " + databaseError.getMessage());
            }
        });
    }

    // Helper method to convert milliseconds to simple time format (HH:MM:SS)
    private String convertToSimpleTime(long milliseconds) {
        long seconds = milliseconds / 1000;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
    }

    // Helper method to convert time string (HH:MM:SS) to milliseconds
    private long convertToMilliseconds(String timeString) {
        String[] parts = timeString.split(":");
        long hours = Long.parseLong(parts[0]);
        long minutes = Long.parseLong(parts[1]);
        long seconds = Long.parseLong(parts[2]);
        return (hours * 3600 + minutes * 60 + seconds) * 1000;
    }

    private void displayAverageTimeInGraph(long averageTime) {
        // Convert milliseconds to hours
        float averageTimeInHours = averageTime / (1000 * 60 * 60);

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, averageTimeInHours));

        BarDataSet dataSet = new BarDataSet(entries, "Average Time Spent (Hours)");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.5f); // Set custom bar width if needed

        // Set custom labels for X-axis if needed
        ArrayList<String> labels = new ArrayList<>();
        labels.add("Average");
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        lineChart.setData(barData);
        lineChart.invalidate(); // refresh chart
    }


    @Override
    protected void onPause() {
        super.onPause();
        onlineStatusRef.setValue(false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        onlineStatusRef.setValue(true);

    }


}






