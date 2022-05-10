package com.example.todo_app;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "task_list")
public class Task {

    @PrimaryKey(autoGenerate = true)
    private int taskId;

    @NonNull
    @ColumnInfo(name = "task_title")
    private final String taskTitle;

    public Task(@NonNull String taskTitle) {
        this.taskTitle = taskTitle;
    }

    @NonNull
    public String getTaskTitle() {
        return this.taskTitle;
    }

    public void setTaskId(int task_id) {
        this.taskId = taskId;
    }

    public int getTaskId() {
        return this.taskId;
    }
}
