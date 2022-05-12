package com.example.todo_app;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskRepository {

    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks, pendingTasks, completedTasks;

    TaskRepository(Application application) {
        TaskRoomDatabase taskRoomDatabase = TaskRoomDatabase.getDatabase(application);
        taskDao = taskRoomDatabase.taskDao();
        allTasks = taskDao.getAllTasks();
        pendingTasks = taskDao.getPendingTasks();
        completedTasks = taskDao.getCompletedTasks();
    }

    LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    LiveData<List<Task>> getPendingTasks() {
        return pendingTasks;
    }

    public LiveData<List<Task>> getCompletedTasks() {
        return completedTasks;
    }

    void insertTask(Task task) {
        TaskRoomDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.insertTask(task);
        });
    }

    void updateTask(Task task, String taskTitle, String taskDescription, String taskCategory, String reminderDate, String reminderTime) {
        TaskRoomDatabase.databaseWriteExecutor.execute(() -> {
            int taskId = taskDao.getTaskId(task.getTaskTitle(), task.getTaskDescription(), task.getTaskCategory(), task.getReminderDate(), task.getReminderTime(), task.getTaskStatus());
            taskDao.updateTask(taskTitle, taskDescription, taskCategory, reminderDate, reminderTime, taskId);
        });
    }

    void deleteTask(Task task) {
        TaskRoomDatabase.databaseWriteExecutor.execute(() -> {
            int taskId = taskDao.getTaskId(task.getTaskTitle(), task.getTaskDescription(), task.getTaskCategory(), task.getReminderDate(), task.getReminderTime(), task.getTaskStatus());
            taskDao.deleteTask(taskId);
        });
    }

    void setTaskStatus(Task task) {
        TaskRoomDatabase.databaseWriteExecutor.execute(() -> {
            int taskId = taskDao.getTaskId(task.getTaskTitle(), task.getTaskDescription(), task.getTaskCategory(), task.getReminderDate(), task.getReminderTime(), task.getTaskStatus());
            taskDao.setTaskStatus("Completed", taskId);
        });
    }

    void deleteAllTasks() {
        TaskRoomDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.deleteAllTasks();
        });
    }

    void deleteCompletedTasks() {
        TaskRoomDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.deleteCompletedTasks();
        });
    }
}
