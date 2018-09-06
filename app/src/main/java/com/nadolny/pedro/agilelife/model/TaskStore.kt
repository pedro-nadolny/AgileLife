package com.nadolny.pedro.agilelife.model

import android.content.Context
import model.Task
import java.util.*
import kotlin.collections.ArrayList

object TaskStore {
    private var database: TaskDatabase? = null
    private var tasks = arrayListOf<Task>()

    fun loadDatabase(context: Context) {
        database = TaskDatabase(context)
        tasks = database!!.getAllTasks()
    }

    fun getTasks(): ArrayList<Task> {
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