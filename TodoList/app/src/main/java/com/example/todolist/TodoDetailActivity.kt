package com.example.todolist

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class TodoDetailActivity : AppCompatActivity() {

    private lateinit var edtTitle: EditText
    private lateinit var edtDescription: EditText
    private lateinit var edtDate: EditText
    private lateinit var edtTime: EditText
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private var todoId: Int = -1
    private lateinit var todos: MutableList<Todo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_detail)

        sharedPreferencesManager = SharedPreferencesManager(this)
        todos = sharedPreferencesManager.getTodos()

        edtTitle = findViewById(R.id.edt_title)
        edtDescription = findViewById(R.id.edt_description)
        edtDate = findViewById(R.id.edt_date)
        edtTime = findViewById(R.id.edt_time)
        btnUpdate = findViewById(R.id.btn_update)
        btnDelete = findViewById(R.id.btn_delete)

        todoId = intent.getIntExtra("TODO_ID", -1)
        val todo = intent.getParcelableExtra<Todo>("TODO")

        todo?.let {
            edtTitle.setText(it.title)
            edtDescription.setText(it.description)
            edtDate.setText(it.date ?: "")
            edtTime.setText(it.time ?: "")
        }

        // DatePicker Dialog
        edtDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                edtDate.setText(selectedDate)
            }, year, month, day)

            datePickerDialog.show()
        }

        // TimePicker Dialog
        edtTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                edtTime.setText(selectedTime)
            }, hour, minute, true)

            timePickerDialog.show()
        }

        btnUpdate.setOnClickListener {
            val updatedTodo = Todo(
                edtTitle.text.toString(),
                edtDescription.text.toString(),
                edtDate.text.toString(),  // Get updated date
                edtTime.text.toString()   // Get updated time
            )
            todos[todoId] = updatedTodo
            sharedPreferencesManager.saveTodos(todos)

            Toast.makeText(this, "To-Do updated successfully!", Toast.LENGTH_SHORT).show()
            setResult(RESULT_OK)
            finish()
        }

        btnDelete.setOnClickListener {
            todos.removeAt(todoId)
            sharedPreferencesManager.saveTodos(todos)

            Toast.makeText(this, "To-Do deleted successfully!", Toast.LENGTH_SHORT).show()
            setResult(RESULT_OK)
            finish()
        }
    }
}
