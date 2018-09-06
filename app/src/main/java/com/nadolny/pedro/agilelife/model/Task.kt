package model

import android.content.Context
import android.content.res.AssetManager
import com.nadolny.pedro.agilelife.utils.DateUtils
import java.util.*
import java.io.*
import android.content.Context.MODE_PRIVATE
import com.nadolny.pedro.agilelife.model.TaskDatabase

class Task(title: String, dueDate: Date, descript: String) {

    companion object {
        fun fromString(str: String): Task? {
            val components = str.split(',')

            if (components.size < 3) {
                return null
            }

            val title = components[0]
            val date = DateUtils.dateFrom(components[1])
            val desc = components[2]

            return Task(title, date, desc)
        }
    }

    var title: String
    var dueDate: Date
    var descript: String
    var id: Long?

    init {
        this.title = title
        this.dueDate = dueDate
        this.descript = descript
        id = null
    }

    override fun toString(): String {
        return this.title + "," + DateUtils.stringFrom(this.dueDate) + "," + this.descript + "\n"
    }
}
