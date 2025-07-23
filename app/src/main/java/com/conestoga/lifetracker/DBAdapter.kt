package com.conestoga.lifetracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DBAdapter(
    private var dataList: MutableList<Pair<Int, String>>,
    private val onDelete: (Int) -> Unit,
    private val onEdit: (Int, String) -> Unit
) : RecyclerView.Adapter<DBAdapter.DBViewHolder>() {

    class DBViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dataText: TextView = view.findViewById(R.id.dbItemText)
        val deleteBtn: ImageButton = view.findViewById(R.id.btnDelete)
        val editBtn: ImageButton = view.findViewById(R.id.btnEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DBViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_db, parent, false)
        return DBViewHolder(view)
    }

    override fun onBindViewHolder(holder: DBViewHolder, position: Int) {
        val (id, name) = dataList[position]
        holder.dataText.text = name
        holder.deleteBtn.setOnClickListener { onDelete(id) }
        holder.editBtn.setOnClickListener { onEdit(id, name) }
    }

    override fun getItemCount(): Int = dataList.size

    fun updateData(newData: List<Pair<Int, String>>) {
        dataList = newData.toMutableList()
        notifyDataSetChanged()
    }
}
