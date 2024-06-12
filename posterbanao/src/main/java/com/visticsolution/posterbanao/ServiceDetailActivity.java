package com.visticsolution.posterbanao;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.visticsolution.posterbanao.binding.BindingAdaptet;
import com.visticsolution.posterbanao.classes.Constants;
import com.visticsolution.posterbanao.databinding.ActivityServiceDetailBinding;
import com.visticsolution.posterbanao.dialog.InquiryFragment;
import com.visticsolution.posterbanao.model.ServicesModel;

public class ServiceDetailActivity extends AppCompatActivity {

    public ServiceDetailActivity() {
    }

    public static ServicesModel model = new ServicesModel();
    ActivityServiceDetailBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityServiceDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.title.setText(model.getTitle());
        binding.offerPrice.setText(getString(R.string.currency)+" "+model.getNew_price());
        binding.priceTv.setText(getString(R.string.currency)+" "+model.getOld_price());
        binding.priceTv.setPaintFlags(binding.priceTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        BindingAdaptet.setImageUrl(binding.expandedImage,model.getThumb_url());
        binding.webView.loadData(model.getDescription(), "text/html; charset=utf-8", "UTF-8");

        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InquiryFragment.model = model;
                new InquiryFragment().show(getSupportFragmentManager(),"");
            }
        });
        binding.webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null && url.startsWith(Constants.BASE_URL)){
                    return false;
                }else{
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                binding.loading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                binding.loading.setVisibility(View.GONE);
            }
        });
    }

}