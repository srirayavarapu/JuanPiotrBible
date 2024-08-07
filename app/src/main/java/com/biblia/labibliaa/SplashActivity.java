package com.biblia.labibliaa;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private final AtomicBoolean isMobileAdsInitializeCalled = new AtomicBoolean(false);
    private final String TAG = SplashActivity.this.getClass().getName();
    private long secondsRemaining;
    private static final long COUNTER_TIME_MILLISECONDS = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_splash);

        App.getInstance().googleMobileAdsConsentManager = GoogleMobileAdsConsentManager.getInstance(getApplicationContext());

        SaveSharedPreferences pref = new SaveSharedPreferences(this);
        if (pref.getState()) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        createTimer(COUNTER_TIME_MILLISECONDS);

        App.getInstance().googleMobileAdsConsentManager.gatherConsent(
                this,
                consentError -> {
                    if (consentError != null) {
                        // Consent not obtained in current session.
                        Log.w(TAG, String.format("%s: %s", consentError.getErrorCode(), consentError.getMessage()));
                    }

                    if (App.getInstance().googleMobileAdsConsentManager.canRequestAds()) {
                        initializeMobileAdsSdk();
                    }
                });

        // This sample attempts to load ads using consent obtained in the previous session.
        if (App.getInstance().googleMobileAdsConsentManager.canRequestAds()) {
            initializeMobileAdsSdk();
        }
    }

    private void initializeMobileAdsSdk() {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return;
        }

        MobileAds.setRequestConfiguration(
                new RequestConfiguration.Builder()
                        .setTestDeviceIds(Arrays.asList(App.getInstance().TEST_DEVICE_HASHED_ID))
                        .build());

        new Thread(
                () -> {
                    // Initialize the Google Mobile Ads SDK on a background thread.
                    MobileAds.initialize(this, initializationStatus -> {
                    });

                    // Load an ad on the main thread.
                    runOnUiThread(
                            () -> {
                                App.getInstance().setLoadInterstitialListener(new App.LoadInterstitialListener() {
                                    @Override
                                    public void onLoadInterstitial() {
                                        startMainActivity();
                                    }

                                    @Override
                                    public void onFailedInterstitial() {
                                        startMainActivity();
                                    }
                                });
                                App.getInstance().loadInterstitial(this);
                            });
                })
                .start();
    }

    private void createTimer(long time) {
        CountDownTimer countDownTimer =
                new CountDownTimer(time, 1000) {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTick(long millisUntilFinished) {
                        secondsRemaining = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1;
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onFinish() {
                        secondsRemaining = 0;
                    }
                };
        countDownTimer.start();
    }

    private void startMainActivity() {
        App.getInstance().showInterstitial(this, true, new App.InterstitialListener() {
            @Override
            public void onCloseInterstitial() {
                App.getInstance().setLoadInterstitialListener(null);
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}