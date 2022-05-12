package com.example.todo_app;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private TaskRepository taskRepository;
    private final LiveData<List<Task>> allTasks, pendingTasks, completedTasks;

    public TaskViewModel(Application application) {
        super(application);
        taskRepository = new TaskRepository(application);
        allTasks = taskRepository.getAllTasks();
        pendingTasks = taskRepository.getPendingTasks();
        completedTasks = taskRepository.getCompletedTasks();
    }

    LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public LiveData<List<Task>> getPendingTasks() {
        return pendingTasks;
    }

    public LiveData<List<Task>> getCompletedTasks() {
        return completedTasks;
    }

    public void insertTask(Task task) {
        taskRepository.insertTask(task);
    }

    public void updateTask (Task task, String taskTitle, String taskDescription, String taskCategory, String reminderDate, String reminderTime) {
        taskRepository.updateTask(task, taskTitle, taskDescription, taskCategory, reminderDate, reminderTime);
    }

    public void deleteTask(Task task) {
        taskRepository.deleteTask(task);
    }

    public void setTaskStatus(Task task) {
        taskRepository.setTaskStatus(task);
    }

    public void deleteAllTasks() {
        taskRepository.deleteAllTasks();
    }

    public void deleteCompletedTasks() {
        taskRepository.deleteCompletedTasks();
    }
}
