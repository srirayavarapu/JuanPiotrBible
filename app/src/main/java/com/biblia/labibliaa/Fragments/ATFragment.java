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

public class ATFragment extends Fragment implements SearchView.OnQueryTextListener{

    ListView search_list_at;
    SearchAdapter adapter_at;
    SearchView editsearch_at;

    DBHelper mDbHelper_at;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_at, container, false);
        search_list_at = (ListView) view.findViewById(R.id.ATListView);

        mDbHelper_at = new DBHelper(getActivity());

        //Get fav name list when db exists
        List<Search> mSearchList_at = mDbHelper_at.getListofBooks("at", "");

        //init Adapter
        adapter_at = new SearchAdapter(getActivity(), mSearchList_at);
        //set adapter for listview
        search_list_at.setAdapter(adapter_at);


        editsearch_at = (SearchView) view.findViewById(R.id.search_at);
        editsearch_at.setOnQueryTextListener(this);

        return view;
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter_at.filter(newText);
        return false;
    }
}