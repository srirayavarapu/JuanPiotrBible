package com.biblia.labibliaa.model;

public class Book {

    private String id;
    private String name;
    private String category;
    private String color;
    private int chapter_count;

    public Book(String id, String name, String category,String color, int chapter_count) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.color=color;
        this.chapter_count = chapter_count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getChapter_count() {
        return chapter_count;
    }

    public void setChapter_count(int chapter_count) {
        this.chapter_count = chapter_count;
    }
}
