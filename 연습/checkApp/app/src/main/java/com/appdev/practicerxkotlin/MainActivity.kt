package com.appdev.practicerxkotlin

import android.content.Intent
import android.content.pm.ResolveInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.*


class MainActivity : AppCompatActivity() {
    val TAG: String = "Test_RxAndroid"

    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: MyViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mRecyclerView = recycler_view
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        mAdapter = MyViewAdapter()
        mRecyclerView.adapter = mAdapter
        mAdapter.mPublishSubject.subscribe { s -> toast(s.name) }
    }

    override fun onStart() {
        super.onStart()

        getItemObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { item ->
                mAdapter.updateItems(item)
                mAdapter.notifyDataSetChanged()
            }
    }

    fun toast(title: String) {
        Toast.makeText(this, title, Toast.LENGTH_SHORT).show()
    }

    fun getItemObservable() : Observable<RecylcerItem> {
        val pm = packageManager
        val i = Intent(Intent.ACTION_MAIN, null)

        i.addCategory(Intent.CATEGORY_LAUNCHER)

        return pm.queryIntentActivities(i, 0).toObservable()
            .sorted(ResolveInfo.DisplayNameComparator(pm))
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .map { item ->
                val img = item.activityInfo.loadIcon(pm)
                val title = item.activityInfo.loadLabel(pm).toString()
                return@map RecylcerItem(img, title)
            }
    }
}
