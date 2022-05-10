package com.example.todo_app;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertTask(Task task);

    @Query("SELECT * FROM task_list ORDER BY task_title ASC")
    LiveData<List<Task>> getAllTasks();

    @Query("DELETE FROM task_list")
    void deleteAllTasks();
}
