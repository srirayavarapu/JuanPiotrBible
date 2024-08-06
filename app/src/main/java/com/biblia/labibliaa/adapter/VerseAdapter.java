package com.biblia.labibliaa.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biblia.labibliaa.R;
import com.biblia.labibliaa.SaveSharedPreferences;
import com.biblia.labibliaa.VerseActivity;
import com.biblia.labibliaa.database.DBHelper;
import com.biblia.labibliaa.model.Verse;
import com.facebook.share.widget.ShareDialog;

import java.util.ArrayList;
import java.util.List;


public class VerseAdapter extends BaseAdapter implements Filterable {

    private Context mContext;
    private final List<Verse> mVerseList;
    private final List<Verse> list;

    private LayoutInflater inflater;
    private Dialog dialogColor;
    private String[] options;
    private ShareDialog shareDialog;
    private SaveSharedPreferences pref;
    TextView number;
    private final int fontSize;
    private int fav_no;
    private int notes_no;
    private int highlight_no;


    private final VerseActivity verseActivity;

    public VerseAdapter(Context mContext, List<Verse> mVerseList, VerseActivity activity, int fontSize) {
        this.mContext = mContext;
        this.mVerseList = mVerseList;
        this.verseActivity = activity;
        this.fontSize = fontSize;
        list = new ArrayList<>(mVerseList);

    }

    @Override
    public int getCount() {
        return mVerseList.size();
    }

    @Override
    public Object getItem(int position) {
        return mVerseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mContext = parent.getContext();
        @SuppressLint("ViewHolder") View v = LayoutInflater.from(mContext).inflate(R.layout.verse_list_item, parent, false);
        number = v.findViewById(R.id.number);
        number.setTextSize(fontSize);

        String verseText = mVerseList.get(position).getVerseNo() + ". " + mVerseList.get(position).getVerseText();
        final SpannableStringBuilder sb = new SpannableStringBuilder(verseText);

        if (mVerseList.get(position).getVerseText().length() > 1) {
            final StyleSpan bss = new StyleSpan(Typeface.BOLD); // Span to make text bold
            sb.setSpan(bss, 0, 3, Spannable.SPAN_INCLUSIVE_INCLUSIVE); // make first 4 characters Bold
        } else {
            final StyleSpan bss = new StyleSpan(Typeface.BOLD); // Span to make text bold
            sb.setSpan(bss, 0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE); // make first 4 characters Bold

        }

        number.setText(sb);

        //Dialog Box
        Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_verse);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        Dialog dialog1 = new Dialog(mContext);
        dialog1.setContentView(R.layout.dialog_highlight_color);
        dialog1.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog1.setCancelable(true);

        /*TextView last_reading = dialog.findViewById(R.id.last_reading);*/
        TextView highlight = dialog.findViewById(R.id.highlight);
        TextView fav = dialog.findViewById(R.id.favorite);
        TextView notes = dialog.findViewById(R.id.note);
        TextView copy = dialog.findViewById(R.id.copy);
        TextView share = dialog.findViewById(R.id.share);

        ImageView color1, color2, color3, color4, color5, color6, color7, color8, color9, color10, color11, color12;
        color1 = dialog1.findViewById(R.id.btn_color_1);
        color2 = dialog1.findViewById(R.id.btn_color_2);
        color3 = dialog1.findViewById(R.id.btn_color_3);
        color4 = dialog1.findViewById(R.id.btn_color_4);
        color5 = dialog1.findViewById(R.id.btn_color_5);
        color6 = dialog1.findViewById(R.id.btn_color_6);
        color7 = dialog1.findViewById(R.id.btn_color_7);
        color8 = dialog1.findViewById(R.id.btn_color_8);
        color9 = dialog1.findViewById(R.id.btn_color_9);
        color10 = dialog1.findViewById(R.id.btn_color_10);
        color11 = dialog1.findViewById(R.id.btn_color_11);
        color12 = dialog1.findViewById(R.id.btn_color_12);


        switch (mVerseList.get(position).getColor()) {
            case "0":
                highlight.setText(mContext.getResources().getString(R.string.Highlight_this_Verse) );
                break;
            case "1":
                number.setBackground(color1.getBackground());
                highlight.setText(mContext.getResources().getString(R.string.unhighlight_this_verse));
                break;
            case "2":
                number.setBackground(color2.getBackground());
                highlight.setText(mContext.getResources().getString(R.string.unhighlight_this_verse));
                break;
            case "3":
                number.setBackground(color3.getBackground());
                highlight.setText(mContext.getResources().getString(R.string.unhighlight_this_verse));
                break;
            case "4":
                number.setBackground(color4.getBackground());
                highlight.setText(mContext.getResources().getString(R.string.unhighlight_this_verse));
                break;
            case "5":
                number.setBackground(color5.getBackground());
                highlight.setText(mContext.getResources().getString(R.string.unhighlight_this_verse));
                break;
            case "6":
                number.setBackground(color6.getBackground());
                highlight.setText(mContext.getResources().getString(R.string.unhighlight_this_verse));
                break;
            case "7":
                number.setBackground(color7.getBackground());
                highlight.setText(mContext.getResources().getString(R.string.unhighlight_this_verse));
                break;
            case "8":
                number.setBackground(color8.getBackground());
                highlight.setText(mContext.getResources().getString(R.string.unhighlight_this_verse));
                break;
            case "9":
                number.setBackground(color9.getBackground());
                highlight.setText(mContext.getResources().getString(R.string.unhighlight_this_verse));
                break;
            case "10":
                number.setBackground(color10.getBackground());
                highlight.setText(mContext.getResources().getString(R.string.unhighlight_this_verse));
                break;
            case "11":
                number.setBackground(color11.getBackground());
                highlight.setText(mContext.getResources().getString(R.string.unhighlight_this_verse));
                break;
            case "12":
                number.setBackground(color12.getBackground());
                highlight.setText(mContext.getResources().getString(R.string.unhighlight_this_verse));
                break;
            default:
                break;
        }


