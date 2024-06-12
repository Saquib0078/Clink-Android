package com.visticsolution.posterbanao.binding;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.customview.ShapesImage;

import de.hdodenhof.circleimageview.CircleImageView;

public class BindingAdaptet {

    @BindingAdapter("android:setDate")
    public static void setDate(TextView textView, String date){
        try {
            textView.setText(Functions.getFormatedDate(date));
        }catch (Exception e){
            textView.setVisibility(View.GONE);
        }
    }


    @BindingAdapter("android:imageURL")
    public static void setImageUrl(ImageView imageView,String url){
        try {
            Glide.with(imageView.getContext()).load(Functions.getItemBaseUrl(url)).error(R.drawable.placeholder).placeholder(R.drawable.placeholder).into(imageView);
        }catch (Exception e){}
    }

    @BindingAdapter("android:imageURL")
    public static void setImageUrl(CircleImageView imageView, String url){
        try {
            Glide.with(imageView.getContext()).load(Functions.getItemBaseUrl(url)).placeholder(R.drawable.placeholder).into(imageView);
        }catch (Exception e){}
    }

    @BindingAdapter("android:imageURL")
    public static void setImageUrl(ShapesImage imageView, String url){
        try {
            Glide.with(imageView.getContext()).load(Functions.getItemBaseUrl(url)).placeholder(R.drawable.placeholder).into(imageView);
        }catch (Exception e){}
    }

    @BindingAdapter("android:isVideo")
    public static void isVideo(ImageView imageView,String url){
        try {
            Log.d("isVideo-->",url);
            if (url.endsWith(".mp4")){
                imageView.setVisibility(View.VISIBLE);
            }else {
                imageView.setVisibility(View.GONE);
            }
        }catch (Exception e){
            Log.e("isVideo-->",e.getMessage());
        }
    }

    @BindingAdapter("android:isTamplate")
    public static void isTamplate(TextView imageView,String json){
        try {
            if (json.equals("")){
                imageView.setVisibility(View.GONE);
            }else {
                imageView.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){
            imageView.setVisibility(View.GONE);
        }
    }

    @BindingAdapter("android:isPremium")
    public static void isPremium(RelativeLayout relativeLayout, String pre){
        try {
            Log.d("isPremium-->",pre);
            if (pre.equals("1")){
                relativeLayout.setVisibility(View.VISIBLE);
            }else{
                relativeLayout.setVisibility(View.GONE);
            }
        }catch (Exception e){
            Log.e("isPremium-->",e.getMessage());
        }
    }


    @BindingAdapter("android:isPremium")
    public static void isPremium(ImageView relativeLayout, String pre){
        try {
            if (pre.equals("1")){
                relativeLayout.setVisibility(View.VISIBLE);
            }else{
                relativeLayout.setVisibility(View.GONE);
            }
        }catch (Exception e){
            Log.e("isPremium-->",e.getMessage());
        }
    }
}
