package com.example.todo_app;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertTask(Task task);

    @Query("UPDATE task_list SET task_title = :taskTitle,  task_description = :taskDescription, task_category = :taskCategory, reminder_date = :reminderDate, reminder_time = :reminderTime WHERE taskId = :taskId")
    void updateTask(String taskTitle, String taskDescription, String taskCategory, String reminderDate, String reminderTime, int taskId);

    @Query("DELETE FROM task_list WHERE taskId = :taskId")
    void deleteTask(int taskId);

    @Query("SELECT * FROM task_list ORDER BY reminder_date ASC")
    LiveData<List<Task>> getAllTasks();

    @Query("SELECT taskId FROM task_list WHERE task_title = :taskTitle AND task_description = :taskDescription AND task_category = :taskCategory AND reminder_date = :reminderDate AND reminder_time = :reminderTime AND task_status = :taskStatus")
    int getTaskId(String taskTitle, String taskDescription, String taskCategory, String reminderDate, String reminderTime, String taskStatus);

    @Query("UPDATE task_list SET task_status = :taskStatus WHERE taskId = :taskId")
    void setTaskStatus(String taskStatus, int taskId);

    @Query("DELETE FROM task_list")
    void deleteAllTasks();




}
