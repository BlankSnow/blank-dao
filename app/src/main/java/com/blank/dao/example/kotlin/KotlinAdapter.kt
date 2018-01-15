package com.blank.dao.example.kotlin

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.blank.dao.ParseObj
import com.blank.dao.example.R
import com.blank.dao.example.kotlin.KotlinAdapter.ItemViewHolder
import com.blank.dao.example.kotlin.config.inflate

class KotlinAdapter(val items: List<KotlinObject>, val listener: (KotlinObject) -> Unit)
    : RecyclerView.Adapter<ItemViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = parent.inflate(R.layout.item_list)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holderItem: ItemViewHolder, position: Int) {
        holderItem.bind(items[position])
        holderItem.view.setOnClickListener { listener(items[position]) }
    }

    class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val textViewId = view.findViewById(R.id.text_view_id) as TextView
        val textViewName = view.findViewById(R.id.text_view_name) as TextView

        fun bind(kotlinObject: KotlinObject) {
            textViewId.text = ParseObj.toString(kotlinObject.someInteger)
            textViewName.text = kotlinObject.someString + " " + kotlinObject.someBoolean
        }
    }
}