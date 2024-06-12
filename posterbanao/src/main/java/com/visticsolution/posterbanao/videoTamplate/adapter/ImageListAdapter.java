package com.visticsolution.posterbanao.videoTamplate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.videoTamplate.VideoEditorActivity;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class ImageListAdapter extends Adapter<ImageListAdapter.MyViewHolder> {

    Context context;
    VideoEditorActivity.ClickAdapter clickAdapter;
    String[] tempImgUrl;
    int videoArr = 0;

    public ImageListAdapter(Context context, int i, String[] strArr, VideoEditorActivity.ClickAdapter clickAdapter) {
        this.context = context;
        this.videoArr = i;
        this.tempImgUrl = strArr;
        this.clickAdapter = clickAdapter;
    }

    public int getItemCount() {
        return this.videoArr;
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {

        Glide.with(context).load(new File(this.tempImgUrl[i])).into(myViewHolder.imageView);
        myViewHolder.imageView.setOnClickListener(view -> ImageListAdapter.this.clickAdapter.clickEvent(i));

    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gv_img_list_, viewGroup, false));
    }

    public static class MyViewHolder extends ViewHolder {
        ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            this.imageView = view.findViewById(R.id.gallery);
        }
    }
}
