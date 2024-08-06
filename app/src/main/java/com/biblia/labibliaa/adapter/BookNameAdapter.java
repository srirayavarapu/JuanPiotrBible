package com.biblia.labibliaa.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biblia.labibliaa.ChapterActivity;
import com.biblia.labibliaa.R;
import com.biblia.labibliaa.model.Book;

import java.util.List;


public class BookNameAdapter extends BaseAdapter {

    //private final MoPubAdsHandlerBannerNdInterstitial adsHandler;
    private Context mContext;
    private final List<Book> mBookNameList;

    public BookNameAdapter(Context mContext, List<Book> mBookNameList/*, MoPubAdsHandlerBannerNdInterstitial adsHandler*/) {
        this.mContext = mContext;
        this.mBookNameList = mBookNameList;
    }

    @Override
    public int getCount() {
        return mBookNameList.size();
    }

    @Override
    public Object getItem(int position) {
        return mBookNameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mContext = parent.getContext();
        @SuppressLint("ViewHolder") View v = View.inflate(mContext, R.layout.book_list_item, null);
        TextView tvName = v.findViewById(R.id.nameTxt);
        RelativeLayout relative = v.findViewById(R.id.relative);
        TextView ivBook = v.findViewById(R.id.ivBook);
        TextView tvChapterCount = v.findViewById(R.id.tvChapterCount);

        tvName.setText(mBookNameList.get(position).getName());
        ivBook.setText(mBookNameList.get(position).getName().charAt(0)+"");
        String s = mContext.getString(R.string.capitulo_s);
        tvChapterCount.setText(mBookNameList.get(position).getChapter_count()+" "+s);
        relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ChapterActivity.class);
                intent.putExtra("book_id", String.valueOf(mBookNameList.get(position).getId()));
                intent.putExtra("book_name", mBookNameList.get(position).getName());
                intent.putExtra("category", mBookNameList.get(position).getCategory());
                mContext.startActivity(intent);
                //adsHandler.showInterstitial();
            }
        });

        return v;
    }
}