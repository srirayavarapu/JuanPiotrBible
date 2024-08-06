package com.biblia.labibliaa.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;

import com.biblia.labibliaa.R;
import com.biblia.labibliaa.adapter.SearchAdapter;
import com.biblia.labibliaa.database.DBHelper;
import com.biblia.labibliaa.model.Book;
import com.biblia.labibliaa.model.Search;

import java.util.ArrayList;
import java.util.List;

public class BookFragment extends Fragment implements SearchView.OnQueryTextListener{

    ListView search_book_verse;
    SearchAdapter adapter_book;
    SearchView editsearch;

    DBHelper mDbHelper_Book;
    private List<Search> mBookSearchList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);

        search_book_verse = (ListView) view.findViewById(R.id.bookListView);
        mDbHelper_Book = new DBHelper(getActivity());

        List<Book> mBookList = mDbHelper_Book.getBookName();
        mBookSearchList = mDbHelper_Book.getListofBooks("book", mBookList.get(0).getName());

        Spinner spinner = view.findViewById(R.id.spinner);
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i< mBookList.size(); i++){
            arrayList.add(mBookList.get(i).getName());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String booksName = parent.getItemAtPosition(position).toString();
                // Toast.makeText(getActivity(),booksName,Toast.LENGTH_LONG).show();
                mBookSearchList = mDbHelper_Book.getListofBooks("book",booksName);
                //init Adapter
                adapter_book = new SearchAdapter(getActivity(), mBookSearchList);

                //set adapter for listview
                search_book_verse.setAdapter(adapter_book);
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });


        //init Adapter
        adapter_book = new SearchAdapter(getActivity(), mBookSearchList);

        //set adapter for listview
        search_book_verse.setAdapter(adapter_book);

        editsearch = (SearchView) view.findViewById(R.id.search_book);
        editsearch.setOnQueryTextListener(this);

        return view;
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter_book.filter(newText);
        return false;
    }
}