package com.nadolny.pedro.agilelife.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.nadolny.pedro.agilelife.R
import com.nadolny.pedro.agilelife.model.TaskStore
import com.nadolny.pedro.agilelife.utils.DateUtils
import kotlinx.android.synthetic.main.activity_details.*


class DetailsActivity : AppCompatActivity() {

    var taskId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        taskId = intent.getLongExtra("taskId", -1)

        editTaskActionButton.setOnClickListener { editTaskActionButton() }
    }

    override fun onStart() {
        super.onStart()
        val task = TaskStore.getTasks().first { it.id == taskId }
        title = task.title
        descriptionDate.text = DateUtils.extenseStringFrom(task.dueDate)
        descriptionText.text = task.descript
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId

        when (id) {
            R.id.deleteTaskAction -> deleteTaskOptionItem()
            else -> return super.onOptionsItemSelected(item)
        }

        return true
    }

    fun deleteTaskOptionItem() {
        if(!TaskStore.removeTask(taskId)) {
            Toast.makeText(applicationContext, "It wasen't possible to exclude your task :/", Toast.LENGTH_LONG).show()
            return
        }

        finish()
        Toast.makeText(applicationContext, R.string.deleted_task_toast_message, Toast.LENGTH_LONG).show()
    }

    fun editTaskActionButton() {
        val intent = Intent(this, EditTaskActivity::class.java)
        intent.putExtra("taskId", taskId)
        startActivity(intent)
    }
}
