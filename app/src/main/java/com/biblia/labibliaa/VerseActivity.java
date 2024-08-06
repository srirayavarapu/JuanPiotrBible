package com.biblia.labibliaa;

import static com.biblia.labibliaa.MainActivity.OpenNavigationDrawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LifecycleOwner;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import com.applovin.mediation.MaxAd;
//import com.applovin.mediation.MaxAdListener;
//import com.applovin.mediation.MaxAdViewAdListener;
//import com.applovin.mediation.MaxError;
//import com.applovin.mediation.ads.MaxAdView;
//import com.applovin.mediation.ads.MaxInterstitialAd;
import com.biblia.labibliaa.adapter.VerseAdapter;
import com.biblia.labibliaa.database.DBHelper;
import com.biblia.labibliaa.model.Verse;

import com.google.android.gms.ads.AdError;
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


import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class VerseActivity extends AppCompatActivity {

    public static int ch_no, book_id;
    private String book_name;
    public static String category;
    private SaveSharedPreferences pref;
    private TextView tvTheme;
    private ListView verse_list;
    private VerseAdapter adapter;
    private DBHelper mDbHelper;
    private List<Verse> mVerselist;
    private int fontSize, verse_no;
    private final String urlPrefix = "http://wordproaudio.org/bibles/app/audio/6";
    private ImageButton btn_play;
    private ImageButton btn_pause;
    private final MediaPlayer mediaPlayer = new MediaPlayer();
    private boolean playPause, initialStage = true;
    boolean isNight = true;

    //ADS
    private static final String TAG = "VerseActivity";
    private int retryAttempt=1;
    public InterstitialAd mInterstitialAd;


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
        //RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("380750B62499D8D682916C01DF43885F"))
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
                        VerseActivity.this.mInterstitialAd = interstitialAd;
                        if (retryAttempt == 1) {
                            //showAd();
                            retryAttempt++;
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
        setContentView(R.layout.activity_verse);


        if (MainActivity.consentInformation.canRequestAds()) {

            //Ads
            loadAd();
            //BANNER ADS
            AdView mAdView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }


        ImageButton btn_previous = findViewById(R.id.btn_previous);
        btn_play = findViewById(R.id.btn_play);
        ImageButton btn_next = findViewById(R.id.btn_next);
        ImageButton btn_zoomIn = findViewById(R.id.btn_zoomIn);
        ImageButton btn_zoomOut = findViewById(R.id.btn_zoomOut);

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

        OpenNavigationDrawer(nav_view, this, dr, pref);

        fontSize = SaveSharedPreferences.getFontSize(this);

        String name = "";
        Intent intent = getIntent();
        try {
            ch_no = Integer.parseInt(intent.getStringExtra("ch_no")); //error s = null
        } catch (Exception e1) {
            ch_no = 1;
        }
        //Toast.makeText(this, ch_no+"", Toast.LENGTH_SHORT).show();
        try {
            book_id = Integer.parseInt(intent.getStringExtra("book_id"));
        } catch (Exception e2) {
            book_id = 1;
        }
        //Toast.makeText(this, book_id+"**", Toast.LENGTH_SHORT).show();
        try {
            book_name = intent.getStringExtra("book_name");
            Log.d("HERETAG", book_name);

        } catch (Exception e3) {
            book_name = "Księga Rodzaju (Rdz)";
        }
        try {
            category = intent.getStringExtra("category");
        } catch (Exception e4) {
            category = "0";
        }
        try {
            verse_no = Integer.parseInt(intent.getStringExtra("verse_no"));
        } catch (Exception e5) {
            verse_no = 1;
        }

        try {
            for (int z = 0; z < book_name.length(); z++) {
                if (book_name.charAt(z) == '(') {
                    int k = z - 1;
                    name = book_name.substring(0, k);
                } else {
                }
            }
        } catch (Exception e6) {

        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (name.equals("")) {
            toolbar.setTitle(book_name + " " + ch_no);
        } else {
            toolbar.setTitle(name + " " + ch_no);
        }
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

        verse_list = (ListView) findViewById(R.id.verse_list);
        mDbHelper = new DBHelper(this);
        //Get book name list when db exists
        mVerselist = mDbHelper.getVerseList(book_id, ch_no);
        Log.d("VERSES", String.valueOf(mVerselist));
       // Toast.makeText(this, mVerselist.size()+" : Size", Toast.LENGTH_SHORT).show();
        //init Adapter
        adapter = new VerseAdapter(this, mVerselist,this,fontSize);
        //set adapter for listview
        verse_list.setAdapter(adapter);
        verse_list.post( new Runnable() {
            @Override
            public void run() {
                //call smooth scroll
                verse_list.setSelection(verse_no-1);
            }
        });

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!playPause) {
                    if (initialStage) {
                        new Player()
                                .execute(urlPrefix + "/" + book_id + "/" + ch_no+".mp3");
                    } else {
                        if (!mediaPlayer.isPlaying())
                            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                        btn_play.setImageResource(R.drawable.ic_pause);
                        mediaPlayer.start();
                    }
                    playPause = true;
                }else {
                    if (mediaPlayer.isPlaying()) {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                        btn_play.setImageResource(R.drawable.ic_play);
                        mediaPlayer.pause();

                    }
                    playPause = false;
                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextClicked();
            }
        });

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevClicked();
            }
        });

        btn_zoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fontSize<35) {
                    fontSize = fontSize + 4;
                    SaveSharedPreferences.saveFontSize(getApplicationContext(), fontSize);
                }else{
                    Toast.makeText(VerseActivity.this, R.string.no_zoom_message, Toast.LENGTH_LONG).show();
                }
                adapter = new VerseAdapter(VerseActivity.this, mVerselist,VerseActivity.this,fontSize);
                //set adapter for listview
                verse_list.setAdapter(adapter);

            }
        });

        btn_zoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fontSize>15) {
                    fontSize = fontSize - 4;
                    SaveSharedPreferences.saveFontSize(getApplicationContext(), fontSize);
                }else{
                    Toast.makeText(VerseActivity.this, R.string.no_zoom_out, Toast.LENGTH_LONG).show();
                }
                adapter = new VerseAdapter(VerseActivity.this, mVerselist,VerseActivity.this,fontSize);
                //set adapter for listview
                verse_list.setAdapter(adapter);
                verse_list.smoothScrollToPosition(10);

            }
        });
    }



    @SuppressLint("StaticFieldLeak")
    class Player extends AsyncTask<String, Void, Boolean> {
        private final ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progress.setMessage(getString(R.string.buffering));
            this.progress.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean prepared;
            try {

                mediaPlayer.setDataSource(params[0]);

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        initialStage = true;
                        playPause = false;
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                        btn_play.setImageResource(R.drawable.ic_pause);
                        mp.stop();
                        mp.reset();
                    }
                });
                mediaPlayer.prepare();
                prepared = true;
            } catch (IllegalArgumentException e) {
                Log.d("IllegarArgument", e.getMessage());
                prepared = false;
                e.printStackTrace();
            } catch (SecurityException | IllegalStateException | IOException e) {
                prepared = false;
                e.printStackTrace();
            }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (progress.isShowing()) {
                progress.cancel();
            }
            Log.d("Prepared", "//" + result);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            btn_play.setImageResource(R.drawable.ic_pause);
