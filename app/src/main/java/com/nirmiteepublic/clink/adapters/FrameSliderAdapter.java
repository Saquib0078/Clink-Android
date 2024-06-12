package com.nirmiteepublic.clink.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nirmiteepublic.clink.R;
import com.nirmiteepublic.clink.models.FrameModel;

import java.util.List;

public class FrameSliderAdapter extends RecyclerView.Adapter<FrameSliderAdapter.ViewHolder> {

    private List<FrameModel> frameData;
    private Context context;

    public FrameSliderAdapter(Context context, List<FrameModel> frameData) {
        this.context = context;
        this.frameData = frameData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutResId = R.layout.item_frame_1 + viewType;
        View view = LayoutInflater.from(context).inflate(layoutResId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FrameModel frameModel = frameData.get(position);
        loadFrameContent(holder, frameModel);
    }

    @Override
    public int getItemCount() {
        return frameData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewFrame;
        TextView textViewFrameText,TexView2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewFrame = itemView.findViewById(R.id.imageView);
            textViewFrameText = itemView.findViewById(R.id.textView);
            TexView2=itemView.findViewById(R.id.text);
            TexView2=itemView.findViewById(R.id.textView4);
        }
    }

    private void loadFrameContent(ViewHolder holder, FrameModel frameModel) {
//        String frameResource = frameModel.getFrameResource();
//        holder.imageViewFrame.setImageResource(frameResource);

//        String frameText = frameModel.getFrameText();
//        String frameText2 = frameModel.getFrameText2();
//        holder.textViewFrameText.setText(frameText);
//        holder.TexView2.setText(frameText2);
        // Customize this based on your specific design


    }
}

