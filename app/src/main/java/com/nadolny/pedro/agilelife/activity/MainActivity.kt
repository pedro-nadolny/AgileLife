package com.nadolny.pedro.agilelife.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.nadolny.pedro.agilelife.R
import com.nadolny.pedro.agilelife.adapters.MainAdapter
import com.nadolny.pedro.agilelife.model.TaskStore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainList.setHasFixedSize(true)
        mainList.layoutManager = LinearLayoutManager(this)

        val listener = { pos: Int ->
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("taskIndex", pos)
            startActivity(intent)
        }

        mainList.adapter = MainAdapter(listener, applicationContext)
        TaskStore.loadDatabase(applicationContext)
    }

    override fun onStart() {
        super.onStart()
        mainList.adapter?.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId

        when (id) {
            R.id.newTaskAction -> createTaskButtonAction()
            R.id.wipeDataAction -> wipeButtonAction()
            else -> return super.onOptionsItemSelected(item)
        }

        return true
    }

    private fun wipeButtonAction() {
        TaskStore.clearTasks()
        mainList.adapter?.notifyDataSetChanged()
    }

    private fun createTaskButtonAction() {
        val intent = Intent(this, CreateTaskActivity::class.java)
        startActivity(intent)
    }
}