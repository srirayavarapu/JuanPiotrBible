package com.biblia.labibliaa;

import static com.biblia.labibliaa.MainActivity.OpenNavigationDrawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.biblia.labibliaa.adapter.NoteAdapter;
import com.biblia.labibliaa.database.DBHelper;
import com.biblia.labibliaa.model.Note;

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


import java.util.List;
import java.util.Map;

public class NoteActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_note);

        if (MainActivity.consentInformation.canRequestAds()) {

            //BANNER ADS
            AdView mAdView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.Note));
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

        ListView note_list = (ListView) findViewById(R.id.notes_list);
        DBHelper mDbHelper = new DBHelper(this);
        //Get fav name list when db exists
        List<Note> mNoteList = mDbHelper.getNotesList();
        TextView tvNoNote = findViewById(R.id.tvNoNote);
        if (mNoteList.isEmpty()){
            tvNoNote.setVisibility(View.VISIBLE);
            note_list.setVisibility(View.GONE);
        }else {
            tvNoNote.setVisibility(View.GONE);
            note_list.setVisibility(View.VISIBLE);
            //init Adapter
            NoteAdapter adapter = new NoteAdapter(this, mNoteList);
            //set adapter for listview
            note_list.setAdapter(adapter);
        }

        //EnableDayNightTheme(this,pref,tvTheme,switch_day_night);
        OpenNavigationDrawer(nav_view, this, dr, pref);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


}