package com.biblia.labibliaa.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import com.biblia.labibliaa.R;
import com.biblia.labibliaa.adapter.SearchAdapter;
import com.biblia.labibliaa.database.DBHelper;
import com.biblia.labibliaa.model.Search;

import java.util.List;

public class AllFragment extends Fragment implements SearchView.OnQueryTextListener{

    ListView search_list;
    SearchAdapter adapter;
    SearchView editsearch;

    DBHelper mDbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all, container, false);
        search_list = (ListView) view.findViewById(R.id.allListView);

        mDbHelper = new DBHelper(getActivity());

        //Get fav name list when db exists
        List<Search> mSearchList = mDbHelper.getListofBooks("all", "");

        //init Adapter
        adapter = new SearchAdapter(getActivity(), mSearchList);
        //set adapter for listview
        search_list.setAdapter(adapter);

        editsearch = (SearchView) view.findViewById(R.id.search_all);
        editsearch.setOnQueryTextListener(this);
        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText);
        return false;
    }
}