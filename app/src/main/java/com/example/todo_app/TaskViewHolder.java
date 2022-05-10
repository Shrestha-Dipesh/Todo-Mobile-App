package com.example.todo_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class TaskViewHolder extends RecyclerView.ViewHolder {

    private final TextView task_item_textView;

    private TaskViewHolder(View itemView) {
        super(itemView);
        task_item_textView = itemView.findViewById(R.id.task_item_textView);
    }

    public void bind(String text) {
        task_item_textView.setText(text);
    }

    static TaskViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item_recyclerview, parent, false);
        return new TaskViewHolder(view);
    }
}
