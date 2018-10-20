package com.nadolny.pedro.agilelife.activity

import android.app.ActionBar
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import com.nadolny.pedro.agilelife.R
import com.nadolny.pedro.agilelife.adapters.ListRowAdapter
import com.nadolny.pedro.agilelife.adapters.TaskRowAdapter
import com.nadolny.pedro.agilelife.model.TaskStore
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.fixedRateTimer


class MainActivity : AppCompatActivity() {

    private var menu: Menu? = null
    var lists = arrayListOf("To-do", "Doing", "Paused", "Done")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TaskStore.loadDatabase(applicationContext)

        leftActionButton.hide()

        mainList.layoutManager = LinearLayoutManager(
                applicationContext,
                LinearLayoutManager.HORIZONTAL,
                false
        )

        val listener = { taskId: Long ->
            val intent = Intent(applicationContext, DetailsActivity::class.java)
            intent.putExtra("taskId", taskId)
            startActivity(intent)
        }

        mainList.adapter = ListRowAdapter(lists, listener, applicationContext)
        rightActionButton.setOnClickListener { createTaskButtonAction() }

        var dragScrollOffset = 0
        fixedRateTimer("dragScroll", false, 1000, 2) {
            if (dragScrollOffset != 0) {
                runOnUiThread {
                    mainList.scrollBy(dragScrollOffset, 0)
                }
            }
        }

        mainList.setOnDragListener { view, event ->
            val x = event.x
            val y = event.y

            println(String.format("%f / %f", x, y))

            when (event.action) {

                DragEvent.ACTION_DRAG_STARTED -> {
                    rightActionButton.setImageResource(R.drawable.ic_edit)
                    leftActionButton.show()
                }

                DragEvent.ACTION_DRAG_ENDED -> {
                    rightActionButton.setImageResource(R.drawable.ic_add_task)
                    leftActionButton.hide()
                    dragScrollOffset = 0
                }

                DragEvent.ACTION_DRAG_LOCATION -> {

                    val layoutManager = mainList.layoutManager as LinearLayoutManager
                    var position = layoutManager.findFirstCompletelyVisibleItemPosition()
                    val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                    val viewUnder = mainList.findChildViewUnder(x, y) ?: return@setOnDragListener true

                    val currentItemPosition = mainList.getChildAdapterPosition(viewUnder)

                    dragScrollOffset = when {
                        x >= mainList.width - 50 -> 1
                        x <= 50 -> -1
                        else -> 0
                    }
                }

                DragEvent.ACTION_DROP -> {
                    val taskAdapter = event.localState as TaskRowAdapter
                    val task = taskAdapter.selectedTask
                    val index = taskAdapter.selectedIndex
                    val taskId = task?.id ?: -1

                    if (task == null || index == null) {
                        return@setOnDragListener true
                    }

                    val actionButtonDropDetectionSize = 150

                    if (x > mainList.width - actionButtonDropDetectionSize && y > mainList.height - actionButtonDropDetectionSize) {
                        val intent = Intent(applicationContext, EditTaskActivity::class.java)
                        intent.putExtra("taskId", taskId)
                        startActivity(intent)
                        return@setOnDragListener true
                    } else if (x < actionButtonDropDetectionSize && y > mainList.height - actionButtonDropDetectionSize) {
                        TaskStore.removeTask(taskId)
                        mainList.adapter?.notifyDataSetChanged()
                        return@setOnDragListener true
                    }

                    val viewUnder = mainList.findChildViewUnder(x, y)

                    if (viewUnder == null) {
                        taskAdapter.tasks.add(index, task)
                        taskAdapter.notifyItemInserted(index)

                        return@setOnDragListener true
                    }

                    val newStatus = mainList.getChildAdapterPosition(viewUnder)
                    TaskStore.editTask(taskId, task.title, task.dueDate, task.descript, newStatus)
                    mainList.adapter?.notifyDataSetChanged()
                }
            }

            return@setOnDragListener true
        }
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
            R.id.selectDateAction -> selectDateFilterAction()
            else -> return super.onOptionsItemSelected(item)
        }

        return true
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
