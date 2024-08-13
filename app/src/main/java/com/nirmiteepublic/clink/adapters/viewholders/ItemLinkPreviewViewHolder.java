//package com.nirmiteepublic.clink.adapters.viewholders;
//
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.text.TextUtils;
//import android.view.View;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.nirmiteepublic.clink.databinding.ItemLinkPreviewBinding;
//import com.nirmiteepublic.clink.models.BroadcastModel;
//
//public class ItemLinkPreviewViewHolder extends RecyclerView.ViewHolder {
//    private ItemLinkPreviewBinding binding;
//
//    public ItemLinkPreviewViewHolder(ItemLinkPreviewBinding binding, Context context) {
//        super(binding.getRoot());
//        this.binding = binding;
//    }
//
//    public void bind(BroadcastModel model) {
//        binding.linkTitle.setText(model.getLinkTitle());
//        binding.linkDescription.setText(model.getLinkDescription());
//
//        if (!TextUtils.isEmpty(model.getLinkImageUrl())) {
//            Glide.with(binding.getRoot().getContext())
//                    .load(model.getLinkImageUrl())
//                    .into(binding.linkImage);
//            binding.linkImage.setVisibility(View.VISIBLE);
//        } else {
//            binding.linkImage.setVisibility(View.GONE);
//        }
//
//        binding.getRoot().setOnClickListener(v -> {
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(model.getLinkUrl()));
//            v.getContext().startActivity(intent);
//        });
//    }
//}
