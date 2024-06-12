package com.visticsolution.posterbanao.editor.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.visticsolution.posterbanao.R;
import com.visticsolution.posterbanao.model.LogosModel;

import java.util.List;

public class LogosAdapter extends RecyclerView.Adapter<LogosAdapter.ViewHolder> {

    private List<LogosModel> list;
    private OnClickEvent listener;
    Context context;
    String name;

    public interface OnClickEvent {
        void onClick(View view, View posterview, LogosModel postItem);
    }

    public LogosAdapter(Context context,String name, List<LogosModel> list, OnClickEvent listener) {
        this.list = list;
        this.name = name;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_logos, parent,false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LogosModel model = list.get(position);
        try {
            if (model.getPremium().equals("1")){
                holder.premium_tag.setVisibility(View.VISIBLE);
            }else{
                holder.premium_tag.setVisibility(View.GONE);
            }
            holder.webView.getSettings().setJavaScriptEnabled(true);
            holder.webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return false;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    holder.progressBar.setVisibility(View.GONE);
                    holder.webView.loadUrl("javascript:changeName('" + name + "')");
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    holder.progressBar.setVisibility(View.VISIBLE);
                }
            });

            holder.webView.loadData(model.getCode(), "text/html", "UTF-8");
            holder.webView.setBackgroundColor(Color.TRANSPARENT);

            holder.webView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                        listener.onClick(view,holder.itemView,model);
                        return true;
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        WebView webView;
        ProgressBar progressBar;
        RelativeLayout premium_tag;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            premium_tag = itemView.findViewById(R.id.premium_tag);
            webView = itemView.findViewById(R.id.logoWebView);
            progressBar = itemView.findViewById(R.id.progress);

        }
    }

}