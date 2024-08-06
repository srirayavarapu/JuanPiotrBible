package com.biblia.labibliaa;

import static com.biblia.labibliaa.MainActivity.OpenNavigationDrawer;

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
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.biblia.labibliaa.Fragments.ATFragment;
import com.biblia.labibliaa.Fragments.AllFragment;
import com.biblia.labibliaa.Fragments.BookFragment;
import com.biblia.labibliaa.Fragments.NTSearchFragment;
import com.biblia.labibliaa.database.DBHelper;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    /*private AdView mAdView;
    private InterstitialAd mInterstitialAd;*/

    DrawerLayout dr;
    ActionBarDrawerToggle actToggle;
    NavigationView nav_view;
    DBHelper mDbHelper;
    SaveSharedPreferences pref;
    private SharedPreferences preferences_ot,preferences_nt;
    boolean isNight = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.purple_700));
        }
        setContentView(R.layout.activity_search);

        if (MainActivity.consentInformation.canRequestAds()) {

            //BANNER ADS
            AdView mAdView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }



        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.Search));
        setSupportActionBar(toolbar);
        @SuppressLint("CutPasteId") DrawerLayout drawer = findViewById(R.id.dr);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mDbHelper = new DBHelper(this);

        dr = findViewById(R.id.dr);
        nav_view = (NavigationView) findViewById(R.id.nav_view);
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

        OpenNavigationDrawer(nav_view, SearchActivity.this, dr, pref);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragments(new BookFragment(), getString(R.string.book));
        viewPagerAdapter.addFragments(new NTSearchFragment(), getString(R.string.nt));
        viewPagerAdapter.addFragments(new ATFragment(), getString(R.string.ot));
        viewPagerAdapter.addFragments(new AllFragment(), getString(R.string.all));

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}