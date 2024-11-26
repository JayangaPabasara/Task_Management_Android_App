package com.example.todolist

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("TODO_PREFS", Context.MODE_PRIVATE)
    private val gson = Gson()

    // Existing functions for Todo items
    fun getTodos(): MutableList<Todo> {
        val json = sharedPreferences.getString("todos", null)
        val type = object : TypeToken<MutableList<Todo>>() {}.type
        return if (json != null) {
            gson.fromJson(json, type)
        } else {
            getTodos()
        }
    }

    fun saveTodos(todos: MutableList<Todo>) {
        val editor = sharedPreferences.edit()
        val json = gson.toJson(todos)
        editor.putString("todos", json)
        editor.apply()
    }

    // New functions for Timer

    // Save timer state (time left, pause offset, is running)
    fun saveTimerState(timeLeft: Long, pauseOffset: Long, isRunning: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putLong("timeLeft", timeLeft)
        editor.putLong("pauseOffset", pauseOffset)
        editor.putBoolean("isRunning", isRunning)
        editor.apply()
    }

    // Get the remaining time
    fun getTimeLeft(): Long {
        return sharedPreferences.getLong("timeLeft", 0)
    }

    // Get the pause offset
    fun getPauseOffset(): Long {
        return sharedPreferences.getLong("pauseOffset", 0)
    }

    // Check if the timer is running
    fun isTimerRunning(): Boolean {
        return sharedPreferences.getBoolean("isRunning", false)
    }

    // Clear timer state when the timer is reset or finished
    fun clearTimerState() {
        val editor = sharedPreferences.edit()
        editor.remove("timeLeft")
        editor.remove("pauseOffset")
        editor.remove("isRunning")
        editor.apply()
    }
}
