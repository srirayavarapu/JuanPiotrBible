package com.biblia.labibliaa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.biblia.labibliaa.Fragments.NTFragment;
import com.biblia.labibliaa.Fragments.OTFragment;
import com.biblia.labibliaa.database.DBHelper;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.FormError;
import com.google.android.ump.UserMessagingPlatform;

public class MainActivity extends AppCompatActivity {

    public static ConsentInformation consentInformation;
    // Use an atomic boolean to initialize the Google Mobile Ads SDK and load ads once.
    private final AtomicBoolean isMobileAdsInitializeCalled = new AtomicBoolean(false);
    private SaveSharedPreferences pref;
    boolean isNight = true;

    //ADS
    private static final String TAG = "MainActivity";
    private int retryAttempt=1;
    public static InterstitialAd mInterstitialAd;
    boolean czyWykonano = false;

    public void showAd() {
        //SHOW FULL ADS
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        } else {
            Log.d(TAG, "The interstitial ad wasn't ready yet.");
//            loadAd();
        }
    }

    public void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                this,
                getString(R.string.admob_interstitial_id),
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        MainActivity.this.mInterstitialAd = interstitialAd;
//                        mInterstitialAd = interstitialAd;
                        if (!czyWykonano) {
                            czyWykonano = true;
                            showAd();
                        }

                        Log.i(TAG, "onAdLoaded");

                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        mInterstitialAd = null;
                                        loadAd();
                                        Log.d(TAG, "The ad was dismissed.");
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        mInterstitialAd = null;
                                        Log.d(TAG, "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        Log.d(TAG, "The ad was shown.");
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;
                        loadAd();

                        @SuppressLint("DefaultLocale") String error =
                                String.format("domain: %s, code: %d, message: %s", loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());
                        Log.i(TAG, error);
                    }
                });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.purple_700));
        }
        setContentView(R.layout.activity_main);






        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.dr);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        DrawerLayout dr = findViewById(R.id.dr);
        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        View headerView = nav_view.getHeaderView(0);
        ImageButton btnNightLight = headerView.findViewById(R.id.btnNightLight);
        pref = new SaveSharedPreferences(this);
        if (pref.getState()) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            btnNightLight.setImageResource(R.drawable.ic_mode_night);
        } else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            btnNightLight.setImageResource(R.drawable.ic_light_mode);
        }
        if (pref.getState()) {
            btnNightLight.setImageResource(R.drawable.ic_mode_night);
        }
        btnNightLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNight){
                    isNight = false;
                    pref.setState(true);
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    btnNightLight.setImageResource(R.drawable.ic_mode_night);
                }else {
                    isNight = true;
                    pref.setState(false);
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    btnNightLight.setImageResource(R.drawable.ic_light_mode);
                }
            }
        });

        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout otNtTab = findViewById(R.id.otNtTab);

        DBHelper mDbHelper = new DBHelper(this);
        //check file exists
        File database = getApplicationContext().getDatabasePath(DBHelper.DBNAME);
        if (!database.exists()) {
            mDbHelper.getReadableDatabase();
            //copy db
            if (copyDatabase(this)) {
                Log.d("Database", "Database copied");
            } else {
                Log.d("Database", "Not copied");
                return;
            }
        }
        mDbHelper.getWritableDatabase();

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new OTFragment(), getResources().getString(R.string.OT));
        viewPagerAdapter.addFragments(new NTFragment(), getResources().getString(R.string.NT));
        viewPager.setAdapter(viewPagerAdapter);
        otNtTab.setupWithViewPager(viewPager);

        OpenNavigationDrawer(nav_view, MainActivity.this, dr, pref);


        // Set tag for under age of consent. false means users are not under age
        // of consent.
        ConsentRequestParameters params = new ConsentRequestParameters
                .Builder()
                .setTagForUnderAgeOfConsent(false)
                .build();

        consentInformation = UserMessagingPlatform.getConsentInformation(this);
        consentInformation.requestConsentInfoUpdate(
                this,
                params,
                (ConsentInformation.OnConsentInfoUpdateSuccessListener) () -> {
                    // TODO: Load and show the consent form.
                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                            this,
                            (ConsentForm.OnConsentFormDismissedListener) loadAndShowError -> {
                                if (loadAndShowError != null) {
                                    // Consent gathering failed.
                                    Log.w(TAG, String.format("%s: %s",
                                            loadAndShowError.getErrorCode(),
                                            loadAndShowError.getMessage()));
                                }

                                // Consent has been gathered.
                                if (consentInformation.canRequestAds()) {
                                    initializeMobileAdsSdk();
                                }
                            }
                    );
                },
                (ConsentInformation.OnConsentInfoUpdateFailureListener) requestConsentError -> {
                    // Consent gathering failed.
                    Log.w(TAG, String.format("%s: %s",
                            requestConsentError.getErrorCode(),
                            requestConsentError.getMessage()));
                });

        // Check if you can initialize the Google Mobile Ads SDK in parallel
        // while checking for new consent information. Consent obtained in
        // the previous session can be used to request ads.
        if (consentInformation.canRequestAds()) {
            initializeMobileAdsSdk();
        }


    }

    private void initializeMobileAdsSdk() {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return;
        }

        // Initialize the Google Mobile Ads SDK.
        MobileAds.initialize(this);

        // TODO: Request an ad.
        // InterstitialAd.load(...);
        loadAd();

        //BANNER ADS
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }





    static class ViewPagerAdapter extends FragmentPagerAdapter {

        private final ArrayList<Fragment> fragments;
        private final ArrayList<String> titles;

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragments(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    private boolean copyDatabase(Context context) {
        try {
            InputStream inputStream = context.getAssets().open(DBHelper.DBNAME);
            String outFileName = DBHelper.DBLOCATION + DBHelper.DBNAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            Log.w("HomeActivity", "DB copied");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void OpenNavigationDrawer(NavigationView nav_view, Context mContext, DrawerLayout dr, final SaveSharedPreferences pref) {
        Activity activity = (Activity) mContext;
        nav_view.setItemIconTintList(null);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.home) {
                    mContext.startActivity(new Intent(mContext, MainActivity.class));
                    activity.finish();
                    //adsHandler1.showInterstitial();

                    dr.closeDrawers();
                } else if (id == R.id.favorite) {
                    mContext.startActivity(new Intent(mContext, FavActivity.class));
                    activity.finish();
                    //adsHandler1.showInterstitial();
                    dr.closeDrawers();

                    //finish();
                } else if (id == R.id.note) {
                    mContext.startActivity(new Intent(mContext, NoteActivity.class));
                    activity.finish();
                    //adsHandler1.showInterstitial();
                    dr.closeDrawers();
                    //finish();
                } else if (id == R.id.highlighted) {
                    mContext.startActivity(new Intent(mContext, HighlightActivity.class));
                    activity.finish();
                    //adsHandler1.showInterstitial();
                    dr.closeDrawers();
                    //finish();
                } else if (id == R.id.share) {
                    shareApp(mContext);
                } else if (id == R.id.rate) {
                    rateUs(mContext);
                } else if (id == R.id.moreApps) {
                    moreApps(mContext);
                } else if (id == R.id.myMusic) {
                    myMusic(mContext);
                }
                return true;
            }
        });
    }

    public static void myMusic(Context mContext) {
        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mContext.getString(R.string.my_music_url))));
    }

    public static void moreApps(Context mContext) {
        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mContext.getString(R.string.more_apps_url))));
    }

    public static void rateUs(Context mContext) {
        try {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + mContext.getPackageName())));
        } catch (Exception e) {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mContext.getString(R.string.google_play_app_url) + mContext.getPackageName())));
        }
    }

    public static void shareApp(Context mContext) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, mContext.getString(R.string.app_name));
            String shareMessage = mContext.getString(R.string.install) + mContext.getString(R.string.app_name) + mContext.getString(R.string.from_playstore);
            shareMessage = shareMessage + mContext.getString(R.string.google_play_app_url) + mContext.getPackageName() + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            mContext.startActivity(Intent.createChooser(shareIntent, mContext.getString(R.string.select_option)));
        } catch (Exception e) {
            //e.toString();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.search_view) {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            intent.putExtra("which", 1);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private boolean isOnPause = false;

    @Override
    protected void onPause() {
        super.onPause();
        isOnPause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isOnPause = false;

    }

}