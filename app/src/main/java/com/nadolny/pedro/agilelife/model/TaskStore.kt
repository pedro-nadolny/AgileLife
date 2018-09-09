package com.nadolny.pedro.agilelife.model

import android.annotation.SuppressLint
import android.content.Context
import com.nadolny.pedro.agilelife.utils.DateUtils
import model.Task
import java.util.*
import kotlin.collections.ArrayList

object TaskStore {

    private var database: TaskDatabase? = null
    private var tasks = arrayListOf<Task>()
    var titleFilter = ""
    var dateFilter = ""

    fun loadDatabase(context: Context) {
        database = TaskDatabase(context)
    }

    fun getTasks(): ArrayList<Task> {
        if (database == null) {
            return arrayListOf()
        }

        var tasks = database!!.getAllTasks()

        if (!titleFilter.isEmpty()) {
            tasks = ArrayList(tasks.filter { titleFilter in it.title })
        }

        if (!dateFilter.isEmpty()) {
            tasks = ArrayList(tasks.filter { task -> DateUtils.stringFrom(task.dueDate) == dateFilter})
        }

        return tasks
    }

    fun newTask(title: String, dueDate: Date, descript: String) {
        if (database == null) {
            return
        }

        val task = Task(title, dueDate, descript)
        task.id = database!!.addTask(task)
        tasks.add(task)
    }

    fun removeTask(id: Long) {
        if (database == null) {
            return
        }

        if (database!!.removeTask(id) > 0) {
            tasks = ArrayList(tasks.filter { task -> task.id == id })
        }
    }

    fun editTask(id: Long, title: String, dueDate: Date, descript: String) {
        if (database == null) {
            return
        }

        val newTask = Task(title, dueDate, descript)
        newTask.id = id

        if (database!!.updateTask(newTask) > 0) {
            val oldTaskIndex = tasks.indexOfFirst { oldTask -> oldTask.id == id }
            tasks[oldTaskIndex] = newTask
        }
    }

    fun clearTasks() {
        if (database == null) {
            return
        }

        database!!.clearTasks()
        tasks = arrayListOf()
    }
}