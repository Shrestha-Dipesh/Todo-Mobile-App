package com.example.todo_app;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

public class TaskListAdapter extends ListAdapter<Task, TaskViewHolder> {

    public TaskListAdapter(@NonNull DiffUtil.ItemCallback<Task> diffCallback) {
        super(diffCallback);
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return TaskViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task currentTask = getItem(position);
        holder.title(currentTask.getTaskTitle());
        holder.description(currentTask.getTaskDescription());
        holder.category(currentTask.getTaskCategory());
        holder.time(currentTask.getReminderTime());
        holder.status(currentTask.getTaskStatus());
        holder.date(currentTask.getReminderDate());
        holder.setCurrentTask(currentTask);
    }

    static class TaskDiff extends DiffUtil.ItemCallback<Task> {
        @Override
        public boolean areItemsTheSame(@NonNull Task oldTask, @NonNull Task newTask) {
            return oldTask == newTask;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Task oldTask, @NonNull Task newTask) {
            return oldTask.getTaskTitle().equals(newTask.getTaskTitle());
        }
    }
}
