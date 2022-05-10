package com.example.todo_app;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "task_list")
public class Task {

    @PrimaryKey(autoGenerate = true)
    private int taskId;

    @NonNull
    @ColumnInfo(name = "task_title")
    private final String taskTitle;

    @NonNull
    @ColumnInfo(name = "task_description")
    private final String taskDescription;

    @NonNull
    @ColumnInfo(name = "task_category")
    private final String taskCategory;

    @NonNull
    @ColumnInfo(name = "reminder_date")
    private final String reminderDate;

    @NonNull
    @ColumnInfo(name = "reminder_time")
    private final String reminderTime;

    @NonNull
    @ColumnInfo(name = "task_status")
    private final String taskStatus;

    public Task(@NonNull String taskTitle, @NonNull String taskDescription, @NonNull String taskCategory, @NonNull String reminderDate, @NonNull String reminderTime, @NonNull String taskStatus) {
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.taskCategory = taskCategory;
        this.reminderDate = reminderDate;
        this.reminderTime = reminderTime;
        this.taskStatus = taskStatus;
    }

    public int getTaskId() {
        return this.taskId;
    }

    @NonNull
    public String getTaskTitle() {
        return this.taskTitle;
    }

    @NonNull
    public String getTaskDescription() {
        return this.taskDescription;
    }

    @NonNull
    public String getTaskCategory() {
        return this.taskCategory;
    }

    @NonNull
    public String getReminderDate() {
        return this.reminderDate;
    }

    @NonNull
    public String getReminderTime() {
        return this.reminderTime;
    }

    @NonNull
    public String getTaskStatus() {
        return this.taskStatus;
    }

    public void setTaskId(int task_id) {
        this.taskId = taskId;
    }


}
