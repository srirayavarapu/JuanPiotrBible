package com.biblia.labibliaa.model;

public class Search {

    private String bookName;
    private String bookId;
    private String category;
    private String ch_no;
    private String verse_no;
    private String verse_text;


    public Search(String bookName, String bookId, String category, String ch_no, String verse_no, String verse_text) {
        this.bookName = bookName;
        this.bookId = bookId;
        this.category = category;
        this.ch_no = ch_no;
        this.verse_no = verse_no;
        this.verse_text = verse_text;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCh_no() {
        return ch_no;
    }

    public void setCh_no(String ch_no) {
        this.ch_no = ch_no;
    }

    public String getVerse_no() {
        return verse_no;
    }

    public void setVerse_no(String verse_no) {
        this.verse_no = verse_no;
    }

    public String getVerse_text() {
        return verse_text;
    }

    public void setVerse_text(String verse_text) {
        this.verse_text = verse_text;
    }

}
