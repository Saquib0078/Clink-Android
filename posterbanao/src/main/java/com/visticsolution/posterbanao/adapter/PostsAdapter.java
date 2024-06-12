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

import java.util.ArrayList;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<PostsModel> list = new ArrayList<>();
    OnItemClickListener onItemClickListener;

    private int itemWidth = 0;
    int column;
    float width;

    public interface OnItemClickListener {
        void onItemClick(View view, PostsModel postsModels, int main_position);
    }

    public PostsAdapter(Context context, List<PostsModel> list, OnItemClickListener onItemClickListener, int column, float width) {
        this.context = context;
        this.onItemClickListener = onItemClickListener;
        this.list = list;
        this.column = column;
        this.width = width;
        itemWidth = App.getColumnWidth(column, width);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        ItemPosterVerticalBinding binding = ItemPosterVerticalBinding.inflate(LayoutInflater.from(context), viewGroup, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holde, int pos) {
        ViewHolder holder = (ViewHolder) holde;int position = pos;

        float f = 1.0f;
        String width = list.get(position).width;
        String height = list.get(position).height;
        f = Float.parseFloat(height) / Float.parseFloat(width);

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.binding.cvBase.getLayoutParams();
        params.width = itemWidth;
        params.height = (int) (itemWidth * f);

        holder.binding.cvBase.requestLayout();
        holder.binding.cvBase.setLayoutParams(params);

        holder.binding.setPosts(list.get(pos));
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v,list.get(position),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d("onChanged___",""+list.size());
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemPosterVerticalBinding binding;

        public ViewHolder(@NonNull ItemPosterVerticalBinding itemView) {
            super(itemView.getRoot());

            binding = itemView;
        }
    }
}
