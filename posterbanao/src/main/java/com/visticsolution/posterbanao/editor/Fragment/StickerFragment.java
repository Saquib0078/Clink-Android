package com.visticsolution.posterbanao.editor.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.editor.adapter.StickerPagerAdapter;
import com.visticsolution.posterbanao.editor.adapter.StickersAdapter;
import com.visticsolution.posterbanao.model.StickerModel;

import java.util.ArrayList;
import java.util.List;

public class StickerFragment extends Fragment {


    RecyclerView recyclerView;

    private List<StickerModel> stickerList = new ArrayList<>();
    StickerPagerAdapter.OnStickerSelect stickerListener;

    public StickerFragment(List<StickerModel> stickerList, StickerPagerAdapter.OnStickerSelect stickerListener) {
        this.stickerList = stickerList;
        this.stickerListener = stickerListener;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_sticker, viewGroup, false);
        this.recyclerView = inflate.findViewById(R.id.overlay_artwork);
        this.recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));
        recyclerView.setAdapter(new StickersAdapter(getContext(), stickerList, new StickerPagerAdapter.OnStickerSelect() {
            @Override
            public void sticker(String path) {
                stickerListener.sticker(path);
            }
        }));

        if (!(stickerList.size() > 0)){
            inflate.findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);
        }else {
            inflate.findViewById(R.id.no_data_layout).setVisibility(View.GONE);
        }
        return inflate;
    }


}
