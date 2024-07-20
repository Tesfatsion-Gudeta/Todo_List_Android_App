package com.example.simpletodolistapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {


    private FloatingActionButton add, delete;
    private LinearLayout layout;
    private TextView task;
    private EditText taskName;
    AlertDialog alertDialog;
    TaskDatabase taskDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RoomDatabase.Callback callback = new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
            }

            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                super.onOpen(db);
            }
        };

        taskDB = Room.databaseBuilder(getApplicationContext(), TaskDatabase.class, "taskDB").addCallback(callback).build();
        retriveData();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dailog, null);
        taskName = view.findViewById(R.id.tasknames);


        builder.setView(view);
        builder.setTitle("ADD A TASK");
        builder.setPositiveButton("save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                addCard(taskName.getText().toString());
                Task tasks = new Task(taskName.getText().toString());
                addTaskInBackground(tasks);


            }
        });

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alertDialog = builder.create();
        add = findViewById(R.id.floatingActionButton);
        layout = findViewById(R.id.linearContainer);
        task = findViewById(R.id.textView);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskName.getText().clear();
                alertDialog.show();
            }
        });


    }

    private void addCard(String taskName) {

        View view = getLayoutInflater().inflate(R.layout.tasks, null);
        layout.addView(view);
        task = view.findViewById(R.id.textView2);
        task.setText(taskName);
        task.getText();

        String t = task.getText().toString();

        delete = view.findViewById(R.id.floatingActionButton3);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.removeView(view);
                deleteTaskInBackground(t);


            }
        });


    }


    public void addTaskInBackground(Task task1) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                taskDB.getTaskDAO().addTask(task1);

            }
        });

    }

    public void deleteTaskInBackground(String task2) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                taskDB.getTaskDAO().deleteTaskText(task2);

            }
        });

    }

    public void retriveData() {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.myLooper());
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                List<Task> tasks4 = taskDB.getTaskDAO().getAllTask();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        for (Task task : tasks4) {
                            addCard(task.getText());
                        }

                    }
                });


            }
        });


    }
}