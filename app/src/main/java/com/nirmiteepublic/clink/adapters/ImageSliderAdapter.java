package com.nirmiteepublic.clink.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ItemImageSliderBinding;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.models.SlidersItem;
import com.nirmiteepublic.clink.ui.activity.pages.EditSliderActivity;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class ImageSliderAdapter extends
        SliderViewAdapter<ImageSliderAdapter.SliderAdapterViewHolder> {

    private final List<SlidersItem> imageSliderModelList;

    public interface OnItemClickListener {
        void onItemClick(String id);
    }

    private OnItemClickListener listener;


    Context context;

    public ImageSliderAdapter(List<SlidersItem> imageSliderModelList) {
        this.imageSliderModelList = imageSliderModelList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        context = parent.getContext();
        return new SliderAdapterViewHolder(ItemImageSliderBinding.inflate(((Activity) parent.getContext()).getLayoutInflater()));
    }

    @Override
    public void onBindViewHolder(SliderAdapterViewHolder holder, final int position) {
//         SlidersItem slidersItem=imageSliderModelList.get(position);
        Glide.with(holder.itemView)
                .load(imageSliderModelList.get(position).getSlider())
                .fitCenter().placeholder(ContextCompat.getDrawable(context, R.drawable.round_image_placeholder))
                .into(holder.binding.image);


        if (UserUtils.isSuperAdmin()) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Handle long press action here
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Options");
                    String[] options = {"Change Slider"};
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {

                                context.startActivity(new Intent(context, EditSliderActivity.class));
                            }
                        }
                    });

                    builder.create().show();
                    return true; // Consume the long click event
                }
            });

        }


    }

    @Override
    public int getCount() {
        return imageSliderModelList.size();
    }

    static class SliderAdapterViewHolder extends ViewHolder {

        ItemImageSliderBinding binding;

        public SliderAdapterViewHolder(ItemImageSliderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}