package com.nadolny.pedro.agilelife.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import com.nadolny.pedro.agilelife.R
import com.nadolny.pedro.agilelife.activity.DetailsActivity
import com.nadolny.pedro.agilelife.model.TaskStore
import com.nadolny.pedro.agilelife.utils.DateUtils
import kotlinx.android.synthetic.main.task_row.view.*
import model.Task
import com.nadolny.pedro.agilelife.activity.MainActivity
import android.view.View.DragShadowBuilder
import android.content.ClipData
import android.os.Build
import android.view.DragEvent
import android.view.View


class TaskRowAdapter(private val tap: (Long) -> (Unit), var tasks: ArrayList<Task>) : RecyclerView.Adapter<TaskRowAdapter.ViewHolder>() {

    var selectedTask: Task? = null
    var selectedIndex: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskRowAdapter.ViewHolder {
        val taskRow = LayoutInflater.from(parent.context)
                .inflate(R.layout.task_row, parent, false) as LinearLayout
        return ViewHolder(taskRow)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, index: Int) {
        val task = tasks[index]
        val id = task.id ?: -1

        viewHolder.taskRow.mainRowTitle.text = task.title
        viewHolder.taskRow.mainRowSubtitle.text = DateUtils.extenseStringFrom(task.dueDate)

        viewHolder.taskRow.setOnClickListener {tap(id)}

        viewHolder.taskRow.setOnLongClickListener { view ->
            val data = ClipData.newPlainText("", "")
            val shadowBuilder = View.DragShadowBuilder(view)
            selectedTask = task
            selectedIndex = index

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                view.startDragAndDrop(data, shadowBuilder, this, 0)
            } else {
                view.startDrag(data, shadowBuilder, this, 0)
            }

            this.tasks.removeAt(index)
            this.notifyItemRemoved(index)

            true
        }
    }

    override fun getItemCount() = tasks.count()
    class ViewHolder(val taskRow: LinearLayout) : RecyclerView.ViewHolder(taskRow)
}
