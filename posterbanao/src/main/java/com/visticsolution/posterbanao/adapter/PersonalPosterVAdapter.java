package com.visticsolution.posterbanao.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.binding.BindingAdaptet;
import com.visticsolution.posterbanao.classes.Functions;
import com.visticsolution.posterbanao.classes.Variables;
import com.visticsolution.posterbanao.databinding.ItemPersonalLayoutBinding;
import com.visticsolution.posterbanao.model.PostsModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PersonalPosterVAdapter extends RecyclerView.Adapter<PersonalPosterVAdapter.ViewHolder> {

    Context context;
    List<PostsModel> list = new ArrayList<>();
    int colorCount = 0;

    public interface OnClickEvent {
        void onClick(View view,View posterview,PostsModel postItem);
    }

    OnClickEvent onClickEvent;

    public PersonalPosterVAdapter(Context context, List<PostsModel> list,OnClickEvent onClickEvent) {
        this.context = context;
        this.list = list;
        this.onClickEvent = onClickEvent;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPersonalLayoutBinding binding = ItemPersonalLayoutBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        int pos = position;
        holder.binding.setPosts(list.get(pos));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        CustomPagerAdapter adapter = new CustomPagerAdapter(list.get(pos).getItem_url());

        holder.binding.recyclerview.setLayoutManager(linearLayoutManager);
        holder.binding.recyclerview.setAdapter(adapter);

        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(holder.binding.recyclerview);

        final int random = new Random().nextInt((5 - 1) + 1) + 1;
        holder.binding.recyclerview.scrollToPosition(random);

        holder.binding.recyclerview.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return e.getAction() == MotionEvent.ACTION_MOVE;
            }
            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });

        if (Functions.IsPremiumEnable(context)){
            holder.binding.removeWatermark.setVisibility(View.GONE);
            holder.binding.watermarkLayout.setVisibility(View.GONE);
        }

        holder.binding.removeWatermark.setOnClickListener(view -> {
            onClickEvent.onClick(view,holder.binding.mainLayOut, list.get(pos));
        });
        holder.binding.downloadBtn.setOnClickListener(view -> {
            onClickEvent.onClick(view,holder.binding.mainLayOut, list.get(pos));
        });
        holder.binding.shareBtn.setOnClickListener(view -> {
            onClickEvent.onClick(view,holder.binding.mainLayOut, list.get(pos));
        });
        holder.binding.editBtn.setOnClickListener(view -> {
            onClickEvent.onClick(view,holder.binding.mainLayOut, list.get(pos));
        });
        holder.binding.nextBtn.setOnClickListener(view -> {
            onClickEvent.onClick(view,holder.binding.mainLayOut, list.get(pos));
        });

        holder.binding.red.setOnClickListener(view -> {
            try {
                LinearLayout layout = linearLayoutManager.findViewByPosition(random).findViewById(R.id.bgView);
                changeBgColor(context.getColor(R.color.red_color_picker),layout);
            }catch (Exception e){
                Log.d("FrameColor__",e.getMessage());
                holder.binding.colorsLay.setVisibility(View.GONE);
            }

        });
        holder.binding.blue.setOnClickListener(view -> {
            try {
                LinearLayout layout = linearLayoutManager.findViewByPosition(random).findViewById(R.id.bgView);
                changeBgColor(context.getColor(R.color.blue_color_picker), layout);
            }catch (Exception e){
                Log.d("FrameColor__",e.getMessage());
                holder.binding.colorsLay.setVisibility(View.GONE);
            }

        });
        holder.binding.brown.setOnClickListener(view -> {
            try {
                LinearLayout layout = linearLayoutManager.findViewByPosition(random).findViewById(R.id.bgView);
                changeBgColor(context.getColor(R.color.brown_color_picker), layout);
            }catch (Exception e){
                Log.d("FrameColor__",e.getMessage());
                holder.binding.colorsLay.setVisibility(View.GONE);
            }

        });
        holder.binding.green.setOnClickListener(view -> {
            try {
                LinearLayout layout = linearLayoutManager.findViewByPosition(random).findViewById(R.id.bgView);
                changeBgColor(context.getColor(R.color.green_color_picker), layout);
            }catch (Exception e){
                Log.d("FrameColor__",e.getMessage());
                holder.binding.colorsLay.setVisibility(View.GONE);
            }

        });
        holder.binding.orange.setOnClickListener(view -> {
            try {
                LinearLayout layout = linearLayoutManager.findViewByPosition(random).findViewById(R.id.bgView);
                changeBgColor(context.getColor(R.color.red_orange_color_picker), layout);
            }catch (Exception e){
                Log.d("FrameColor__",e.getMessage());
                holder.binding.colorsLay.setVisibility(View.GONE);
            }
        });
        holder.binding.violet.setOnClickListener(view -> {
            try {
                LinearLayout layout = linearLayoutManager.findViewByPosition(random).findViewById(R.id.bgView);
                changeBgColor(context.getColor(R.color.violet_color_picker), layout);
            }catch (Exception e){
                Log.d("FrameColor__",e.getMessage());
                holder.binding.colorsLay.setVisibility(View.GONE);
            }
        });
    }

    private void changeBgColor(int color, LinearLayout layout) {
        if (layout != null){
            layout.getBackground().setTint(color);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemPersonalLayoutBinding binding;

        public ViewHolder(@NonNull ItemPersonalLayoutBinding itemView) {
            super(itemView.getRoot());

            binding = itemView;
        }
    }

    public class CustomPagerAdapter extends RecyclerView.Adapter<CustomPagerAdapter.ViewHolder> {

        List<Integer> list = new ArrayList<>();
        String item_url;

        public CustomPagerAdapter(String url) {
            item_url = url;
            list.add(R.layout.personal_frame_one);
            list.add(R.layout.personal_frame_one);
            list.add(R.layout.personal_frame_two);
            list.add(R.layout.personal_frame_three);
            list.add(R.layout.personal_frame_four);
        }

        @NonNull
        @Override
        public CustomPagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(context).inflate(list.get(viewType),parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull CustomPagerAdapter.ViewHolder holder, int position) {
            holder.setIsRecyclable(false);

            BindingAdaptet.setImageUrl(holder.iv,Functions.getItemBaseUrl(item_url));
            BindingAdaptet.setImageUrl(holder.imageView,Functions.getSharedPreference(context).getString(Variables.P_PIC,""));
            holder.nameTv.setText(" "+Functions.getSharedPreference(context).getString(Variables.NAME,"")+" ");
            try {
                holder.number_tv.setText(" "+Functions.getSharedPreference(context).getString(Variables.NUMBER,"")+" ");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                holder.designation.setText(" "+Functions.getSharedPreference(context).getString(Variables.U_DESIGNATION,"")+" ");
            } catch (Exception e) {
                e.printStackTrace();
            }

//            colorCount++;
//            if (colorCount == 9){
//                colorCount = 0;
//            }
//            String[] colorsTxt = context.getResources().getStringArray(R.array.cat_colors);
//            holder.frameLay.getBackground().setTint(Color.parseColor(colorsTxt[colorCount]));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView nameTv,number_tv,designation;
            ImageView imageView,iv;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.profile_pic);
                iv = itemView.findViewById(R.id.iv);
                nameTv = itemView.findViewById(R.id.user_name);
                number_tv = itemView.findViewById(R.id.number);
                designation = itemView.findViewById(R.id.designation);
            }
        }
    }
}
