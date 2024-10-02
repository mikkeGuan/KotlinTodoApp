package com.example.kotlintodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlintodo.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {

    private lateinit var todoAdapter: TodoAdapter
    private lateinit var binding: ActivityMainBinding
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup RecyclerView and Adapter
        todoAdapter = TodoAdapter(mutableListOf())
        binding.recyclerviewTodoItems.adapter = todoAdapter
        binding.recyclerviewTodoItems.layoutManager = LinearLayoutManager(this)

        // Fetch data from Firebase when the app starts
        getTodos()

        // Add new Todo
        binding.btnAddTodo.setOnClickListener {
            val todoTitle = binding.edittextTodoTitle.text.toString()
            if (todoTitle.isNotEmpty()) {
                addTodoToFirebase(Todo(todoTitle))
                binding.edittextTodoTitle.text.clear()
            }
        }

        // Delete completed Todos
        binding.btnDeleteTodo.setOnClickListener {
            deleteCompletedTodos()
        }
    }

    // Fetch Todos from Firestore
    private fun getTodos() {
        db.collection("todos").get().addOnSuccessListener { result ->
            val todosList = result.map { document -> document.toObject(Todo::class.java) }
            todoAdapter.updateTodos(todosList)
        }
    }

    // Add a Todo to Firestore
    private fun addTodoToFirebase(todo: Todo) {
        db.collection("todos").add(todo).addOnSuccessListener {
            todoAdapter.addTodo(todo)
        }
    }

    // Delete completed Todos from Firestore
    private fun deleteCompletedTodos() {
        val completedTodos = todoAdapter.getCompletedTodos()
        completedTodos.forEach { todo ->
            db.collection("todos")
                .whereEqualTo("title", todo.title)
                .get()
                .addOnSuccessListener { documents ->
                    documents.forEach { document ->
                        db.collection("todos").document(document.id).delete()
                    }
                }
        }

        todoAdapter.deleteCompletedTodos()
    }
}
