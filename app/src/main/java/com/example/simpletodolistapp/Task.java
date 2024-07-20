package com.example.simpletodolistapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Task")
public class Task {

    @ColumnInfo(name = "Task_id")
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "text")
    String text;

    @Ignore
    public Task() {
    }

    public Task(String text) {
        this.text = text;
        this.id = 0;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
