package com.biblia.labibliaa.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biblia.labibliaa.App;
import com.biblia.labibliaa.HighlightActivity;
import com.biblia.labibliaa.R;
import com.biblia.labibliaa.VerseActivity;
import com.biblia.labibliaa.database.DBHelper;
import com.biblia.labibliaa.model.Highlight;

import java.util.List;

public class HighlightAdapter extends BaseAdapter {

    //private final MoPubAdsHandlerBannerNdInterstitial adsHandler;
    private Context mContext;
    private final List<Highlight> mHighlightList;

    public HighlightAdapter(Context mContext, List<Highlight> mHighlightList/*, MoPubAdsHandlerBannerNdInterstitial adsHandler */) {
        this.mContext = mContext;
        this.mHighlightList = mHighlightList;
        //this.adsHandler = adsHandler;
    }

    @Override
    public int getCount() {
        return mHighlightList.size();
    }

    @Override
    public Object getItem(int i) {
        return mHighlightList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        mContext = viewGroup.getContext();
        @SuppressLint("ViewHolder") View v = View.inflate(mContext, R.layout.highlight_list, null);
        TextView verse_head = v.findViewById(R.id.verse_head);
        TextView verse_text = v.findViewById(R.id.verse_text);
        LinearLayout linearLayout = v.findViewById(R.id.linearLayout);


        DBHelper dbHelper = new DBHelper(mContext);

        Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_highlight);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        TextView gotoHighlight = dialog.findViewById(R.id.gotoFav);
        TextView deleteHighlight = dialog.findViewById(R.id.deleteFav);
        TextView copyHighlight = dialog.findViewById(R.id.copyFav);
        TextView shareHighlight = dialog.findViewById(R.id.shareFav);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();

                gotoHighlight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        App.getInstance().showInterstitial(((Activity) mContext), false, new App.InterstitialListener() {
                            @Override
                            public void onCloseInterstitial() {
                                dialog.dismiss();
                                Intent intent = new Intent(mContext, VerseActivity.class);
                                intent.putExtra("book_id", mHighlightList.get(i).getBook_id());
                                intent.putExtra("ch_no", mHighlightList.get(i).getCh_no());
                                intent.putExtra("book_name", mHighlightList.get(i).getName());
                                intent.putExtra("verse_no", mHighlightList.get(i).getVerse_no());
                                mContext.startActivity(intent);
                            }
                        });
                    }
                });

                deleteHighlight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        new AlertDialog.Builder(mContext)
                                .setTitle(mContext.getResources().getString(R.string.want_to_remove_highlight))
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dbHelper.highlightVerse("0", Integer.parseInt(mHighlightList.get(i).getBook_id()), Integer.parseInt(mHighlightList.get(i).getCh_no()), mHighlightList.get(i).getVerse_no(), 0);
                                        mContext.startActivity(new Intent(mContext, HighlightActivity.class));
                                        ((HighlightActivity) mContext).finish();
                                    }
                                }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create().show();

                    }
                });

                copyHighlight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                        String appLink = "https://play.google.com/store/apps/details?id=" + mContext.getPackageName();
                        String copyVerse = mHighlightList.get(i).getVerse_text() + "\n" + mHighlightList.get(i).getName() + " " + mHighlightList.get(i).getCh_no() + " : " + mHighlightList.get(i).getVerse_no() + "\n" + "\n" + appLink;
                        ClipData clip = ClipData.newPlainText("Verse", copyVerse);
                        clipboard.setPrimaryClip(clip);
                        dialog.dismiss();
                        Toast.makeText(mContext, R.string.verse_copy_ok, Toast.LENGTH_SHORT).show();
                    }
                });

                shareHighlight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String appLink = "https://play.google.com/store/apps/details?id=" + mContext.getPackageName();
                        String shareVerse = mHighlightList.get(i).getVerse_text() + "\n" + mHighlightList.get(i).getName() + " " + mHighlightList.get(i).getCh_no() + " : " + mHighlightList.get(i).getVerse_no() + "\n" + "\n" + appLink;
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

        verse_head.setText(mHighlightList.get(i).getName() + " " + mHighlightList.get(i).getCh_no() + " : " + mHighlightList.get(i).getVerse_no());
        verse_text.setText(mHighlightList.get(i).getVerse_text());
        //Log.d("COLOR",mHighlightList.get(i).getColor());
        switch (mHighlightList.get(i).getColor()) {
            case "0":
                break;
            case "1":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    linearLayout.setBackground(mContext.getDrawable(R.drawable.rounded_rectangle_1));
                }
                break;
            case "2":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    linearLayout.setBackground(mContext.getDrawable(R.drawable.rounded_rectangle_2));
                }
                break;
            case "3":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    linearLayout.setBackground(mContext.getDrawable(R.drawable.rounded_rectangle_3));
                }
                break;
            case "4":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    linearLayout.setBackground(mContext.getDrawable(R.drawable.rounded_rectangle_4));
                }
                break;
            case "5":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    linearLayout.setBackground(mContext.getDrawable(R.drawable.rounded_rectangle_5));
                }
                break;
            case "6":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    linearLayout.setBackground(mContext.getDrawable(R.drawable.rounded_rectangle_6));
                }
                break;
            case "7":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    linearLayout.setBackground(mContext.getDrawable(R.drawable.rounded_rectangle_7));
                }
                break;
            case "8":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    linearLayout.setBackground(mContext.getDrawable(R.drawable.rounded_rectangle_8));
                }
                break;
            case "9":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    linearLayout.setBackground(mContext.getDrawable(R.drawable.rounded_rectangle_9));
                }
                break;
            case "10":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    linearLayout.setBackground(mContext.getDrawable(R.drawable.rounded_rectangle_10));
                }
                break;
            case "11":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    linearLayout.setBackground(mContext.getDrawable(R.drawable.rounded_rectangle_11));
                }
                break;
            case "12":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    linearLayout.setBackground(mContext.getDrawable(R.drawable.rounded_rectangle_12));
                }
                break;
            default:
                break;
        }

        return v;
    }

}
