package com.visticsolution.posterbanao.editor.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.visticsolution.posterbanao.MainActivity;
import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.editor.listener.OnClickCallback;
import com.visticsolution.posterbanao.editor.utils.Configure;

import java.io.File;
import java.util.ArrayList;

public class FontAdapter extends BaseAdapter {
    private static final String TAG = "FontAdapter";

    final String[] imageid;
    public OnClickCallback<ArrayList<String>, Integer, String, Activity> mSingleCallback;
    int arraySize;
    boolean isDownloadProgress = true;
    Activity mContext;
    int selPos = -1;

    public FontAdapter(Activity activity, String[] strArr) {
        this.mContext = activity;
        this.imageid = strArr;

        this.arraySize = strArr.length;
    }

    public long getItemId(int i) {
        return 0;
    }

    public int getCount() {
        return this.arraySize;
    }

    public Object getItem(int i) {
        return 0;
    }

    public void setItemClickCallback(OnClickCallback onClickCallback) {
        this.mSingleCallback = onClickCallback;
    }

    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            view = ((LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.grid_assets_main, null);
            viewHolder = new ViewHolder();
            viewHolder.layItem = view.findViewById(R.id.layItem);
            viewHolder.txtView = view.findViewById(R.id.grid_text);
            viewHolder.txtDownloadFont = view.findViewById(R.id.txtDownloadFont);
            viewHolder.downloadProgress = view.findViewById(R.id.downloadProgress);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.downloadProgress.setVisibility(View.INVISIBLE);
        String[] strArr = this.imageid;
        if (i < strArr.length) {
            viewHolder.txtDownloadFont.setVisibility(View.GONE);
            if (i == 0) {
                viewHolder.txtView.setTypeface(Typeface.DEFAULT);
            } else {
                MimeTypeMap.getFileExtensionFromUrl(this.imageid[i]);
                try {
                    TextView textView = viewHolder.txtView;
                    AssetManager assets = this.mContext.getAssets();
                    textView.setTypeface(Typeface.createFromAsset(assets, "font/" + this.imageid[i]));
                } catch (Exception e) {
                    Log.e(TAG, "getView: font not found");
                }
            }
        } else {
            int length = i - strArr.length;

            StringBuilder sb = new StringBuilder();

            sb.append(new File(Configure.GetFileDir(this.mContext).getPath() + File.separator + "font/").getPath());

            sb.append("/");

            sb.append(MainActivity.fontArraylist.get(0).getFonts().get(length));

            File file = new File(sb.toString());

            TextView textView2 = viewHolder.txtView;

            textView2.setText(MainActivity.fontArraylist.get(0).getFonts().get(length) + "");

            if (file.exists()) {
                viewHolder.txtDownloadFont.setVisibility(View.GONE);
                try {
                    viewHolder.txtView.setTypeface(Typeface.createFromFile(file));
                } catch (RuntimeException e2) {
                    Log.e(TAG, "getView: RuntimeException font not found");
                    viewHolder.txtView.setTypeface(Typeface.DEFAULT);
                }
            } else {
                viewHolder.txtDownloadFont.setVisibility(View.VISIBLE);
            }
        }

        viewHolder.txtView.setTextColor(this.mContext.getResources().getColor(R.color.textColor));

        int i2 = this.selPos;

        if (i2 >= 0 && i == i2) {

            viewHolder.txtView.setTextColor(this.mContext.getResources().getColor(R.color.app_color));
        }

        viewHolder.txtView.setOnClickListener(view12 -> {
            if (i < FontAdapter.this.imageid.length) {
                FontAdapter.this.mSingleCallback.onClickCallBack(null, i, FontAdapter.this.imageid[i], FontAdapter.this.mContext);
                return;
            }
            int length = i - FontAdapter.this.imageid.length;
            if (new File(new File(Configure.GetFileDir(FontAdapter.this.mContext).getPath() + File.separator + "font/").getPath() + "/" + MainActivity.fontArraylist.get(0).getFonts().get(length)).exists()) {
                FontAdapter.this.mSingleCallback.onClickCallBack(null, i, MainActivity.fontArraylist.get(0).getFonts().get(length), FontAdapter.this.mContext);
            }
        });
        return view;
    }

    public void downoloadsticker(String str, String str2, String str3) {
        AndroidNetworking.download(str, str2, str3).build().startDownload(new DownloadListener() {
            public void onDownloadComplete() {
                FontAdapter.this.isDownloadProgress = true;
                FontAdapter.this.notifyDataSetChanged();
                Log.e(FontAdapter.TAG, "onDownloadComplete: ");
            }

            public void onError(ANError aNError) {
                FontAdapter.this.isDownloadProgress = true;
                Log.e(FontAdapter.TAG, "onError: ");
                Toast.makeText(FontAdapter.this.mContext, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setSelected(int i) {
        this.selPos = i;
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        ProgressBar downloadProgress;
        RelativeLayout layItem;
        ImageView txtDownloadFont;
        TextView txtView;

        public ViewHolder() {
        }
    }
}
