package com.visticsolution.posterbanao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.asksira.loopingviewpager.LoopingPagerAdapter;
import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.binding.BindingAdaptet;
import com.visticsolution.posterbanao.interfaces.AdapterClickListener;
import com.visticsolution.posterbanao.model.SliderModel;

import java.util.List;

public class SliderPagerAdapter extends LoopingPagerAdapter {


    private List<SliderModel> list;
    private AdapterClickListener listener;
    Context context;

    public SliderPagerAdapter(Context context, List<SliderModel> List, AdapterClickListener listener) {
        super(List, true);
        this.context = context;
        this.list = List;
        this.listener = listener;
    }

    @Override
    protected void bindView(@NonNull View view, int i, int i1) {

        if(list!=null && list.size()>0){
            ImageView image;

            image = view.findViewById(R.id.image);

            BindingAdaptet.setImageUrl(image, list.get(i).getImage());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v,i,list.get(i));
                }
            });

            view.setTag("" + i);
        }

    }

    @Override
    protected View inflateView(int i, @NonNull ViewGroup viewGroup, int i1) {
        return LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_slider_layout, viewGroup, false);
    }


}
