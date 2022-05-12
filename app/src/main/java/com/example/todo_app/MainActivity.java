package com.example.todo_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab_add_task;
    public static final int NEW_TASK_ACTIVITY_REQUEST_CODE = 1;
    private TaskViewModel taskViewModel;
    private RecyclerView recyclerView;
    private final TaskListAdapter taskListAdapter = new TaskListAdapter(new TaskListAdapter.TaskDiff());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(taskListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(this, tasks -> {
            taskListAdapter.submitList(tasks);
        });

        fab_add_task = findViewById(R.id.add_task_fab);
        fab_add_task.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, TaskActivity.class);
            startActivityForResult(intent, NEW_TASK_ACTIVITY_REQUEST_CODE);
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == NEW_TASK_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle bundle = intent.getExtras();

            String taskTitle = bundle.getString("TASK_TITLE");
            String taskDescription = bundle.getString("TASK_DESCRIPTION");
            String taskCategory = bundle.getString("TASK_CATEGORY");
            String reminderDate = bundle.getString("REMINDER_DATE");
            String reminderTime = bundle.getString("REMINDER_TIME");

            Task task = new Task(taskTitle, taskDescription, taskCategory, reminderDate, reminderTime, "Pending");
            taskViewModel.insertTask(task);
        }
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            switch(direction) {
                case ItemTouchHelper.RIGHT:
                    taskViewModel.setTaskStatus(taskListAdapter.getCurrentTask());
                    Toast.makeText(MainActivity.this, "Task Completed", Toast.LENGTH_SHORT).show();
                    break;

                case ItemTouchHelper.LEFT:
                    Task deletedTask = taskListAdapter.getCurrentTask();
                    taskViewModel.deleteTask(taskListAdapter.getCurrentTask());
                    Snackbar.make(recyclerView, "Task Deleted", Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    taskViewModel.insertTask(deletedTask);
                                    Toast.makeText(MainActivity.this, "Task Restored", Toast.LENGTH_SHORT).show();
                                }
                            }).show();
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(getResources().getColor(R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete_task)
                    .addSwipeRightBackgroundColor(getResources().getColor(R.color.green))
                    .addSwipeRightActionIcon(R.drawable.ic_complete_task)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };
}