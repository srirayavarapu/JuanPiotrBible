package com.biblia.labibliaa.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biblia.labibliaa.R;
import com.biblia.labibliaa.VerseActivity;
import com.biblia.labibliaa.model.Search;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Search> searchList = null;
    private final ArrayList<Search> arraylist;

    public SearchAdapter(Context context, List<Search> searchList) {
        mContext = context;
        this.searchList = searchList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Search>();
        this.arraylist.addAll(searchList);
    }

    public static class ViewHolder {
        TextView name;
        TextView nameLabel;
        LinearLayout linearLayout;
    }

    @Override
    public int getCount() {
        return searchList.size();
    }

    @Override
    public Search getItem(int position) {
        return searchList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @SuppressLint({"InflateParams", "SetTextI18n"})
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.search_list_items, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.nameLabel=(TextView)view.findViewById(R.id.nameLabel);
            holder.linearLayout=(LinearLayout)view.findViewById(R.id.linearLayout);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.nameLabel.setText(searchList.get(position).getBookName()+" "+searchList.get(position).getCh_no()+":"+searchList.get(position).getVerse_no());
        holder.name.setText(searchList.get(position).getVerse_text());


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, VerseActivity.class);
                intent.putExtra("book_id", String.valueOf(searchList.get(position).getBookId()));
                intent.putExtra("ch_no",searchList.get(position).getCh_no());
                intent.putExtra("book_name",searchList.get(position).getBookName());
                intent.putExtra("category",VerseActivity.category);
                intent.putExtra("verse_no",searchList.get(position).getVerse_no());
                mContext.startActivity(intent);
            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText ) {
        charText = charText.toLowerCase(Locale.getDefault());
        searchList.clear();

        if (charText.length() == 0) {
            searchList.addAll(arraylist);
        } else {
            for (Search wp : arraylist) {
                if(wp.getVerse_text()!=null) {
                    if (wp.getVerse_text().toLowerCase(Locale.getDefault()).contains(charText) ||
                            wp.getBookName().toLowerCase(Locale.getDefault()).contains(charText) ||
                            wp.getCh_no().toLowerCase(Locale.getDefault()).contains(charText)) {
                        searchList.add(wp);
                    }
                }else {

                }
            }
        }
        notifyDataSetChanged();
    }
}
