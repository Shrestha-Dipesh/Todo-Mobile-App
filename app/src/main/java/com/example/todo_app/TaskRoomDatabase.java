package com.example.todo_app;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Task.class}, version = 1, exportSchema = false)
public abstract class TaskRoomDatabase extends RoomDatabase {

    public abstract TaskDao taskDao();

    public static volatile TaskRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static TaskRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TaskRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TaskRoomDatabase.class, "task_database").addCallback(roomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase database) {
            super.onCreate(database);

            databaseWriteExecutor.execute(() -> {
                TaskDao task_dao = INSTANCE.taskDao();
                task_dao.deleteAllTasks();

                Task task = new Task("Drink Water", "Remember to drink water", "Sports","10/05/2022", "06 : 25", "Pending");
                task_dao.insertTask(task);

                task = new Task("Read Book", "Remember to read book", "Education", "11/11/2022", "03 : 20", "Pending");
                task_dao.insertTask(task);

                task = new Task("Read Book", "Remember to read book", "Education", "11/11/2022", "03 : 20", "Pending");
                task_dao.insertTask(task);
            });
        }
    };
}
