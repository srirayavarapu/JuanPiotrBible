package com.biblia.labibliaa;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.onesignal.Continue;
import com.onesignal.OneSignal;
import com.onesignal.debug.LogLevel;

public class App extends Application {
    private static final String ONESIGNAL_APP_ID = "9680928b-542e-4f82-ab37-c330507c0084";
    private static App instance;
    public final String TEST_DEVICE_HASHED_ID = "ABCDEF012345";

    public static App getInstance() {
        return instance;
    }

    private InterstitialAd mInterstitialAd;
    public GoogleMobileAdsConsentManager googleMobileAdsConsentManager;
    InterstitialListener interstitialListener;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        OneSignal.getDebug().setLogLevel(LogLevel.VERBOSE);
        OneSignal.initWithContext(this, ONESIGNAL_APP_ID);
        OneSignal.getNotifications().requestPermission(false, Continue.none());

    }

    public interface InterstitialListener {
        void onCloseInterstitial();
    }

    public interface LoadInterstitialListener {
        void onLoadInterstitial();

        void onFailedInterstitial();
    }

    LoadInterstitialListener loadInterstitialListener;

    public void setLoadInterstitialListener(LoadInterstitialListener loadInterstitialListener) {
        this.loadInterstitialListener = loadInterstitialListener;
    }

    public void loadInterstitial(Activity activity) {
        if (googleMobileAdsConsentManager.canRequestAds()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(activity, activity.getString(R.string.admob_interstitial_id), adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            mInterstitialAd = interstitialAd;

                            if (loadInterstitialListener != null)
                                loadInterstitialListener.onLoadInterstitial();

                            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdClicked() {

                                }

                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    mInterstitialAd = null;

                                    if (interstitialListener != null)
                                        interstitialListener.onCloseInterstitial();

                                    if (loadInterstitialListener == null)
                                        loadInterstitial(activity);
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(AdError adError) {
                                    mInterstitialAd = null;
                                }

                                @Override
                                public void onAdImpression() {

                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                    // Called when ad is shown.
                                }
                            });
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            mInterstitialAd = null;
                            if (loadInterstitialListener != null)
                                loadInterstitialListener.onFailedInterstitial();
                        }
                    });
        }
    }

    public int count, adCount = 3;

    public void showInterstitial(Activity activity, boolean isOne, InterstitialListener interstitialListener) {
        this.interstitialListener = interstitialListener;
        if (isOne) {
            if (mInterstitialAd != null) {
                mInterstitialAd.show(activity);
            } else {
                if (this.interstitialListener != null)
                    this.interstitialListener.onCloseInterstitial();

                if (loadInterstitialListener == null)
                    loadInterstitial(activity);
            }
        } else {
            count++;
            if (count == adCount) {
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(activity);
                } else {
                    if (this.interstitialListener != null)
                        this.interstitialListener.onCloseInterstitial();

                    if (loadInterstitialListener == null)
                        loadInterstitial(activity);
                }
                count = 0;
            } else {
                if (this.interstitialListener != null)
                    this.interstitialListener.onCloseInterstitial();
            }
        }
    }

    public void showBanner(AdView adView) {
        if (googleMobileAdsConsentManager.canRequestAds()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }
    }
}
