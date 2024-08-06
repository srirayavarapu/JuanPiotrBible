package com.biblia.labibliaa.Fragments;

import android.content.Context;
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

public class NTSearchFragment extends Fragment implements SearchView.OnQueryTextListener{

    ListView search_list_nt;
    SearchAdapter adapter_nt;
    SearchView editsearch_nt;
    Context context;

    DBHelper mDbHelper_nt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        View view = inflater.inflate(R.layout.fragment_nt_search, container, false);

        search_list_nt = (ListView) view.findViewById(R.id.NtListView);

        mDbHelper_nt = new DBHelper(getActivity());

        //Get fav name list when db exists
        List<Search> mSearchList_nt = mDbHelper_nt.getListofBooks("nt", "");

        //init Adapter
        adapter_nt = new SearchAdapter(getActivity(), mSearchList_nt);
        //set adapter for listview
        search_list_nt.setAdapter(adapter_nt);


        editsearch_nt = (SearchView) view.findViewById(R.id.search_nt);
        editsearch_nt.setOnQueryTextListener(this);

        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter_nt.filter(newText);
        return false;
    }
}