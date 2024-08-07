package com.biblia.labibliaa;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

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

import com.biblia.labibliaa.Fragments.NTFragment;
import com.biblia.labibliaa.Fragments.OTFragment;
import com.biblia.labibliaa.database.DBHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private SaveSharedPreferences pref;
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


        //BANNER ADS
        App.getInstance().loadInterstitial(MainActivity.this);
        AdView mAdView = findViewById(R.id.adView);
        App.getInstance().showBanner(mAdView);
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
                    App.getInstance().showInterstitial(((Activity) mContext), false, new App.InterstitialListener() {
                        @Override
                        public void onCloseInterstitial() {
                            mContext.startActivity(new Intent(mContext, MainActivity.class));
                            activity.finish();
                        }
                    });
                    dr.closeDrawers();
                } else if (id == R.id.favorite) {
                    App.getInstance().showInterstitial(((Activity) mContext), false, new App.InterstitialListener() {
                        @Override
                        public void onCloseInterstitial() {
                            mContext.startActivity(new Intent(mContext, FavActivity.class));
                            activity.finish();
                        }
                    });
                    dr.closeDrawers();
                } else if (id == R.id.note) {
                    App.getInstance().showInterstitial(((Activity) mContext), false, new App.InterstitialListener() {
                        @Override
                        public void onCloseInterstitial() {
                            mContext.startActivity(new Intent(mContext, NoteActivity.class));
                            activity.finish();
                        }
                    });
                    dr.closeDrawers();
                    //finish();
                } else if (id == R.id.highlighted) {
                    App.getInstance().showInterstitial(((Activity) mContext), false, new App.InterstitialListener() {
                        @Override
                        public void onCloseInterstitial() {
                            mContext.startActivity(new Intent(mContext, HighlightActivity.class));
                            activity.finish();
                        }
                    });
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
        inflater.inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.search_view) {
            App.getInstance().showInterstitial(this, false, new App.InterstitialListener() {
                @Override
                public void onCloseInterstitial() {
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    intent.putExtra("which", 1);
                    startActivity(intent);
                }
            });
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