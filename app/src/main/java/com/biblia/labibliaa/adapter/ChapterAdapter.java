package com.biblia.labibliaa.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biblia.labibliaa.App;
import com.biblia.labibliaa.ChapterActivity;
import com.biblia.labibliaa.R;
import com.biblia.labibliaa.VerseActivity;

public class ChapterAdapter extends BaseAdapter {

    //private final MoPubAdsHandlerBannerNdInterstitial adsHandler;
    Context mContext;
    private final int[] mChapterNo;
    private final int book_id;
    private final String book_name = ChapterActivity.book_name;
    private final String category = ChapterActivity.category;

    public ChapterAdapter(Context mContext, int[] mChapterNo, int book_id/*, MoPubAdsHandlerBannerNdInterstitial adsHandler*/) {
        this.mContext = mContext;
        this.mChapterNo = mChapterNo;
        this.book_id = book_id;
        //this.adsHandler=adsHandler;
    }

    @Override
    public int getCount() {
        return mChapterNo.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        mContext = viewGroup.getContext();
        @SuppressLint("ViewHolder") View v = LayoutInflater.from(mContext).inflate(R.layout.chapter_grid, viewGroup, false);
        TextView tvChapter = v.findViewById(R.id.tvChapter);
        RelativeLayout cardView = v.findViewById(R.id.cardView);

        tvChapter.setText(String.valueOf(mChapterNo[i]));
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                App.getInstance().showInterstitial(((Activity) mContext), false, new App.InterstitialListener() {
                    @Override
                    public void onCloseInterstitial() {
                        Intent intent = new Intent(mContext, VerseActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("ch_no", String.valueOf(mChapterNo[i]));
                        intent.putExtra("book_id", String.valueOf(book_id));
                        intent.putExtra("book_name", book_name);
                        intent.putExtra("category", category);
                        intent.putExtra("verse_no", "0");
                        mContext.startActivity(intent);
                    }
                });
            }
        });

        return v;
    }
}
