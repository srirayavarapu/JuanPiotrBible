package com.biblia.labibliaa;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveSharedPreferences {

    Context context;
    SharedPreferences sharedPreferences;

    public SaveSharedPreferences(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("Apreferences", Context.MODE_PRIVATE);
    }

    public void setState(Boolean bo){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("editor", bo);
        editor.apply();
    }

    public boolean getState(){
        return sharedPreferences.getBoolean("editor", false);
    }

    public static void saveFontSize(Context context, int fontSize){
        SharedPreferences pref = context.getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("FONT_SIZE", fontSize);
        editor.apply();
    }

    public static int getFontSize(Context context){
        SharedPreferences pref = context.getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE);
        return pref.getInt("FONT_SIZE",19);
    }

    public static void saveFavNo(Context context,int fav_no){
        SharedPreferences preferences=context.getSharedPreferences("FAV_NO_PREF",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putInt("FAV_NO",fav_no);
        editor.apply();
    }

    public static void saveHighlightNo(Context context,int highlight_no){
        SharedPreferences preferences=context.getSharedPreferences("HIGHLIGHT_NO_PREF",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putInt("HIGHLIGHT_NO",highlight_no);
        editor.apply();
    }

    public static void saveNoteNo(Context context,int note_no){
        SharedPreferences preferences=context.getSharedPreferences("NOTE_NO_PREF",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putInt("NOTE_NO",note_no);
        editor.apply();
    }

    public static int getFavNo(Context context){
        SharedPreferences preferences = context.getSharedPreferences("FAV_NO_PREF", Context.MODE_PRIVATE);
        return preferences.getInt("FAV_NO",0);
    }

    public static int getHighlightNo(Context context){
        SharedPreferences preferences = context.getSharedPreferences("HIGHLIGHT_NO_PREF", Context.MODE_PRIVATE);
        return preferences.getInt("HIGHLIGHT_NO",0);
    }

    public static int getNoteNo(Context context){
        SharedPreferences preferences = context.getSharedPreferences("NOTE_NO_PREF", Context.MODE_PRIVATE);
        return preferences.getInt("NOTE_NO",0);
    }
}
