package com.github.muhrifqii.parserss.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.kittinunf.fuel.Fuel
import com.github.muhrifqii.parserss.RSSFeedObject
import com.github.muhrifqii.parserss.fuel.responseRss

class MainActivity : AppCompatActivity() {

    var adapter = RVAdapter(listOf())
    private val toolbar by lazy {
        findViewById<Toolbar>(R.id.toolbar)
    }
    private val recyclerView by lazy {
        findViewById<RecyclerView>(R.id.recyclerView)
    }

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
