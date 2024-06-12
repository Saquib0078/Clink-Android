package com.visticsolution.posterbanao.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.visticsolution.posterbanao.classes.App;
import com.visticsolution.posterbanao.databinding.ItemPosterVerticalBinding;
import com.visticsolution.posterbanao.model.PostsModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PostsSelectedAdapter extends RecyclerView.Adapter<PostsSelectedAdapter.ViewHolder> {

    Context context;
    List<PostsModel> makeUpEditImage;
    int selectedPosition = 500;
    OnItemClickListener onItemClickListener;

    private int itemWidth = 0;
    int column;
    float width;

    public interface OnItemClickListener {
        void onItemClick(View view, PostsModel postsModels, int main_position);
    }

    public PostsSelectedAdapter(Context context2, List<PostsModel> iArr, OnItemClickListener onOverlaySelected, int column, float width) {
        this.context = context2;
        this.makeUpEditImage = iArr;
        this.onItemClickListener = onOverlaySelected;
        this.column = column;
        this.width = width;
        itemWidth = App.getColumnWidth(column, width);
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
        return this.makeUpEditImage.size();
    }

    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.binding.setPosts(makeUpEditImage.get(i));

        if (makeUpEditImage.get(i).getItem_url().endsWith(".mp4")){
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) viewHolder.binding.cvBase.getLayoutParams();
            params.width = itemWidth;
            params.height = itemWidth;

            viewHolder.binding.cvBase.requestLayout();
            viewHolder.binding.cvBase.setLayoutParams(params);
        }else{
            float f = 1.0f;
            String width = makeUpEditImage.get(i).width;
            String height = makeUpEditImage.get(i).height;
            f = Float.parseFloat(height) / Float.parseFloat(width);

            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) viewHolder.binding.cvBase.getLayoutParams();
            params.width = itemWidth;
            params.height = (int) (itemWidth * f);

            Log.d("onBindViewHolder___","h - "+(int) (itemWidth * f)+" "+itemWidth);
            viewHolder.binding.cvBase.requestLayout();
            viewHolder.binding.cvBase.setLayoutParams(params);
        }

        if (this.selectedPosition == i) {
            viewHolder.binding.viewImage.setVisibility(View.VISIBLE);
        } else {
            viewHolder.binding.viewImage.setVisibility(View.INVISIBLE);
        }
        viewHolder.binding.getRoot().setOnClickListener(view -> {
            PostsSelectedAdapter recyclerOverLayAdapter = PostsSelectedAdapter.this;
            recyclerOverLayAdapter.notifyItemChanged(recyclerOverLayAdapter.selectedPosition);
            PostsSelectedAdapter recyclerOverLayAdapter2 = PostsSelectedAdapter.this;
            recyclerOverLayAdapter2.selectedPosition = i;
            recyclerOverLayAdapter2.notifyItemChanged(recyclerOverLayAdapter2.selectedPosition);
            onItemClickListener.onItemClick(viewHolder.binding.getRoot(),makeUpEditImage.get(i),i);
        });
    }

    @NotNull
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ItemPosterVerticalBinding binding = ItemPosterVerticalBinding.inflate(LayoutInflater.from(context), viewGroup, false);
        return new ViewHolder(binding);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemPosterVerticalBinding binding;

        public ViewHolder(@NonNull ItemPosterVerticalBinding itemView) {
            super(itemView.getRoot());

            binding = itemView;
        }
    }


}
