package com.example.minimaltodolist

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var editTextTask: EditText
    private lateinit var buttonAdd: Button
    private lateinit var recyclerViewTasks: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private val taskList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextTask = findViewById(R.id.editTextTask)
        buttonAdd = findViewById(R.id.buttonAdd)
        recyclerViewTasks = findViewById(R.id.recyclerViewTasks)

        taskAdapter = TaskAdapter(taskList) { position ->
            taskList.removeAt(position)
            taskAdapter.notifyItemRemoved(position)
        }

        recyclerViewTasks.layoutManager = LinearLayoutManager(this)
        recyclerViewTasks.adapter = taskAdapter

        buttonAdd.setOnClickListener {
            val task = editTextTask.text.toString().trim()
            if (TextUtils.isEmpty(task)) {
                Toast.makeText(this, "Task cannot be empty", Toast.LENGTH_SHORT).show()
            } else {
                taskList.add(task)
                taskAdapter.notifyItemInserted(taskList.size - 1)
                editTextTask.text.clear()
            }
        }
    }
}

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val tasks: List<String>,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTask: TextView = itemView.findViewById(android.R.id.text1)
        val buttonDelete: ImageButton = itemView.findViewById(android.R.id.icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.textSize = 16f

        val deleteButton = ImageButton(parent.context)
        deleteButton.setImageResource(android.R.drawable.ic_menu_delete)
        deleteButton.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        (view as ViewGroup).addView(deleteButton)

        return TaskViewHolder(view).apply {
            buttonDelete.setOnClickListener {
                onDeleteClick(adapterPosition)
            }
        }
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.textViewTask.text = tasks[position]
    }

    override fun getItemCount(): Int = tasks.size
}