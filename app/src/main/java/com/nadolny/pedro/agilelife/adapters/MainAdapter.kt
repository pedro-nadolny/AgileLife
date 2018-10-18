package com.nadolny.pedro.agilelife.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import com.nadolny.pedro.agilelife.R
import com.nadolny.pedro.agilelife.model.TaskStore
import com.nadolny.pedro.agilelife.utils.DateUtils
import kotlinx.android.synthetic.main.row_main.view.*

class MainAdapter(private val clickListener: (Int) -> Unit, private val context: Context) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapter.ViewHolder {
        val rowMain = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_main, parent, false) as LinearLayout
        return ViewHolder(rowMain)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, index: Int) {
        val task = TaskStore.getTasks()[index]
        viewHolder.rowMain.mainRowTitle.text = task.title
        viewHolder.rowMain.mainRowSubtitle.text = DateUtils.extenseStringFrom(task.dueDate)
        viewHolder.rowMain.setOnClickListener {clickListener(index)}
    }

    override fun getItemCount() = TaskStore.getTasks().count()
    class ViewHolder(val rowMain: LinearLayout) : RecyclerView.ViewHolder(rowMain)
}
