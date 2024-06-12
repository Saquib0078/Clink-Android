package com.visticsolution.posterbanao.ads;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.visticsolution.posterbanao.classes.Constants;
import com.visticsolution.posterbanao.classes.Functions;

public class RewardedAdapter {

    private static RewardedAd AdMobrewardedVideoAd;
    private static Activity mcontext;
    private static Listener listener;
    private static boolean loading;

    public RewardedAdapter(Activity context, Listener mlistener) {
        mcontext = context;
        listener = mlistener;
    }

    public static void LoadAds() {
        if (loading){
            return;
        }
        if (Functions.getSharedPreference(mcontext).getString("show_ads","false").equals("true")
                && Functions.getSharedPreference(mcontext).getString("show_admob_rewarded","false").equals("true")
                && !Functions.IsPremiumEnable(mcontext)) {
            switch (Constants.AD_NETWORK) {
                case Constants.ADMOB:
                    loading = true;
                    AdRequest.Builder builder = new AdRequest.Builder();
                    AdMobrewardedVideoAd.load(mcontext, Functions.getSharedPreference(mcontext).getString("admob_rewarde_id","false"), builder.build(),
                            new RewardedAdLoadCallback() {
                                @Override
                                public void onAdLoaded(@NonNull RewardedAd interstitialAd) {
                                    loading = false;
                                    AdMobrewardedVideoAd = interstitialAd;
                                    listener.onAdLoaded();
                                }

                                @Override
                                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                    loading = false;
                                    AdMobrewardedVideoAd = null;
                                    listener.onAdFailedToLoad();
                                }
                            });
                    break;
            }
        } else {
            AdMobrewardedVideoAd = null;
        }
    }

    public static boolean isLoaded() {
        return AdMobrewardedVideoAd == null ? false : true;
    }

    public static void showAds() {
        if (AdMobrewardedVideoAd != null) {
            AdMobrewardedVideoAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdClicked() {
                    // Called when a click is recorded for an ad.
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    // Set the ad reference to null so you don't show the ad a second time.
                    AdMobrewardedVideoAd = null;
                    listener.onAdDismissed();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    // Called when ad fails to show.
                    AdMobrewardedVideoAd = null;
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
            if (AdMobrewardedVideoAd != null) {
                AdMobrewardedVideoAd.show(mcontext, new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        listener.onAdReward();
                    }
                });
            }
        }
    }

    public interface Listener {

        void onAdLoaded();

        void onAdReward();

        void onAdFailedToLoad();

        void onAdDismissed();

    }
}
