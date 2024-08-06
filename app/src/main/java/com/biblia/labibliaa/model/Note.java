package com.biblia.labibliaa.model;

public class Note {
    private String no;
    private String name;
    private String color;
    private String ch_no;
    private String verse_no;
    private String verse_text;
    private String verse_note;

    public Note(String no,String name, String color, String ch_no, String verse_no, String verse_text, String verse_note) {
        this.no=no;
        this.name = name;
        this.color = color;
        this.ch_no = ch_no;
        this.verse_no = verse_no;
        this.verse_text = verse_text;
        this.verse_note = verse_note;
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

    public String getVerse_note() {
        return verse_note;
    }

    public void setVerse_note(String verse_note) {
        this.verse_note = verse_note;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }
}
