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

import org.jetbrains.annotations.NotNull;

public class RecyclerTextBgAdapter extends RecyclerView.Adapter<RecyclerTextBgAdapter.ViewHolder> {

    Context context;
    int[] makeUpEditImage;
    int selectedPosition = 500;

    public RecyclerTextBgAdapter(Context context2, int[] iArr) {
        this.context = context2;
        this.makeUpEditImage = iArr;
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
        return this.makeUpEditImage.length;
    }

    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        Glide.with(this.context).load(this.makeUpEditImage[i]).thumbnail(0.1f).apply((((new RequestOptions().dontAnimate()).centerCrop()).placeholder(R.drawable.placeholder)).error(R.drawable.placeholder)).into(viewHolder.imageView);
        if (this.selectedPosition == i) {
            viewHolder.viewImage.setVisibility(View.VISIBLE);
        } else {
            viewHolder.viewImage.setVisibility(View.INVISIBLE);
        }
        viewHolder.layout.setOnClickListener(view -> {
            RecyclerTextBgAdapter recyclerTextBgAdapter = RecyclerTextBgAdapter.this;
            recyclerTextBgAdapter.notifyItemChanged(recyclerTextBgAdapter.selectedPosition);
            RecyclerTextBgAdapter recyclerTextBgAdapter2 = RecyclerTextBgAdapter.this;
            recyclerTextBgAdapter2.selectedPosition = i;
            recyclerTextBgAdapter2.notifyItemChanged(recyclerTextBgAdapter2.selectedPosition);
        });
    }


    @NotNull
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_adapterbg, viewGroup, false));
        viewGroup.setId(i);
        viewGroup.setFocusable(false);
        viewGroup.setFocusableInTouchMode(false);
        return viewHolder;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        LinearLayout layout;
        ImageView viewImage;

        public ViewHolder(View view) {
            super(view);
            this.imageView = view.findViewById(R.id.item_image);
            this.viewImage = view.findViewById(R.id.view_image);
            this.layout = view.findViewById(R.id.lay);
        }
    }
}
