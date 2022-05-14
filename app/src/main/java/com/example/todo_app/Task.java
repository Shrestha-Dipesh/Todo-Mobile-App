package com.example.todo_app;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "task_list")
public class Task implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int taskId;

    @NonNull
    @ColumnInfo(name = "task_title")
    private String taskTitle;

    @NonNull
    @ColumnInfo(name = "task_description")
    private String taskDescription;

    @NonNull
    @ColumnInfo(name = "task_category")
    private String taskCategory;

    @NonNull
    @ColumnInfo(name = "reminder_date")
    private String reminderDate;

    @NonNull
    @ColumnInfo(name = "reminder_time")
    private String reminderTime;

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

    public void setTaskId(int task_id) {
        this.taskId = taskId;
    }

    public int getTaskId() {
        return this.taskId;
    }

    public void setTaskTitle(@NonNull String taskTitle) {
        this.taskTitle = taskTitle;
    }

    @NonNull
    public String getTaskTitle() {
        return this.taskTitle;
    }


    public void setTaskDescription(@NonNull String taskDescription) {
        this.taskDescription = taskDescription;
    }

    @NonNull
    public String getTaskDescription() {
        return this.taskDescription;
    }

    public void setTaskCategory(@NonNull String taskCategory) {
        this.taskCategory = taskCategory;
    }

    @NonNull
    public String getTaskCategory() {
        return this.taskCategory;
    }

    public void setReminderDate(@NonNull String reminderDate) {
        this.reminderDate = reminderDate;
    }

    @NonNull
    public String getReminderDate() {
        return this.reminderDate;
    }

    public void setReminderTime(@NonNull String reminderTime) {
        this.reminderTime = reminderTime;
    }

    @NonNull
    public String getReminderTime() {
        return this.reminderTime;
    }

    @NonNull
    public String getTaskStatus() {
        return this.taskStatus;
    }


}
