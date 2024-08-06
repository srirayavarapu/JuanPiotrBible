package com.biblia.labibliaa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

//import com.google.android.gms.ads.AdError;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.FullScreenContentCallback;
//import com.google.android.gms.ads.LoadAdError;
//import com.google.android.gms.ads.interstitial.InterstitialAd;
//import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.concurrent.TimeUnit;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    //ADS
    private static final String TAG = "SPLASH ACTIVITY";
    private int retryAttempt=1;
//    public InterstitialAd mInterstitialAd;

//    public void showAd() {
//        //SHOW FULL ADS
//        if (mInterstitialAd != null) {
//            mInterstitialAd.show(this);
//        } else {
//            Log.d(TAG, "The interstitial ad wasn't ready yet.");
////            loadAd();
//        }
//    }

//    public void loadAd() {
//        AdRequest adRequest = new AdRequest.Builder().build();
//        InterstitialAd.load(
//                this,
//                getString(R.string.admob_interstitial_id),
//                adRequest,
//                new InterstitialAdLoadCallback() {
//                    @Override
//                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
//                        // The mInterstitialAd reference will be null until
//                        // an ad is loaded.
//                        SplashActivity.this.mInterstitialAd = interstitialAd;
//                        if (retryAttempt >= 10) {
//                            //showAd();
//                            retryAttempt++;
//                        }
//
//                        Log.i(TAG, "onAdLoaded");
//
//                        interstitialAd.setFullScreenContentCallback(
//                                new FullScreenContentCallback() {
//                                    @Override
//                                    public void onAdDismissedFullScreenContent() {
//                                        // Called when fullscreen content is dismissed.
//                                        // Make sure to set your reference to null so you don't
//                                        // show it a second time.
//                                        mInterstitialAd = null;
//                                        loadAd();
//                                        Log.d(TAG, "The ad was dismissed.");
//                                    }
//
//                                    @Override
//                                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
//                                        // Called when fullscreen content failed to show.
//                                        // Make sure to set your reference to null so you don't
//                                        // show it a second time.
//                                        mInterstitialAd = null;
//                                        Log.d(TAG, "The ad failed to show.");
//                                    }
//
//                                    @Override
//                                    public void onAdShowedFullScreenContent() {
//                                        // Called when fullscreen content is shown.
//                                        Log.d(TAG, "The ad was shown.");
//                                    }
//                                });
//                    }
//
//                    @Override
//                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                        // Handle the error
//                        Log.i(TAG, loadAdError.getMessage());
//                        mInterstitialAd = null;
//                        loadAd();
//
//                        @SuppressLint("DefaultLocale") String error =
//                                String.format("domain: %s, code: %d, message: %s", loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());
//                        Log.i(TAG, error);
//                    }
//                });
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_splash);

        //LOAD ADS
//        loadAd();
        //SHOW FULL ADS


        SaveSharedPreferences pref = new SaveSharedPreferences(this);
        if (pref.getState()) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

//                showAd();

                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}