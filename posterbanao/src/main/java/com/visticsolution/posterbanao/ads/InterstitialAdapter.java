package com.visticsolution.posterbanao.ads;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.visticsolution.posterbanao.classes.Constants;
import com.visticsolution.posterbanao.classes.Functions;

public class InterstitialAdapter {

    private static InterstitialAd mInterstitialAd;
    private static Activity mcontext;
    private static Listener listener;

    public InterstitialAdapter(Activity context) {
        mcontext = context;
    }

    public static void Interstitial(Activity context, Listener mlistener) {
        mcontext = context;
        listener = mlistener;
    }

    public static void LoadAds() {
        if (Functions.getSharedPreference(mcontext).getString("show_ads","false").equals("true")
                && Functions.getSharedPreference(mcontext).getString("show_admob_interstital","false").equals("true")
                && !Functions.IsPremiumEnable(mcontext)) {
            switch (Constants.AD_NETWORK) {
                case Constants.ADMOB:
                    AdRequest.Builder builder = new AdRequest.Builder();

                    InterstitialAd.load(mcontext, Functions.getSharedPreference(mcontext).getString("admob_interstitial_ad","false"), builder.build(),
                            new InterstitialAdLoadCallback() {
                                @Override
                                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                    // The mInterstitialAd reference will be null until
                                    // an ad is loaded.
                                    mInterstitialAd = interstitialAd;
                                    listener.onAdLoaded();
                                }

                                @Override
                                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                    // Handle the error
                                    mInterstitialAd = null;
                                    listener.onAdFailedToLoad();
                                }
                            });
                    break;
            }
        } else {
            mInterstitialAd = null;
        }
    }

    public static boolean isLoaded() {
        return mInterstitialAd == null ? false : true;
    }

    public static void showAds() {
        if (mInterstitialAd != null) {
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdClicked() {
                    // Called when a click is recorded for an ad.
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    // Set the ad reference to null so you don't show the ad a second time.
                    mInterstitialAd = null;
                    listener.onAdDismissed();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    // Called when ad fails to show.
                    mInterstitialAd = null;
                    listener.onAdFailedToLoad();
                }

                @Override
                public void onAdImpression() {
                    // Called when an impression is recorded for an ad.
                }
                @Override
                public void onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                }
            });
            if (mInterstitialAd != null) {
                mInterstitialAd.show(mcontext);
            }
        }
    }

    public interface Listener {

        void onAdLoaded();

        void onAdFailedToLoad();

        void onAdDismissed();

    }
}
