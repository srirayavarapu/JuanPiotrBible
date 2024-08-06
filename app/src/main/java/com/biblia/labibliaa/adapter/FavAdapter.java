package com.biblia.labibliaa.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biblia.labibliaa.FavActivity;
import com.biblia.labibliaa.R;
import com.biblia.labibliaa.VerseActivity;
import com.biblia.labibliaa.database.DBHelper;
import com.biblia.labibliaa.model.Fav;

import java.util.List;

public class FavAdapter extends BaseAdapter {

    //private final MoPubAdsHandlerBannerNdInterstitial adsHandler;
    private Context mContext;
    private final List<Fav> mFavList;

    public FavAdapter(Context mContext, List<Fav> mFavList/*, MoPubAdsHandlerBannerNdInterstitial adsHandler*/) {
        this.mContext = mContext;
        this.mFavList = mFavList;
        //this.adsHandler = adsHandler;
    }

    @Override
    public int getCount() {
        return mFavList.size();
    }

    @Override
    public Object getItem(int i) {
        return mFavList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        mContext = viewGroup.getContext();
        @SuppressLint("ViewHolder") View v = View.inflate(mContext, R.layout.fav_list, null);
        TextView verse_head = v.findViewById(R.id.verse_head);
        TextView verse_text = v.findViewById(R.id.verse_text);
        LinearLayout linearLayout = v.findViewById(R.id.linearLayout);
        String appLink = "https://play.google.com/store/apps/details?id=" + mContext.getPackageName();

        DBHelper dbHelper=new DBHelper(mContext);

        Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_fav);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        TextView gotoFav = dialog.findViewById(R.id.gotoFav);
        TextView deleteFav = dialog.findViewById(R.id.deleteFav);
        TextView copyFav = dialog.findViewById(R.id.copyFav);
        TextView shareFav = dialog.findViewById(R.id.shareFav);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();

                gotoFav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(mContext,VerseActivity.class);
                        intent.putExtra("book_id",mFavList.get(i).getBook_id());
                        intent.putExtra("ch_no",mFavList.get(i).getCh_no());
                        intent.putExtra("book_name",mFavList.get(i).getName());
                        intent.putExtra("verse_no",mFavList.get(i).getVerse_no());
                        mContext.startActivity(intent);
                        //adsHandler.showInterstitial();
                    }
                });

                deleteFav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        new AlertDialog.Builder(mContext)
                                .setTitle(R.string.want_to_remove_verse_fav)
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dbHelper.addAsFav(Integer.parseInt(mFavList.get(i).getBook_id()), Integer.parseInt(mFavList.get(i).getCh_no()),mFavList.get(i).getVerse_no(),0,0);
                                        mContext.startActivity(new Intent(mContext, FavActivity.class));
                                        ((FavActivity)mContext).finish();
                                    }
                                }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();

                    }
                });

                copyFav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                        String appLink = "https://play.google.com/store/apps/details?id=" + mContext.getPackageName();
                        String copyVerse = mFavList.get(i).getVerse_text() +"\n"+ mFavList.get(i).getName() + " " + mFavList.get(i).getCh_no() + " : " + mFavList.get(i).getVerse_no() +"\n"+ "\n" + appLink;
                        ClipData clip = ClipData.newPlainText("Verse", copyVerse);
                        clipboard.setPrimaryClip(clip);
                        dialog.dismiss();
                        Toast.makeText(mContext, R.string.verse_copy_ok, Toast.LENGTH_SHORT).show();
                    }
                });

                shareFav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String appLink = "https://play.google.com/store/apps/details?id=" + mContext.getPackageName();
                        String shareVerse = mFavList.get(i).getVerse_text() +"\n"+ mFavList.get(i).getName() + " " + mFavList.get(i).getCh_no() + " : " + mFavList.get(i).getVerse_no() +"\n"+ "\n" + appLink;
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareVerse);
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, mContext.getResources().getString(R.string.bible_verse_share));
                        mContext.startActivity(Intent.createChooser(shareIntent, mContext.getResources().getString(R.string.Share_this_Verse)));
                        dialog.dismiss();
                    }
                });
            }
        });

        verse_head.setText(mFavList.get(i).getName() + " " + mFavList.get(i).getCh_no() + " : " + mFavList.get(i).getVerse_no());
        verse_text.setText(mFavList.get(i).getVerse_text());
        switch (mFavList.get(i).getColor()) {
            case "0":
                break;
            case "1":
                linearLayout.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_rectangle_1));
                break;
            case "2":
                linearLayout.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_rectangle_2));
                break;
            case "3":
                linearLayout.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_rectangle_3));
                break;
            case "4":
                linearLayout.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_rectangle_4));
                break;
            case "5":
                linearLayout.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_rectangle_5));
                break;
            case "6":
                linearLayout.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_rectangle_6));
                break;
            case "7":
                linearLayout.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_rectangle_7));
                break;
            case "8":
                linearLayout.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_rectangle_8));
                break;
            case "9":
                linearLayout.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_rectangle_9));
                break;
            case "10":
                linearLayout.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_rectangle_10));
                break;
            case "11":
                linearLayout.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_rectangle_11));
                break;
            case "12":
                linearLayout.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_rectangle_12));
                break;
            default:
                break;
        }

        return v;
    }
}
