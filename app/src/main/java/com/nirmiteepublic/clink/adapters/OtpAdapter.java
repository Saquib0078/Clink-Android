package com.nirmiteepublic.clink.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.models.OtpDataItem;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OtpAdapter extends RecyclerView.Adapter<OtpAdapter.OtpViewHolder> {
    private List<OtpDataItem> otps;

    Context context;

    public OtpAdapter(List<OtpDataItem> otps) {
        this.otps = otps;
    }

    @NonNull
    @Override
    public OtpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.otp_item, parent, false);
        return new OtpViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OtpViewHolder holder, int position) {
        OtpDataItem otp = otps.get(position);
         holder.mobileTextView.setText(otp.getNum());
        holder.otpTextView.setText(otp.getOtp());
        holder.dateTv.setText(otp.getTime());
    }

    @Override
    public int getItemCount() {
        return otps.size();
    }

    static class OtpViewHolder extends RecyclerView.ViewHolder {
        TextView mobileTextView;
        TextView otpTextView;
        TextView dateTv;

        OtpViewHolder(View itemView) {
            super(itemView);
            mobileTextView = itemView.findViewById(R.id.mobileTextView);
            otpTextView = itemView.findViewById(R.id.otpTextView);
            dateTv = itemView.findViewById(R.id.date);
        }
    }

    public void updateData(List<OtpDataItem> newOtps) {
        this.otps = newOtps;
        notifyDataSetChanged();
    }
}
