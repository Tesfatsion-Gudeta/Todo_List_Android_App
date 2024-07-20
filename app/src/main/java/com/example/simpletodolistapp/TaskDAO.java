package com.example.simpletodolistapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface TaskDAO {

    @Insert
    public void addTask(Task taskText);

    @Update
    public void updateTask(Task taskText);

    @Delete
    public void deleteTask(Task taskText);

    @Query("select * from Task")
    public List<Task> getAllTask();

    @Query("select * from Task where Task_id==:Task_id")
    public Task getTask(int Task_id);

}
