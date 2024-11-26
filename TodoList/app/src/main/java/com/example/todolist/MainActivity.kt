package com.example.todolist

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var rvTodos: RecyclerView
    private lateinit var adapter: TodoAdapter
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private var todos: MutableList<Todo> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        sharedPreferencesManager = SharedPreferencesManager(this)
        todos = sharedPreferencesManager.getTodos()

        rvTodos = findViewById(R.id.rv_todos)
        adapter = TodoAdapter(this, todos) { position ->
            val selectedTodo = todos[position]
            val intent = Intent(this, TodoDetailActivity::class.java).apply {
                putExtra("TODO", selectedTodo)
                putExtra("TODO_ID", position)
            }
            startActivityForResult(intent, 2)
        }
        rvTodos.layoutManager = LinearLayoutManager(this)
        rvTodos.adapter = adapter

        val fabAddTodo: FloatingActionButton = findViewById(R.id.btn_add_todo)
        fabAddTodo.setOnClickListener {
            val intent = Intent(this, AddTodoActivity::class.java)
            startActivityForResult(intent, 1)
        }

        // Timer button listener to start TimerActivity
        val fabTimer: FloatingActionButton = findViewById(R.id.btn_timer)
        fabTimer.setOnClickListener {
            val intent = Intent(this, TimerActivity::class.java)
            startActivity(intent)
        }

        val alarm: FloatingActionButton = findViewById(R.id.btn_reminder)
        alarm.setOnClickListener {
            val intent = Intent(this, AlarmActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK || requestCode == 2 && resultCode == RESULT_OK) {
            todos = sharedPreferencesManager.getTodos()
            adapter.updateTodos(todos)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "TODO_CHANNEL",
                "Todo Reminder",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}
