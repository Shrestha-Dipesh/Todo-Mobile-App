package com.example.todo_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.Arrays;

public class TaskActivity extends AppCompatActivity {

    private Spinner spinner_task_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        spinner_task_category = findViewById(R.id.task_category_spinner);
        ArrayList<String> category_list = new ArrayList<>(Arrays.asList("-Select a category-", "Education", "Sports", "Groceries"));
        ArrayAdapter<String> array_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, category_list);
        spinner_task_category.setAdapter(array_adapter);
    }
}