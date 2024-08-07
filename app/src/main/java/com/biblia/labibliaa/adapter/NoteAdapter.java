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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biblia.labibliaa.App;
import com.biblia.labibliaa.NoteActivity;
import com.biblia.labibliaa.R;
import com.biblia.labibliaa.SaveSharedPreferences;
import com.biblia.labibliaa.VerseActivity;
import com.biblia.labibliaa.database.DBHelper;
import com.biblia.labibliaa.model.Note;

import java.util.List;

public class NoteAdapter extends BaseAdapter {

    private Context mContext;
    private final List<Note> mNoteList;
    private SaveSharedPreferences pref;

    public NoteAdapter(Context mContext, List<Note> mNoteList) {
        this.mContext = mContext;
        this.mNoteList = mNoteList;
    }

    @Override
    public int getCount() {
        return mNoteList.size();
    }

    @Override
    public Object getItem(int i) {
        return mNoteList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        mContext = viewGroup.getContext();
        @SuppressLint("ViewHolder") View v = View.inflate(mContext, R.layout.notes_list, null);
        TextView verse_head = v.findViewById(R.id.verse_head);
        TextView verse_text = v.findViewById(R.id.verse_text);
        TextView verse_note = v.findViewById(R.id.verse_note);
        LinearLayout linearLayout = v.findViewById(R.id.linearLayout);

        DBHelper dbHelper = new DBHelper(mContext);
        pref = new SaveSharedPreferences(mContext);

        //Dialog Box
        Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_notes);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        TextView editNote = dialog.findViewById(R.id.editNote);
        TextView deleteNote = dialog.findViewById(R.id.deleteNote);
        TextView gotoNote = dialog.findViewById(R.id.gotoNote);
        TextView copyNote = dialog.findViewById(R.id.copyNote);
        TextView shareNote = dialog.findViewById(R.id.shareNote);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();

                editNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        lp.setMargins(50, 0, 500, 0);
                        EditText input = new EditText(mContext);
                        input.setHint(mNoteList.get(i).getVerse_note());
                        input.setText(mNoteList.get(i).getVerse_note());

                        if (input.getParent() != null) {
                            ((ViewGroup) input.getParent()).removeView(input); // <- fix
                        }
                        input.setLayoutParams(lp);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                        alertDialogBuilder.setTitle(mNoteList.get(i).getName() + " " + mNoteList.get(i).getCh_no() + ":" + mNoteList.get(i).getVerse_no());
                        alertDialogBuilder.setView(input);
                        alertDialogBuilder.setMessage(mNoteList.get(i).getVerse_text());
                        alertDialogBuilder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("HHHH", mNoteList.get(i).getCh_no() +
                                        mNoteList.get(i).getNo() + mNoteList.get(i).getVerse_no() + input.getText().toString());
                                Long result = dbHelper.addNote(Integer.parseInt(mNoteList.get(i).getNo()),
                                        Integer.parseInt(mNoteList.get(i).getCh_no()), mNoteList.get(i).getVerse_no(),
                                        input.getText().toString(), SaveSharedPreferences.getNoteNo(mContext));

                                if (result != null) {
                                    Toast.makeText(mContext, R.string.note_updated, Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                    mContext.startActivity(new Intent(mContext, NoteActivity.class));
                                } else {
                                    Toast.makeText(mContext, R.string.review_submitted_error, Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                        alertDialogBuilder.setNegativeButton(R.string.cancel, null);
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

                deleteNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        new AlertDialog.Builder(mContext)
                                .setTitle(R.string.want_to_remove_note)
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dbHelper.addNote(Integer.parseInt(mNoteList.get(i).getNo()), Integer.parseInt(mNoteList.get(i).getCh_no()), mNoteList.get(i).getVerse_no(), null, 0);
//                                        Log.d("NG", VerseActivity.getBook_id()+"  "+ mNoteList.get(i).getCh_no()+"  "+mNoteList.get(i).getVerse_no()+"  ");
                                        mContext.startActivity(new Intent(mContext, NoteActivity.class));
                                        ((NoteActivity) mContext).finish();
                                    }
                                }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create().show();

                    }
                });

                gotoNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        App.getInstance().showInterstitial(((Activity) mContext), false, new App.InterstitialListener() {
                            @Override
                            public void onCloseInterstitial() {
                                dialog.dismiss();
                                Intent intent = new Intent(mContext, VerseActivity.class);
                                intent.putExtra("book_id", mNoteList.get(i).getNo());
                                intent.putExtra("ch_no", mNoteList.get(i).getCh_no());
                                intent.putExtra("book_name", mNoteList.get(i).getName());
                                intent.putExtra("verse_no", mNoteList.get(i).getVerse_no());
                                mContext.startActivity(intent);
                            }
                        });
                    }
                });

                copyNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                        String appLink = "https://play.google.com/store/apps/details?id=" + mContext.getPackageName();
                        String copyVerse = mNoteList.get(i).getVerse_text() + "\n" + mNoteList.get(i).getName() + " " + mNoteList.get(i).getCh_no() + " : " + mNoteList.get(i).getVerse_no() + "\n" + "\n" + appLink;
                        ClipData clip = ClipData.newPlainText("Verse", copyVerse);
                        clipboard.setPrimaryClip(clip);
                        dialog.dismiss();
                        Toast.makeText(mContext, R.string.verse_copy_ok, Toast.LENGTH_SHORT).show();
                    }
                });

                shareNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String appLink = "https://play.google.com/store/apps/details?id=" + mContext.getPackageName();
                        String shareVerse = mNoteList.get(i).getVerse_text() + "\n" + mNoteList.get(i).getName() + " " + mNoteList.get(i).getCh_no() + " : " + mNoteList.get(i).getVerse_no() + "\n" + "\n" + appLink;
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

        verse_head.setText(mNoteList.get(i).getName() + " " + mNoteList.get(i).getCh_no() + " : " + mNoteList.get(i).getVerse_no());
        verse_text.setText(mNoteList.get(i).getVerse_text());
        verse_note.setText(mNoteList.get(i).getVerse_note());
        /*switch (mNoteList.get(i).getColor()) {
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
        }*/

        return v;
    }
}