//            pp.setColorFilter(ContextCompat.getColor(VersesActivity.this, android.R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
            mediaPlayer.start();

            initialStage = false;
        }

        public Player() {
            progress = new ProgressDialog(VerseActivity.this);
        }

    }
    @Override
    protected void onPause() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
//                pp.setColorFilter(R.attr.colorAccent, android.graphics.PorterDuff.Mode.SRC_IN);
                btn_play.setImageResource(R.drawable.ic_play);
                mediaPlayer.pause();
            }
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
//                pp.setColorFilter(R.attr.colorAccent, android.graphics.PorterDuff.Mode.SRC_IN);
                btn_play.setImageResource(R.drawable.ic_play);
                mediaPlayer.pause();
            }
            mediaPlayer.release();
        }
        super.onDestroy();
    }

    public void nextClicked() {
        int total=mDbHelper.getChapterCount(book_id);
        int next=ch_no+1;

        if (next<=total) {
            Intent intent = new Intent(VerseActivity.this, VerseActivity.class);
            intent.putExtra("book_id", String.valueOf(book_id));
            intent.putExtra("ch_no", String.valueOf(next));
            intent.putExtra("book_name", book_name);
            intent.putExtra("verse_no","0");
            intent.putExtra("category",category);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(VerseActivity.this, R.string.no_more_chapters, Toast.LENGTH_LONG).show();
        }
    }

    public void prevClicked() {
        if (ch_no>1) {
            /*showNotification(R.drawable.ic_play,ch_no-1);
            textToSpeech.stop();*/
            Intent intent = new Intent(VerseActivity.this, VerseActivity.class);
            intent.putExtra("book_id",String.valueOf(book_id));
            int next=ch_no-1;
            intent.putExtra("ch_no",String.valueOf(next));
            intent.putExtra("book_name", book_name);
            intent.putExtra("verse_no","0");
            intent.putExtra("category",category);
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(VerseActivity.this,R.string.no_more_chapters, Toast.LENGTH_LONG).show();
        }
    }

    public void updateList(){
        mDbHelper = new DBHelper(this);
        //Get book name list when db exists
        mVerselist = mDbHelper.getVerseList(book_id, ch_no);
        Log.d("Verse", String.valueOf(mVerselist));
        //init Adapter
        adapter = new VerseAdapter(VerseActivity.this, mVerselist,VerseActivity.this,fontSize);
        //set adapter for listview
        verse_list.setAdapter(adapter);
    }

    public int getCh_no(){
        return ch_no;
    }

    public static int getBook_id(){
        return book_id;
    }

    public int getVerse_no(){return verse_no;}

    public String getBook_name(){
        return book_name;
    }
}