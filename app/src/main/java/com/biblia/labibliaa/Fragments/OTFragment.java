package com.biblia.labibliaa.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.biblia.labibliaa.R;
import com.biblia.labibliaa.adapter.BookNameAdapter;
import com.biblia.labibliaa.database.DBHelper;
import com.biblia.labibliaa.model.Book;

import java.util.List;

public class OTFragment extends Fragment {
    private ListView ot_book_list;
    private BookNameAdapter adapter;
    private DBHelper mDbHelper;
    private List<Book> mBookList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ot, container, false);
        ot_book_list = view.findViewById(R.id.ot_book_list);
        /*adsHandler = new MoPubAdsHandlerBannerNdInterstitial(getActivity());
        adsHandler.handleBannerAds();*/

        mDbHelper = new DBHelper(getActivity());
        mDbHelper.getTableName(mDbHelper.getWritableDatabase());

        mBookList = mDbHelper.getBookbyCategory(0);
        adapter = new BookNameAdapter(getActivity(),mBookList/*,adsHandler*/);
        ot_book_list.setAdapter(adapter);

        for (int i=0; i<mBookList.size(); i++) {
            Log.e("Count", mBookList.get(i).getName()+"");
        }
        return view;
    }
}