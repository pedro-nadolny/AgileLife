package com.nadolny.pedro.agilelife.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.nadolny.pedro.agilelife.R
import com.nadolny.pedro.agilelife.model.TaskStore
import com.nadolny.pedro.agilelife.utils.DateUtils
import kotlinx.android.synthetic.main.activity_details.*


class DetailsActivity : AppCompatActivity() {

    var taskIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        taskIndex = intent.getIntExtra("taskIndex", 0)
    }

    override fun onStart() {
        super.onStart()
        val task = TaskStore.getTasks().get(taskIndex)
        title = task.title
        descriptionDate.text = DateUtils.stringFrom(task.dueDate)
        descriptionText.text = task.descript
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        if (id === null) { return false }

        if (id == R.id.editTaskAction) {
            val intent = Intent(this, EditTaskActivity::class.java)
            intent.putExtra("taskIndex", taskIndex)
            startActivity(intent)
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
