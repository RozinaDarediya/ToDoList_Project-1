package com.example.admin.todolist.model;

/**
 * Created by Admin on 6/24/2017.
 */

public class FinishedEachrow{
    private String title, date;
    private int id;

    public FinishedEachrow(int id, String title, String date) {
        this.title = title;
        this.date = date;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
