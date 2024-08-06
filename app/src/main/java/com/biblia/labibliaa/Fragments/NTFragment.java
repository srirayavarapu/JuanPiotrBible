package com.biblia.labibliaa.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.biblia.labibliaa.R;
import com.biblia.labibliaa.adapter.BookNameAdapter;
import com.biblia.labibliaa.database.DBHelper;
import com.biblia.labibliaa.model.Book;

import java.util.List;

public class NTFragment extends Fragment {
    private ListView nt_book_list;
    private BookNameAdapter adapter;
    private DBHelper mDbHelper;
    private List<Book> mBookList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nt, container, false);
        nt_book_list = view.findViewById(R.id.nt_book_list);
        //MoPubAdsHandlerBannerNdInterstitial adsHandler = new MoPubAdsHandlerBannerNdInterstitial(getActivity());

        mDbHelper = new DBHelper(getActivity());
        mDbHelper.getTableName(mDbHelper.getWritableDatabase());

        mBookList = mDbHelper.getBookbyCategory(1);
        adapter = new BookNameAdapter(getActivity(),mBookList/*,adsHandler*/);
        nt_book_list.setAdapter(adapter);
        return view;
    }
}