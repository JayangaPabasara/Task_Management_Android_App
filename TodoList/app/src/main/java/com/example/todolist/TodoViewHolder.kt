package com.example.todolist

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvTitle: TextView = itemView.findViewById(R.id.tv_todo_title)
    val tvDescription: TextView = itemView.findViewById(R.id.tv_todo_description)
    val tvDate: TextView = itemView.findViewById(R.id.tv_todo_date)
    val tvTime: TextView = itemView.findViewById(R.id.tv_todo_time)
    val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
}
