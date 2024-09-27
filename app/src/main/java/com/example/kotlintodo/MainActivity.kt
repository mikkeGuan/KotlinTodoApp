package com.example.kotlintodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlintodo.databinding.ActivityMainBinding

class MainActivity : ComponentActivity() {

    private lateinit var todoAdapter: TodoAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        todoAdapter = TodoAdapter(mutableListOf())
        binding.recyclerviewTodoItems.adapter = todoAdapter
        binding.recyclerviewTodoItems.layoutManager = LinearLayoutManager(this)

        binding.btnAddTodo.setOnClickListener {
            val todoTitle = binding.edittextTodoTitle.text.toString()
            if (todoTitle.isNotEmpty()) {
                val todo = Todo(todoTitle)
                todoAdapter.addTodo(todo)
                binding.edittextTodoTitle.text.clear()
            }
        }

        binding.btnDeleteTodo.setOnClickListener {
            todoAdapter.deleteTodos()
        }
    }
}
