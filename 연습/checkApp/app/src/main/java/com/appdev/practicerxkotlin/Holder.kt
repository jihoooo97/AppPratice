package com.appdev.practicerxkotlin

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.item.view.*

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val iconimg = itemView.i_img
    val nameTv = itemView.i_name

    fun getClickObserver(item: RecylcerItem): Observable<RecylcerItem> {
        return Observable.create { e ->
            itemView.setOnClickListener { view ->
                e.onNext(item)
            }
        }
    }

}