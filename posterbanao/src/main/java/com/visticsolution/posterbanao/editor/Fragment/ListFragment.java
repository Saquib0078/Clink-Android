package com.visticsolution.posterbanao.editor.Fragment;


import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.editor.EditorActivity;
import com.visticsolution.posterbanao.editor.adapter.LockLayerItemAdapter;
import com.woxthebox.draglistview.DragListView;

import java.util.ArrayList;


public class ListFragment extends Fragment {
    public static View hintView;
    public static RelativeLayout layNotext;
    public ArrayList<Pair<Long, View>> mItemArray = new ArrayList<>();
    private ArrayList<View> arrView = new ArrayList<>();
    private DragListView mDragListView;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.list_layout, viewGroup, false);

        this.mDragListView = inflate.findViewById(R.id.drag_list_view);
        this.mDragListView.getRecyclerView().setVerticalScrollBarEnabled(true);
        this.mDragListView.setDragListListener(new DragListView.DragListListenerAdapter() {
            @Override
            public void onItemDragStarted(int i) {
            }

            @Override
            public void onItemDragEnded(int i, int i2) {
                if (i != i2) {
                    for (int size = ListFragment.this.mItemArray.size() - 1; size >= 0; size--) {
                        ((ListFragment.this.mItemArray.get(size)).second).bringToFront();
                    }
                    EditorActivity.txtStkrRel.requestLayout();
                    EditorActivity.txtStkrRel.postInvalidate();
                }
            }
        });

//        ((TextView) inflate.findViewById(R.id.txt_Nolayers)).setTypeface(Constants.getTextTypeface(getActivity()));

        layNotext = inflate.findViewById(R.id.lay_text);
        hintView = inflate.findViewById(R.id.HintView);
        (inflate.findViewById(R.id.lay_frame)).setOnClickListener(view -> {

            if (EditorActivity.layContainer.getVisibility() == View.VISIBLE) {
                EditorActivity.layContainer.animate().translationX((float) (-EditorActivity.layContainer.getRight())).setDuration(200).setInterpolator(new AccelerateInterpolator()).start();
                new Handler().postDelayed(() -> {
                    EditorActivity.layContainer.setVisibility(View.GONE);
                    EditorActivity.btnLayControls.setVisibility(View.VISIBLE);
                }, 200);
            }
        });
        return inflate;
    }

    public void getLayoutChild() {
        this.arrView.clear();
        this.mItemArray.clear();
        if (EditorActivity.txtStkrRel.getChildCount() != 0) {
            layNotext.setVisibility(View.GONE);
            for (int childCount = EditorActivity.txtStkrRel.getChildCount() - 1; childCount >= 0; childCount--) {
                this.mItemArray.add(new Pair((long) childCount, EditorActivity.txtStkrRel.getChildAt(childCount)));
                this.arrView.add(EditorActivity.txtStkrRel.getChildAt(childCount));
            }
        } else {
            layNotext.setVisibility(View.VISIBLE);
        }
        setupListRecyclerView();
    }

    private void setupListRecyclerView() {
        this.mDragListView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mDragListView.setAdapter(new LockLayerItemAdapter(getActivity(), this.mItemArray, R.layout.list_item, R.id.drag_img, false), true);
        this.mDragListView.setCanDragHorizontally(false);
    }


}
