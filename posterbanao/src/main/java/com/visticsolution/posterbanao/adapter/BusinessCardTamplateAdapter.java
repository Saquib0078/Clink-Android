package com.visticsolution.posterbanao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.visticsolution.posterbanao.databinding.ItemVisitingcardTamplateBinding;
import com.visticsolution.posterbanao.editor.EditorActivity;
import com.visticsolution.posterbanao.editor.utils.BCardTamplateUtils;
import com.visticsolution.posterbanao.model.PostsModel;
import com.visticsolution.posterbanao.model.TamplateModel;

import java.util.ArrayList;
import java.util.List;

public class BusinessCardTamplateAdapter extends RecyclerView.Adapter<BusinessCardTamplateAdapter.ViewHolder> {

    Context context;
    List<TamplateModel> list = new ArrayList<>();

    public BusinessCardTamplateAdapter(Context context, List<TamplateModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemVisitingcardTamplateBinding binding = ItemVisitingcardTamplateBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
        int position = pos;
        holder.binding.setBusinessCard(list.get(position));
        try {
            holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (list.get(position).getPremium().equals("1")){
                        PostsModel postsModel = new PostsModel();
                        postsModel.setPremium("1");
                        EditorActivity.postsModel = postsModel;

                        BCardTamplateUtils bCardTamplateUtils = new BCardTamplateUtils(context);
                        bCardTamplateUtils.openEditorActivity(list.get(position).getJson());
                    }else{
                        BCardTamplateUtils bCardTamplateUtils = new BCardTamplateUtils(context);
                        bCardTamplateUtils.openEditorActivity(list.get(position).getJson());
                    }
                }
            });
        }catch (Exception e){}
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemVisitingcardTamplateBinding binding;

        public ViewHolder(@NonNull ItemVisitingcardTamplateBinding itemView) {
            super(itemView.getRoot());

            binding = itemView;

        }
    }
}
