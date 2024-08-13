//package com.nirmiteepublic.clink.adapters;
//
//import android.content.Context;
//import android.net.Uri;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.viewpager.widget.PagerAdapter;
//
//import com.bumptech.glide.Glide;
//import com.nirmiteepublic.clink.R;
//
//import java.util.List;
//import java.util.Objects;
//
//public class MultipleImageViewAdapter extends PagerAdapter {
//
//    // Context object
//    Context context;
//
//    // Array of images
//    List<Uri> images;
//    List<String> imagesStr;
//
//    // Layout Inflater
//    LayoutInflater mLayoutInflater;
//
//
//    // Viewpager Constructor
//    public MultipleImageViewAdapter(Context context, List<Uri> images, List<String> imagesStr) {
//        this.context = context;
//        this.images = images;
//        this.imagesStr = imagesStr;
//        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//    }
//
//    @Override
//    public int getCount() {
//        // return the number of images
//        if (images != null) {
//            return images.size();
//        } else {
//            return imagesStr.size();
//        }
//    }
//
//    @Override
//    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
//        return view == object;
//    }
//
//    @NonNull
//    @Override
//    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
//        // inflating the item.xml
//        View itemView = mLayoutInflater.inflate(R.layout.item_multiple_image_view, container, false);
//
//        // referencing the image view from the item.xml file
////        ImageView imageView = itemView.findViewById(R.id.slider_image_view);
//
//        // setting the image in the imageView
////        imageView.setI(images.get(position));
////        Glide.with(context).load(com.visticsolution.posterbanao.R.drawable.placeholder).into(imageView);
////        Glide.with(context).load("https://pornroleplay.org/uploads/posts/2019-12/xev-bellringer-motivating-my-student-image-1.jpg").into(imageView);
//
//        // Adding the View
//        Objects.requireNonNull(container).addView(itemView);
//
//        return itemView;
//    }
//
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//
//        container.removeView((LinearLayout) object);
//    }
//}
