package com.example.todo_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity {

    public static final int NEW_TASK_ACTIVITY_REQUEST_CODE = 1;
    private TaskViewModel taskViewModel;
    private RecyclerView recyclerView;
    private final TaskListAdapter taskListAdapter = new TaskListAdapter(new TaskListAdapter.TaskDiff());
    private AlertDialog alertDialog;
    private TextView textView_total_tasks, textView_pending_tasks, textView_completed_tasks;
    private String selectedTask = "Total";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set recyclerview adapter
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(taskListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Set the total, pending and completed tasks count
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        textView_total_tasks = findViewById(R.id.total_tasks_textView);
        taskViewModel.getAllTasks().observe(this, tasks -> {
            String totalTasksCount = String.valueOf(taskViewModel.getAllTasks().getValue().size());
            textView_total_tasks.setText("Total: " + totalTasksCount);
            textView_total_tasks.setTextColor(getResources().getColor(R.color.primary_blue));
        });

        textView_pending_tasks = findViewById(R.id.pending_tasks_textView);
        taskViewModel.getPendingTasks().observe(this, tasks -> {
            String pendingTasksCount = String.valueOf(taskViewModel.getPendingTasks().getValue().size());
            textView_pending_tasks.setText("Pending: " + pendingTasksCount);
        });

        textView_completed_tasks = findViewById(R.id.completed_tasks_textView);
        taskViewModel.getCompletedTasks().observe(this, tasks -> {
            String completedTasksCount = String.valueOf(taskViewModel.getCompletedTasks().getValue().size());
            textView_completed_tasks.setText("Completed: " + completedTasksCount);
        });

        //Launch add new task activity
        FloatingActionButton fab_add_task = findViewById(R.id.add_task_fab);
        fab_add_task.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, TaskActivity.class);
            startActivityForResult(intent, NEW_TASK_ACTIVITY_REQUEST_CODE);
        });

        //Display the task delete options
        FloatingActionButton fab_delete_tasks = findViewById(R.id.delete_tasks_fab);
        fab_delete_tasks.setOnClickListener(view -> {
            showDeleteDialog();
        });

        //Display the selected task category
        switch (selectedTask) {
            case "Total":
                taskViewModel.getAllTasks().observe(this, taskListAdapter::submitList);
                break;
            case "Pending":
                taskViewModel.getPendingTasks().observe(this, taskListAdapter::submitList);
                break;
            case "Completed":
                taskViewModel.getCompletedTasks().observe(this, taskListAdapter::submitList);
                ;
                break;
        }

        //Attach ItemTouchHelper to recyclerview
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        //Display the clicked task category list
        textView_total_tasks.setOnClickListener(view -> {
            taskViewModel.getAllTasks().observe(this, tasks -> {
                taskListAdapter.submitList(tasks);
                selectedTask = "Total";
                textView_total_tasks.setTextColor(getResources().getColor(R.color.primary_blue));
                textView_pending_tasks.setTextColor(getResources().getColor(R.color.gray));
                textView_completed_tasks.setTextColor(getResources().getColor(R.color.gray));
            });
        });

        textView_pending_tasks.setOnClickListener(view -> {
            taskViewModel.getPendingTasks().observe(this, tasks -> {
                taskListAdapter.submitList(tasks);
                selectedTask = "Pending";
                textView_pending_tasks.setTextColor(getResources().getColor(R.color.primary_blue));
                textView_total_tasks.setTextColor(getResources().getColor(R.color.gray));
                textView_completed_tasks.setTextColor(getResources().getColor(R.color.gray));
            });
        });

        textView_completed_tasks.setOnClickListener(view -> {
            taskViewModel.getCompletedTasks().observe(this, tasks -> {
                taskListAdapter.submitList(tasks);
                selectedTask = "Completed";
                textView_completed_tasks.setTextColor(getResources().getColor(R.color.primary_blue));
                textView_total_tasks.setTextColor(getResources().getColor(R.color.gray));
                textView_pending_tasks.setTextColor(getResources().getColor(R.color.gray));
            });
        });

        //Save and load the app theme setting
        SharedPreferences sharedPreferences = getSharedPreferences("AppSettingPrefs", 0);
        boolean isDarkMode = sharedPreferences.getBoolean("NightMode", false);
        SharedPreferences.Editor sharedPreferenceEdit = sharedPreferences.edit();

        //Set the theme of the app
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        //Toggle the theme of the app
        FloatingActionButton fab_dark_mode = findViewById(R.id.dark_mode_fab);
        fab_dark_mode.setOnClickListener(view -> {
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                sharedPreferenceEdit.putBoolean("NightMode", false);
                Toast.makeText(MainActivity.this, "Dark Mode Disabled", Toast.LENGTH_SHORT).show();

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                sharedPreferenceEdit.putBoolean("NightMode", true);
                Toast.makeText(MainActivity.this, "Dark Mode Enabled", Toast.LENGTH_SHORT).show();
            }
            sharedPreferenceEdit.apply();
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        //Add the received task to database
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
            //Get the list of selected task category
            LiveData<List<Task>> liveTaskList = null;
            switch (selectedTask) {
                case "Total":
                    liveTaskList = taskViewModel.getAllTasks();
                    break;
                case "Pending":
                    liveTaskList = taskViewModel.getPendingTasks();
                    break;
                case "Completed":
                    liveTaskList = taskViewModel.getCompletedTasks();
                    break;
            }

            //Get the swiped task
            List<Task> taskList = liveTaskList.getValue();
            Task task = taskList.get(viewHolder.getPosition());

            switch (direction) {
                case ItemTouchHelper.RIGHT:
                    taskViewModel.setTaskStatus(task);
                    Toast.makeText(MainActivity.this, "Task Completed", Toast.LENGTH_SHORT).show();
                    break;

                case ItemTouchHelper.LEFT:
                    Task deletedTask = task;
                    taskViewModel.deleteTask(task);

                    //Undo the deletion of task using snackbar
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
            //Decorate the swipe actions
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(getResources().getColor(R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete_task)
                    .setActionIconTint(getResources().getColor(R.color.white))
                    .addSwipeRightBackgroundColor(getResources().getColor(R.color.green))
                    .addSwipeRightActionIcon(R.drawable.ic_complete_task)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    public void showDeleteDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        final View deleteDialogView = getLayoutInflater().inflate(R.layout.delete_popup, null);
        ImageView imageView_cancel = deleteDialogView.findViewById(R.id.cancel_imageView);
        Button button_all_tasks = deleteDialogView.findViewById(R.id.all_tasks_button);
        Button button_completed_tasks = deleteDialogView.findViewById(R.id.completed_tasks_button);

        //Display the delete options popup
        alertDialogBuilder.setView(deleteDialogView);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        //Dismiss the popup when X is clicked
        imageView_cancel.setOnClickListener(view -> {
            alertDialog.dismiss();
        });

        //Delete all tasks
        button_all_tasks.setOnClickListener(view -> {
            taskViewModel.deleteAllTasks();
            alertDialog.dismiss();
            Toast.makeText(this, "All Tasks Deleted", Toast.LENGTH_SHORT).show();
        });

        //Delete completed tasks
        button_completed_tasks.setOnClickListener(view -> {
            taskViewModel.deleteCompletedTasks();
            alertDialog.dismiss();
            Toast.makeText(this, "Completed Tasks Removed", Toast.LENGTH_SHORT).show();
        });
    }
}