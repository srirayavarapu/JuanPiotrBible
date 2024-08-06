package com.biblia.labibliaa;

import android.annotation.SuppressLint;
import android.app.Application;

import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.onesignal.Continue;
import com.onesignal.OneSignal;
import com.onesignal.debug.LogLevel;

public class App extends Application {
    private static final String ONESIGNAL_APP_ID = "9680928b-542e-4f82-ab37-c330507c0084";
    private static App instance;

    private static final String TAG = "Application App";
    private int retryAttempt;
    private InterstitialAd mInterstitialAd;

    public static App getInstance() {
        return instance;
    }

    @SuppressLint("StaticFieldLeak")
    //private static AppOpenManager appOpenManager;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // Make sure to set the mediation provider value to "max" to ensure proper functionality
//        AppLovinSdk.getInstance( this ).setMediationProvider( "max" );
//        AppLovinSdk.initializeSdk( this, new AppLovinSdk.SdkInitializationListener() {
//            @Override
//            public void onSdkInitialized(final AppLovinSdkConfiguration config)
//            {
//                // AppLovin SDK is initialized, start loading ads
//            }
//        } );


//        OneSignal.startInit(this)
//                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
//                .unsubscribeWhenNotificationsAreDisabled(true)
//                .setNotificationOpenedHandler(new OpenHandler())
//                .init();
//        String UUID = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();

        // Enable verbose OneSignal logging to debug issues if needed.
        /*OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);*/


//        MobileAds.initialize(
//                this,
//                new OnInitializationCompleteListener() {
//                    @Override
//                    public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
//                        Log.e("AppOpenManager","true");
//                    }
//                });

        //TEST ADS
//        List<String> deviceIds = new ArrayList<String>();
//        deviceIds.add("380750B62499D8D682916C01DF43885F");
//        RequestConfiguration requestConfiguration = new RequestConfiguration
//                .Builder()
//                .setTestDeviceIds(deviceIds)
//                .build();
//        MobileAds.setRequestConfiguration(requestConfiguration);

//        loadAd();

        OneSignal.getDebug().setLogLevel(LogLevel.VERBOSE);
        OneSignal.initWithContext(this, ONESIGNAL_APP_ID);
        OneSignal.getNotifications().requestPermission(false, Continue.none());

    }

//    public void loadAd() {
//        //RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("380750B62499D8D682916C01DF43885F"))
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
//                        mInterstitialAd = interstitialAd;
//                        Log.i(TAG, "onAdLoaded");
//
//                        interstitialAd.setFullScreenContentCallback(
//                                new FullScreenContentCallback() {
//                                    @Override
//                                    public void onAdDismissedFullScreenContent() {
//                                        // Called when fullscreen content is dismissed.
//                                        // Make sure to set your reference to null so you don't
//                                        // show it a second time.
//                                        //mInterstitialAd = null;
//                                        loadAd();
//                                        Log.d(TAG, "The ad was dismissed.");
//                                    }
//
//                                    @Override
//                                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
//                                        // Called when fullscreen content failed to show.
//                                        // Make sure to set your reference to null so you don't
//                                        // show it a second time.
//                                        //mInterstitialAd = null;
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
//                        //mInterstitialAd = null;
//
//                        loadAd();
//
////                        retryAttempt++;
////                        long delayMillis = TimeUnit.SECONDS.toMillis( (long) Math.pow( 2, Math.min( 6, retryAttempt ) ) );
////                        new Handler().postDelayed(new Runnable()
////                        {
////                            //load ads
////                            @Override
////                            public void run()
////                            {
////                                loadAd();
////                            }
////
////                        }, delayMillis );
//
//                        @SuppressLint("DefaultLocale") String error =
//                                String.format("domain: %s, code: %d, message: %s", loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());
//                        Log.i(TAG, error);
//                    }
//                });
//    }


}
