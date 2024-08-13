package com.visticsolution.posterbanao.editor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.model.FrameModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RecyclerFrameAdapter extends RecyclerView.Adapter<RecyclerFrameAdapter.ViewHolder> {

    Context context;
    List<FrameModel> list = new ArrayList<>();
    int selectedPosition = 500;
    FramePagerAdapter.OnFrameSelect onOverlaySelected;

    public RecyclerFrameAdapter(Context context2, List<FrameModel> iArr, FramePagerAdapter.OnFrameSelect onOverlaySelected) {
        this.context = context2;
        this.list = iArr;
        this.onOverlaySelected = onOverlaySelected;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int i) {
        return i;
    }

    public int getItemCount() {
        return this.list.size();
    }

    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        Glide.with(this.context).load(Functions.getItemBaseUrl(list.get(i).getThumbnail())).thumbnail(0.1f).apply((((new RequestOptions().dontAnimate()).centerCrop()).placeholder(R.drawable.placeholder)).error(R.drawable.placeholder)).into(viewHolder.imageView);
        if (this.selectedPosition == i) {
            viewHolder.viewImage.setVisibility(View.VISIBLE);
        } else {
            viewHolder.viewImage.setVisibility(View.INVISIBLE);
        }
        if (list.get(i).getPremium().equals("1")){
            viewHolder.premium_image.setVisibility(View.VISIBLE);
        }else{
            viewHolder.premium_image.setVisibility(View.GONE);
        }
        viewHolder.layout.setOnClickListener(view -> {
            RecyclerFrameAdapter recyclerOverLayAdapter = RecyclerFrameAdapter.this;
            recyclerOverLayAdapter.notifyItemChanged(recyclerOverLayAdapter.selectedPosition);
            RecyclerFrameAdapter recyclerOverLayAdapter2 = RecyclerFrameAdapter.this;
            recyclerOverLayAdapter2.selectedPosition = i;
            recyclerOverLayAdapter2.notifyItemChanged(recyclerOverLayAdapter2.selectedPosition);
            onOverlaySelected.onSelect(list.get(i));
        });
    }

    @NotNull
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_frame_adapter, viewGroup, false));
        viewGroup.setId(i);
        viewGroup.setFocusable(false);
        viewGroup.setFocusableInTouchMode(false);
        return viewHolder;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        LinearLayout layout;
        ImageView viewImage,premium_image;

        public ViewHolder(View view) {
            super(view);
            this.imageView = view.findViewById(R.id.item_image);
            this.premium_image = view.findViewById(R.id.premium_image);
            this.viewImage = view.findViewById(R.id.view_image);
            this.layout = view.findViewById(R.id.lay);
        }
    }
}