        //Database object
        DBHelper dbHelper = new DBHelper(mContext);
        pref = new SaveSharedPreferences(mContext);
        fav_no=SaveSharedPreferences.getFavNo(mContext);
        highlight_no=SaveSharedPreferences.getHighlightNo(mContext);
        notes_no=SaveSharedPreferences.getNoteNo(mContext);

        LinearLayout linearLayout = v.findViewById(R.id.linearLayout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "Here");

                dialog.show();

                /*last_reading.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        try {
                            SaveSharedPreferences.saveLastReadVerse(mContext,
                                    VerseActivity.getBook_id(),
                                    verseActivity.getBook_name(),
                                    verseActivity.getCh_no(),
                                    mVerseList.get(position).getVerseNo(),
                                    mVerseList.get(position).getVerseText(),
                                    VerseActivity.category);

                            Toast.makeText(mContext, mContext.getResources().getString(R.string.marked_last_read), Toast.LENGTH_SHORT).show();
                        }
                        catch (Exception e) {
                            Log.d("TAGHERE",e.toString());
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.review_submitted_error), Toast.LENGTH_SHORT).show();

                        }
                    }
                });*/

                highlight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                        if (!mVerseList.get(position).getColor().equals("0")) {
                            Long result = dbHelper.highlightVerse("0", VerseActivity.getBook_id(),
                                    verseActivity.getCh_no(), mVerseList.get(position).getVerseNo(),0);

                            if (result != null) {
                                number.setBackground(null);
                                verseActivity.updateList();
                            } else {
                                Toast.makeText(mContext, R.string.review_submitted_error, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            dialog1.show();

                            View.OnClickListener dialogClick = new View.OnClickListener() {
                                @SuppressLint("NonConstantResourceId")
                                @Override
                                public void onClick(View view) {
                                    int id = view.getId();
                                    if (id == R.id.btn_color_1) {
                                        setColor(color1.getBackground(), 1);
                                        dialog1.dismiss();
                                    } else if (id == R.id.btn_color_2) {
                                        setColor(color2.getBackground(), 2);
                                        dialog1.dismiss();
                                    } else if (id == R.id.btn_color_3) {
                                        setColor(color3.getBackground(), 3);
                                        dialog1.dismiss();
                                    } else if (id == R.id.btn_color_4) {
                                        setColor(color4.getBackground(), 4);
                                        dialog1.dismiss();
                                    } else if (id == R.id.btn_color_5) {
                                        setColor(color5.getBackground(), 5);
                                        dialog1.dismiss();
                                    } else if (id == R.id.btn_color_6) {
                                        setColor(color6.getBackground(), 6);
                                        dialog1.dismiss();
                                    } else if (id == R.id.btn_color_7) {
                                        setColor(color7.getBackground(), 7);
                                        dialog1.dismiss();
                                    } else if (id == R.id.btn_color_8) {
                                        setColor(color8.getBackground(), 8);
                                        dialog1.dismiss();
                                    } else if (id == R.id.btn_color_9) {
                                        setColor(color9.getBackground(), 9);
                                        dialog1.dismiss();
                                    } else if (id == R.id.btn_color_10) {
                                        setColor(color10.getBackground(), 10);
                                        dialog1.dismiss();
                                    } else if (id == R.id.btn_color_11) {
                                        setColor(color11.getBackground(), 11);
                                        dialog1.dismiss();
                                    } else if (id == R.id.btn_color_12) {
                                        setColor(color12.getBackground(), 12);
                                        dialog1.dismiss();
                                    }
                                }
                            };
                            color1.setOnClickListener(dialogClick);
                            color2.setOnClickListener(dialogClick);
                            color3.setOnClickListener(dialogClick);
                            color4.setOnClickListener(dialogClick);
                            color5.setOnClickListener(dialogClick);
                            color6.setOnClickListener(dialogClick);
                            color7.setOnClickListener(dialogClick);
                            color8.setOnClickListener(dialogClick);
                            color9.setOnClickListener(dialogClick);
                            color10.setOnClickListener(dialogClick);
                            color11.setOnClickListener(dialogClick);
                            color12.setOnClickListener(dialogClick);
                        }
                    }

                    @SuppressLint("ResourceAsColor")
                    void setColor(Drawable color, int no) {

                        Long result = dbHelper.highlightVerse(String.valueOf(no), VerseActivity.getBook_id(),
                                verseActivity.getCh_no(), mVerseList.get(position).getVerseNo(),highlight_no+1);
                        if (result != null) {
                            SaveSharedPreferences.saveHighlightNo(mContext,highlight_no+1);
                            highlight_no=highlight_no+1;
                            number.setBackground(color);
                            verseActivity.updateList();
                        } else {
                            Toast.makeText(mContext, R.string.review_submitted_error, Toast.LENGTH_LONG).show();
                        }
                    }

                });

                fav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Long result = dbHelper.addAsFav(VerseActivity.getBook_id(), verseActivity.getCh_no(), mVerseList.get(position).getVerseNo(), 1, fav_no + 1);

                        if (result != null) {
                            SaveSharedPreferences.saveFavNo(mContext,fav_no + 1);
                            fav_no=fav_no+1;
                            Toast.makeText(mContext, R.string.Added_to_Favorites, Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(mContext, R.string.review_submitted_error, Toast.LENGTH_LONG).show();
                        }
                    }
                });

                notes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        lp.setMargins(50, 0, 500, 0);
                        EditText input = new EditText(mContext);
                        input.setHint(R.string.Add_note_to_verse); //Dodaj notatkę

                        if (input.getParent() != null) {
                            ((ViewGroup) input.getParent()).removeView(input); // <- fix
                        }
                        input.setLayoutParams(lp);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                        alertDialogBuilder.setTitle(verseActivity.getBook_name() + " " + verseActivity.getCh_no() + ":" + mVerseList.get(position).getVerseNo());
                        alertDialogBuilder.setView(input);
                        alertDialogBuilder.setMessage(mVerseList.get(position).getVerseText());
                        alertDialogBuilder.setPositiveButton(mContext.getResources().getString(R.string.save), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Long result = dbHelper.addNote(VerseActivity.getBook_id(), verseActivity.getCh_no(),
                                        mVerseList.get(position).getVerseNo(), input.getText().toString(),notes_no + 1);

                                if (result != null) {
                                    SaveSharedPreferences.saveNoteNo(mContext,notes_no + 1);
                                    notes_no=notes_no + 1;
                                    Toast.makeText(mContext, R.string.note_updated, Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(mContext, R.string.review_submitted_error, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        alertDialogBuilder.setNegativeButton(mContext.getResources().getString(R.string.cancel), null);
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                        Button btnYes = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                        Button btnNo = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);

                        if (pref.getState()) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                if (btnYes != null && btnNo != null) {
                                    btnYes.setTextColor(mContext.getResources().getColor(R.color.white));
                                    btnNo.setTextColor(mContext.getResources().getColor(R.color.white));
                                } else {
                                    Log.d("HHHHH", "Buttons of Dialog are null");
                                }
                            }
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                if (btnYes != null && btnNo != null) {
                                    btnYes.setTextColor(mContext.getResources().getColor(R.color.black));
                                    btnNo.setTextColor(mContext.getResources().getColor(R.color.black));
                                } else {
                                    Log.d("HHHHH", "Buttons of Dialog are null");
                                }
                            }
                        }
                    }
                });

                copy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                        String appLink = "https://play.google.com/store/apps/details?id=" + mContext.getPackageName();
                        String copyVerse = mVerseList.get(position).getVerseText() +"\n"+ verseActivity.getBook_name() + " " + verseActivity.getCh_no() + ":" + mVerseList.get(position).getVerseNo()  +"\n"+ "\n" + appLink;
                        ClipData clip = ClipData.newPlainText("Verse", copyVerse);
                        clipboard.setPrimaryClip(clip);
                        dialog.dismiss();
                        Toast.makeText(mContext, R.string.verse_copy_ok, Toast.LENGTH_SHORT).show();
                    }
                });

                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String appLink = "https://play.google.com/store/apps/details?id=" + mContext.getPackageName();
                        String shareVerse = mVerseList.get(position).getVerseText() +"\n"+ verseActivity.getBook_name() + " " + verseActivity.getCh_no() + ":" + mVerseList.get(position).getVerseNo()  +"\n"+ "\n" + appLink;
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
        return v;
    }

    public String getVerseText(int pos) {
        return mVerseList.get(pos).getVerseText();
    }

    public void zoomIn(){
        number.setTextSize(12);
    }

    public List<Verse> getList(){
        return list;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence keywords) {
            ArrayList<Verse> filteredData = new ArrayList<>();

            if (keywords.toString().isEmpty()){
                filteredData.addAll(list);
            }else {
                for (Verse obj : list){
                     if (obj.getVerseText().toString().toLowerCase().contains(keywords.toString().toLowerCase()))
                         filteredData.add(obj);
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredData;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mVerseList.clear();
            mVerseList.addAll((ArrayList<Verse>)results.values);
            notifyDataSetChanged();
        }
    };
}