package com.nirmiteepublic.clink.adapters.viewholders;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.nirmiteepublic.clink.adapters.GraphicsAdapter;
import com.nirmiteepublic.clink.databinding.ItemGrid2RowBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.RetrofitClient;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.models.UniversalModel;
import com.nirmiteepublic.clink.ui.activity.EditGraphicsActivity;
import com.nirmiteepublic.clink.ui.activity.ViewAllActivity;
import com.nirmiteepublic.clink.ui.activity.pages.UploadGraphicsActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemGrid2RowViewHolder extends RecyclerView.ViewHolder {

    private final Context context;
    private final UniversalModel universalModel;
    ItemGrid2RowBinding binding;


    public ItemGrid2RowViewHolder(ItemGrid2RowBinding binding, Context context, UniversalModel universalModel) {
        super(binding.getRoot());
        this.binding = binding;
        this.context = context;
        this.universalModel = universalModel;
    }


    public void bind() {
        binding.title.setText(universalModel.getTitle());

        GraphicsAdapter adapter = new GraphicsAdapter(universalModel.getGraphicModelList(),null);
        binding.recyclerView.setAdapter(adapter);

        if (UserUtils.isSuperAdmin()) {
            binding.edit.setVisibility(View.VISIBLE);
            binding.delete.setVisibility(View.VISIBLE);
            binding.create.setVisibility(View.VISIBLE);

        }

        binding.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ViewAllActivity.class);
                intent.putExtra("id",universalModel.getId());
                context.startActivity(intent);
            }
        });

        binding.create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, UploadGraphicsActivity.class);
                intent.putExtra("id",universalModel.getId());
                context.startActivity(intent);
                Toast.makeText(context, universalModel.getId(), Toast.LENGTH_SHORT).show();
            }
        });
        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, EditGraphicsActivity.class);
                intent.putExtra("id",universalModel.getId());
                context.startActivity(intent);
            }
        });


        binding.delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    RetrofitClient.getInstance(context).getApiInterfaces().deleteGraphics(universalModel.getId()).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (Exception e) {
                    Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


//        ChipBtnAdapter chipBtnAdapter = new ChipBtnAdapter(universalModel.getChipButtonList());
//        binding.chipBtnRecyclerView.setAdapter(chipBtnAdapter);

    }


}