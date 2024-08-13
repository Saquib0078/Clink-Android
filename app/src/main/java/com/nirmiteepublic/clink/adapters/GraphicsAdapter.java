package com.nirmiteepublic.clink.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.adapters.viewholders.ItemGraphicImageViewHolder;
import com.nirmiteepublic.clink.databinding.ItemGraphicsImageBinding;
import com.nirmiteepublic.clink.functions.retrofit.req.ApiInterfaces;
import com.nirmiteepublic.clink.functions.utils.UserUtils;
import com.nirmiteepublic.clink.models.GraphicModel;
import com.nirmiteepublic.clink.ui.activity.pages.SelectedImageActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class GraphicsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public interface GraphicItemClickListener {
        void onGraphicItemClicked(String graphicId,String id);
    }
    private final List<GraphicModel> graphicModelList;

    Context context;
    ImageView image;
    Bitmap bitmap;

    public GraphicItemClickListener itemClickListener;


    public GraphicsAdapter(List<GraphicModel> graphicModelList, GraphicItemClickListener itemClickListener) {
        this.graphicModelList = graphicModelList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (graphicModelList.get(viewType).getType().equals("image-graphic")) {
            return new ItemGraphicImageViewHolder(ItemGraphicsImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), parent.getContext(), graphicModelList.get(viewType));
        }
        return new ItemGraphicImageViewHolder(ItemGraphicsImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), parent.getContext(), graphicModelList.get(viewType));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemGraphicImageViewHolder) {
            ((ItemGraphicImageViewHolder) holder).bind();
        }
        GraphicModel graphicModel = graphicModelList.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SelectedImageActivity.class);
                intent.putExtra("selectedImage",graphicModel.getGraphicID() );

                context.startActivity(intent);


            }
        });

        if (UserUtils.isSuperAdmin()) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Handle long press action here
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Options");
                    String[] options = {"Edit", "Delete"};
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {

//                                Toast.makeText(context, ""+graphicModel.getId(), Toast.LENGTH_SHORT).show();

                                Toast.makeText(context, "Edit option selected", Toast.LENGTH_SHORT).show();
                            } else if (which == 1) {
                                if (itemClickListener != null) {
                                    itemClickListener.onGraphicItemClicked(graphicModel.getId(),graphicModel.getGraphicID());
                                }
                                Toast.makeText(context, graphicModel.getId(), Toast.LENGTH_SHORT).show();

                                Toast.makeText(context, "Delete option selected", Toast.LENGTH_SHORT).show();
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
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return graphicModelList.size();
    }


}
