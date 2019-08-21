package com.github.muhrifqii.parserss.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.kittinunf.fuel.Fuel
import com.github.muhrifqii.parserss.RSSFeedObject
import com.github.muhrifqii.parserss.fuel.responseRss

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var adapter = RVAdapter(listOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        loadFeedUsingFuel()
    }

    fun loadFeedUsingFuel() {
        Fuel.get("http://dp3ap2.jogjaprov.go.id/rss")
            .responseRss<RSSFeedObject> { result ->
                result.fold({
                    adapter.items = it.items
                    adapter.notifyDataSetChanged()
                }, {
                    print(it)
                })
            }
    }
}
