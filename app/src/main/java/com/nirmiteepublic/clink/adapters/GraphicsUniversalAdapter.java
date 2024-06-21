package com.nirmiteepublic.clink.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nirmiteepublic.clink.adapters.viewholders.ItemGraphicImageViewHolder;
import com.nirmiteepublic.clink.adapters.viewholders.ItemGrid2RowViewHolder;
import com.nirmiteepublic.clink.databinding.ItemGraphicsImageBinding;
import com.nirmiteepublic.clink.databinding.ItemGrid2RowBinding;
import com.nirmiteepublic.clink.models.UniversalModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GraphicsUniversalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<UniversalModel> itemsModelList;
     Context context;
    public GraphicsUniversalAdapter(List<UniversalModel> itemsModelList) {
        this.itemsModelList = itemsModelList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        switch (itemsModelList.get(viewType).getType()) {
            case "image-graphic-row-2":
                return new ItemGrid2RowViewHolder(ItemGrid2RowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), parent.getContext(), itemsModelList.get(viewType));
            case "image-graphic":
                return new ItemGraphicImageViewHolder(ItemGraphicsImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), parent.getContext(), itemsModelList.get(viewType).getGraphicModelList().get(viewType));

        }

        notifyDataSetChanged();
        return new ItemGraphicImageViewHolder(ItemGraphicsImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), parent.getContext(), itemsModelList.get(viewType).getGraphicModelList().get(viewType));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
          UniversalModel universalModel=itemsModelList.get(position);
          holder.itemView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  String dateString = universalModel.getCreatedAt();
                  SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                  ((RecyclerView) v.getParent()).smoothScrollToPosition(holder.getAdapterPosition());

                  try {
                      // Parse the date string
                      Date date = inputFormat.parse(dateString);

                      // Define a desired output format
                      SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                      // Format the parsed date to the desired format
                      String formattedDate = outputFormat.format(date);
                      Toast.makeText(context, formattedDate, Toast.LENGTH_SHORT).show();
                      // Print the formatted date
                  } catch (ParseException e) {
                      // Handle the parse exception if the date string is not in the expected format
                      e.printStackTrace();
                  }
              }
          });
        if (holder instanceof ItemGrid2RowViewHolder ) {
            ((ItemGrid2RowViewHolder) holder).bind();
        } else if (holder instanceof ItemGraphicImageViewHolder) {
            ((ItemGraphicImageViewHolder) holder).bind();
        }


    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return itemsModelList.size();
    }

}
