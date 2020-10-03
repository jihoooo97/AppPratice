package com.appdev.practicerxkotlin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.subjects.PublishSubject

class MyViewAdapter() : RecyclerView.Adapter<MyViewHolder>() {

    private val mItems: ArrayList<RecylcerItem> = ArrayList<RecylcerItem>()
    val mPublishSubject: PublishSubject<RecylcerItem> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = mItems[position]
        holder.iconimg.setImageDrawable(item.img)
        holder.nameTv.text = item.name
        holder.getClickObserver(item).subscribe(mPublishSubject)
    }

    fun updateItems(items: ArrayList<RecylcerItem>) {
        mItems.addAll(items)
    }
    fun updateItems(item: RecylcerItem) {
        mItems.add(item)
    }

}