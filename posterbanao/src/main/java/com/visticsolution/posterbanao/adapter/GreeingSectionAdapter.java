package com.visticsolution.posterbanao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.visticsolution.posterbanao.databinding.ItemSectionHorizontalLayoutBinding;
import com.visticsolution.posterbanao.databinding.ItemSectionLayoutBinding;
import com.visticsolution.posterbanao.model.PostsModel;
import com.visticsolution.posterbanao.model.SectionModel;

import java.util.List;

/**
 * Created by qboxus on 3/20/2018.
 */

public class GreeingSectionAdapter extends RecyclerView.Adapter<GreeingSectionAdapter.ViewHolder> {

    public Context context;
    List<SectionModel> datalist;

    public interface OnItemClickListener {
        void onItemClick(View view, List<PostsModel> postsModels, int main_position, int child_position);
    }

    public static GreeingSectionAdapter.OnItemClickListener listener;

    public GreeingSectionAdapter(Context context, List<SectionModel> arrayList, OnItemClickListener listener) {
        this.context = context;
        datalist = arrayList;
        GreeingSectionAdapter.listener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        ItemSectionLayoutBinding binding = ItemSectionLayoutBinding.inflate(LayoutInflater.from(context), viewGroup, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
        int position = pos;
        SectionModel item = datalist.get(position);
        holder.binding.setSection(item);

        HorizontalAdapter adapter = new HorizontalAdapter(context, position, item.getPosts());
        holder.binding.horizontalRecylerview.setAdapter(adapter);
        holder.bind(position, item.getPosts());
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemSectionLayoutBinding binding;

        public ViewHolder(ItemSectionLayoutBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public void bind(final int pos, final List<PostsModel> list) {
            binding.countAndSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v,list,pos,0);
                }
            });
        }
    }


    public static class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.CustomViewHolder> {
        public Context context;

        List<PostsModel> datalist;
        int main_position;

        public HorizontalAdapter(Context context, int position, List<PostsModel> arrayList) {
            this.context = context;
            datalist = arrayList;
            this.main_position = position;
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
            ItemSectionHorizontalLayoutBinding binding = ItemSectionHorizontalLayoutBinding.inflate(LayoutInflater.from(context), viewGroup, false);
            return new CustomViewHolder(binding);
        }

        @Override
        public int getItemCount() {
            return datalist.size();
        }

        class CustomViewHolder extends RecyclerView.ViewHolder {

            ItemSectionHorizontalLayoutBinding binding;

            public CustomViewHolder(ItemSectionHorizontalLayoutBinding view) {
                super(view.getRoot());

                binding = view;
            }

            public void bind(final int pos, final List<PostsModel> datalist) {
                binding.getRoot().setOnClickListener(v -> {
                    listener.onItemClick(binding.getRoot(), datalist, main_position, pos);
                });
            }
        }

        @Override
        public void onBindViewHolder(final CustomViewHolder holder, final int i) {
            PostsModel item = datalist.get(i);
            holder.binding.setPosts(item);
            holder.bind(i, datalist);
        }
    }

}