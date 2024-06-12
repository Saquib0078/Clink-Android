package com.visticsolution.posterbanao.editor.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.databinding.ItemMusicBinding;
import com.visticsolution.posterbanao.model.MusicModel;

import java.util.List;


public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {

    private List<MusicModel> list;
    private MusicPagerAdapter.OnMusicSelect listener;
    Activity context;
    int selectedPosition = 500;

    public MusicAdapter(Activity context, List<MusicModel> list, MusicPagerAdapter.OnMusicSelect listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMusicBinding binding = ItemMusicBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MusicModel model = list.get(position);
        holder.binding.setMusic(model);
        if (this.selectedPosition == position) {
            holder.binding.playBtn.setIcon(context.getDrawable(com.google.android.exoplayer2.R.drawable.exo_controls_pause));
        } else {
            holder.binding.playBtn.setIcon(context.getDrawable(com.google.android.exoplayer2.R.drawable.exo_controls_play));
        }
        holder.binding.playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedPosition == position){
                    selectedPosition = 500;
                    listener.onStop();
                    holder.binding.playBtn.setIcon(context.getDrawable(com.google.android.exoplayer2.R.drawable.exo_controls_play));
                }else{
                    listener.onPlay(model.item_url);
                    MusicAdapter recyclerOverLayAdapter = MusicAdapter.this;
                    recyclerOverLayAdapter.notifyItemChanged(recyclerOverLayAdapter.selectedPosition);
                    MusicAdapter recyclerOverLayAdapter2 = MusicAdapter.this;
                    selectedPosition = position;
                    recyclerOverLayAdapter2.notifyItemChanged(recyclerOverLayAdapter2.selectedPosition);
                }
            }
        });

        holder.binding.selecBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.premium.equals("1") && !Functions.IsPremiumEnable(context)){
                    Functions.showToast(context,context.getString(R.string.please_buy_premium_for_premium_content));
                }else {
                    listener.onSelect(model.item_url);
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        ItemMusicBinding binding;

        ViewHolder(@NonNull ItemMusicBinding itemView) {
            super(itemView.getRoot());

            binding = itemView;

        }
    }

}