package com.example.todo_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class TaskActivity extends AppCompatActivity {

    private EditText editText_task_title;
    private EditText editText_task_description;
    private Spinner spinner_task_category;
    private EditText editText_reminder_date;
    private EditText editText_reminder_time;
    private DatePickerDialog.OnDateSetListener setDateListener;
    private int hour, minute;
    public static final String EXTRA_REPLY = "com.example.todo_app.REPLY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        //Create and add item to spinner
        spinner_task_category = findViewById(R.id.task_category_spinner);
        ArrayList<String> categoryList = new ArrayList<>(Arrays.asList("-Select a category-", "Education", "Work", "Groceries", "Sports", "Miscellaneous"));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoryList);
        spinner_task_category.setAdapter(arrayAdapter);

        //Create a calendar
        editText_reminder_date = findViewById(R.id.reminder_date_editText);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        //Display calendar on date textview click
        editText_reminder_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(TaskActivity.this, R.style.DialogTheme, setDateListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                datePickerDialog.show();
                datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.primary_blue));
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.primary_blue));
            }
        });

        //Set the date on the textview
        setDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                editText_reminder_date.setText(date);
            }
        };

        //Set the time on the textview
        editText_reminder_time = findViewById(R.id.reminder_time_editText);
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selected_hour, int selected_minute) {
                hour = selected_hour;
                minute = selected_minute;
                editText_reminder_time.setText(String.format(Locale.getDefault(), "%02d : %02d", hour, minute));
            }
        };

        //Display clock on time textview click
        editText_reminder_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(TaskActivity.this, R.style.DialogTheme, onTimeSetListener, hour, minute, true);
                timePickerDialog.show();
                timePickerDialog.getButton(TimePickerDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.primary_blue));
                timePickerDialog.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.primary_blue));
            }
        });

        //Add task when button is clicked
        editText_task_title = findViewById(R.id.task_title_editText);
        editText_task_description = findViewById(R.id.task_description_editText);
        final Button button_add_task = findViewById(R.id.add_task_button);
        button_add_task.setOnClickListener(view -> {
            //Create an intent bundle and pass all task details if not null
            if (TextUtils.isEmpty(editText_task_title.getText()) || TextUtils.isEmpty(editText_task_description.getText()) || spinner_task_category.getSelectedItem().toString().equals("-Select a category-") || TextUtils.isEmpty(editText_reminder_date.getText()) || TextUtils.isEmpty(editText_reminder_time.getText())) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            } else {
                Intent replyIntent = new Intent();
                Bundle replyBundle = new Bundle();
                String taskTitle = editText_task_title.getText().toString();
                replyBundle.putString("TASK_TITLE", taskTitle);

                String taskDescription = editText_task_description.getText().toString();
                replyBundle.putString("TASK_DESCRIPTION", taskDescription);

                String taskCategory = spinner_task_category.getSelectedItem().toString();
                replyBundle.putString("TASK_CATEGORY", taskCategory);

                String reminderDate = editText_reminder_date.getText().toString();
                replyBundle.putString("REMINDER_DATE", reminderDate);

                String reminderTime = editText_reminder_time.getText().toString();
                replyBundle.putString("REMINDER_TIME", reminderTime);

                replyIntent.putExtras(replyBundle);
                setResult(RESULT_OK, replyIntent);
                finish();
            }

        });

        //Fill in the details if task is to be updated
        Task currentTask = (Task) getIntent().getSerializableExtra("CURRENT_TASK");
        if (currentTask != null) {
            TextView textView_add_task = findViewById(R.id.add_task_textview);
            textView_add_task.setText("Update Task");
            TextView textView_fill = findViewById(R.id.fill_textview);
            textView_fill.setText("Fill the details below to update task in your TODO");
            button_add_task.setText("Update Task");
            editText_task_title.setText(currentTask.getTaskTitle());
            editText_task_description.setText(currentTask.getTaskDescription());
            editText_reminder_date.setText(currentTask.getReminderDate());
            editText_reminder_time.setText(currentTask.getReminderTime());

            int spinnerPosition = 0;
            switch (currentTask.getTaskCategory()) {
                case "Education":
                    spinnerPosition = 1;
                    break;

                case "Work":
                    spinnerPosition = 2;
                    break;

                case "Groceries":
                    spinnerPosition = 3;
                    break;

                case "Sports":
                    spinnerPosition = 4;
                    break;

                case "Miscellaneous":
                    spinnerPosition = 5;
                    break;
            }

            spinner_task_category.setSelection(spinnerPosition);

            //Pass the updated task details to main activity
            button_add_task.setOnClickListener(view -> {
                if (TextUtils.isEmpty(editText_task_title.getText()) || TextUtils.isEmpty(editText_task_description.getText()) || spinner_task_category.getSelectedItem().toString().equals("-Select a category-") || TextUtils.isEmpty(editText_reminder_date.getText()) || TextUtils.isEmpty(editText_reminder_time.getText())) {
                    Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(TaskActivity.this, MainActivity.class);
                    TaskViewModel taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
                    String taskTitle = editText_task_title.getText().toString();
                    String taskDescription = editText_task_description.getText().toString();
                    String taskCategory = spinner_task_category.getSelectedItem().toString();
                    String reminderDate = editText_reminder_date.getText().toString();
                    String reminderTime = editText_reminder_time.getText().toString();
                    taskViewModel.updateTask(currentTask, taskTitle, taskDescription, taskCategory, reminderDate, reminderTime);
                    startActivity(intent);
                }
            });
        }
    }
}