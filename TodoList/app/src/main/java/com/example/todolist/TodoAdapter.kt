package com.example.todolist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TodoAdapter(
    private val context: Context,
    private var todos: List<Todo>,
    private val onItemClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<TodoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todo = todos[position]
        holder.tvTitle.text = todo.title
        holder.tvDescription.text = todo.description

        // Bind the date and time
        holder.tvDate.text = todo.date ?: "No Date"
        holder.tvTime.text = todo.time ?: "No Time"

        // Set click listener to open the detail view
        holder.itemView.setOnClickListener {
            onItemClicked(position)
        }
    }

    override fun getItemCount() = todos.size

    fun updateTodos(updatedTodos: List<Todo>) {
        this.todos = updatedTodos
        notifyDataSetChanged()
    }
}

