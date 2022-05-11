package com.example.todo_app;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskViewHolder extends RecyclerView.ViewHolder {

    private final TextView task_title_textView;
    private final TextView task_description_textView;
    private final TextView task_category_textView;
    private final TextView reminder_day_textView;
    private final TextView reminder_date_textView;
    private final TextView reminder_month_textView;
    private final TextView reminder_time_textView;
    private final TextView task_status_textView;
    private final ImageView task_status_imageView;
    private Task currentTask;

    private TaskViewHolder(View itemView) {
        super(itemView);
        task_title_textView = itemView.findViewById(R.id.task_title_textView);
        task_description_textView = itemView.findViewById(R.id.task_description_textView);
        task_category_textView = itemView.findViewById(R.id.task_category_textView);
        reminder_day_textView = itemView.findViewById(R.id.reminder_day_textView);
        reminder_date_textView = itemView.findViewById(R.id.reminder_date_textView);
        reminder_month_textView = itemView.findViewById(R.id.reminder_month_textView);
        reminder_time_textView = itemView.findViewById(R.id.reminder_time_textView);
        task_status_textView = itemView.findViewById(R.id.task_status_textView);
        ImageView delete_task_imageView = itemView.findViewById(R.id.delete_task_imageView);
        task_status_imageView = itemView.findViewById(R.id.task_status_imageView);

        task_title_textView.setOnClickListener(view -> {
            Context context = itemView.getContext();
            Intent intent = new Intent(context, TaskActivity.class);
            intent.putExtra("CURRENT_TASK", currentTask);
            context.startActivity(intent);
        });

        delete_task_imageView.setOnClickListener(view -> {
            Context context = itemView.getContext();
            TaskViewModel taskViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(TaskViewModel.class);
            taskViewModel.deleteTask(currentTask);
            Toast.makeText(context, "Task Deleted", Toast.LENGTH_SHORT).show();
        });

        task_status_imageView.setOnClickListener(view -> {
            Context context = itemView.getContext();
            TaskViewModel taskViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(TaskViewModel.class);
            taskViewModel.setTaskStatus(currentTask);
            Toast.makeText(context, "Task Completed", Toast.LENGTH_SHORT).show();
        });
    }

    public void title(String title) {
        task_title_textView.setText(title);
    }

    public void description(String description) {
        task_description_textView.setText(description);
    }

    public void category(String category) {
        task_category_textView.setText("Category: " + category);
    }

    public void time(String time) {
        reminder_time_textView.setText(time);
    }

    public void status(String status) {
        task_status_textView.setText(status);
        if (status.equals("Completed")) {
            Context context = itemView.getContext();
            task_status_textView.setTextColor(context.getResources().getColor(R.color.green));
            task_status_imageView.setColorFilter(context.getResources().getColor(R.color.green));
            task_status_imageView.setAlpha(0.8F);
        }
    }

    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }

    public void date(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EE dd MMM yyyy", Locale.US);
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        try {
            Date parsedDate= inputDateFormat.parse(date);
            String outputDateString = dateFormat.format(parsedDate);
            String[] dateParts = outputDateString.split(" ");
            reminder_day_textView.setText(dateParts[0]);
            reminder_date_textView.setText(dateParts[1]);
            reminder_month_textView.setText(dateParts[2]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    static TaskViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item_recyclerview, parent, false);
        return new TaskViewHolder(view);
    }
}
