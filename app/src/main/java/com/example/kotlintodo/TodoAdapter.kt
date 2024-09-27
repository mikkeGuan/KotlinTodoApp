package com.example.kotlintodo

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlintodo.databinding.ItemTodoBinding

data class Todo(
    val title: String,
    var isChecked: Boolean = false
)

class TodoAdapter(
    private val todos: MutableList<Todo>
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    class TodoViewHolder(val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    private fun strikeThrough(textView: TextView, isChecked: Boolean) {
        if (isChecked) {
            textView.paintFlags = textView.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else {
            textView.paintFlags = textView.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentTodo = todos[position]
        holder.binding.apply {
            textviewTodoTitle.text = currentTodo.title
            checkboxDone.isChecked = currentTodo.isChecked
            strikeThrough(textviewTodoTitle, currentTodo.isChecked)
            checkboxDone.setOnCheckedChangeListener { _, isChecked ->
                strikeThrough(textviewTodoTitle, isChecked)
                currentTodo.isChecked = isChecked
            }
        }
    }

    fun addTodo(todo: Todo) {
        todos.add(todo)
        notifyItemInserted(todos.size - 1)
    }

    fun deleteTodos() {
        todos.removeAll { todo ->
            todo.isChecked
        }
        notifyDataSetChanged()
    }
}
