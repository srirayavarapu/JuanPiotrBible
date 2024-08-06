package com.biblia.labibliaa.model;

public class Highlight {

    private String name;
    private String color;
    private String ch_no;
    private String verse_no;
    private String verse_text;
    private String book_id;

    public Highlight(String name, String color, String ch_no, String verse_no, String verse_text, String book_id) {
        this.name = name;
        this.color = color;
        this.ch_no = ch_no;
        this.verse_no = verse_no;
        this.verse_text = verse_text;
        this.book_id = book_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

}
