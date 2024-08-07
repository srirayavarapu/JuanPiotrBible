package com.biblia.labibliaa;

//import static com.biblia.labibliaa.MainActivity.OpenNavigationDrawer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.biblia.labibliaa.adapter.HighlightAdapter;
import com.biblia.labibliaa.database.DBHelper;
import com.biblia.labibliaa.model.Highlight;
import com.google.android.gms.ads.AdView;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class HighlightActivity extends AppCompatActivity {

    /*private AdView mAdView;
    private InterstitialAd mInterstitialAd;*/

    private SaveSharedPreferences pref;
    private TextView tvTheme;

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
        setContentView(R.layout.activity_highlight);

        //BANNER ADS
        AdView mAdView = findViewById(R.id.adView);
        App.getInstance().showBanner(mAdView);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.Highlight));
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
                if (isNight) {
                    isNight = false;
                    pref.setState(true);
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    btnNightLight.setImageResource(R.drawable.ic_mode_night);
                } else {
                    isNight = true;
                    pref.setState(false);
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    btnNightLight.setImageResource(R.drawable.ic_light_mode);
                }
            }
        });

        ListView highlight_list = (ListView) findViewById(R.id.highlight_list);
        DBHelper mDbHelper = new DBHelper(this);
        //Get fav name list when db exists
        List<Highlight> mHighlightList = mDbHelper.getHighlightList();
        Log.d("HS", mHighlightList.toString());
        TextView tvNoHighlight = findViewById(R.id.tvNoHighlight);
        if (mHighlightList.isEmpty()) {
            tvNoHighlight.setVisibility(View.VISIBLE);
            highlight_list.setVisibility(View.GONE);
        } else {
            tvNoHighlight.setVisibility(View.GONE);
            highlight_list.setVisibility(View.VISIBLE);
            //init Adapter
            /*,adsHandler*/
            HighlightAdapter adapter = new HighlightAdapter(this, mHighlightList/*,adsHandler*/);
            //set adapter for listview
            highlight_list.setAdapter(adapter);

            //EnableDayNightTheme(this,pref,tvTheme,switch_day_night);
            OpenNavigationDrawer(nav_view, HighlightActivity.this, dr, pref);
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}