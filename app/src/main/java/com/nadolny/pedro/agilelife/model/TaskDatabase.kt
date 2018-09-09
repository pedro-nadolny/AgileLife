package com.nadolny.pedro.agilelife.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.nadolny.pedro.agilelife.utils.DateUtils
import model.Task

class TaskDatabase: SQLiteOpenHelper {

    constructor(context: Context): super(context, DB_NAME, null, DB_VERSION) {
        this.context = context
    }

    private var context: Context

    private companion object {
        const val DB_NAME = "tasks.sqlite"
        const val DB_TABLE = "tasks"
        const val COL_ID = "_id"
        const val COL_TITLE = "title"
        const val COL_DESC = "desc"
        const val COL_DATE = "date"
        const val DB_VERSION = 1
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {}

    override fun onCreate(database: SQLiteDatabase?) {
        val sql = "create table if not exists " + DB_TABLE + "(" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT, " +
                COL_DESC + " TEXT, " +
                COL_DATE + " TEXT);"

        database?.execSQL(sql)
    }

    fun addTask(task: Task): Long {
        val database = writableDatabase

        val id = database.insert(DB_TABLE, "", contentValuesOf(task))
        task.id = id
        database.close()

        return id
    }

    fun removeTask(id: Long): Int {
        val database = writableDatabase
        val _id = id.toString()
        val numberOfRowsDeleted = database.delete(DB_TABLE, COL_ID+"=?", arrayOf(_id))

        database.close()
        return numberOfRowsDeleted
    }

    fun updateTask(task: Task): Int {
        val database = writableDatabase
        val _id = task.id.toString()

        val numberOfRowsUpdated = database.update(DB_TABLE, contentValuesOf(task), COL_ID+"=?", arrayOf(_id))
        database.close()

        return numberOfRowsUpdated
    }

    fun getAllTasks(): ArrayList<Task> {
        val database = readableDatabase
        val query = "SELECT * FROM " + DB_TABLE + " ORDER BY " + COL_DATE
        val cursor = database.rawQuery(query, arrayOf())
        val tasks = arrayListOf<Task>()

        if (!cursor.moveToFirst()) {
            database.close()
            return tasks
        }

        do {
            val title = cursor.getString(cursor.getColumnIndex(COL_TITLE))
            val desc = cursor.getString(cursor.getColumnIndex(COL_DESC))
            val date = cursor.getString(cursor.getColumnIndex(COL_DATE))

            val task = Task(title, DateUtils.dateFrom(date), desc)
            task.id = cursor.getLong(cursor.getColumnIndex(COL_ID))

            tasks.add(task)
        } while (cursor.moveToNext())

        database.close()
        return tasks
    }

    fun getTasksSearchBy(title: String): ArrayList<Task> {
        val database = readableDatabase

        val query =
                "SELECT * FROM " + DB_TABLE +
                " WHERE " + COL_TITLE +
                " LIKE %" + title + "%"
                " ORDER BY " + COL_DATE

        val cursor = database.rawQuery(query, arrayOf())
        val tasks = arrayListOf<Task>()

        if (!cursor.moveToFirst()) {
            database.close()
            return tasks
        }

        do {
            val title = cursor.getString(cursor.getColumnIndex(COL_TITLE))
            val desc = cursor.getString(cursor.getColumnIndex(COL_DESC))
            val date = cursor.getString(cursor.getColumnIndex(COL_DATE))

            val task = Task(title, DateUtils.dateFrom(date), desc)
            task.id = cursor.getLong(cursor.getColumnIndex(COL_ID))

            tasks.add(task)
        } while (cursor.moveToNext())

        database.close()
        return tasks
    }

    fun clearTasks() {
        writableDatabase.execSQL("DELETE FROM " + DB_TABLE)
    }

    private fun contentValuesOf(task: Task): ContentValues {
        val values = ContentValues()

        values.put(COL_TITLE, task.title)
        values.put(COL_DESC, task.descript)
        values.put(COL_DATE, DateUtils.stringFrom(task.dueDate))

        return values
    }
}