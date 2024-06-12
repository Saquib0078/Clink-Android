package com.visticsolution.posterbanao.ads;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.visticsolution.posterbanao.classes.Constants;
import com.visticsolution.posterbanao.classes.Functions;

public class BannerAdapter {

    public static void showBannerAds(Context context, LinearLayout mAdViewLayout) {
        if (Functions.getSharedPreference(context).getString("show_ads","false").equals("true")
                && Functions.getSharedPreference(context).getString("show_admob_banner","false").equals("true")
                && !Functions.IsPremiumEnable(context)) {
            switch (Constants.AD_NETWORK) {
                case Constants.ADMOB:
                    AdView mAdView = new AdView(context);
                    mAdView.setAdSize(AdSize.BANNER);
                    mAdView.setAdUnitId(Functions.getSharedPreference(context).getString("admob_banner_id","false"));
                    AdRequest.Builder builder = new AdRequest.Builder();
                    mAdView.loadAd(builder.build());
                    mAdViewLayout.removeAllViews();
                    mAdViewLayout.addView(mAdView);
                    mAdViewLayout.setGravity(Gravity.CENTER);
                    break;
            }
        } else {
            mAdViewLayout.setVisibility(View.GONE);
        }
    }
}
