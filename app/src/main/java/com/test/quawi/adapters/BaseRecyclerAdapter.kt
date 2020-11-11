package com.test.quawi.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<I, VH: BaseViewHolder<I>>() : RecyclerView.Adapter<VH>() {

    protected open val items = mutableListOf<I>()

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bindItem(items[position])

    open fun addItems(items: List<I>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    open fun addItem(item: I) {
        items.add(item)
        notifyItemInserted(items.size)
    }

    fun isEmpty() = items.isEmpty()

    fun size() = items.size

}

abstract class BaseViewHolder<I>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bindItem(item: I)
}