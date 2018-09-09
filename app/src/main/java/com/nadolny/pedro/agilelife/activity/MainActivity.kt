package com.nadolny.pedro.agilelife.activity

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.DatePicker
import com.nadolny.pedro.agilelife.R
import com.nadolny.pedro.agilelife.adapters.MainAdapter
import com.nadolny.pedro.agilelife.model.TaskStore
import com.nadolny.pedro.agilelife.utils.DateUtils
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private var menu: Menu? = null

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

        newTaskActionButton.setOnClickListener { createTaskButtonAction() }
    }

    override fun onStart() {
        super.onStart()
        mainList.adapter?.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        if (menu == null) {
            return super.onCreateOptionsMenu(menu)
        }


        this.menu = menu
        configureSearchView(menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun configureSearchView(menu: Menu) {
        val searchView = menu.findItem(R.id.searchTasksAction).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                TaskStore.titleFilter = query ?: ""
                mainList.adapter?.notifyDataSetChanged()
                return false
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId

        when (id) {
            R.id.wipeDataAction -> wipeButtonAction()
            R.id.selectDateAction -> selectDateFilterAction()
            else -> return super.onOptionsItemSelected(item)
        }

        return true
    }

    private fun wipeButtonAction() {

        val builder = AlertDialog.Builder(this@MainActivity)

        builder.setTitle("Wipe all tasks?")
        builder.setMessage("Doing this will make all your tasks be deleted and lost forever. Can not be undone. Are you sure?")

        builder.setNeutralButton("CANCEL") { _, _ ->

        }

        builder.setPositiveButton("YES") { _, _ ->
            TaskStore.clearTasks()
            mainList.adapter?.notifyDataSetChanged()
        }

        builder.create().show()
    }

    private fun createTaskButtonAction() {
        val intent = Intent(this, CreateTaskActivity::class.java)
        startActivity(intent)
    }

    private fun selectDateFilterAction() {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        val listener =  DatePickerDialog.OnDateSetListener { _, year, month, day ->
            TaskStore.dateFilter = String.format("%02d/%02d/%04d", day, month + 1, year)
            mainList.adapter?.notifyDataSetChanged()
            setCalendarIconActionColor("#ff4081")
        }

        val dialog = DatePickerDialog(this@MainActivity, listener, year, month, day)

        dialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "CLEAR") { _, _ ->
            TaskStore.dateFilter = ""
            mainList.adapter?.notifyDataSetChanged()

            setCalendarIconActionColor("#ffffff")
        }

        dialog.setButton(DatePickerDialog.BUTTON_NEUTRAL, "CANCEL", dialog)
        dialog.show()

        dialog.getButton(DatePickerDialog.BUTTON_NEUTRAL).setOnClickListener { _ ->
            dialog.dismiss()
        }
    }

    private fun setCalendarIconActionColor(color: String) {
        if (menu == null) {
            return
        }

        val icon = menu!!.findItem(R.id.selectDateAction).icon
        icon.setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_ATOP)
    }
}
