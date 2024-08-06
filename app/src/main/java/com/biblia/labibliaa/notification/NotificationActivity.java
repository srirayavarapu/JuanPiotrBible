package com.biblia.labibliaa.notification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.biblia.labibliaa.R;
import com.biblia.labibliaa.SplashActivity;
import com.biblia.labibliaa.nativead.NativeTemplateStyle;
import com.biblia.labibliaa.nativead.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.nativead.NativeAd;

public class NotificationActivity extends AppCompatActivity {
    private boolean remote;

    private TextView titleTextView;
    private TextView bodyTextView;

    public static Intent getIntent(@NonNull Context context,
                                   @NonNull String title,
                                   @NonNull String body,
                                   boolean remote) {
        Intent intent = new Intent(context, NotificationActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("body", body);
        intent.putExtra("remote", remote);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        titleTextView = findViewById(R.id.tvTitle);
        bodyTextView  = findViewById(R.id.tvBody);


        AdView mAdView   = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        loadNativeAd();
        onNewIntent(getIntent());



    }

    private void loadNativeAd() {
        AdLoader adLoader = new AdLoader.Builder(this, getString(R.string.admob_native_id))
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                        setupNativeAd(nativeAd);
                    }
                })
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());

    }

    private void setupNativeAd(NativeAd nativeAd) {
        NativeTemplateStyle styles = new
                NativeTemplateStyle.Builder().build();

        TemplateView template = findViewById(R.id.nativeAd);
        template.setStyles(styles);
        template.setNativeAd(nativeAd);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String title = intent.getStringExtra("title");
        String body = intent.getStringExtra("body");
        remote = intent.getBooleanExtra("remote", false);
        titleTextView.setText(title);
        bodyTextView.setText(body);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startMain() {
        startActivity(new Intent(this, SplashActivity.class));
        finishAffinity();
    }

    @Override
    public void onBackPressed() {
        if (remote) {
            startMain();
        } else {
            finish();
        }
    }
}