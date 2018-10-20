package model

import com.nadolny.pedro.agilelife.utils.DateUtils
import java.util.*

class Task(title: String, dueDate: Date, descript: String, status: Int) {

    var title: String
    var dueDate: Date
    var descript: String
    var id: Long?
    var status: Int

    init {
        this.title = title
        this.dueDate = dueDate
        this.descript = descript
        this.status = status
        id = null
    }

    override fun toString(): String {
        var array = arrayListOf(title, dueDate, descript, status)
        return array.joinToString(";")
    }

    companion object {
        fun fromString(str: String): Task? {
            val components = str.split(';')

            if (components.size < 4) {
                return null
            }

            val title = components[0]
            val date = DateUtils.dateFrom(components[1])
            val desc = components[2]
            val status = components[3].toInt()

            return Task(title, date, desc, status)
        }
    }
}
