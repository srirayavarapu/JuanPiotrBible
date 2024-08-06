package com.biblia.labibliaa.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import com.biblia.labibliaa.model.Book;
import com.biblia.labibliaa.model.Fav;
import com.biblia.labibliaa.model.Highlight;
import com.biblia.labibliaa.model.Note;
import com.biblia.labibliaa.model.Search;
import com.biblia.labibliaa.model.Verse;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME="Bible.db";
    @SuppressLint("SdCardPath")
    public static final String DBLOCATION="/data/data/com.biblia.labibliaa/databases/";
    private final Context mContext;
    private SQLiteDatabase mDatabase;

    public DBHelper(Context context){
        super(context,DBNAME,null,3);
        this.mContext=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.disableWriteAheadLogging();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void openDatabase() {
        String dbPath = mContext.getDatabasePath(DBNAME).getPath();
        if (mDatabase != null && mDatabase.isOpen()) {
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    public void getTableName(SQLiteDatabase database){
        Cursor c = database.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table'", null);

        StringBuilder actual = new StringBuilder();
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                actual.append(c.getString(0)).append(",");
                c.moveToNext();
            }
        }

        Log.d("Table", actual.toString());
        c.close();
    }

    public List<Search> getListofBooks(String fragment, String bookname) {
        Search books = null;
        List<Search> bookList = new ArrayList<>();
        openDatabase();
        switch (fragment) {
            case "book": {
                Cursor cursor = mDatabase.rawQuery("SELECT Books.book_name,Books.book_id,Books.is_new_testament,Verses.chapter_number,Verses.verse_number,Verses.verse_text FROM Books,Verses WHERE Books.book_id=Verses.book_id AND Books.book_name=?", new String[]{String.valueOf(bookname)});
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    books = new Search(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
                    bookList.add(books);
                    cursor.moveToNext();
                }
                cursor.close();
                closeDatabase();
                break;
            }
            case "at": {
                Cursor cursor = mDatabase.rawQuery("SELECT Books.book_name,Books.book_id,Books.is_new_testament,Verses.chapter_number,Verses.verse_number,Verses.verse_text FROM Books,Verses WHERE Books.book_id=Verses.book_id AND Books.is_new_testament='0'", null);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    books = new Search(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
                    bookList.add(books);
                    cursor.moveToNext();
                }
                cursor.close();
                closeDatabase();

                break;
            }
            case "nt": {
                Cursor cursor = mDatabase.rawQuery("SELECT Books.book_name,Books.book_id,Books.is_new_testament,Verses.chapter_number,Verses.verse_number,Verses.verse_text FROM Books,Verses WHERE Books.book_id=Verses.book_id AND Books.is_new_testament='1'", null);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    books = new Search(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
                    bookList.add(books);
                    cursor.moveToNext();
                }
                cursor.close();
                closeDatabase();

                break;
            }
            case "random": {
                Cursor cursor = mDatabase.rawQuery("SELECT Books.book_name,Books.book_id,Books.is_new_testament,Verses.chapter_number,Verses.verse_number,Verses.verse_text FROM Books,Verses WHERE Books.book_id=Verses.book_id ORDER BY RANDOM() LIMIT 1", null);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    books = new Search(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
                    bookList.add(books);
                    cursor.moveToNext();
                }
                cursor.close();
                closeDatabase();

                break;
            }
            default: {
                Cursor cursor = mDatabase.rawQuery("SELECT Books.book_name,Books.book_id,Books.is_new_testament,Verses.chapter_number,Verses.verse_number,Verses.verse_text FROM Books,Verses WHERE Books.book_id=Verses.book_id ", null);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    books = new Search(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
                    bookList.add(books);
                    cursor.moveToNext();
                }
                cursor.close();
                closeDatabase();
                break;
            }
        }

        return bookList;
    }

    public List<Book> getBookName() {
        Book books = null;
        List<Book> bookList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT book_id,book_name,is_new_testament,book_color FROM Books ", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            books = new Book(cursor.getString(0), cursor.getString(1), cursor.getString(2),cursor.getString(3),0);
            bookList.add(books);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return bookList;
    }

    public List<Book> getBookbyCategory(int category) {
        try {
            Book books = null;
            List<Book> bookList = new ArrayList<>();
            openDatabase();
            Cursor cursor = mDatabase.rawQuery("SELECT b.book_id,b.book_name,b.is_new_testament,b.book_color,(SELECT COUNT(1) FROM Chapters c WHERE b.book_id = c.book_id) as chapter_count FROM Books b WHERE is_new_testament = ?", new String[]{String.valueOf(category)});
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                books = new Book(cursor.getString(0), cursor.getString(1), cursor.getString(2),cursor.getString(3), cursor.getInt(4));
                bookList.add(books);
                cursor.moveToNext();
            }
            cursor.close();
            closeDatabase();
            return bookList;
        } catch (Exception e){
            return new ArrayList<>();
        }

    }

    public int getChapterCount(int bookId){
        int count;
        openDatabase();
        Cursor cursor=mDatabase.rawQuery("SELECT COUNT(*) FROM Chapters WHERE book_id = ?", new String[]{String.valueOf(bookId)});
        cursor.moveToFirst();
        count =Integer.parseInt(cursor.getString(0));

        //Only 1 result
        cursor.close();
        closeDatabase();
        return count;

    }

    public List<Verse> getVerseList(int bookId, int chNo) {
        Verse verse = null;
        List<Verse> verseList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT verse_number,verse_text,verse_coloor FROM Verses WHERE chapter_number = " + String.valueOf(chNo)+ " AND book_id =" + String.valueOf(bookId),null );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            verse = new Verse(cursor.getString(0), cursor.getString(1),cursor.getString(2));
            verseList.add(verse);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return verseList;
    }

    public long addAsFav(int book_id, int ch_no, String verse_no, int z,int fav_no) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("is_fav", z);
        contentValues.put("fav_no",fav_no);
        String[] whereArgs = {Integer.toString(book_id), Integer.toString(ch_no),verse_no};
        openDatabase();
        long returnValue = mDatabase.update("Verses",contentValues, "book_id=? AND chapter_number=? AND verse_number=?", whereArgs);
        closeDatabase();
        return returnValue;
    }

    public long highlightVerse(String color, int book_id, int ch_no, String verse_no,int highlight_no) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("verse_coloor", color);
        contentValues.put("highlight_no",highlight_no);
        String[] whereArgs = {Integer.toString(book_id), Integer.toString(ch_no),verse_no};
        openDatabase();
        long returnValue = mDatabase.update("Verses",contentValues, "book_id=? AND chapter_number=? AND verse_number=?", whereArgs);
        closeDatabase();
        return returnValue;
    }

    public long addNote(int book_id, int ch_no, String verse_no, String noteText,int note_no) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("verse_note", noteText);
        contentValues.put("note_no",note_no);
        String[] whereArgs = {Integer.toString(book_id), Integer.toString(ch_no),verse_no};
        openDatabase();
        long returnValue = mDatabase.update("Verses",contentValues, "book_id=? AND chapter_number=? AND verse_number=?", whereArgs);
        closeDatabase();
        return returnValue;
    }

    public List<Fav> getFavList() {
        Fav fav = null;
        List<Fav> favList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT Books.book_name,Verses.verse_coloor,Verses.chapter_number,Verses.verse_number," +
                "Verses.verse_text,Verses.book_id FROM Books,Verses WHERE Books.book_id=Verses.book_id " +
                "AND Verses.is_fav='1' ORDER BY Verses.fav_no DESC",null );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            fav = new Fav(cursor.getString(0), cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5));
            favList.add(fav);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return favList;
    }

    public List<Highlight> getHighlightList() {
        Highlight highlight = null;
        List<Highlight> highlightList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT Books.book_name,Verses.verse_coloor,Verses.chapter_number,Verses.verse_number," +
                "Verses.verse_text,Verses.book_id FROM Books,Verses WHERE Books.book_id=Verses.book_id AND Verses.verse_coloor!='0' ORDER BY Verses.highlight_no DESC",null );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            highlight = new Highlight(cursor.getString(0), cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5));
            highlightList.add(highlight);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return highlightList;
    }


    public List<Note> getNotesList() {
        Note note = null;
        List<Note> noteList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT Books.book_id,Books.book_name,Verses.verse_coloor,Verses.chapter_number,Verses.verse_number," +
                "Verses.verse_text,Verses.verse_note FROM Books,Verses WHERE Books.book_id=Verses.book_id " +
                "AND Verses.verse_note IS NOT NULL ORDER BY Verses.note_no DESC",null );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            note = new Note(cursor.getString(0), cursor.getString(1),cursor.getString(2),
                    cursor.getString(3),cursor.getString(4),cursor.getString(5),
                    cursor.getString(6));
            noteList.add(note);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return noteList;
    }

    public long addBook(Book books) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", books.getId());
        contentValues.put("NAME", books.getName());
        contentValues.put("PRICE", books.getCategory());
        openDatabase();
        long returnValue = mDatabase.insert("PRODUCT", null, contentValues);
        closeDatabase();
        return returnValue;
    }
}
