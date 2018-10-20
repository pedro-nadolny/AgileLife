package com.nadolny.pedro.agilelife.adapters

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import com.nadolny.pedro.agilelife.R
import com.nadolny.pedro.agilelife.model.TaskStore
import kotlinx.android.synthetic.main.list_row.view.*

class ListRowAdapter(var lists: ArrayList<String>, private var listener: (Long) -> (Unit), private var context: Context): RecyclerView.Adapter<ListRowAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListRowAdapter.ViewHolder {

        val listRow = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_row, parent, false) as LinearLayout

        return ListRowAdapter.ViewHolder(listRow)
    }

    override fun onBindViewHolder(viewHolder: ListRowAdapter.ViewHolder, index: Int) {

        val tasks = TaskStore.getTasksWithStatus(index)

        viewHolder.listRow.taskList.setHasFixedSize(true)
        viewHolder.listRow.taskList.layoutManager = LinearLayoutManager(context)
        viewHolder.listRow.listTitle.text = lists[index]

        val adapter = TaskRowAdapter(listener, tasks)
        viewHolder.listRow.taskList.adapter = adapter
    }

    override fun getItemCount() = lists.size
    class ViewHolder(val listRow: LinearLayout) : RecyclerView.ViewHolder(listRow)
}
