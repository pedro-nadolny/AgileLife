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
        tasks = getTasks()
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

        tasks.sortBy { it.dueDate }

        return tasks
    }

    fun getTasksWithStatus(status: Int): ArrayList<Task> {
        var list = TaskStore.getTasks().filter { it.status == status }
        return ArrayList(list)
    }

    fun newTask(title: String, dueDate: Date, descript: String, status: Int) {
        if (database == null) {
            return
        }

        val task = Task(title, dueDate, descript, status)
        task.id = database!!.addTask(task)
        tasks.add(task)
        tasks.sortBy { it.dueDate }
    }

    fun removeTask(id: Long): Boolean {
        if (database == null || database!!.removeTask(id) <= 0)
            return false

        val list = tasks.filter { it.id != id }
        tasks = ArrayList(list)
        return true
    }

    fun editTask(id: Long, title: String, dueDate: Date, descript: String, status: Int): Boolean {
        if (database == null) return false

        val index = tasks.indexOfFirst { it.id == id }
        if (index == -1) return false

        val newTask = Task(title, dueDate, descript, status)
        newTask.id = id

        if (database!!.updateTask(newTask) <= 0) return false

        tasks[index] = newTask
        tasks.sortBy { it.dueDate }
        return true
    }

    fun clearTasks() {
        if (database == null) {
            return
        }

        database!!.clearTasks()
        tasks = arrayListOf()
    }
}