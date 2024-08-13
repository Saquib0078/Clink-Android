package com.nirmiteepublic.clink.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.databinding.ItemMediaImageBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.models.SlidersItem;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditSliderAdapter extends RecyclerView.Adapter<EditSliderAdapter.Viewholder> {
    private final List<SlidersItem> imageSliderModelList;

    public EditSliderAdapter(List<SlidersItem> imageSliderModelList) {
        this.imageSliderModelList = imageSliderModelList;
    }

    Context context;

    @NonNull
    @Override
    public EditSliderAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new EditSliderAdapter.Viewholder(ItemMediaImageBinding.inflate(((Activity) parent.getContext()).getLayoutInflater()));
    }

    @Override
    public void onBindViewHolder(@NonNull EditSliderAdapter.Viewholder holder, int position) {
        SlidersItem item = imageSliderModelList.get(position);
        String imageUrl = RetrofitClient.BASE_URL + "graphics/getgraphics/" + imageSliderModelList.get(position).getSlider();

        Glide.with(holder.itemView)
                .load(imageUrl)
                .fitCenter().placeholder(ContextCompat.getDrawable(context, R.drawable.round_image_placeholder))
                .into(holder.binding.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Options");
                builder.setItems(new CharSequence[]{ "Delete"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            DeleteSlider(imageSliderModelList.get(position).getSlider());
                        }
                    }
                });
                builder.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return imageSliderModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ItemMediaImageBinding binding;

        public Viewholder(ItemMediaImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    private void DeleteSlider(String id){
        RetrofitClient.getInstance(context).getApiInterfaces().DeleteSlider(id)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }
}
