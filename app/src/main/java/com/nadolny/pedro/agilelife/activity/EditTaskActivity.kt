package com.nadolny.pedro.agilelife.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.nadolny.pedro.agilelife.R
import com.nadolny.pedro.agilelife.model.TaskStore
import com.nadolny.pedro.agilelife.utils.DateUtils
import com.nadolny.pedro.agilelife.utils.toEditable
import kotlinx.android.synthetic.main.activity_edit_task.*

class EditTaskActivity : AppCompatActivity() {

    var _id: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)

        saveTaskButton.setOnClickListener { saveTask() }

        _id = intent.getLongExtra("taskId", -1)
        val task = TaskStore.getTasks().first { it.id == _id }

        editActivityTaskName.text = task.title.toEditable()
        editActivityTaskDescription.text = task.descript.toEditable()

        val taskDay = DateUtils.getDayFrom(task.dueDate)
        val taskMonth = DateUtils.getMonthFrom(task.dueDate) - 1
        val taskYear = DateUtils.getYearFrom(task.dueDate)
        editActivityTaskDate.updateDate(taskYear, taskMonth, taskDay)
    }

    private fun saveTask() {
        val taskName = editActivityTaskName.text.toString()
        val taskDesc = editActivityTaskDescription.text.toString()
        val taskDate = DateUtils.dateFrom(editActivityTaskDate)

        if (taskName.isEmpty()) {
            Toast.makeText(this, "Pick a name!", Toast.LENGTH_LONG).show()
            return
        }

        val success = TaskStore.editTask(_id, taskName, taskDate, taskDesc)

        if (!success) {
            Toast.makeText(this, "Ops, it isn't possible to update your task :(", Toast.LENGTH_LONG).show()
            return
        }

        finish()
    }
}