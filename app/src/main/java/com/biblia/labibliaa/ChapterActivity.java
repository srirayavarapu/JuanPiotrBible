package com.biblia.labibliaa;

import static com.biblia.labibliaa.MainActivity.OpenNavigationDrawer;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageButton;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.biblia.labibliaa.adapter.ChapterAdapter;
import com.biblia.labibliaa.database.DBHelper;
import com.google.android.gms.ads.AdView;
import com.google.android.material.navigation.NavigationView;

public class ChapterActivity extends AppCompatActivity {

    private SaveSharedPreferences pref;
    public static String book_name, category;
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
        setContentView(R.layout.activity_chapter);

        AdView mAdView = findViewById(R.id.adView);
        App.getInstance().showBanner(mAdView);


        //get value from home activity
        Intent intent = getIntent();
        int book_id = Integer.parseInt(intent.getStringExtra("book_id"));
        book_name = intent.getStringExtra("book_name");
        category = intent.getStringExtra("category");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(book_name);
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

        //connect to database
        DBHelper dbHelper = new DBHelper(this);
        //get no of chapters in book
        int total = dbHelper.getChapterCount(book_id);

        //create array of chapters
        int[] chapterNo = new int[total];
        for (int i = 0; i < total; i++) {
            chapterNo[i] = i + 1;
        }

        //initialization
        GridView gridView = findViewById(R.id.gridView);
        ChapterAdapter chapterAdapter = new ChapterAdapter(this, chapterNo, book_id/*,adsHandler*/);

        //attach view to adapter
        gridView.setAdapter(chapterAdapter);

//        showAd();

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

        // EnableDayNightTheme(ChapterActivity.this,pref,tvTheme,switch_day_night);
        OpenNavigationDrawer(nav_view, ChapterActivity.this, dr, pref);
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
        //retryAttempt=1;
        isOnPause = false;
    }

}