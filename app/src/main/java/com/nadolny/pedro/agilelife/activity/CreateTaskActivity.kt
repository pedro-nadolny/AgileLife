package com.nadolny.pedro.agilelife.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.nadolny.pedro.agilelife.R
import com.nadolny.pedro.agilelife.model.TaskStore
import com.nadolny.pedro.agilelife.utils.DateUtils

import kotlinx.android.synthetic.main.activity_create_task.*
import java.util.*

class CreateTaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)
        createTaskButton.setOnClickListener { createTask() }
    }

    fun createTask() {
        val taskName = taskName.text.toString()
        val taskDesc = taskDescription.text.toString()
        val taskDate = DateUtils.dateFrom(taskDate)

        if (taskName.isEmpty()) {
            Toast.makeText(this, "Insira um nome!", Toast.LENGTH_LONG).show()
            return
        }

        if (taskDesc.isEmpty()) {
            Toast.makeText(this, "Insira uma descrição!", Toast.LENGTH_LONG).show()
            return
        }

        TaskStore.newTask(taskName, taskDate, taskDesc)
        finish()
    }
}
