package com.biblia.labibliaa.model;

public class Verse {

    private String verseNo;
    private String verseText;
    private String color;

    public Verse(String verseNo, String verseText, String color) {
        this.verseNo = verseNo;
        this.verseText = verseText;
        this.color=color;
    }

    public String getVerseNo() {
        return verseNo;
    }

    public void setVerseNo(String verseNo) {
        this.verseNo = verseNo;
    }

    public String getVerseText() {
        return verseText;
    }

    public void setVerseText(String verseText) {
        this.verseText = verseText;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
